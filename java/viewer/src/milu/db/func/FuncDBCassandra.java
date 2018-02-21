package milu.db.func;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

public class FuncDBCassandra extends FuncDBAbstract 
{

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
				mapView.put( "funcName", rs.getString("function_name") );
				this.funcLst.add( mapView );
				*/
				SchemaEntity funcEntity = SchemaEntityFactory.createInstance( rs.getString("function_name"), SchemaEntity.SCHEMA_TYPE.FUNC );
				funcEntityLst.add( funcEntity );
			}
		}
		
		return funcEntityLst;
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

	// Source of Function
	@Override
	public String getSRC( String schemaName, String funcName ) throws SQLException
	{
		StringBuffer src = 
			new StringBuffer
			( 
				"CREATE OR REPLACE FUNCTION \n" + funcName 
			);
		
		String sql = 
			" select \n" +
			"   argument_types,       \n" +
			"   argument_names,       \n" +
			"   body,                 \n" +
			"   called_on_null_input, \n" +
			"   language,             \n" +
			"   return_type           \n" +
			" from   \n" +
			"   system_schema.functions \n" +
			" where  \n" +
			"   keyspace_name  = '" + schemaName   + "' \n" +
			"   and  \n" +
			"   function_name  = '" + funcName + "'";
		
		System.out.println( " -- getSRC(Func) -----------" );
		System.out.println( sql );
		System.out.println( " ---------------------------" );
		Statement stmt = this.myDBAbs.createStatement();
		ResultSet rs = stmt.executeQuery( sql );
		while ( rs.next() )
		{
			// [column, num]
			// [state, type, amount]
			String argument_names = rs.getString( "argument_names" );
			argument_names = argument_names.replaceAll( "(\\[|\\])", "" );
			String[] argNameLst = argument_names.split( "," );
			
			// [text, int]
			// [map<text, int>, text, int]
			String argument_types = rs.getString( "argument_types" );
			argument_types = argument_types.replaceAll( "(\\[|\\])", "" );
			//argument_types = argument_types.replaceAll( "(<[^>]+>,|,)", "$1\n" );
			argument_types = argument_types.replaceAll( "(<[^>]+>|),", "$1\n" );
			System.out.println( "argument_types[" + argument_types + "]" );
			//String[] argTypeLst = argument_types.split( "," );
			String[] argTypeLst = argument_types.split( "\n" );
			
			src.append( " ( " );
			for ( int i = 0; i < argNameLst.length; i++ )
			{
				if ( i != 0 )
				{
					src.append( "," );
				}
				src.append( argNameLst[i] + " " + argTypeLst[i] );
			}
			src.append( " ) \n" );
			
			
			if ( "true".equals( rs.getString( "called_on_null_input") ) )
			{
				src.append( "CALLED ON NULL INPUT \n" );
			}
			else
			{
				src.append( "RETURNS NULL ON NULL INPUT \n" );
			}
			src.append( "RETURNS "  + rs.getString("return_type") + " \n" );
			src.append( "LANGUAGE " + rs.getString("language") + " AS \n" );
			src.append( "$$ " + rs.getString("body") + " $$; \n" );
		}
		return src.toString();
	}
}
