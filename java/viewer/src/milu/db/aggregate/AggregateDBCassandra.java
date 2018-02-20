package milu.db.aggregate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

public class AggregateDBCassandra extends AggregateDBAbstract {

	@Override
	public List<SchemaEntity> selectEntityLst(String schemaName) throws SQLException 
	{
		List<SchemaEntity>  aggregateEntityLst = new ArrayList<>();

		String sql = this.listSQL( schemaName );
		System.out.println( " -- selectEntityLst(Aggregate) ----" );
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
				SchemaEntity funcEntity = SchemaEntityFactory.createInstance( rs.getString("aggregate_name"), SchemaEntity.SCHEMA_TYPE.AGGREGATE );
				aggregateEntityLst.add( funcEntity );
			}
		}
		
		return aggregateEntityLst;
	}

	@Override
	protected String listSQL(String schemaName)
	{
		String sql =
			" select \n"           +
		    "   aggregate_name \n" +
			" from  \n" +
			"   system_schema.aggregates \n" +
			" where \n" +
			"   keyspace_name = '" + schemaName + "' \n" +
			" order by aggregate_name";
		return sql;
	}

	@Override
	public String getSRC(String schemaName, String aggregateName) throws SQLException {
		StringBuffer src = 
			new StringBuffer
			( 
				"CREATE OR REPLACE AGGREGATE \n" + aggregateName 
			);
		
		String sql = 
			" select \n" +
			"   argument_types, \n" +
			"   aggregate_name, \n" +
			"   final_func,     \n" +
			"   initcond,       \n" +
			"   return_type,    \n" +
			"   state_func,     \n" +
			"   state_type      \n" +
			" from   \n" +
			"   system_schema.aggregates \n" +
			" where  \n" +
			"   keyspace_name  = '" + schemaName    + "' \n" +
			"   and  \n" +
			"   aggregate_name = '" + aggregateName + "'";
		
		System.out.println( " -- getSRC(Aggregate) ------" );
		System.out.println( sql );
		System.out.println( " ---------------------------" );
		Statement stmt = this.myDBAbs.createStatement();
		ResultSet rs = stmt.executeQuery( sql );
		while ( rs.next() )
		{
			/*
			// [column, num]
			// [state, type, amount]
			String argument_names = rs.getString( "aggregate_name" );
			argument_names = argument_names.replaceAll( "(\\[|\\])", "" );
			String[] argNameLst = argument_names.split( "," );
			*/
			
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
			for ( int i = 0; i < argTypeLst.length; i++ )
			{
				if ( i != 0 )
				{
					src.append( "," );
				}
				src.append( argTypeLst[i] );
			}
			src.append( " ) \n" );
			
			src.append( "SFUNC "     + rs.getString("state_func") + " \n" );
			String state_type = rs.getString("state_type");
			System.out.println( "state_type[" + state_type + "]" );
			state_type = state_type.replaceAll( "frozen<(.+)>", "$1" );
			src.append( "STYPE "     + state_type + " \n" );
			String final_func = rs.getString("final_func");
			if ( final_func != null && "null".equals(final_func) == false )
			{
				src.append( "FINALFUNC " + final_func + " \n" );
			}
			src.append( "INITCOND "  + rs.getString("initcond")   + " \n" );
			src.append( ";" );
		}
		return src.toString();
	}

}
