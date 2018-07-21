package milu.db.access;

import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;

import java.sql.Timestamp;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import milu.db.MyDBSQLite;

public class ExecSQLTransactionPrepared extends ExecSQLAbstract 
{
	//private List<String> colClassNameLst = null;
	private List<String> colTypeLst = null;
	
	/*
	public void setColClassNameLst( List<String> colClassNameLst )
	{
		this.colClassNameLst = colClassNameLst;
	}
	*/
	
	public void setColTypeLst( List<String> colTypeLst )
	{
		this.colTypeLst = colTypeLst;
	}

	@Override
	public void exec( final int checkCnt, final int fetchPos ) throws SQLException, Exception 
	{
		//System.out.println( "transaction:" + this.sqlBag.getSQL() );
		try
		(
			PreparedStatement stmt   = this.myDBAbs.createPreparedStatement(this.sqlBag.getSQL());
		)
		{
			this.clear();
			this.execStartTime = System.nanoTime();
			
			this.colNameLst.add( "Type" );
			this.colNameLst.add( "Row" );
			
			int preLstSize = this.preLst.size();
			for ( int i=1; i <= preLstSize; i++ )
			{
				Object obj = this.preLst.get(i-1);
				if ( obj == null )
				{
					if ( this.colTypeLst == null )
					{
						stmt.setObject( i, obj );
					}
					else if ( i >= this.colTypeLst.size() )
					{
						stmt.setObject( i, obj );
					}
					else
					{
						String colType = this.colTypeLst.get(i-1).toUpperCase();
						if ( "NUMERIC".equals(colType) )
						{
							stmt.setNull( i, java.sql.Types.NUMERIC );
						}
						else if ( "NUMBER".equals(colType) )
						{
							stmt.setNull( i, java.sql.Types.NUMERIC );
						}
						// MySQL
						// Cassandra(zhicwu)
						else if ( "INT".equals(colType) )
						{
							stmt.setNull( i, java.sql.Types.INTEGER );
						}
						// MySQL
						else if ( "MEDIUMINT UNSIGNED".equals(colType) )
						{
							stmt.setNull( i, java.sql.Types.INTEGER );
						}
						// MySQL
						else if ( "SMALLINT".equals(colType) )
						{
							stmt.setNull( i, java.sql.Types.SMALLINT );
						}
						// MySQL
						else if ( "SMALLINT UNSIGNED".equals(colType) )
						{
							stmt.setNull( i, java.sql.Types.SMALLINT );
						}
						// MySQL
						else if ( "TINYINT".equals(colType) )
						{
							stmt.setNull( i, java.sql.Types.TINYINT );
						}
						// MySQL
						else if ( "TINYINT UNSIGNED".equals(colType) )
						{
							stmt.setNull( i, java.sql.Types.TINYINT );
						}
						// MySQL
						// Cassandra(zhicwu)
						else if ( "DECIMAL".equals(colType) )
						{
							stmt.setNull( i, java.sql.Types.DECIMAL );
						}
						else if ( "CHAR".equals(colType) )
						{
							stmt.setNull( i, java.sql.Types.CHAR );
						}
						// MySQL
						// Cassandra(zhicwu)
						else if ( "VARCHAR".equals(colType) )
						{
							stmt.setNull( i, java.sql.Types.VARCHAR );
						}
						// Oracle
						else if ( "VARCHAR2".equals(colType) )
						{
							stmt.setNull( i, java.sql.Types.VARCHAR );
						}
						// SQLite
						else if ( "TEXT".equals(colType) )
						{
							stmt.setNull( i, java.sql.Types.VARCHAR );
						}
						// Oracle
						// Cassandra(zhicwu)
						else if ( "DATE".equals(colType) )
						{
							stmt.setNull( i, java.sql.Types.DATE );
						}
						// MySQL
						else if ( "YEAR".equals(colType) )
						{
							stmt.setNull( i, java.sql.Types.DATE );
						}
						// MySQL
						else if ( "DATETIME".equals(colType) )
						{
							stmt.setNull( i, java.sql.Types.TIMESTAMP );
						}
						// MySQL
						// Cassandra(zhicwu)
						else if ( "TIMESTAMP".equals(colType) )
						{
							stmt.setNull( i, java.sql.Types.TIMESTAMP );
						}
						// Oracle
						else if ( "CLOB".equals(colType) )
						{
							stmt.setNull( i, java.sql.Types.CLOB );
						}
						// Oracle
						else if ( "SYS.XMLTYPE".equals(colType) )
						{
							stmt.setNull( i, java.sql.Types.SQLXML );
						}
						// MySQL
						//   GEOMETRY(sakila.address)
						//   BLOB(sakila.staff)
						else
						{
							stmt.setObject( i, obj );
						}
					}
				}
				// obj is not null.
				else
				{
					if ( obj instanceof Timestamp )
					{
						// SQLite does not support "java.sql.Timestamp"
						if ( this.myDBAbs instanceof MyDBSQLite )
						{
							Timestamp objTS = (Timestamp)obj;
							String strTS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(objTS);
							stmt.setString( i, strTS );
						}
						else
						{
							stmt.setObject( i, obj );
						}
					}
					else
					{
						stmt.setObject( i, obj );
					}
				}
			}
			
			int cnt = stmt.executeUpdate();
			List<Object> data = new ArrayList<>();
			data.add( this.sqlBag.getType().getVal() );
			data.add( Integer.valueOf(cnt) );
			this.dataLst.add(data);
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
			this.execEndTime = System.nanoTime();
		}
	}

}
