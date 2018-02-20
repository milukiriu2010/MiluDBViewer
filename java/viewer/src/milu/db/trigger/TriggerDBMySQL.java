package milu.db.trigger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class TriggerDBMySQL extends TriggerDBAbstract 
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
				mapView.put( "triggerName", rs.getString("trigger_name") );
				this.triggerLst.add( mapView );
			}
		}
	}

	@Override
	protected String listSQL(String schemaName) 
	{
		String sql = 
			" select distinct \n" +
			"   trigger_name     \n"       +
			" from   \n"       +
			"   information_schema.triggers \n" + 
			" where \n"        + 
			"   trigger_schema = '" + schemaName + "' \n" +
			" order by trigger_name";
		return sql;
	}

}
