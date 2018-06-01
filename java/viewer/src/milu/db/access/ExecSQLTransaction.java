package milu.db.access;

import java.util.ArrayList;
import java.util.List;

import java.sql.SQLException;
import java.sql.Statement;

public class ExecSQLTransaction extends ExecSQLAbstract 
{

	@Override
	public void exec(int checkCnt) throws SQLException, Exception 
	{
		System.out.println( "transaction:" + this.sqlBag.getSQL() );
		try
		(
			Statement stmt   = this.myDBAbs.createStatement();
		)
		{
			this.clear();
			this.execStartTime = System.nanoTime();
			
			this.colNameLst.add( "Type" );
			this.colNameLst.add( "Row" );
			
			int cnt = stmt.executeUpdate(this.sqlBag.getSQL());
			List<Object> data = new ArrayList<>();
			data.add( this.sqlBag.getType().toString().replace("_", " ") );
			data.add( String.valueOf(cnt) );
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
