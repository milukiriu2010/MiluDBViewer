package milu.db.view;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import milu.db.MyDBAbstract;

public class ViewDBPostgres extends ViewDBAbstract
{
	public ViewDBPostgres( MyDBAbstract myDBAbs )
	{
		super( myDBAbs );
	}

	@Override
	public void selectViewLst(String schemaName) throws SQLException 
	{
		this.clear();

		String sql = this.viewLstSQL( schemaName );
		System.out.println( " -- selectViewLst ---------------" );
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
	protected String viewLstSQL(String schemaName) 
	{
		String sql = 
			"select \n" +
			"  c.relname  relname \n" +
			"from \n" +
			"  pg_class c join \n" +
			"  pg_namespace n on n.oid = c.relnamespace \n" +
			"where \n" +
			"  n.nspname = '" + schemaName + "' \n" +
			"  and \n" +
			"  c.relkind = 'v' \n" +
			"order by c.relname";
		return sql;
	}

}
