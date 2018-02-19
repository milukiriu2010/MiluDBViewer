package milu.db.type;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class TypeDBPostgres extends TypeDBAbstract 
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
				mapView.put( "typeName", rs.getString("user_defined_type_name") );
				mapView.put( "status"  , rs.getString("status") );
				this.typeLst.add( mapView );
			}
		}
	}

	@Override
	protected String listSQL(String schemaName) 
	{
		String sql = 
			" select \n" +
			"   user_defined_type_name    \n" +
			" from  \n"  +
			"   information_schema.user_defined_types \n" + 
			" where \n"  + 
			"   user_defined_type_schema = '" + schemaName + "' \n" +
			" order by user_defined_type_schema, user_defined_type_name";
		return sql;
	}

}
