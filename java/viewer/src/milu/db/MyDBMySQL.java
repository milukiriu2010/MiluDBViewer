package milu.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import milu.entity.schema.SchemaEntity.SCHEMA_TYPE;

public class MyDBMySQL extends MyDBAbstract 
{
	public MyDBMySQL()
	{
		this.suppoertedTypeLst.add( SCHEMA_TYPE.ROOT_TABLE );
		this.suppoertedTypeLst.add( SCHEMA_TYPE.ROOT_VIEW );
		this.suppoertedTypeLst.add( SCHEMA_TYPE.ROOT_SYSTEM_VIEW );
		this.suppoertedTypeLst.add( SCHEMA_TYPE.ROOT_FUNC );
		this.suppoertedTypeLst.add( SCHEMA_TYPE.ROOT_PROC );
		this.suppoertedTypeLst.add( SCHEMA_TYPE.ROOT_TRIGGER );
	}
	
	/**
	 * Load JDBC Driver
	 ***********************************************
	 * @throws ClassNotFoundException
	 */
	@Override
	protected void loadDriver() throws ClassNotFoundException 
	{
		Class.forName( "com.mysql.jdbc.Driver" );
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
		return 3306;
	}
	
	/**
	 * Name on GUI Items
	 */
	@Override
	public String toString()
	{
		return "MySQL";
	}
	
	@Override
	protected void createConnectionParameter(Map<String, String> dbOptMap) 
	{
		// URL Example
		// ---------------------------------------------------
		// jdbc:mysql://localhost:3306/sakila
		// ---------------------------------------------------
		// https://www.javatpoint.com/example-to-connect-to-the-mysql-database
		this.url = 
			"jdbc:mysql://"+
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
	 * invalid
	 * http://frodo.looijaard.name/article/mysql-identify-invalid-views
	 *************************************
	 * @return String
	 */
	/*
	@Override
	protected String schemaTableSQL( String schema )
	{
		String sql = 
			" select \n" +
			"   table_schema, \n" +
			"	table_name   \n" +
	  		//"   case \n" + 
	  		//"     when table_comment like '%invalid%' then 'INVALID' \n" +
	  		//"     else                                     'VALID' \n"   +
	  		//"   end status \n" +
			" from \n" +
			"   information_schema.tables \n" +
			" where \n" +
			"   table_type='BASE TABLE' \n" +
			"   and \n" +
			"   table_schema = '" + schema + "' \n" +
			" order by table_schema, table_name";
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
			" select \n" +
			"   table_schema, \n" +
			"   table_name \n" +
	  		//"   case \n" + 
	  		//"     when table_comment like '%invalid%' then 'INVALID' \n" +
	  		//"     else                                     'VALID' \n"   +
	  		//"   end status \n" +
			" from \n" +
	  		"   information_schema.tables \n" +
			" where \n" +
			"   table_type='VIEW' \n" + 
			"   and \n" + 
			"   table_schema = '" + schema + "' \n" +
			" order by table_schema, table_name";
		return sql;
	}
	*/
	
	/**
	 * SQL to get system view lists of schema.
	 * call by getSchemaSystemView
	 *************************************
	 * @return String
	 */
	/*
	@Override
	protected String schemaSystemViewSQL( String schema )
	{
		String sql = 
			" select table_schema, table_name from information_schema.tables " +
			" where " +
			" table_type='SYSTEM VIEW' " + 
			" and " + 
			" table_schema = '" + schema + "'" +
			" order by table_schema, table_name";
		return sql;
	}
	*/
	
	/**
	 * SQL to get index lists of schema.
	 * call by getIndexBySchemaTable
	 *****************************************
	 * https://stackoverflow.com/questions/5213339/how-to-see-indexes-for-a-database-or-table
select 
  table_name,
  index_name,
  concat( '{', group_concat(column_name separator ','), '}' ) index_keys,
  case index_name
    when 'PRIMARY' then 't'
    else                'f'
  end  is_primary,
  case non_unique
    when 0 then 't'
    when 1 then 'f'
  end  is_unique,
  null is_functional,
  null is_partial
from 
  information_schema.statistics
where
table_schema = 'sakila'
group by table_name,index_name
order by table_name,index_name,seq_in_index
	 *****************************************
SELECT TABLE_NAME,
    COUNT(1) index_count,
    GROUP_CONCAT(DISTINCT(index_name) SEPARATOR ',\n ') indexes
FROM INFORMATION_SCHEMA.STATISTICS
WHERE TABLE_SCHEMA = 'sakila'
   AND INDEX_NAME != 'primary'
GROUP BY TABLE_NAME
ORDER BY COUNT(1) DESC;	
	 *****************************************
	 * @param schema
	 * @param table
	 * @return SQL
	 *****************************************
	 */
	protected String schemaIndexSQL( String schema, String table )
	{
		String sql = 
			" select \n"        + 
			"   table_name, \n" +
			"   index_name, \n" +
			//"   concat( '{', group_concat(column_name separator ','), '}' ) index_keys, \n" +
			"   case index_name \n "          +
			"     when 'PRIMARY' then 't' \n" +
			"     else                'f' \n" +
			"   end  is_primary,  \n"         +
			"   case non_unique   \n"         +
			"     when 0 then 't' \n"         +
			"     when 1 then 'f' \n"         +
			"   end  is_unique,   \n"         +
			"   case \n"                      +
			"     when sub_part is not null then 't' \n"  +
			"     else                           'f' \n"  +
			"   end is_functional  \n"       +
			//"   null is_partial   \n"         +
			" from \n" +
			"   information_schema.statistics \n" +
			" where \n " +
			"   table_schema = '" + schema + "' \n" +
			"   and \n" +
			"   table_name = '" + table + "' \n" +
			" group by table_name,index_name \n" +
			" order by table_name,index_name,seq_in_index";
			
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
			" select \n"                             + 
			"   column_name, \n"                     +
			"   column_type  data_type,     \n"      +
			"   null         data_size,     \n"      +
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
	 * https://stackoverflow.com/questions/733349/list-of-stored-procedures-functions-mysql-command-line
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
			"   db,  \n"       +
			"   name \n"       +
			" from   \n"       +
			"   mysql.proc \n" + 
			" where \n"        + 
			"   db = '" + schema + "' \n" +
			"   and \n"        +
			"   type = 'FUNCTION' \n" +
			" order by name";
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
		StringBuffer src = 
			new StringBuffer
			( 
				"DELIMITER //\n" +
				"DROP FUNCTION IF EXISTS " + function + "//\n" +
				"CREATE FUNCTION \n" + function 
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
			"   db = '" + schema   + "' \n" +
			"   and  \n" +
			"   name  = '" + function + "' \n" +
			"   and  \n" +
			"   type  = 'FUNCTION'";
		
		System.out.println( " -- getFunctionSourceBySchemaFunc -----------" );
		System.out.println( sql );
		System.out.println( " --------------------------------------------" );
		Statement stmt = this.conn.createStatement();
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
	
	/**
	 * SQL to get procedure lists of schema.
	 * call by getSchemaProc
	 *************************************
	 * @return schema
	 * @throws UnsupportedOperationException
	 */
	/*
	@Override
	protected String schemaProcSQL( String schema )
	{
		String sql = 
			" select distinct \n" +
			"   db,  \n"       +
			"   name \n"       +
			" from   \n"       +
			"   mysql.proc \n" + 
			" where \n"        + 
			"   db = '" + schema + "' \n" +
			"   and \n"        +
			"   type = 'PROCEDURE' \n" +
			" order by name";
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
	@Override
	public String getProcedureSourceBySchemaProc( String schema, String procedure )
			throws SQLException
	{
		StringBuffer src = 
			new StringBuffer
			( 
				"DELIMITER //\n" +
				"DROP PROCEDURE IF EXISTS " + procedure + "//\n" +
				"CREATE PROCEDURE \n" + procedure 
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
			"   db = '" + schema   + "' \n" +
			"   and  \n" +
			"   name  = '" + procedure + "' \n" +
			"   and  \n" +
			"   type  = 'PROCEDURE'";
		
		System.out.println( " -- getProcedureSourceBySchemaProc -----------" );
		System.out.println( sql );
		System.out.println( " ---------------------------------------------" );
		Statement stmt = this.conn.createStatement();
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
	
	
	/**
	 * SQL to get trigger lists of schema.
	 * call by getSchemaTrigger
	 *************************************
	 * @return schema
	 */
	@Override
	protected String schemaTriggerSQL( String schema )
	{
		String sql = 
			" select distinct \n" +
			"   trigger_schema,  \n"       +
			"   trigger_name     \n"       +
			" from   \n"       +
			"   information_schema.triggers \n" + 
			" where \n"        + 
			"   trigger_schema = '" + schema + "' \n" +
			" order by trigger_name";
		return sql;
	}
	
	/**
	 * Get SchemaInfo(Trigger Source)
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
		StringBuffer src = 
			new StringBuffer
			( 
				"DELIMITER //\n" +
				"DROP TRIGGER IF EXISTS " + trigger + "//\n" +
				"CREATE TRIGGER \n" + trigger + " \n"
			);
		
		String sql = 
			" select \n" +
			"   action_timing,      \n" +
			"   event_manipulation, \n" +
			"   event_object_table, \n" +
			"   action_statement    \n" +
			" from   \n" +
			"   information_schema.triggers \n" +
			" where  \n" +
			"   trigger_schema = '" + schema   + "' \n" +
			"   and  \n" +
			"   trigger_name  = '"  + trigger + "'";
		
		System.out.println( " -- getTriggerSourceBySchemaTrigger -----------" );
		System.out.println( sql );
		System.out.println( " ----------------------------------------------" );
		Statement stmt = this.conn.createStatement();
		ResultSet rs = stmt.executeQuery( sql );
		while ( rs.next() )
		{
			// AFTER
			src.append( rs.getString("action_timing")      + " " );
			// INSERT
			src.append( rs.getString("event_manipulation") + " ON " );
			src.append( rs.getString("event_object_table") + " \n" );
			src.append( "FOR EACH ROW \n" );
			src.append( rs.getString("action_statement") + " \n" );
			src.append( "//\n" );
			src.append( "\n" );
			src.append( "DELIMITER ;" );
		}
		return src.toString();
	}
	
}
