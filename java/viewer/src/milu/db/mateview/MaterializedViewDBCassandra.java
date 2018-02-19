package milu.db.mateview;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class MaterializedViewDBCassandra extends MaterializedViewDBAbstract {

	@Override
	public void selectEntityLst(String schemaName) throws SQLException 
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
				mapView.put( "viewName", rs.getString("view_name") );
				this.viewLst.add( mapView );
			}
		}
	}

	@Override
	protected String listSQL(String schemaName)
	{
		String sql =
			" select \n"           +
		    "   view_name      \n" +
			" from  \n" +
			"   system_schema.views \n" +
			" where \n" +
			"   keyspace_name = '" + schemaName + "' \n" +
			" order by view_name";
		return sql;
	}

}
