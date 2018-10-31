package milu.db.access;

import java.util.ArrayList;
import java.util.List;


import java.sql.SQLException;
import java.sql.CallableStatement;

import milu.ctrl.sql.parse.CallObj;
import milu.ctrl.sql.parse.MySQLType;
import milu.tool.MyStringTool;

public class ExecSQLTransactionCall extends ExecSQLAbstract 
{
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

			// data on ObjTableView
			List<Object> data = new ArrayList<>();
			// OUTパラメータを使っている位置のリスト
			List<Integer> outLst = new ArrayList<>();
			
			for ( int i = 0; i < this.callLst.size(); i++ )
			{
				CallObj callObj = this.callLst.get(i);
				CallObj.ParamType paramType = callObj.getParamType();
				MySQLType sqlType = callObj.getSqlType();
				String inColName = callObj.getInColName();
				// INパラメータを設定
				if ( ( paramType == CallObj.ParamType.IN ) || ( paramType == CallObj.ParamType.IN_OUT ) )
				{
					// INパラーメータのカラム名
					this.colNameLst.add( "IN"+Integer.valueOf(i+1) );
					
					int placeHolderInPos = MyStringTool.getNumFromAlpha(inColName, 0 );
					stmt.setObject(i+1, this.preLst.get(placeHolderInPos));
					// INパラメータをここでObjTableViewへ出力する
					data.add(this.preLst.get(placeHolderInPos));
				}
				// OUTパラメータを設定
				if ( ( paramType == CallObj.ParamType.OUT ) || ( paramType == CallObj.ParamType.IN_OUT ) )
				{
					stmt.registerOutParameter(i+1, sqlType.getVal());
					
					// OUTパラーメータのカラム名
					this.colNameLst.add( "OUT"+Integer.valueOf(i+1) );
					outLst.add(i+1);
				}
			}
			
			stmt.execute();
			// OUTパラメータをここでObjTableViewへ出力する
			outLst.forEach((pos) -> {
				try {
					data.add(stmt.getObject(pos));
				}
				catch ( SQLException sqlEx0 )
				{
					sqlEx0.printStackTrace();
				}
			});
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
