package milu.db.func;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

public class FuncDBPostgres extends FuncDBAbstract {

	@Override
	public List<SchemaEntity> selectEntityLst(String schemaName) throws SQLException 
	{
		List<SchemaEntity>  funcEntityLst = new ArrayList<>();

		String sql = this.listSQL( schemaName );
		System.out.println( " -- selectEntityLst(Func) ---------" );
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
				/*
				Map<String, String> mapView = new HashMap<String,String>();
				mapView.put( "funcName", rs.getString("routine_name") );
				this.funcLst.add( mapView );
				*/
				SchemaEntity funcEntity = SchemaEntityFactory.createInstance( rs.getString("routine_name"), SchemaEntity.SCHEMA_TYPE.FUNC );
				funcEntityLst.add( funcEntity );				
			}
		}
		
		return funcEntityLst;
	}

	@Override
	protected String listSQL(String schemaName) 
	{
		String sql = 
			" select distinct \n" +
			"   routines.routine_name  routine_name \n" +
			" from  \n"  +
			"   information_schema.routines \n" + 
			" where \n"  + 
			"   routines.routine_schema = '" + schemaName + "' \n" +
			" order by routines.routine_name";
		return sql;
	}


	// Source of Function
	@Override
	public String getSRC( String schemaName, String funcName ) throws SQLException
	{
		StringBuffer src = 
			new StringBuffer
			( 
				"CREATE OR REPLACE FUNCTION \n" + funcName 
			);
		
		
		String sql1 =
			" SELECT \n" +
			"   p.proname, \n" +
			"   pg_get_function_result(p.oid)              results,    \n" +
			"   pg_get_function_identity_arguments(p.oid)  param_list, \n" +
			"   l.lanname      external_language, \n" +
			"   p.proisstrict  is_null_call, \n"      +
			"   p.prosrc       prosrc \n"             +
			" FROM \n" +
			"   pg_proc p INNER JOIN \n" +
			"   pg_namespace n ON (p.pronamespace = n.oid) join \n" +
			"   pg_language  l ON (l.oid = p.prolang) \n" +
			" WHERE \n" +
			"   n.nspname = '" + schemaName   + "' \n" +
			"   and \n" +
			"   p.proname  = '" + funcName + "'	\n";
		System.out.println( " -- getSRC(Func) -----------" );
		System.out.println( sql1 );
		System.out.println( " ---------------------------" );
		Statement stmt1 = this.myDBAbs.createStatement();
		ResultSet rs1 = stmt1.executeQuery( sql1 );
		String quote = "$$";
		while ( rs1.next() )
		{
			src.append( "(" + rs1.getString("param_list") + ") " );
			src.append( "RETURNS " + rs1.getString("results") + " \n" );
			src.append( "AS " + quote + " \n" );
			src.append( rs1.getString("prosrc") + " \n" );
			src.append( quote );
			src.append( " LANGUAGE " + rs1.getString("external_language") + " \n" );
			if ( "t".equals( rs1.getString( "is_null_call") ) )
			{
				src.append( "RETURNS NULL ON NULL INPUT \n" );
			}
			src.append( "; \n" );
		}
		
		stmt1.close();
		rs1.close();
		
		return src.toString();
	}
}
