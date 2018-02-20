package milu.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

import milu.entity.schema.SchemaEntity.SCHEMA_TYPE;

import java.util.HashMap;

public class MyDBCassandra extends MyDBAbstract
{
	public MyDBCassandra()
	{
		this.suppoertedTypeLst.add( SCHEMA_TYPE.ROOT_TABLE );
		this.suppoertedTypeLst.add( SCHEMA_TYPE.ROOT_MATERIALIZED_VIEW );
		this.suppoertedTypeLst.add( SCHEMA_TYPE.ROOT_FUNC );
		this.suppoertedTypeLst.add( SCHEMA_TYPE.ROOT_AGGREGATE );
		this.suppoertedTypeLst.add( SCHEMA_TYPE.ROOT_TYPE );
	}
	
	@Override
	protected void loadDriver() throws ClassNotFoundException
	{
		Class.forName( "com.github.cassandra.jdbc.CassandraDriver" );
	}

	@Override
	public String getDriverUrl(Map<String, String> dbOptMap)
	{
		this.createConnectionParameter( dbOptMap );
		return this.url;
	}

	@Override
	public int getDefaultPort()
	{
		return 9042;
	}
	
	/**
	 * Name on GUI Items
	 */
	@Override
	public String toString()
	{
		return "Cassandra";
	}

	@Override
	protected void createConnectionParameter(Map<String, String> dbOptMap) 
	{
		this.url =
			"jdbc:c*:datastax://"+
			dbOptMap.get( "Host" )+":"+dbOptMap.get( "Port" )+"/"+
			dbOptMap.get( "DBName" );
	}
	
	/**
	 * SQL to get schema lists of schema.
	 * call by getSchemaLst
	 *************************************
	 * @return String
	 */
	/*
	@Override
	protected String schemaSchemaSQL()
	{
		String sql = "select keyspace_name, durable_writes from system_schema.keyspaces";
		return sql;
	}
	*/
	
	/**
	 * SQL to get table lists of schema.
	 * call by getSchemaTable
	 *************************************
	 * https://www.i2tutorials.com/cassandra-tutorial/cassandra-create-keyspace/
	 * select * from system_schema.keyspaces
	 * select * from system_schema.tables
	 *************************************
	 * @return String
	 */
	/*
	@Override
	protected String schemaTableSQL( String schema )
	{
		String sql =
			" select \n"           +
			"   keyspace_name, \n" +
			"   table_name     \n" +
			" from \n"             +
			"   system_schema.tables \n" +
			" where \n" +
			"   keyspace_name = '" + schema + "' \n" +
			" order by table_name";
		return sql;
	}
	*/
	
	
	/**
	 * Get SchemaInfo(Index)
	 *********************************
	 * @return List<Map<String>>
	 * ex) postgres
	 * +----------+-------------------+----------------------+-----------+------------+------------+---------+------------------------+---------------+---------------+
	 * + SCHEMA   + TABLE             + INDEX                + is_unique + is_primary + index_type + indkey  + index_keys             + is_functional + is_partial    +
	 * +----------+-------------------+----------------------+-----------+------------+------------+---------+------------------------+---------------+---------------+
	 * + public   + city              + city_pkey            + t         + t          + btree      + 1       + {id}                   + f             + f             +
	 * + public   + country           + country_pkey         + t         + t          + btree      + 1       + {code}                 + f             + f             +
	 * + public   + countrylanguage   + countrylanguage_pkey + t         + t          + btree      + 1 2     + {countrycode,language} + f             + f             +
	 * +----------+-------------------+----------------------+-----------+------------+------------+---------+------------------------+---------------+---------------+
	 * @throws SQLException
	 *********************************
	 */
	/*
	public List<Map<String,String>> getIndexBySchemaTable( String schema, String table )
		throws SQLException
	{
		List<Map<String,String>> dataLst = new ArrayList<Map<String,String>>();
		
		String sql = this.schemaIndexSQL( schema, table );
		System.out.println( " -- getIndexBySchemaTable(Cassandra) -----------" );
		System.out.println( sql );
		System.out.println( " -----------------------------------------------" );
		
		Statement stmt = this.conn.createStatement();
		ResultSet rs = stmt.executeQuery( sql );
		
		// column name list for "partition_key"
		List<String> partitionColumnLst = new LinkedList<String>();
		
		// column name list for "clustering"
		List<String> clusteringColumnLst = new LinkedList<String>();
		
		while ( rs.next() )
		{
			String kind     = rs.getString( "kind" );
			String position = rs.getString( "position" );
			int pos = Integer.parseInt( position );
			if ( "partition_key".equals(kind) )
			{
				if ( pos > partitionColumnLst.size() )
				{
					pos = partitionColumnLst.size();
				}
				partitionColumnLst.add( pos, rs.getString("column_name") );
			}
			else if ( "clustering".equals(kind) )
			{
				if ( pos > clusteringColumnLst.size() )
				{
					pos = clusteringColumnLst.size();
				}
				clusteringColumnLst.add( pos, rs.getString("column_name") );
			}
		}
		
		// partition_key
		Map<String,String> partitionKey  = new HashMap<String,String>();
		partitionKey.put( "index_name", "partition_key" );
		partitionKey.put( "is_primary", "t" );
		partitionKey.put( "index_keys", String.join( ",", partitionColumnLst ) );
		dataLst.add( partitionKey );

		if ( clusteringColumnLst.size() > 0 )
		{
			Map<String,String> clusteringKey = new HashMap<String,String>();
			clusteringKey.put( "index_name", "clustering" );
			clusteringKey.put( "index_keys", String.join( ",", clusteringColumnLst ) );
			dataLst.add( clusteringKey );
		}
		
		return dataLst;
	}
	*/
	/**
	 * SQL to get index lists of schema.
	 * call by getIndexBySchemaTable
	 *****************************************
	 * @param schema
	 * @param table
	 * @return
	 *****************************************
	 */
	/*
	protected String schemaIndexSQL( String schema, String table )
	{
		String sql =
			" select         \n" +
		    "   column_name, \n" +
		    "   kind,        \n" +
		    "   position,    \n" +
		    "   clustering_order \n" +
			" from           \n" +
			"   system_schema.columns \n" +
			" where \n" +
			"   keyspace_name = '" + schema + "' \n" +
			"   and \n" +
			"   table_name = '" + table + "'";
		return sql;
	}
	*/
	/**
	 * Get SchemaInfo(Table Definition)
	 ***************************************
	 * @param schema
	 * @param table
	 * @return
	 * @throws SQLException
	 */
	/*
	public List<Map<String,String>> getTableDefBySchemaTable( String schema, String table )
			throws SQLException
	{
		List<Map<String,String>> dataLst = new ArrayList<Map<String,String>>();
		
		String sql = this.schemaTableDefSQL( schema, table );

		System.out.println( " -- getTableDefBySchemaTable(Cassandra) -----------" );
		System.out.println( sql );
		System.out.println( " --------------------------------------------------" );
		
		Statement stmt = this.conn.createStatement();
		ResultSet rs = stmt.executeQuery( sql );
		while ( rs.next() )
		{
			Map<String,String> dataRow = new HashMap<String,String>();
			dataRow.put( "column_name"   , rs.getString("column_name") );
			dataRow.put( "data_type"     , rs.getString("type") );
			dataRow.put( "data_size"     , null );
			String nullable = "NULL OK";
			// partition_key => PRIMARY KEY
			// clustering    => part of PRIMARY KEY
			// regular       => 
			String kind = rs.getString("kind");
			if ( "partition_key".equals(kind) )
			{
				nullable = "NULL NG";
			}
			else if ( "clustering".equals(kind) )
			{
				nullable = "NULL NG";
			}
			dataRow.put( "nullable"      , nullable );
			dataRow.put( "data_default"  , "" );
			dataLst.add( dataRow );
		}
		return dataLst;
	}
	*/	
	
	/**
	 * SQL to get table definition
	 * call by getTableDefBySchemaTable
	 *****************************************
	 * @param schema
	 * @param table
	 * @return
	 */
	/*
	protected String schemaTableDefSQL( String schema, String table )
	{
		String sql =
			" select         \n" +
		    "   column_name, \n" +
		    "   type,        \n" +
		    "   kind         \n" +
			" from           \n" +
			"   system_schema.columns \n" +
			" where \n" +
			"   keyspace_name = '" + schema + "' \n" +
			"   and \n" +
			"   table_name = '" + table + "'";
		return sql;
	}
	*/
	
	/**
	 * SQL to get materialized view lists of schema.
	 * call by getSchemaMaterializedView
	 *************************************************
	 * see system_schema.columns to get DDL
	 *************************************************
	 * @return schema
	 */
	/*
	protected String schemaMaterializedViewSQL( String schema )
	{
		String sql =
			" select \n"           +
		    "   keyspace_name, \n" +
		    "   view_name      \n" +
			" from  \n" +
			"   system_schema.views \n" +
			" where \n" +
			"   keyspace_name = '" + schema + "' \n" +
			" order by view_name";
		return sql;
	}
	*/
	
	/**
	 * SQL to get function lists of schema.
	 * call by getSchemaFunc
	 *************************************
	 * @return schema
	 */
	/*
	@Override
	protected String schemaFuncSQL( String schema )
	{
		String sql =
			" select \n"           +
		    "   keyspace_name, \n" +
		    "   function_name  \n" +
			" from  \n" +
			"   system_schema.functions \n" +
			" where \n" +
			"   keyspace_name = '" + schema + "' \n" +
			" order by function_name";
		return sql;
	}
	*/
	
	/**
	 * Get SchemaInfo(Function Source)
	 ***************************************
	 * @param schema
	 * @param function
	 * @return
	 * @throws SQLException
	 */
	/*
	@Override
	public String getFunctionSourceBySchemaFunc( String schema, String function )
			throws SQLException
	{
		StringBuffer src = 
			new StringBuffer
			( 
				"CREATE OR REPLACE FUNCTION \n" + function 
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
			"   keyspace_name  = '" + schema   + "' \n" +
			"   and  \n" +
			"   function_name  = '" + function + "'";
		
		System.out.println( " -- getFunctionSourceBySchemaFunc -----------" );
		System.out.println( sql );
		System.out.println( " --------------------------------------------" );
		Statement stmt = this.conn.createStatement();
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
	*/
	/**
	 * SQL to get aggregate lists of schema.
	 * call by getSchemaFunc
	 *************************************
	 * @return schema
	 * @throws UnsupportedOperationException
	 */
	/*
	@Override
	protected String schemaAggregateSQL( String schema )
		throws UnsupportedOperationException
	{
		String sql =
			" select \n"           +
		    "   keyspace_name, \n" +
		    "   aggregate_name \n" +
			" from  \n" +
			"   system_schema.aggregates \n" +
			" where \n" +
			"   keyspace_name = '" + schema + "' \n" +
			" order by aggregate_name";
		return sql;
	}
	*/
	
	/**
	 * Get SchemaInfo(Aggregate Source)
	 ***************************************
	 * @param schema
	 * @param aggregate
	 * @return
	 * @throws SQLException
	 */
	/*
	public String getAggregateSourceBySchemaAggregate( String schema, String aggregate )
		throws 
			SQLException
	{
		StringBuffer src = 
			new StringBuffer
			( 
				"CREATE OR REPLACE AGGREGATE \n" + aggregate 
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
			"   keyspace_name  = '" + schema    + "' \n" +
			"   and  \n" +
			"   aggregate_name = '" + aggregate + "'";
		
		System.out.println( " -- getAggregateSourceBySchemaAggregate -----------" );
		System.out.println( sql );
		System.out.println( " --------------------------------------------------" );
		Statement stmt = this.conn.createStatement();
		ResultSet rs = stmt.executeQuery( sql );
		while ( rs.next() )
		{
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
	*/
	/**
	 * SQL to get type lists of schema.
	 * call by getSchemaType
	 *************************************
	 * @return schema
	 */
	/*
	@Override
	protected String schemaTypeSQL( String schema )
	{
		String sql =
			" select \n"           +
		    "   keyspace_name, \n" +
		    "   type_name \n" +
			" from  \n" +
			"   system_schema.types \n" +
			" where \n" +
			"   keyspace_name = '" + schema + "' \n" +
			" order by type_name";
		return sql;
	}
	*/
	
	/**
	 * Get SchemaInfo(Type Source)
	 ***************************************
	 * @param schema
	 * @param type
	 * @return
	 * @throws SQLException
	 */
	/*
	public String getTypeSourceBySchemaType( String schema, String type )
		throws 
			SQLException
	{
		StringBuffer src = 
			new StringBuffer
			( 
				"DROP TYPE IF EXISTS " + type + "; \n" +
				"CREATE TYPE \n" + type + " \n"
			);
		
		String sql = 
			" select \n" +
			"   field_types, \n" +
			"   field_names  \n" +
			" from   \n" +
			"   system_schema.types \n" +
			" where  \n" +
			"   keyspace_name  = '" + schema + "' \n" +
			"   and  \n" +
			"   type_name = '"      + type   + "'";
		
		System.out.println( " -- getTypeSourceBySchemaType -----------" );
		System.out.println( sql );
		System.out.println( " ----------------------------------------" );
		Statement stmt = this.conn.createStatement();
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
	*/
}
