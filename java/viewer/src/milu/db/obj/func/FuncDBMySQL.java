package milu.db.obj.func;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

public class FuncDBMySQL extends FuncDBAbstract {

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
				SchemaEntity funcEntity = SchemaEntityFactory.createInstance( rs.getString("name"), SchemaEntity.SCHEMA_TYPE.FUNC );
				funcEntityLst.add( funcEntity );
			}
		}
		
		return funcEntityLst;
	}

	// -----------------------------------------
	// https://ocelot.ca/blog/blog/2017/08/22/no-more-mysql-proc-in-mysql-8-0/
	// -----------------------------------------
	// MySQL 5.7.22
	//     mysql.proc
	// -----------------------------------------
	// MySQL 8.0
	//     information_schema.routines
	//     information_schema.parameters
	// -----------------------------------------
	@Override
	protected String listSQL(String schemaName) 
	{
		/*
		String sql = 
			" select distinct \n" +
			"   name \n"       +
			" from   \n"       +
			"   mysql.proc \n" + 
			" where \n"        + 
			"   db = '" + schemaName + "' \n" +
			"   and \n"        +
			"   type = 'FUNCTION' \n" +
			" order by name";
		*/
		String sql = 
			" select distinct \n" +
			"   routine_name  name \n"       +
			" from   \n"       +
			"   information_schema.routines \n" + 
			" where \n"        + 
			"   routine_schema = '" + schemaName + "' \n" +
			"   and \n"        +
			"   routine_type = 'FUNCTION' \n" +
			" order by routine_name";
		
		return sql;
	}

	// Source of Function
	@Override
	public String getSRC( String schemaName, String funcName ) throws SQLException
	{
		StringBuffer src = 
			new StringBuffer
			( 
				"DELIMITER //\n" +
				"DROP FUNCTION IF EXISTS " + funcName + "//\n" +
				"CREATE FUNCTION \n" + funcName 
			);

		String sql1 = 
				" select \n" +
				"   parameter_mode,  \n" +
				"   parameter_name,  \n" +
				"   convert( data_type using utf8 ) data_type_convert \n" +
				" from   \n" +
				"   information_schema.parameters \n" +
				" where  \n" +
				"   specific_schema = '" + schemaName   + "' \n" +
				"   and  \n" +
				"   specific_name  = '" + funcName + "' \n" +
				"   and  \n" +
				"   ordinal_position > 0 \n" +
				" order by ordinal_position";
		System.out.println( " -- getSRC1(Func) -----------" );
		System.out.println( sql1 );
		System.out.println( " ----------------------------" );
		Statement stmt1 = this.myDBAbs.createStatement();
		ResultSet rs1 = stmt1.executeQuery( sql1 );
		
		List<String>  paramLst = new ArrayList<>();
		while ( rs1.next() )
		{
			paramLst.add(
				rs1.getString( "parameter_name") + " " +
				rs1.getString( "data_type_convert")		
			);
		}
		
		String lineSP = System.getProperty("line.separator");
		String strParams = paramLst.stream().collect(Collectors.joining(", ","( "," )"+lineSP));
		src.append(strParams);

		stmt1.close();
		rs1.close();
		
		
		String sql2 = 
			" select \n" +
			"   is_deterministic,  \n" +
			"   dtd_identifier,    \n" +
			"   routine_definition \n" +
			" from   \n" +
			"   information_schema.routines \n" +
			" where  \n" +
			"   routine_schema = '" + schemaName   + "' \n" +
			"   and  \n" +
			"   routine_name  = '" + funcName + "' \n" +
			"   and  \n" +
			"   routine_type  = 'FUNCTION'";
		
		System.out.println( " -- getSRC2(Func) -----------" );
		System.out.println( sql2 );
		System.out.println( " ----------------------------" );
		Statement stmt2 = this.myDBAbs.createStatement();
		ResultSet rs2 = stmt2.executeQuery( sql2 );
		while ( rs2.next() )
		{
			src.append( "RETURNS " + rs2.getString("dtd_identifier") + " \n" );
			if ( "YES".equals( rs2.getString( "is_deterministic") ) )
			{
				src.append( "DETERMINISTIC \n" );
			}
			src.append( rs2.getString("routine_definition") + "; \n" );
			src.append( "//\n" );
			src.append( "\n" );
			src.append( "DELIMITER ;" );
		}
		
		stmt2.close();
		rs2.close();
		return src.toString();
	}
	
	/*
	 * for MySQL 5.7.22 
	// Source of Function
	@Override
	public String getSRC( String schemaName, String funcName ) throws SQLException
	{
		StringBuffer src = 
			new StringBuffer
			( 
				"DELIMITER //\n" +
				"DROP FUNCTION IF EXISTS " + funcName + "//\n" +
				"CREATE FUNCTION \n" + funcName 
			);
		
		String sql = 
			" select \n" +
			"   is_deterministic, \n" +
			"   param_list,       \n" +
			"   returns,          \n" +
			"   body_utf8"            +
			" from   \n" +
			"   mysql.proc \n" +
			" where  \n" +
			"   db = '" + schemaName   + "' \n" +
			"   and  \n" +
			"   name  = '" + funcName + "' \n" +
			"   and  \n" +
			"   type  = 'FUNCTION'";
		
		System.out.println( " -- getSRC(Func) -----------" );
		System.out.println( sql );
		System.out.println( " ---------------------------" );
		Statement stmt = this.myDBAbs.createStatement();
		ResultSet rs = stmt.executeQuery( sql );
		while ( rs.next() )
		{
			src.append( "(" + rs.getString("param_list") + ") \n" );
			src.append( "RETURNS " + rs.getString("returns") + " \n" );
			if ( "YES".equals( rs.getString( "is_deterministic") ) )
			{
				src.append( "DETERMINISTIC \n" );
			}
			src.append( rs.getString("body_utf8") + " \n" );
			src.append( "//\n" );
			src.append( "\n" );
			src.append( "DELIMITER ;" );
		}
		return src.toString();
	}
	*/
}
