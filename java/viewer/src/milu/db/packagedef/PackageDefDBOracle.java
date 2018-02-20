package milu.db.packagedef;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PackageDefDBOracle extends PackageDefDBAbstract 
{

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
			"   object_type = 'PACKAGE' \n"  +
			" order by object_name";
		return sql;
	}

	// Source of Package Definition
	@Override
	public String getSRC( String schemaName, String packageDefName ) throws SQLException
	{
		StringBuffer src = new StringBuffer( "CREATE OR REPLACE \n" );
		
		String sql = 
			" select \n" +
			"   text \n" +
			" from   \n" +
			"   all_source \n" +
			" where  \n" +
			"   owner = '" + schemaName     + "' \n" +
			"   and  \n" +
			"   name  = '" + packageDefName + "' \n" +
			"   and  \n" +
			"   type  = 'PACKAGE' \n" +
			" order by line";
		
		System.out.println( " -- getSRC(Package Definition) --------------" );
		System.out.println( sql );
		System.out.println( " --------------------------------------------" );
		Statement stmt = this.myDBAbs.createStatement();
		ResultSet rs = stmt.executeQuery( sql );
		while ( rs.next() )
		{
			src.append( rs.getString("text") );
		}
		return src.toString();
	}
	
}
