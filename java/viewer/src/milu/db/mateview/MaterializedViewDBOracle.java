package milu.db.mateview;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class MaterializedViewDBOracle extends MaterializedViewDBAbstract
{
	public void selectEntityLst( String schemaName ) throws SQLException
	{
		this.clear();

		String sql = this.listSQL( schemaName );
		System.out.println( " -- selectMaterializedViewLst -----" );
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
				mapView.put( "viewName", rs.getString("object_name") );
				mapView.put( "status"  , rs.getString("status") );
				this.viewLst.add( mapView );
			}
		}
	}
	
	protected String listSQL( String schemaName )
	{
		String sql = 
			" select object_name, status from all_objects \n" +
			" where \n" +
			" object_type='MATERIALIZED VIEW' \n" + 
			" and \n" + 
			" owner = '" + schemaName + "' \n" +
			" order by owner, object_name";
		return sql;
	}
}
