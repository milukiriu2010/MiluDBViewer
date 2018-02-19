package milu.db.mateview;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class MaterializedViewDBPostgres extends MaterializedViewDBAbstract {

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
				mapView.put( "viewName", rs.getString("relname") );
				this.viewLst.add( mapView );
			}
		}
	}

	@Override
	protected String listSQL(String schemaName) 
	{
		String sql =
			" select \n" +
			"   c.relname  relname \n" +
			" from \n" +
			"   pg_class  c  join \n" +
			"   pg_namespace n on c.relnamespace = n.oid \n" +
			" where \n" +
			"   c.relkind = 'm' \n" +
			"   and \n" +
			"   n.nspname = '" + schemaName + "' \n" +
			" order by c.relname";
		return sql;
	}

}
