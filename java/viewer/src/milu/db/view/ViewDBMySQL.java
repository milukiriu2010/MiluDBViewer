package milu.db.view;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import milu.db.MyDBAbstract;

public class ViewDBMySQL extends ViewDBAbstract 
{
	public ViewDBMySQL( MyDBAbstract myDBAbs )
	{
		super( myDBAbs );
	}

	@Override
	public void selectViewLst(String schemaName ) throws SQLException 
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
				mapView.put( "viewName", rs.getString("table_name") );
				String table_comment = rs.getString("table_comment");
				System.out.println( "table_comment:" + table_comment );
				if ( table_comment != null && table_comment.contains("invalid") )
				{
					mapView.put( "status", "INVALID" );
				}
				else
				{
					mapView.put( "status", "VALID" );
				}
				this.viewLst.add( mapView );
			}
		}
	}

	@Override
	protected String viewLstSQL(String schemaName ) 
	{
		String sql = 
			" select \n" +
			"   *    \n" +
			" from   \n" +
	  		"   information_schema.tables \n" +
			" where \n" +
			"   table_type='VIEW' \n" + 
			"   and \n" + 
			"   table_schema = '" + schemaName + "' \n" +
			" order by table_schema, table_name";
		return sql;
	}

}
