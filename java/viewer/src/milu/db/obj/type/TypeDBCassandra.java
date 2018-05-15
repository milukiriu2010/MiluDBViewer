package milu.db.obj.type;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

public class TypeDBCassandra extends TypeDBAbstract {

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
				SchemaEntity typeEntity = SchemaEntityFactory.createInstance( rs.getString("type_name"), SchemaEntity.SCHEMA_TYPE.TYPE );
				typeEntityLst.add( typeEntity );
			}
		}
		
		return typeEntityLst;
	}

	@Override
	protected String listSQL(String schemaName) 
	{
		String sql =
			" select \n"           +
		    "   type_name \n" +
			" from  \n" +
			"   system_schema.types \n" +
			" where \n" +
			"   keyspace_name = '" + schemaName + "' \n" +
			" order by type_name";
		return sql;
	}

	// Source of Type
	@Override
	public String getSRC( String schemaName, String typeName ) throws SQLException
	{
		StringBuffer src = 
			new StringBuffer
			( 
				"DROP TYPE IF EXISTS " + typeName + "; \n" +
				"CREATE TYPE \n" + typeName + " \n"
			);
		
		String sql = 
			" select \n" +
			"   field_types, \n" +
			"   field_names  \n" +
			" from   \n" +
			"   system_schema.types \n" +
			" where  \n" +
			"   keyspace_name  = '" + schemaName + "' \n" +
			"   and  \n" +
			"   type_name = '"      + typeName   + "'";
		
		System.out.println( " -- getSRC(Type) --------------" );
		System.out.println( sql );
		System.out.println( " ------------------------------" );
		Statement stmt = this.myDBAbs.createStatement();
		ResultSet rs = stmt.executeQuery( sql );
		while ( rs.next() )
		{
			// [column, num]
			// [state, type, amount]
			String field_names = rs.getString( "field_names" );
			field_names = field_names.replaceAll( "(\\[|\\])", "" );
			String[] fieldNameLst = field_names.split( "," );
			
			// [text, int]
			// [map<text, int>, text, int]
			String field_types = rs.getString( "field_types" );
			field_types = field_types.replaceAll( "(\\[|\\])", "" );
			//argument_types = argument_types.replaceAll( "(<[^>]+>,|,)", "$1\n" );
			field_types = field_types.replaceAll( "(<[^>]+>|),", "$1\n" );
			System.out.println( "field_types[" + field_types + "]" );
			//String[] argTypeLst = argument_types.split( "," );
			String[] fieldTypeLst = field_types.split( "\n" );
			
			src.append( " ( " );
			for ( int i = 0; i < fieldTypeLst.length; i++ )
			{
				if ( i != 0 )
				{
					src.append( "," );
				}
				src.append( fieldNameLst[i] + " " );
				src.append( fieldTypeLst[i] );
			}
			src.append( " ) \n" );
			src.append( ";" );
		}
		return src.toString();
	}

}
