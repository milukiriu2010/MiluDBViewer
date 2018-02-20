package milu.db.trigger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class TriggerDBPostgres extends TriggerDBAbstract 
{

	@Override
	public void selectEntityLst(String schemaName) throws SQLException 
	{
		this.clear();

		String sql = this.listSQL( schemaName );
		System.out.println( " -- selectTriggerLst -----------------" );
		System.out.println( sql );
		System.out.println( " ----------------------------------" );
		
		try
		(
			Statement stmt = this.myDBAbs.createStatement();
			ResultSet rs   = stmt.executeQuery( sql );
		)
		{
			while ( rs.next() )
			{
				Map<String, String> mapView = new HashMap<String,String>();
				mapView.put( "triggerName", rs.getString("tgname") );
				this.triggerLst.add( mapView );
			}
		}
	}

	@Override
	protected String listSQL(String schemaName) 
	{
		String sql =
			" select \n" +
			"   t.tgname   tgname \n" +
			" from \n"   +
			"   pg_trigger   t join \n" +
			"   pg_class     c on ( t.tgrelid = c.oid ) join \n" +
			"   pg_namespace n on ( c.relnamespace = n.oid ) \n" +
			" where \n" +
			"   n.nspname = '" + schemaName + "' \n" +
			"   and \n" +
			"   t.tgname not like 'RI_ConstraintTrigger%' \n" +
			" order by t.tgname";
		return sql;
	}

}
