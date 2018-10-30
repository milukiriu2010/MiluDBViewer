package milu.db.access;

import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;

import java.sql.Timestamp;
import java.sql.SQLException;
import java.sql.CallableStatement;

import milu.ctrl.sql.parse.MySQLType;
import milu.db.MyDBSQLite;
import milu.gui.stmt.call.CallObj;

public class ExecSQLTransactionCall extends ExecSQLAbstract 
{
	private List<String> colTypeNameLst = null;
	
	public void setColTypeNameLst( List<String> colTypeNameLst )
	{
		this.colTypeNameLst = colTypeNameLst;
	}

	@Override
	public void exec( final int checkCnt, final int fetchPos ) throws SQLException, Exception 
	{
		//System.out.println( "transaction:" + this.sqlBag.getSQL() );
		String sql = "{CALL " + this.sqlBag.getSQL() + "}";
		try
		(
			CallableStatement stmt   = this.myDBAbs.createCallableStatement(sql);
		)
		{
			this.clear();
			this.execStartTime = System.nanoTime();
			
			this.colNameLst.add( "Type" );
			//this.colNameLst.add( "Row" );
			
			
			for ( int i = 0; i < this.callLst.size(); i++ )
			{
				CallObj callObj = this.callLst.get(i);
				CallObj.ParamType paramType = callObj.getParamType();
				MySQLType sqlType = callObj.getSqlType();
				String inColName = callObj.getInColName();
				// INパラメータを設定
				if ( ( paramType == CallObj.ParamType.IN ) || ( paramType == CallObj.ParamType.IN_OUT ) )
				{
					
					
					
					
					
					
					// 後でpreLstを取るロジックを修正
					stmt.setObject(i+1, this.preLst.get(0));
				}
				// OUTパラメータを設定
				if ( ( paramType == CallObj.ParamType.OUT ) || ( paramType == CallObj.ParamType.IN_OUT ) )
				{
					stmt.registerOutParameter(i+1, sqlType.getVal());
				}
			}
			
			stmt.execute();
			List<Object> data = new ArrayList<>();
			data.add( this.sqlBag.getType().getVal() );
			
			// OUTパラメータをここで出力する
			//data.add( Integer.valueOf(cnt) );
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
