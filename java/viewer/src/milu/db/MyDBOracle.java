package milu.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import milu.entity.schema.SchemaEntity.SCHEMA_TYPE;

public class MyDBOracle extends MyDBAbstract 
{
	public MyDBOracle()
	{
		this.suppoertedTypeLst.add( SCHEMA_TYPE.ROOT_TABLE );
		this.suppoertedTypeLst.add( SCHEMA_TYPE.ROOT_VIEW );
		this.suppoertedTypeLst.add( SCHEMA_TYPE.ROOT_MATERIALIZED_VIEW );
		this.suppoertedTypeLst.add( SCHEMA_TYPE.ROOT_FUNC );
		this.suppoertedTypeLst.add( SCHEMA_TYPE.ROOT_PROC );
		this.suppoertedTypeLst.add( SCHEMA_TYPE.ROOT_PACKAGE_DEF );
		this.suppoertedTypeLst.add( SCHEMA_TYPE.ROOT_PACKAGE_BODY );
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
		Class.forName( "oracle.jdbc.driver.OracleDriver" );
	}
	
	/**
	 * Get Driver URL
	 ***********************************************
	 */
	public String getDriverUrl( Map<String, String> dbOptMap )
	{
		this.createConnectionParameter(dbOptMap);
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
		return 1521;
	}
	
	/**
	 * Name on GUI Items
	 */
	@Override
	public String toString()
	{
		return "Oracle";
	}
	
	/**
	 * 
	 */
	@Override
	protected void createConnectionParameter(Map<String, String> dbOptMap) 
	{
		// URL Example
		// ---------------------------------------------------
		// (1) jdbc:oracle:thin:@localhost:1521:xe
		// (2) jdbc:oracle:thin:@localhost:1521/xe
		// ---------------------------------------------------
		// When using (1) stype, this ORA error happens.
		//   "ORA-12505 :TNS listener does not currently know of SID given in connect descriptor"
		// so, change to (2) style.
		// https://stackoverflow.com/questions/30861061/ora-12505-tns-listener-does-not-currently-know-of-sid-given-in-connect-descript
		this.url = 
				"jdbc:oracle:thin:@"+
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
		String sql = "select username from all_users order by username";
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
			" select owner, object_name, status from all_objects \n" +
			" where \n" +
			" object_type='TABLE'      \n" +
			" and \n" +
			" owner = '" + schema + "' \n" +
			" order by owner, object_name";
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
			" select owner, object_name, status from all_objects \n" +
			" where \n" +
			" object_type='VIEW' \n" + 
			" and \n" + 
			" owner = '" + schema + "' \n" +
			" order by owner, object_name";
		return sql;
	}
	*/
	
	/**
	 * SQL to get materialized view lists of schema.
	 * call by getSchemaMaterializedView
	 *************************************************
	 * @return schema
	 */
	/*
	@Override
	protected String schemaMaterializedViewSQL( String schema )
	{
		String sql = 
			" select owner, object_name, status from all_objects \n" +
			" where \n" +
			" object_type='MATERIALIZED VIEW' \n" + 
			" and \n" + 
			" owner = '" + schema + "' \n" +
			" order by owner, object_name";
		return sql;
	}
	*/
	
	/**
	 * SQL to get index lists of schema.
	 * call by getIndexBySchemaTable
	 *****************************************
select
  ai.index_name             index_name,
  case ai.uniqueness
    when 'UNIQUE' then 't'
    else               'f'
  end                       is_unique,
  case ac.constraint_type
    when 'P'      then 't'
    else               'f'
  end                       is_primary,
  case ai.index_type
    when 'FUNCTION-BASED NORMAL' then 't'
    else                              'f'
  end                       is_functional,
  null                      is_partial,
  listagg( aic.column_name,',') within group ( order by aic.column_name ) index_keys
from 
  all_indexes      ai,
  all_ind_columns  aic,
  all_constraints  ac
where
  ai.owner = 'MILU'
  and
  ai.table_name = 'M_NPB_TEAM_LIST'
  and
  ai.owner = aic.index_owner
  and
  ai.table_name = aic.table_name
  and
  ai.index_name = aic.index_name
  and
  ai.owner = ac.owner(+)
  and
  ai.index_name = ac.constraint_name(+)
group by ai.index_name, ai.uniqueness, ac.constraint_type, ai.index_type
order by ai.index_name
	 *****************************************
	 * @param schema
	 * @param table
	 * @return SQL
	 *****************************************
	 */
	protected String schemaIndexSQL( String schema, String table )
	{
		String sql = 
			" select \n"                                     +
			"   ai.index_name             index_name, \n"    +
			"   case ai.uniqueness \n"                       +
			"     when 'UNIQUE' then 't' \n"                 +
			"     else               'f' \n"                 +
			"   end                       is_unique,  \n"    +
			"   case ac.constraint_type  \n"                 +
			"     when 'P'      then 't' \n"                 +
			"     else               'f' \n"                 +
			"   end                       is_primary, \n"    +
			"   case ai.index_type       \n"                 +
			"     when 'FUNCTION-BASED NORMAL' then 't' \n"  +
			"     else                              'f' \n"  + 
			"   end                       is_functional, \n" +
			//"   null                      is_partial, \n"    +
			"   case ai.status \n"  +
			"     when  'VALID'  then 'VALID'   \n" +
			"     else                'INVALID' \n" +
			"   end status    \n"  +
			//"   listagg( aic.column_name,',') within group ( order by aic.column_name ) index_keys \n" +
			" from \n"  + 
			"   all_indexes      ai,  \n" +
			"   all_ind_columns  aic, \n" +
			"   all_constraints  ac   \n" +
			" where \n" +
			"   ai.owner = '" + schema + "' \n"            +
			"   and \n"                                    +
			"   ai.table_name = '" + table + "' \n"        +
			"   and \n"                                    +
			"   ai.owner = aic.index_owner \n"             +
			"   and \n"                                    +
			"   ai.table_name = aic.table_name \n"         +
			"   and \n"                                    +
			"   ai.index_name = aic.index_name \n"         +
			"   and \n"                                    +
			"   ai.owner = ac.owner(+) \n"                 +
			"   and \n"                                    +
			"   ai.index_name = ac.constraint_name(+) \n " +
			" group by ai.index_name, ai.uniqueness, ac.constraint_type, ai.index_type, ai.status \n" +
			" order by ai.index_name";
				
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
	protected String schemaTableDefSQL( String schema, String table )
	{
		String sql = 
			" select \n"         + 
			"   column_name, \n" +
			"   data_type,   \n" +
	   		"   case         \n" + 
	   		"     when       \n" +
	        "       data_precision is null and regexp_like( data_type, '*CHAR*' ) \n" + 
	        "                       then cast(data_length as varchar2(5)) \n" + 
	        "     else \n" +
	     	"       case \n" +
	        "         when data_scale is null then cast(data_precision as varchar2(5)) \n" + 
	        "         when data_scale =  0    then cast(data_precision as varchar2(5)) \n" +
	        "         else                         data_precision||','||data_scale \n"     +
	        "       end \n"                    +
			"   end data_size, \n"             +
			"   case nullable  \n"             +
			"     when 'Y' then 'NULL OK'  \n" +
			"     else          'NULL NG'  \n" +
			"   end nullable,  \n"             +
			"   data_default   \n"             +
			" from             \n"             + 
			"   all_tab_columns  \n"           +
			" where            \n"             +
			"   owner = '" + schema + "' \n"     +
			"   and              \n"             +
			"   table_name = '" + table + "' \n" + 
			" order by column_id";
		return sql;
	}
	
	/**
	 * SQL to get function lists of schema.
	 * call by getSchemaView
	 *************************************
	 * @return schema
	 */
	/*
	@Override
	protected String schemaFuncSQL( String schema )
	{
		// status
		//   =>
		//   VALID
		//   INVALID
		String sql =
			" select \n"         +
			"   owner,      \n"  +
			"   object_name, \n" +
			"   status       \n" +
			" from \n"           +
			"   all_objects  \n" +
			" where \n"          +
			"   owner = '" + schema + "' \n" +
			"   and \n"          +
			"   object_type = 'FUNCTION' \n" +
			" order by object_name";
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
	@Override
	public String getFunctionSourceBySchemaFunc( String schema, String function )
			throws SQLException
	{
		StringBuffer src = new StringBuffer( "CREATE OR REPLACE \n" );
		
		String sql = 
			" select \n" +
			"   text \n" +
			" from   \n" +
			"   all_source \n" +
			" where  \n" +
			"   owner = '" + schema   + "' \n" +
			"   and  \n" +
			"   name  = '" + function + "' \n" +
			"   and  \n" +
			"   type  = 'FUNCTION' \n" +
			" order by line";
		
		System.out.println( " -- getFunctionSourceBySchemaFunc -----------" );
		System.out.println( sql );
		System.out.println( " --------------------------------------------" );
		Statement stmt = this.conn.createStatement();
		ResultSet rs = stmt.executeQuery( sql );
		while ( rs.next() )
		{
			src.append( rs.getString("text") );
		}
		return src.toString();
	}
	
	/**
	 * SQL to get procedure lists of schema.
	 * call by getSchemaProc
	 *************************************
	 * @return schema
	 * @throws UnsupportedOperationException
	 */
	/*
	protected String schemaProcSQL( String schema )
		throws UnsupportedOperationException
	{
		String sql =
			" select \n"         +
			"   owner,       \n" +
			"   object_name, \n" +
			"   status       \n" +
			" from \n"           +
			"   all_objects \n"  +
			" where \n"          +
			"   owner = '" + schema + "' \n"  +
			"   and \n"         +
			"   object_type = 'PROCEDURE' \n" +
			" order by object_name";
			return sql;
	}
	*/
	
	/**
	 * Get SchemaInfo(Procedure Source)
	 ***************************************
	 * @param schema
	 * @param procedure
	 * @return
	 * @throws SQLException
	 */
	public String getProcedureSourceBySchemaProc( String schema, String procedure )
		throws 
			SQLException
	{
		StringBuffer src = new StringBuffer( "CREATE OR REPLACE \n" );
		
		String sql = 
			" select \n" +
			"   text \n" +
			" from   \n" +
			"   all_source \n" +
			" where  \n" +
			"   owner = '" + schema   + "' \n" +
			"   and  \n" +
			"   name  = '" + procedure + "' \n" +
			"   and  \n" +
			"   type  = 'PROCEDURE' \n" +
			" order by line";
		
		System.out.println( " -- getProcedureSourceBySchemaProc -----------" );
		System.out.println( sql );
		System.out.println( " ---------------------------------------------" );
		Statement stmt = this.conn.createStatement();
		ResultSet rs = stmt.executeQuery( sql );
		while ( rs.next() )
		{
			src.append( rs.getString("text") );
		}
		return src.toString();
	}
	
	/**
	 * SQL to get package lists of schema.
	 * call by getSchemaPackageDef
	 *************************************
	 * @return schema
	 */
	/*
	@Override
	protected String schemaPackageDefSQL( String schema )
	{
		String sql =
			" select \n"         +
			"   owner,       \n" +
			"   object_name, \n" +
			"   status       \n" +
			" from \n"           +
			"   all_objects \n"  +
			" where \n"          +
			"   owner = '" + schema + "' \n" +
			"   and \n"          +
			"   object_type = 'PACKAGE' \n"  +
			" order by object_name";
		return sql;
	}
	*/
	
	
	/**
	 * Get SchemaInfo(Package Source)
	 ***************************************
	 * @param schema
	 * @param packageDef
	 * @return
	 * @throws SQLException
	 */
	public String getPackageDefSourceBySchemaPackageDef( String schema, String packageDef )
		throws 
			UnsupportedOperationException,
			SQLException
	{
		StringBuffer src = new StringBuffer( "CREATE OR REPLACE \n" );
		
		String sql = 
			" select \n" +
			"   text \n" +
			" from   \n" +
			"   all_source \n" +
			" where  \n" +
			"   owner = '" + schema     + "' \n" +
			"   and  \n" +
			"   name  = '" + packageDef + "' \n" +
			"   and  \n" +
			"   type  = 'PACKAGE' \n" +
			" order by line";
		
		System.out.println( " -- getPackageDefSourceBySchemaPackageDef -----------" );
		System.out.println( sql );
		System.out.println( " ----------------------------------------------------" );
		Statement stmt = this.conn.createStatement();
		ResultSet rs = stmt.executeQuery( sql );
		while ( rs.next() )
		{
			src.append( rs.getString("text") );
		}
		return src.toString();
	}
	
	/**
	 * SQL to get package body lists of schema.
	 * call by getSchemaPackageBody
	 *************************************
	 * @return schema
	 * @throws UnsupportedOperationException
	 */
	/*
	@Override
	protected String schemaPackageBodySQL( String schema )
		throws UnsupportedOperationException
	{
		String sql =
			" select \n"         +
			"   owner,       \n" +
			"   object_name, \n" +
			"   status       \n" +
			" from \n"           +
			"   all_objects \n"  +
			" where \n"          +
			"   owner = '" + schema + "' \n" +
			"   and \n"          +
			"   object_type = 'PACKAGE BODY' \n"  +
			" order by object_name";
		return sql;
	}
	*/
	
	/**
	 * Get SchemaInfo(Package Body Source)
	 ***************************************
	 * @param schema
	 * @param packageDef
	 * @return
	 * @throws SQLException
	 */
	public String getPackageBodySourceBySchemaPackageBody( String schema, String packageBody )
		throws 
			SQLException
	{
		StringBuffer src = new StringBuffer( "CREATE OR REPLACE \n" );
		
		String sql = 
			" select \n" +
			"   text \n" +
			" from   \n" +
			"   all_source \n" +
			" where  \n" +
			"   owner = '" + schema      + "' \n" +
			"   and  \n" +
			"   name  = '" + packageBody + "' \n" +
			"   and  \n" +
			"   type  = 'PACKAGE BODY' \n" +
			" order by line";
		
		System.out.println( " -- getPackageBodySourceBySchemaPackageBody -----------" );
		System.out.println( sql );
		System.out.println( " ------------------------------------------------------" );
		Statement stmt = this.conn.createStatement();
		ResultSet rs = stmt.executeQuery( sql );
		while ( rs.next() )
		{
			src.append( rs.getString("text") );
		}
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
	@Override
	protected String schemaTypeSQL( String schema )
		throws UnsupportedOperationException
	{
		String sql =
			" select \n"         +
			"   owner,       \n" +
			"   object_name, \n" +
			"   status       \n" +
			" from \n"           +
			"   all_objects \n"  +
			" where \n"          +
			"   owner = '" + schema + "' \n" +
			"   and \n"          +
			"   object_type = 'TYPE' \n"  +
			" order by object_name";
		return sql;
	}
	*/
	/**
	 * Get SchemaInfo(Type Source)
	 ***************************************
	 * @param schema
	 * @param packageDef
	 * @return
	 * @throws SQLException
	 */
	public String getTypeSourceBySchemaType( String schema, String type )
		throws 
			SQLException
	{
		StringBuffer src = new StringBuffer( "CREATE OR REPLACE \n" );
		
		String sql = 
			" select \n" +
			"   text \n" +
			" from   \n" +
			"   all_source \n" +
			" where  \n" +
			"   owner = '" + schema + "' \n" +
			"   and  \n" +
			"   name  = '" + type   + "' \n" +
			"   and  \n" +
			"   type  = 'TYPE' \n"  +
			" order by line";
		
		System.out.println( " -- getTypeSourceBySchemaType -----------" );
		System.out.println( sql );
		System.out.println( " ----------------------------------------" );
		Statement stmt = this.conn.createStatement();
		ResultSet rs = stmt.executeQuery( sql );
		while ( rs.next() )
		{
			src.append( rs.getString("text") );
		}
		stmt.close();
		rs.close();
		return src.toString();
	}
	
	/**
	 * SQL to get trigger lists of schema.
	 * call by getSchemaTrigger
	 *************************************
	 * @return schema
	 * @throws UnsupportedOperationException
	 */
	/*
	@Override
	protected String schemaTriggerSQL( String schema )
	{
		String sql =
			" select \n"         +
			"   owner,       \n" +
			"   object_name, \n" +
			"   status       \n" +
			" from \n"           +
			"   all_objects \n"  +
			" where \n"          +
			"   owner = '" + schema + "' \n" +
			"   and \n"          +
			"   object_type = 'TRIGGER' \n"  +
			" order by object_name";
		return sql;
	}
	*/
	/**
	 * Get SchemaInfo(Trigger Source)
	 ***************************************
	 * @param schema
	 * @param trigger
	 * @return
	 * @throws SQLException
	 */
	public String getTriggerSourceBySchemaTrigger( String schema, String trigger )
		throws 
			UnsupportedOperationException,
			SQLException
	{
		StringBuffer src = new StringBuffer( "CREATE OR REPLACE \n" );
		
		String sql = 
			" select \n" +
			"   text \n" +
			" from   \n" +
			"   all_source \n" +
			" where  \n" +
			"   owner = '" + schema  + "' \n" +
			"   and  \n" +
			"   name  = '" + trigger + "' \n" +
			"   and  \n" +
			"   type  = 'TRIGGER' \n"  +
			" order by line";
		
		System.out.println( " -- getTriggerSourceBySchemaTrigger -----------" );
		System.out.println( sql );
		System.out.println( " ----------------------------------------------" );
		Statement stmt = this.conn.createStatement();
		ResultSet rs = stmt.executeQuery( sql );
		while ( rs.next() )
		{
			src.append( rs.getString("text") );
		}
		stmt.close();
		rs.close();
		return src.toString();
	}
	
	/**
	 * SQL to get sequence lists of schema.
	 * call by getSchemaSequence
	 *************************************
	 * @return schema
	 * @throws UnsupportedOperationException
	 */
	/*
	@Override
	protected String schemaSequenceSQL( String schema )
	{
		String sql =
			" select \n"         +
			"   owner,       \n" +
			"   object_name, \n" +
			"   status       \n" +
			" from \n"           +
			"   all_objects \n"  +
			" where \n"          +
			"   owner = '" + schema + "' \n" +
			"   and \n"          +
			"   object_type = 'SEQUENCE' \n"  +
			" order by object_name";
		return sql;
	}
	*/
}
