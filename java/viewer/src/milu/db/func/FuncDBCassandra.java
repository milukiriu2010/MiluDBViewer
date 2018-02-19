package milu.db.func;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class FuncDBCassandra extends FuncDBAbstract 
{

	@Override
	public void selectEntityLst(String schemaName) throws SQLException 
	{
		this.clear();

		String sql = this.listSQL( schemaName );
		System.out.println( " -- selectFuncLst -----------------" );
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
				mapView.put( "funcName", rs.getString("function_name") );
				this.funcLst.add( mapView );
			}
		}
	}

	@Override
	protected String listSQL(String schemaName) 
	{
		String sql =
			" select \n"           +
		    "   function_name  \n" +
			" from  \n" +
			"   system_schema.functions \n" +
			" where \n" +
			"   keyspace_name = '" + schemaName + "' \n" +
			" order by function_name";
		return sql;
	}

}
