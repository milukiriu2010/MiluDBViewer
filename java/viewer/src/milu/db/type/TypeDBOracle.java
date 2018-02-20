package milu.db.type;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

public class TypeDBOracle extends TypeDBAbstract 
{

	@Override
	public List<SchemaEntity> selectEntityLst(String schemaName) throws SQLException 
	{
		List<SchemaEntity>  typeEntityLst = new ArrayList<>();

		String sql = this.listSQL( schemaName );
		System.out.println( " -- selectEntityLst(Type) ---------" );
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
				mapView.put( "typeName", rs.getString("object_name") );
				mapView.put( "status"  , rs.getString("status") );
				this.typeLst.add( mapView );
				*/
				SchemaEntity typeEntity = SchemaEntityFactory.createInstance( rs.getString("object_name"), SchemaEntity.SCHEMA_TYPE.TYPE );
				String strStatus = rs.getString("status");
				if ( strStatus != null && "INVALID".equals(strStatus) )
				{
					typeEntity.setState( SchemaEntity.STATE.INVALID );
				}
				typeEntityLst.add( typeEntity );
			}
		}
		
		return typeEntityLst;
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

	// Source of Type
	@Override
	public String getSRC( String schemaName, String typeName ) throws SQLException
	{
		StringBuffer src = new StringBuffer( "CREATE OR REPLACE \n" );
		
		String sql = 
			" select \n" +
			"   text \n" +
			" from   \n" +
			"   all_source \n" +
			" where  \n" +
			"   owner = '" + schemaName + "' \n" +
			"   and  \n" +
			"   name  = '" + typeName   + "' \n" +
			"   and  \n" +
			"   type  = 'TYPE' \n"  +
			" order by line";
		
		System.out.println( " -- getSRC(Type) --------------" );
		System.out.println( sql );
		System.out.println( " ------------------------------" );
		Statement stmt = this.myDBAbs.createStatement();
		ResultSet rs = stmt.executeQuery( sql );
		while ( rs.next() )
		{
			src.append( rs.getString("text") );
		}
		stmt.close();
		rs.close();
		return src.toString();
	}
}
