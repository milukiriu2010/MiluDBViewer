package milu.db.obj.proc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

public class ProcDBMySQL extends ProcDBAbstract 
{

	@Override
	public List<SchemaEntity> selectEntityLst(String schemaName) throws SQLException 
	{
		List<SchemaEntity>  procEntityLst = new ArrayList<>();

		String sql = this.listSQL( schemaName );
		System.out.println( " -- selectEntityLst(Proc) ---------" );
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
				mapView.put( "procName", rs.getString("name") );
				this.procLst.add( mapView );
				*/
				SchemaEntity procEntity = SchemaEntityFactory.createInstance( rs.getString("name"), SchemaEntity.SCHEMA_TYPE.PROC );
				procEntityLst.add( procEntity );				
			}
		}
		
		return procEntityLst;
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
			"   type = 'PROCEDURE' \n" +
			" order by name";
		return sql;
	}

	// Source of Procedure
	@Override
	public String getSRC( String schemaName, String procName ) throws SQLException
	{
		StringBuffer src = 
			new StringBuffer
			( 
				"DELIMITER //\n" +
				"DROP PROCEDURE IF EXISTS " + procName + "//\n" +
				"CREATE PROCEDURE \n" + procName 
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
			"   name  = '" + procName + "' \n" +
			"   and  \n" +
			"   type  = 'PROCEDURE'";
		
		System.out.println( " -- getSRC(Proc) -----------" );
		System.out.println( sql );
		System.out.println( " ---------------------------" );
		Statement stmt = this.myDBAbs.createStatement();
		ResultSet rs = stmt.executeQuery( sql );
		while ( rs.next() )
		{
			src.append( "(" + rs.getString("param_list") + ") \n" );
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
