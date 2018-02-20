package milu.db.trigger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class TriggerDBOracle extends TriggerDBAbstract 
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
				mapView.put( "triggerName", rs.getString("object_name") );
				mapView.put( "status"  , rs.getString("status") );
				this.triggerLst.add( mapView );
			}
		}
	}

	@Override
	protected String listSQL(String schemaName) 
	{
		String sql =
			" select \n"         +
			"   object_name, \n" +
			"   status       \n" +
			" from \n"           +
			"   all_objects \n"  +
			" where \n"          +
			"   owner = '" + schemaName + "' \n" +
			"   and \n"          +
			"   object_type = 'TRIGGER' \n"  +
			" order by object_name";
		return sql;
	}

}
