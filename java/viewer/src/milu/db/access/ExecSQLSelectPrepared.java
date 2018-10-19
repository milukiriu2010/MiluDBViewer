package milu.db.access;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExecSQLSelectPrepared extends ExecSQLAbstract 
{
	// Column Class Name List
	private List<String>  colClassNameLst = new ArrayList<String>();
	
	// Column Meta Info Head List
	private List<Object> colMetaInfoHeadLst = 
			Arrays.asList("Name","Class","Type","TypeName","Size","Precision","Scale","Nullable","AutoIncrement");


	// Column Meta Info Data List
	// -------------------------------------------
	// 0:ColumnName
	// 1:ColumnClassName
	// 2:ColumnType
	// 3:ColumnTypeName
	// 4:ColumnDisplaySize
	// 5:Precision
	// 6:Scale
	// 7:isNullable
	// 8:isAutoIncrement
	private List<Map<String,Object>> colMetaInfoDataLst = new ArrayList<>();
	
	public List<Object> getColMetaInfoHeadLst()
	{
		return this.colMetaInfoHeadLst;
	}	
	
	public List<Map<String,Object>> getColMetaInfoDataLst()
	{
		return this.colMetaInfoDataLst;
	}
	
	@Override
	protected void clear()
	{
		super.clear();
		
		this.colClassNameLst.clear();
		this.colMetaInfoDataLst.clear();
	}
	
	@Override
	public void exec(int checkCnt, int fetchPos) 
		throws 
			SQLException, 
			MyDBOverFetchSizeException, 
			Exception 
	{
		System.out.println( "select:" + this.sqlBag.getSQL() );
		Statement stmt   = null;
		ResultSet rs     = null;
		Exception lastEx = null;
		
		double progress = this.assignedSize/(double)checkCnt;
		try
		{
			this.clear();
			this.execStartTime = System.nanoTime();
			
			boolean isAbsoluteOK = false;
			if ( fetchPos == 1 )
			{
				stmt = this.myDBAbs.createStatement();
				rs   = stmt.executeQuery( this.sqlBag.getSQL() );
				isAbsoluteOK = true;
			}
			else
			{
				try
				{
					stmt = this.myDBAbs.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
					rs   = stmt.executeQuery( this.sqlBag.getSQL() );
					rs.absolute(fetchPos);
					isAbsoluteOK = true;
				}
				catch ( SQLException sqlEx2 )
				{
					System.out.println( "ResultSet absolute is not support." );
					if ( stmt != null )
					{
						try
						{
							stmt.close();
						}
						catch ( SQLException sqlEx3 )
						{
						}
					}
					
					if ( rs != null )
					{
						try
						{
							rs.close();
						}
						catch ( SQLException sqlEx3 )
						{
						}
					}
					
					stmt = this.myDBAbs.createStatement();
					rs   = stmt.executeQuery( this.sqlBag.getSQL() );
					isAbsoluteOK = false;
				}
			}
			
			// Get Column Attribute
			// https://examples.javacodegeeks.com/core-java/sql/resultsetmetadata/java-sql-resultsetmetadata-example/
			ResultSetMetaData rsmd = rs.getMetaData();
			// Result ColumnName
			int headCnt = rsmd.getColumnCount();
			System.out.println( "---DB COLUMN----------------" );
			for ( int i = 1; i <= headCnt; i++ )
			{
				this.colNameLst.add( rsmd.getColumnName( i ) );
				//this.colTypeNameLst.add( rsmd.getColumnTypeName( i ) );
				this.colClassNameLst.add( rsmd.getColumnClassName( i ) );
				
				Map<String,Object> mapObj = new LinkedHashMap<>();
				mapObj.put( (String)this.colMetaInfoHeadLst.get(0), rsmd.getColumnName( i ) );
				mapObj.put( (String)this.colMetaInfoHeadLst.get(1), rsmd.getColumnClassName( i ) );
				mapObj.put( (String)this.colMetaInfoHeadLst.get(2), rsmd.getColumnType( i ) );
				mapObj.put( (String)this.colMetaInfoHeadLst.get(3), rsmd.getColumnTypeName( i ) );
				mapObj.put( (String)this.colMetaInfoHeadLst.get(4), Integer.valueOf(rsmd.getColumnDisplaySize(i)) );
				mapObj.put( (String)this.colMetaInfoHeadLst.get(5), Integer.valueOf(rsmd.getPrecision(i)) );
				mapObj.put( (String)this.colMetaInfoHeadLst.get(6), Integer.valueOf(rsmd.getScale(i)) );
				mapObj.put( (String)this.colMetaInfoHeadLst.get(7), Integer.valueOf(rsmd.isNullable(i)) );
				mapObj.put( (String)this.colMetaInfoHeadLst.get(8), rsmd.isAutoIncrement(i) );
				
				this.colMetaInfoDataLst.add( mapObj );
			}
			System.out.println( "----------------------------" );
			
			// 
			int startPos = 1;
			// Fetch Count
			int fetchCnt = 0;
			while ( rs.next() )
			{
				if ( this.isCancel == true )
				{
					break;
				}
				if ( isAbsoluteOK == false )
				{
					if ( startPos < fetchPos )
					{
						startPos++;
						continue;
					}
				}
				if ( fetchCnt >= checkCnt )
				{
					throw new MyDBOverFetchSizeException();
				}
				List<Object> dataRow = new ArrayList<>();
				for ( int i = 1; i <= headCnt; i++ )
				{
					Object colName      = this.colNameLst.get( i-1 );
					Object colClassName = this.colClassNameLst.get( i-1 );
					try
					{
						// java.sql.SQLEXception: Attempt to read a SQLXML that is not readable
						if ( "java.sql.SQLXML".equals(colClassName) )
						{
							dataRow.add( rs.getString( (String)colName ) );
						}
						else if ( "oracle.jdbc.OracleClob".equals(colClassName) )
						{
							dataRow.add( rs.getString( (String)colName ) );
						}
						else
						{
							dataRow.add( rs.getObject( (String)colName ) );
						}
					}
					catch ( Exception ex )
					{
						lastEx = ex;
						StringWriter sw = new StringWriter();
						PrintWriter  pw = new PrintWriter(sw);
						ex.printStackTrace(pw);
						String strEx = sw.toString();
						
						// put Exception String on Cell 
						String[] strExSplit = strEx.split( "\n" );
						dataRow.add( strExSplit[0] );
					}
				}
				dataLst.add( dataRow );
				fetchCnt++;
				if ( this.progressInf != null )
				{
					this.progressInf.addProgress(progress);
					this.progressInf.setMsg("...");
				}
			}
		}
		catch ( SQLException sqlEx )
		{
			try
			{
				this.myDBAbs.processAfterException();
			}
			catch ( SQLException sqlEx0 )
			{
				// suppress close error
			}
			
			throw sqlEx;
		}
		finally
		{
			try
			{
				if ( stmt != null )
				{
					stmt.close();
				}
			}
			catch ( SQLException sqlEx1 )
			{
				// suppress close error
			}
			
			try
			{
				if ( rs != null )
				{
					rs.close();
				}
			}
			catch ( SQLException sqlEx2 )
			{
				// suppress close error
			}
			
			this.execEndTime = System.nanoTime();
			
			if ( lastEx != null )
			{
				throw lastEx;
			}
		}
	}

}
