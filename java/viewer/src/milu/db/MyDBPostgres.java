package milu.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import milu.entity.schema.SchemaEntity.SCHEMA_TYPE;

public class MyDBPostgres extends MyDBAbstract 
{
	public MyDBPostgres()
	{
		this.suppoertedTypeLst.add( SCHEMA_TYPE.ROOT_TABLE );
		this.suppoertedTypeLst.add( SCHEMA_TYPE.ROOT_VIEW );
		this.suppoertedTypeLst.add( SCHEMA_TYPE.ROOT_MATERIALIZED_VIEW );
		this.suppoertedTypeLst.add( SCHEMA_TYPE.ROOT_FUNC );
		this.suppoertedTypeLst.add( SCHEMA_TYPE.ROOT_TYPE );
		this.suppoertedTypeLst.add( SCHEMA_TYPE.ROOT_TRIGGER );
		this.suppoertedTypeLst.add( SCHEMA_TYPE.ROOT_SEQUENCE );
	}
	
	/**
	 * Load JDBC Driver
	 ***********************************************
	 * @throws ClassNotFoundException
	 */
	@Override
	protected void loadDriver() throws ClassNotFoundException
	{
		Class.forName( "org.postgresql.Driver" );
	}
	
	/**
	 * Get Driver URL
	 ***********************************************
	 */
	public String getDriverUrl( Map<String, String> dbOptMap )
	{
		this.createConnectionParameter( dbOptMap );
		return this.url;
	}
	
	/**
	 * Get Default Port Number
	 **********************************************
	 * @return 
	 */
	@Override
	public int getDefaultPort()
	{
		return 5432;
	}
	
	/**
	 * Name on GUI Items
	 */
	@Override
	public String toString()
	{
		return "PostgreSQL";
	}
	
	@Override
	protected void createConnectionParameter(Map<String, String> dbOptMap) 
	{
		// URL Example
		// "jdbc:postgresql://localhost:5432/miludb"
		this.url = 
				"jdbc:postgresql://"+
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
		String sql = "select schema_name from information_schema.schemata order by schema_name";
		return sql;
	}
	*/
	
	/**
	 * SQL to get table lists of schema.
	 * call by getSchemaTable
	 *************************************
	 * @return String
	 */
	/*
	@Override
	protected String schemaTableSQL( String schema )
	{
		String sql = 
			"select \n" +
			"  n.nspname, \n" +
			"  c.relname  \n" +
			"from \n" +
			"  pg_class c join \n" +
			"  pg_namespace n on n.oid = c.relnamespace \n" +
			"where \n" +
			"  n.nspname = '" + schema + "' \n" +
			"  and \n" +
			"  c.relkind = 'r' \n" +
			"order by c.relname";
		return sql;
	}
	*/
	/**
	 * SQL to get view lists of schema.
	 * call by getSchemaView
	 *************************************
	 * @return String
	 */
	/*
	@Override
	protected String schemaViewSQL( String schema )
	{
		String sql = 
			"select \n" +
			"  n.nspname, \n" +
			"  c.relname  \n" +
			"from \n" +
			"  pg_class c join \n" +
			"  pg_namespace n on n.oid = c.relnamespace \n" +
			"where \n" +
			"  n.nspname = '" + schema + "' \n" +
			"  and \n" +
			"  c.relkind = 'v' \n" +
			"order by c.relname";
		return sql;
	}
	*/
	
	/**
	 * SQL to get materialized view lists of schema.
	 * call by getSchemaMaterializedView
	 *************************************************
	 * @return schema
	 * @throws UnsupportedOperationException
	 */
	/*
	@Override
	protected String schemaMaterializedViewSQL( String schema )
	{
		String sql =
			" select \n" +
			"   n.nspname, \n" +
			"   c.relname  \n" +
			" from \n" +
			"   pg_class  c  join \n" +
			"   pg_namespace n on c.relnamespace = n.oid \n" +
			" where \n" +
			"   c.relkind = 'm' \n" +
			"   and \n" +
			"   n.nspname = '" + schema + "' \n" +
			" order by c.relname";
		return sql;
	}
	*/
	
	/**
	 * SQL to get index lists of schema.
	 * call by getIndexBySchemaTable
	 *****************************************
	 * About oid
	 *   https://www.postgresql.jp/document/8.0/html/datatype-oid.html
	 *****************************************
	 * https://stackoverflow.com/questions/6777456/list-all-index-names-column-names-and-its-table-name-of-a-postgresql-database	
SELECT
  U.usename                AS user_name,
  ns.nspname               AS schema_name,
  idx.indrelid :: REGCLASS AS table_name,
  i.relname                AS index_name,
  idx.indisunique          AS is_unique,
  idx.indisprimary         AS is_primary,
  am.amname                AS index_type,
  idx.indkey,
       ARRAY(
           SELECT pg_get_indexdef(idx.indexrelid, k + 1, TRUE)
           FROM
             generate_subscripts(idx.indkey, 1) AS k
           ORDER BY k
       ) AS index_keys,
  (idx.indexprs IS NOT NULL) OR (idx.indkey::int[] @> array[0]) AS is_functional,
  idx.indpred IS NOT NULL AS is_partial
FROM pg_index AS idx
  JOIN pg_class AS i
    ON i.oid = idx.indexrelid
  JOIN pg_am AS am
    ON i.relam = am.oid
  JOIN pg_namespace AS NS ON i.relnamespace = NS.OID
  JOIN pg_user AS U ON i.relowner = U.usesysid
WHERE NOT nspname LIKE 'pg%'
	 *****************************************
	 * https://stackoverflow.com/questions/2204058/list-columns-with-indexes-in-postgresql
select
    t.relname as table_name,
    i.relname as index_name,
    a.attname as column_name
from
    pg_class t,
    pg_class i,
    pg_index ix,
    pg_attribute a
where
    t.oid = ix.indrelid
    and i.oid = ix.indexrelid
    and a.attrelid = t.oid
    and a.attnum = ANY(ix.indkey)
    and t.relkind = 'r'
order by
    t.relname,
    i.relname
	 *****************************************
SELECT  
	c.relname AS table,
	f.attname AS column,  
	pg_catalog.format_type(f.atttypid,f.atttypmod) AS type,
	f.attnotnull AS notnull,  
	i.relname as index_name,
	CASE  
	    WHEN i.oid<>0 THEN 't'  
	    ELSE 'f'  
	END AS is_index,  
	CASE  
	    WHEN p.contype = 'p' THEN 't'  
	    ELSE 'f'  
	END AS primarykey,  
	CASE  
	    WHEN p.contype = 'u' THEN 't' 
	    WHEN p.contype = 'p' THEN 't' 
	    ELSE 'f'
	END AS uniquekey,
	CASE
	    WHEN f.atthasdef = 't' THEN d.adsrc
	END AS default  
FROM pg_attribute f  
JOIN pg_class c ON c.oid = f.attrelid  
JOIN pg_type t ON t.oid = f.atttypid  
LEFT JOIN pg_attrdef d ON d.adrelid = c.oid AND d.adnum = f.attnum  
LEFT JOIN pg_namespace n ON n.oid = c.relnamespace  
LEFT JOIN pg_constraint p ON p.conrelid = c.oid AND f.attnum = ANY (p.conkey)  
LEFT JOIN pg_class AS g ON p.confrelid = g.oid
LEFT JOIN pg_index AS ix ON f.attnum = ANY(ix.indkey) and c.oid = f.attrelid and c.oid = ix.indrelid 
LEFT JOIN pg_class AS i ON ix.indexrelid = i.oid 
WHERE c.relkind = 'r'::char  
AND n.nspname = 'public'  -- Replace with Schema name 
--AND c.relname = 'nodes'  -- Replace with table name, or Comment this for get all tables
AND f.attnum > 0
ORDER BY c.relname,f.attname;
	 *****************************************
	 * @param schema
	 * @param table
	 * @return SQL
	 *****************************************
	 */
	protected String schemaIndexSQL( String schema, String table )
	{
		// The following SQL doesn't work well for "information_schema"
		if ( "information_schema".equals( schema ) )
		{
			return null;
		}
		
		String sql = 
			" SELECT \n" +
			"   U.usename                AS user_name, \n"   +
			"   ns.nspname               AS schema_name, \n" +
			"   idx.indrelid::REGCLASS   AS table_name, \n"  +
			"   i.relname                AS index_name, \n"  +
			"   idx.indisunique          AS is_unique,  \n"  +
			"   idx.indisprimary         AS is_primary, \n"  +
			"   am.amname                AS index_type, \n"  +
			"   idx.indkey, \n"                              +
			"   ARRAY( \n"                                                +
			"     SELECT pg_get_indexdef(idx.indexrelid, k + 1, TRUE) \n" +
			"     FROM \n"                                                +
			"       generate_subscripts(idx.indkey, 1) AS k \n"            +
			"     ORDER BY k \n"                                          +
			"   ) AS index_keys, \n"                                      +
			"   (idx.indexprs IS NOT NULL) OR \n"                     +
			"   (idx.indkey::int[] @> array[0]) AS is_functional, \n" +
			"   case idx.indisvalid       \n" + 
			"     when 't' then 'VALID'   \n" + 
			"     else          'INVALID' \n" + 
			"   end  status,              \n" + 
			"   idx.indpred IS NOT NULL AS is_partial \n" +
			" FROM \n" +
			"   pg_index          AS idx \n" +
			"   JOIN pg_class     AS i   ON i.oid          = idx.indexrelid \n" +
			"   JOIN pg_am        AS am  ON i.relam        = am.oid         \n" +
			"   JOIN pg_namespace AS NS  ON i.relnamespace = NS.OID \n"         +
			"   JOIN pg_user      AS U   ON i.relowner     = U.usesysid \n"     +
			" WHERE \n"                          + 
			"   NS.nspname = '" + schema + "' \n" +
			"   and \n"                          +
			"   idx.indrelid = '" + table +"'::REGCLASS \n" +
			" ORDER BY i.relname";
		
		return sql;
	}
	
	/**
	 * SQL to get table definition
	 * call by getTableDefBySchemaTable
	 *****************************************
	 * @param schema
	 * @param table
	 * @return
	 */
	@Override
	protected String schemaTableDefSQL( String schema, String table )
	{
		String sql = 
			" select \n"         + 
			"   column_name, \n" +
			"   data_type,   \n" +
			"   case         \n" +
			"     when  numeric_precision is null and data_type like '%char%'  \n" +
			"                                 then replace( to_char( character_maximum_length, '99999' ), ' ', '' ) \n" +
			"     when  numeric_scale is null then replace( to_char(numeric_precision, '99999' ), ' ', '' )         \n" +
			"     when  numeric_scale =  0    then replace( to_char(numeric_precision, '99999' ), ' ', '' )         \n" +
			"     else                             numeric_precision||','||numeric_scale \n" +
			"   end data_size,    \n"                +
			"   case is_nullable  \n"                +
			"     when 'YES' then 'NULL OK' \n"      +
			"     else            'NULL NG' \n"      +
			"   end nullable,  \n"                   +
			"   column_default data_default \n"      +
			" from             \n"                   + 
			"   information_schema.columns  \n"      +
			" where            \n"                   +
			"   table_schema = '" + schema + "' \n"  +
			"   and              \n"                 +
			"   table_name = '" + table + "' \n"     + 
			" order by ordinal_position";
		return sql;
	}	
	 
	/**
	 * SQL to get function lists of schema.
	 * call by getSchemaView
	 *************************************
	 * https://stackoverflow.com/questions/1347282/how-can-i-get-a-list-of-all-functions-stored-in-the-database-of-a-particular-sch
	 *************************************
	 * @return schema
	 * @throws UnsupportedOperationException
	 */
	/*
	@Override
	protected String schemaFuncSQL( String schema )
	{
		String sql = 
			" select distinct \n" +
			"   routines.routine_schema, \n" +
			"   routines.routine_name     \n" +
			" from  \n"  +
			"   information_schema.routines \n" + 
			" where \n"  + 
			"   routines.routine_schema = '" + schema + "' \n" +
			" order by routines.routine_schema, routines.routine_name";
		return sql;
	}
	*/
	
	/**
	 * Get SchemaInfo(Function Source)
	 ***************************************
	 * https://qiita.com/SRsawaguchi/items/411801e254ee66f511f1
	 ***************************************
	  SELECT p.proname, pg_get_function_result(p.oid), pg_get_function_identity_arguments(p.oid), oidvectortypes(p.proargtypes), p.proargtypes, p.prosrc 
FROM pg_proc p INNER JOIN pg_namespace ns ON (p.pronamespace = ns.oid)
WHERE ns.nspname = 'public'
	 ***************************************
	  SELECT 'ALTER FUNCTION '
            || quote_ident(n.nspname) || '.' 
            || quote_ident(p.proname) || '(' 
            || pg_catalog.pg_get_function_identity_arguments(p.oid)
            || ') OWNER TO owner_usr;' AS command
FROM   pg_catalog.pg_proc p
JOIN   pg_catalog.pg_namespace n ON n.oid = p.pronamespace 
WHERE  n.nspname = 'public'
	 ***************************************
	 https://stackoverflow.com/questions/4386631/is-it-possible-to-discover-the-column-types-from-a-postgres-function
	 ***************************************
$ psql -E -c '\df+'
********* QUERY **********
SELECT n.nspname as "Schema",
 p.proname as "Name",
 pg_catalog.pg_get_function_result(p.oid) as "Result data type",
 pg_catalog.pg_get_function_arguments(p.oid) as "Argument data types",
CASE
 WHEN p.proisagg THEN 'agg'
 WHEN p.proiswindow THEN 'window'
 WHEN p.prorettype = 'pg_catalog.trigger'::pg_catalog.regtype THEN 'trigger'
 ELSE 'normal'
END as "Type",
CASE
 WHEN p.provolatile = 'i' THEN 'immutable'
 WHEN p.provolatile = 's' THEN 'stable'
 WHEN p.provolatile = 'v' THEN 'volatile'
END as "Volatility",
 pg_catalog.pg_get_userbyid(p.proowner) as "Owner",
 l.lanname as "Language",
 p.prosrc as "Source code",
 pg_catalog.obj_description(p.oid, 'pg_proc') as "Description"
FROM pg_catalog.pg_proc p
    LEFT JOIN pg_catalog.pg_namespace n ON n.oid = p.pronamespace
    LEFT JOIN pg_catalog.pg_language l ON l.oid = p.prolang
WHERE pg_catalog.pg_function_is_visible(p.oid)
     AND n.nspname <> 'pg_catalog'
     AND n.nspname <> 'information_schema'
ORDER BY 1, 2, 4;
	 **************************
	 ***************************************
	 * @param schema
	 * @param function
	 * @return
	 * @throws SQLException
	 */
	public String getFunctionSourceBySchemaFunc( String schema, String function )
		throws 
			SQLException
	{
		StringBuffer src = 
			new StringBuffer
			( 
				"CREATE OR REPLACE FUNCTION \n" + function 
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
			"   n.nspname = '" + schema   + "' \n" +
			"   and \n" +
			"   p.proname  = '" + function + "'	\n";
		/*
		String sql2 = 
			" select \n" +
			"   data_type, \n" +
			"   external_language,  \n" +
			"   is_deterministic,   \n" +
			"   is_null_call,       \n" +
			"   routine_definition  \n" +
			" from   \n" +
			"   information_schema.routines \n" +
			" where  \n" +
			"   routine_schema = '" + schema   + "' \n" +
			"   and  \n" +
			"   routine_name  = '" + function + "' \n" +
			"   and  \n" +
			"   routine_type  = 'FUNCTION'";
		*/
		System.out.println( " -- getFunctionSourceBySchemaFunc -----------" );
		System.out.println( sql1 );
		//System.out.println( " --------------------------------------------" );
		//System.out.println( sql2 );
		System.out.println( " --------------------------------------------" );
		Statement stmt1 = this.conn.createStatement();
		ResultSet rs1 = stmt1.executeQuery( sql1 );
		//Statement stmt2 = this.conn.createStatement();
		//ResultSet rs2 = stmt2.executeQuery( sql2 );
		String quote = "$$";
		//while ( rs1.next() && rs2.next() )
		while ( rs1.next() )
		{
			src.append( "(" + rs1.getString("param_list") + ") " );
			src.append( "RETURNS " + rs1.getString("results") + " \n" );
			src.append( "AS " + quote + " \n" );
			src.append( rs1.getString("prosrc") + " \n" );
			src.append( quote );
			src.append( " LANGUAGE " + rs1.getString("external_language") + " \n" );
			//if ( "YES".equals( rs2.getString( "is_null_call") ) )
			if ( "t".equals( rs1.getString( "is_null_call") ) )
			{
				src.append( "RETURNS NULL ON NULL INPUT \n" );
			}
			src.append( "; \n" );
		}
		
		stmt1.close();
		rs1.close();
		//stmt2.close();
		//rs2.close();
		
		return src.toString();
	}	
	
	
	/**
	 * SQL to get type lists of schema.
	 * call by getSchemaType
	 *************************************
	 * @return schema
	 * @throws UnsupportedOperationException
	 */
	/*
	protected String schemaTypeSQL( String schema )
	{
		String sql = 
			" select \n" +
			"   user_defined_type_schema, \n" +
			"   user_defined_type_name    \n" +
			" from  \n"  +
			"   information_schema.user_defined_types \n" + 
			" where \n"  + 
			"   user_defined_type_schema = '" + schema + "' \n" +
			" order by user_defined_type_schema, user_defined_type_name";
		return sql;
	}
	*/
	
	/**
	 * Get SchemaInfo(Type Source)
	 ***************************************
	 * https://stackoverflow.com/questions/9535937/is-there-a-way-to-show-a-user-defined-postgresql-enumerated-type-definition
	 ***************************************
	 * @param schema
	 * @param type
	 * @return
	 * @throws SQLException
	 */
	@Override
	public String getTypeSourceBySchemaType( String schema, String type )
		throws 
			SQLException
	{
		StringBuffer src = 
			new StringBuffer
			( 
				"DROP TYPE IF EXISTS " + type + ";\n " + 
				"CREATE TYPE " + type + " AS " 
			);
		
		String sql = 
			" select \n" + 
			"   a.attname  attname, \n" +
			"   t.typname  typname  \n" +
			" from \n" +  
			"   pg_class c join \n" + 
			"   pg_attribute a on c.oid = a.attrelid join \n" +
			"   pg_type t ON a.atttypid = t.oid \n" +
			" where \n" +
			"   c.relname = '" + type + "'";
		
		System.out.println( " -- getTypeSourceBySchemaType -----------" );
		System.out.println( sql );
		System.out.println( " ----------------------------------------" );
		Statement stmt = this.conn.createStatement();
		ResultSet rs = stmt.executeQuery( sql );
		int i = 0;
		src.append( "(" );
		while ( rs.next() )
		{
			if ( i != 0 )
			{
				src.append( "," );
			}
			src.append( rs.getString("attname") + " " + rs.getString("typname") );
			i++;
		}
		src.append( ");" );
		return src.toString();
	}
	
	/**
	 * SQL to get trigger lists of schema.
	 * call by getSchemaTrigger
	 *************************************
	 *
 select 
   n.nspname, 
   t.tgname   
 from 
   pg_trigger   t join 
   pg_class     c on ( t.tgrelid = c.oid ) join 
   pg_namespace n on ( c.relnamespace = n.oid ) 
 where 
   n.nspname = 'public' 
 order by t.tgname
	 ************************************
	 * @return String
	 */
	/*
	@Override
	protected String schemaTriggerSQL( String schema )
	{
		String sql =
			" select \n" +
			"   n.nspname, \n" +
			"   t.tgname   \n" +
			" from \n"   +
			"   pg_trigger   t join \n" +
			"   pg_class     c on ( t.tgrelid = c.oid ) join \n" +
			"   pg_namespace n on ( c.relnamespace = n.oid ) \n" +
			" where \n" +
			"   n.nspname = '" + schema + "' \n" +
			"   and \n" +
			"   t.tgname not like 'RI_ConstraintTrigger%' \n" +
			" order by t.tgname";
		return sql;
	}
	*/
	/**
	 * Get SchemaInfo(Trigger Source)
	 ***************************************
	 * select pg_get_functiondef('emp_stamp'::regproc) src
	 ***************************************
	 * @param schema
	 * @param trigger
	 * @return
	 * @throws SQLException
	 */
	@Override
	public String getTriggerSourceBySchemaTrigger( String schema, String trigger )
		throws 
			SQLException
	{
		String src = "";
		
		String sql = "select pg_get_functiondef('" + trigger + "'::regproc) src";
		
		System.out.println( " -- getTriggerSourceBySchemaTrigger -----------" );
		System.out.println( sql );
		System.out.println( " ----------------------------------------------" );
		Statement stmt = this.conn.createStatement();
		ResultSet rs = stmt.executeQuery( sql );
		while ( rs.next() )
		{
			src = rs.getString( "src" );
		}
		return src;
	}
	
	/**
	 * SQL to get sequence lists of schema.
	 * call by getSchemaSequence
	 *************************************
	 * @return String
	 */
	/*
	protected String schemaSequenceSQL( String schema )
	{
		String sql =
			"select \n" +
			"  n.nspname, \n" +
			"  c.relname  \n" +
			"from \n" +
			"  pg_class c join \n" +
			"  pg_namespace n on n.oid = c.relnamespace \n" +
			"where \n" +
			"  n.nspname = '" + schema + "' \n" +
			"  and \n" +
			"  c.relkind = 'S' \n" +
			"order by c.relname";
		return sql;
	}
	*/
	
}
