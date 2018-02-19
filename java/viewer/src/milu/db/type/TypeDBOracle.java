package milu.db.type;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class TypeDBOracle extends TypeDBAbstract 
{

	@Override
	public void selectEntityLst(String schemaName) throws SQLException 
	{
		this.clear();

		String sql = this.listSQL( schemaName );
		System.out.println( " -- selectTypeLst -----------------" );
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
				mapView.put( "typeName", rs.getString("object_name") );
				mapView.put( "status"  , rs.getString("status") );
				this.typeLst.add( mapView );
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
			"   object_type = 'TYPE' \n"  +
			" order by object_name";
		return sql;
	}

}
