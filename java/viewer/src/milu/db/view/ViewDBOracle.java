package milu.db.view;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import milu.db.MyDBAbstract;

public class ViewDBOracle extends ViewDBAbstract 
{
	public ViewDBOracle( MyDBAbstract myDBAbs )
	{
		super( myDBAbs );
	}
	
	@Override
	public void selectViewLst(String schemaName) throws SQLException 
	{
		Statement stmt   = null;
		ResultSet rs     = null;
		
		try
		{
			this.clear();
			
			stmt = this.myDBAbs.createStatement();
			
			String sql = this.viewLstSQL( schemaName );
			System.out.println( " -- selectViewLst ---------------" );
			System.out.println( sql );
			System.out.println( " ----------------------------------" );
			rs   = stmt.executeQuery( sql );
						
			while ( rs.next() )
			{
				Map<String, String> mapView = new HashMap<String,String>();
				mapView.put( "name"  , rs.getString("object_name") );
				mapView.put( "status", rs.getString("status") );
				this.dataLst.add( mapView );
			}
		}
		finally
		{
			try
			{
				if ( stmt != null )
				{
					stmt.close();
				}
			}
			catch ( SQLException sqlEx1 )
			{
				// suppress close error
			}
			
			try
			{
				if ( rs != null )
				{
					rs.close();
				}
			}
			catch ( SQLException sqlEx2 )
			{
				// suppress close error
			}
		}
	}

	@Override
	protected String viewLstSQL(String schemaName) 
	{
		String sql = 
			" select \n" +
			"   object_name, \n" +
			"   status \n" +
			" from \n" +
			"   all_objects \n" +
			" where \n" +
			"   object_type='VIEW' \n" + 
			"   and \n" + 
			"   owner = '" + schemaName + "' \n" +
			" order by owner, object_name";
		return sql;
	}

}
