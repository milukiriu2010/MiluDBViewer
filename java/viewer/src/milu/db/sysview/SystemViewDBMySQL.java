package milu.db.sysview;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class SystemViewDBMySQL extends SystemViewDBAbstract {

	@Override
	public void selectEntityLst(String schemaName) throws SQLException 
	{
		this.clear();

		String sql = this.listSQL( schemaName );
		System.out.println( " -- selectSystemViewLst -----------" );
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
				/*
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
				*/
				this.viewLst.add( mapView );
			}
		}
	}

	@Override
	protected String listSQL(String schemaName) 
	{
		String sql = 
			" select table_name from information_schema.tables \n" +
			" where \n" +
			" table_type='SYSTEM VIEW' \n" + 
			" and \n" + 
			" table_schema = '" + schemaName + "' \n" +
			" order by table_name";
		return sql;
	}

}
