package milu.db.obj.packagebody;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PackageBodyDBOracle extends PackageBodyDBAbstract 
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
			"   object_type = 'PACKAGE BODY' \n"  +
			" order by object_name";
		return sql;
	}

	// Source of Package Body
	@Override
	public String getSRC( String schemaName, String packageBodyName ) throws SQLException
	{
		StringBuffer src = new StringBuffer( "CREATE OR REPLACE \n" );
		
		String sql = 
			" select \n" +
			"   text \n" +
			" from   \n" +
			"   all_source \n" +
			" where  \n" +
			"   owner = '" + schemaName      + "' \n" +
			"   and  \n" +
			"   name  = '" + packageBodyName + "' \n" +
			"   and  \n" +
			"   type  = 'PACKAGE BODY' \n" +
			" order by line";
		
		System.out.println( " -- getSRC(Package Body) --------------" );
		System.out.println( sql );
		System.out.println( " --------------------------------------" );
		Statement stmt = this.myDBAbs.createStatement();
		ResultSet rs = stmt.executeQuery( sql );
		while ( rs.next() )
		{
			src.append( rs.getString("text") );
		}
		return src.toString();
	}

}
