package milu.db.func;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
				/*
				Map<String, String> mapView = new HashMap<String,String>();
				mapView.put( "funcName", rs.getString("name") );
				this.funcLst.add( mapView );
				*/
				SchemaEntity funcEntity = SchemaEntityFactory.createInstance( rs.getString("name"), SchemaEntity.SCHEMA_TYPE.FUNC );
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
			"   name \n"       +
			" from   \n"       +
			"   mysql.proc \n" + 
			" where \n"        + 
			"   db = '" + schemaName + "' \n" +
			"   and \n"        +
			"   type = 'FUNCTION' \n" +
			" order by name";
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
}
