package milu.db.access;

import java.util.ArrayList;
import java.util.List;

import java.sql.SQLException;
import java.sql.PreparedStatement;

public class ExecSQLTransactionPrepared extends ExecSQLAbstract 
{

	@Override
	public void exec(int checkCnt) throws SQLException, Exception 
	{
		System.out.println( "transaction:" + this.sqlBag.getSQL() );
		try
		(
			PreparedStatement stmt   = this.myDBAbs.createPreparedStatement(this.sqlBag.getSQL());
		)
		{
			this.clear();
			this.execStartTime = System.nanoTime();
			
			this.colNameLst.add( "Type" );
			this.colNameLst.add( "Row" );
			
			for ( int i=1; i <= this.preLst.size(); i++ )
			{
				Object obj = this.preLst.get(i);
				stmt.setObject( i, obj );
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
