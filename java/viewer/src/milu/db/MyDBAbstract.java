package milu.db;

import java.util.Map;

import milu.entity.schema.SchemaEntity;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

abstract public class MyDBAbstract
	implements
		Comparable<MyDBAbstract>
{
	// DB Connection
	protected Connection conn = null;
	
	// DB Username
	protected String  username = null;
	
	// DB Password
	protected String  password = null;
	
	// DB URL
	protected String  url = null;
	
	// Supported Type List
	protected List<SchemaEntity.SCHEMA_TYPE>  suppoertedTypeLst = 
			new ArrayList<SchemaEntity.SCHEMA_TYPE>();
			
	// SchemaEntity Root
	protected SchemaEntity schemaRoot = null;
	
	@Override
	public int compareTo( MyDBAbstract obj )
	{
		return this.toString().compareTo( obj.toString() );
	}
	
	/**
	 * Load JDBC Driver
	 ***********************************************
	 * @throws ClassNotFoundException
	 */
	abstract protected void loadDriver() throws ClassNotFoundException;
	
	/**
	 * Get Driver URL
	 ***********************************************
	 * @return URL
	 */
	abstract public String getDriverUrl( Map<String, String> dbOptMap );
	
	/**
	 * Get Default Port Number
	 **********************************************
	 * @return Port Number
	 */
	abstract public int getDefaultPort();
	
	/***********************************************
	 * Get DB Username
	 ***********************************************
	 * @return UserName
	 */
	public String getUsername()
	{
		return this.username;
	}
	
	/***********************************************
	 * Get DB URL
	 ***********************************************
	 * @return URL
	 */
	public String getUrl()
	{
		return this.url;
	}
	
	public SchemaEntity getSchemaRoot()
	{
		return this.schemaRoot;
	}
	
	public List<SchemaEntity.SCHEMA_TYPE> getSupportedTypeLst()
	{
		return this.suppoertedTypeLst;
	}
	
	/***********************************************
	 * Create URL for Connection
	 ***********************************************
	 * @param  dbOptMap 
	 *  Parameters for DB Connection
	 *	  UserName
	 *	  Password
	 *	  DBName
	 *    Host
	 *    Port
	 *********************************************** 
	 */
	abstract protected void createConnectionParameter( Map<String,String> dbOptMap );
	
	/***********************************************
	 * Connect to DB
	 ***********************************************
	 * @param  dbOptMap 
	 *  Parameters for DB Connection
	 *	  UserName
	 *	  Password
	 *	  DBName
	 *    Host
	 *    Port
	 ***********************************************
	 * @throws ClassNotFoundException
	 * @throws SQLException 
	 */
	public void connect( Map<String,String> dbOptMap ) 
		 throws ClassNotFoundException, SQLException
	{
		// close connection, if already connected.
		try
		{
			this.close();
		}
		catch ( SQLException sqlEx )
		{
			// suppress error
		}
		
		// Load Driver
	    this.loadDriver();
	    
	    // set username
	    this.username = dbOptMap.get( "UserName" );
	    
	    // set password
	    this.password = dbOptMap.get( "Password" );
	    
	    // Create URL for DB Connection
		this.createConnectionParameter( dbOptMap );
	    
		System.out.println( "URL     :" + this.url );
		System.out.println( "UserName:" + this.username );
		//System.out.println( "Password:" + this.password );
		
		// Connection
		this.conn = 
	    	DriverManager.getConnection( this.url, this.username, this.password );
		this.conn.setAutoCommit( false );
		
		this.schemaRoot = SchemaEntityFactory.createInstance( this.url, SchemaEntity.SCHEMA_TYPE.ROOT );
	}
	
	/**
	 * ReConnect to DB
	 ******************************
	 * @throws SQLException
	 */
	public void reconnect() 
		 throws SQLException
	{
		// close connection, if already connected.
		try
		{
			this.close();
		}
		catch ( SQLException sqlEx )
		{
			// suppress error
		}
		
		// Connection
		this.conn = 
	    	DriverManager.getConnection( this.url, this.username, this.password );
		this.conn.setAutoCommit( false );
		System.out.println( "DB[" + this.url + "] Connected!!" );
	}
	
	/***********************************************
	 * Close connection to DB
	 ***********************************************
	 * @return
	 *    nothing
	 * @throws SQLException
	 *********************************************** 
	 */
	public void close()
		throws SQLException
	{
		this.schemaRoot = null;
		if ( this.conn != null )
		{
			this.conn.close();
			this.conn = null;
			System.out.println( "DB[" + this.url + "] closed." );
		}
	}
	
	/**
	 * 
	 * @return
	 *   true  => connected
	 *   false => not connected
	 */
	public boolean isConnected()
	{
		return ( this.conn != null );
	}
	
	/**
	 * Commit
	 ************************************* 
	 * @throws SQLException
	 */
	public void commit()
		throws SQLException
	{
		if ( this.conn != null )
		{
			this.conn.commit();
		}
	}
	
	/**
	 * Rollback
	 *************************************
	 * @throws SQLException
	 */
	public void rollback()
		throws SQLException
	{
		if ( this.conn != null )
		{
			this.conn.rollback();
		}
	}
	
	public Statement createStatement()
		throws SQLException
	{
		return this.conn.createStatement();
	}
	
	/**
	 * Get SchemaInfo(Schema)
	 *********************************
	 * @return Lis<String>
	 * +----------+
	 * + SCHEMA1  +
	 * + SCHEMA2  +
	 * + SCHEMA3  +
	 * +----------+
	 * @throws SQLException
	 *********************************
	 */
	/*
	public List<List<String>> getSchemaLst()
		throws SQLException
	{
		List<List<String>> dataLst = new ArrayList<List<String>>();
		
		String sql = this.schemaSchemaSQL();
		if ( sql != null )
		{
			System.out.println( " -- getSchemaLst ------------------" );
			System.out.println( sql );
			System.out.println( " ----------------------------------" );
			try
			(
				Statement stmt = this.conn.createStatement();
				ResultSet rs = stmt.executeQuery( sql )
			)
			{
				ResultSetMetaData rsmd = rs.getMetaData();
				int headCnt = rsmd.getColumnCount();
				while ( rs.next() )
				{
					List<String> dataRow = new ArrayList<String>();
					//dataRow.add( rs.getString(1) );
					for ( int i = 1; i <= headCnt; i++ )
					{
						dataRow.add( rs.getString(i) );
					}					
					dataLst.add( dataRow );
				}
			}
		}
		
		return dataLst;
	}
	*/
	
	/**
	 * SQL to get schema lists of schema.
	 * call by getSchemaLst
	 *************************************
	 * @return String
	 * @throws UnsupportedOperationException
	 */
	/*
	protected String schemaSchemaSQL()
		throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}
	*/
	
	/**
	 * Get SchemaInfo(Table)
	 *************************************
	 * @return Lis<String>
	 * +----------+----------+---------+
	 * + SCHEMA1  + TABLE1-1 + STATUS  +
	 * + SCHEMA1  + TABLE1-2 + VALID   +
	 * + SCHEMA2  + TABLE2-1 + INVALID +
	 * +----------+----------+---------+
	 * @throws SQLException
	 *************************************
	 */
	/*
	public List<List<String>> getSchemaTable( String schema )
		throws SQLException
	{
		List<List<String>> dataLst = new ArrayList<List<String>>();
		
		String sql = this.schemaTableSQL( schema );
		if ( sql != null )
		{
			System.out.println( " -- getSchemaTable ------------------" );
			System.out.println( sql );
			System.out.println( " ------------------------------------" );
			try
			(
				Statement stmt = this.conn.createStatement();
				ResultSet rs = stmt.executeQuery( sql )
			)
			{
				ResultSetMetaData rsmd = rs.getMetaData();
				int headCnt = rsmd.getColumnCount();
				while ( rs.next() )
				{
					List<String> dataRow = new ArrayList<String>();
					for ( int i = 1; i <= headCnt; i++ )
					{
						dataRow.add( rs.getString(i) );
					}
					dataLst.add( dataRow );
				}
			}
		}
		
		return dataLst;
	}
	*/
	
	/**
	 * SQL to get table lists of schema.
	 * call by getSchemaTable
	 *************************************
	 * @return String
	 * @throws UnsupportedOperationException
	 */
	/*
	protected String schemaTableSQL( String schema )
		throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}
	*/
	
	/**
	 * Get SchemaInfo(View)
	 *************************************
	 * @return Lis<String>
	 * +----------+----------+---------+
	 * + SCHEMA1  + VIEW1-1  + STATUS  +
	 * + SCHEMA1  + VIEW1-2  + VALID   +
	 * +----------+----------+---------+
	 * @throws SQLException
	 *************************************
	 */
	/*
	public List<List<String>> getSchemaView( String schema )
		throws SQLException
	{
		List<List<String>> dataLst = new ArrayList<List<String>>();
		
		String sql = this.schemaViewSQL( schema );
		if ( sql != null )
		{
			System.out.println( " -- getSchemaView -------------------" );
			System.out.println( sql );
			System.out.println( " ------------------------------------" );
			try
			(
				Statement stmt = this.conn.createStatement();
				ResultSet rs = stmt.executeQuery( sql )
			)
			{
				ResultSetMetaData rsmd = rs.getMetaData();
				int headCnt = rsmd.getColumnCount();
				while ( rs.next() )
				{
					List<String> dataRow = new ArrayList<String>();
					//dataRow.add( rs.getString(1) );
					//dataRow.add( rs.getString(2) );
					for ( int i = 1; i <= headCnt; i++ )
					{
						dataRow.add( rs.getString(i) );
					}
					dataLst.add( dataRow );
				}
			}
		}
		
		return dataLst;
	}
	*/
	
	/**
	 * SQL to get view lists of schema.
	 * call by getSchemaView
	 *************************************
	 * @return sql
	 * @throws UnsupportedOperationException
	 */
	/*
	protected String schemaViewSQL( String schema )
		throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}
	*/
	
	/**
	 * Get SchemaInfo(System View)
	 *********************************
	 * @return Lis<String>
	 * +----------+-----------------+
	 * + SCHEMA1  + SYSTEM VIEW1-1  +
	 * + SCHEMA1  + SYSTEM VIEW1-2  +
	 * +----------+-----------------+
	 * @throws SQLException
	 *********************************
	 */
	public List<List<String>> getSchemaSystemView( String schema )
		throws SQLException
	{
		List<List<String>> dataLst = new ArrayList<List<String>>();
		
		String sql = this.schemaSystemViewSQL( schema );
		if ( sql != null )
		{
			System.out.println( " -- getSchemaSystemView -------------------" );
			System.out.println( sql );
			System.out.println( " ------------------------------------------" );
			try
			(
				Statement stmt = this.conn.createStatement();
				ResultSet rs = stmt.executeQuery( sql )
			)
			{
				ResultSetMetaData rsmd = rs.getMetaData();
				int headCnt = rsmd.getColumnCount();
				while ( rs.next() )
				{
					List<String> dataRow = new ArrayList<String>();
					//dataRow.add( rs.getString(1) );
					//dataRow.add( rs.getString(2) );
					for ( int i = 1; i <= headCnt; i++ )
					{
						dataRow.add( rs.getString(i) );
					}
					dataLst.add( dataRow );
				}
			}
		}
		
		return dataLst;
	}
	
	/**
	 * SQL to get system view lists of schema.
	 * call by getSchemaSystemView
	 *************************************************
	 * @return String
	 * @throws UnsupportedOperationException
	 */
	protected String schemaSystemViewSQL( String schema )
		throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Get SchemaInfo(Materialized View)
	 *********************************
	 * @return Lis<String>
	 * +----------+-----------------------+
	 * + SCHEMA1  + MATERIALIZED VIEW1-1  +
	 * + SCHEMA1  + MATERIALIZED VIEW1-2  +
	 * +----------+-----------------------+
	 * @throws SQLException
	 *********************************
	 */
	public List<List<String>> getSchemaMaterializedView( String schema )
		throws SQLException
	{
		List<List<String>> dataLst = new ArrayList<List<String>>();
		
		String sql = this.schemaMaterializedViewSQL( schema );
		if ( sql != null )
		{
			System.out.println( " -- getSchemaMaterializedView -------------------" );
			System.out.println( sql );
			System.out.println( " ------------------------------------------------" );
			try
			(
				Statement stmt = this.conn.createStatement();
				ResultSet rs = stmt.executeQuery( sql )
			)
			{
				ResultSetMetaData rsmd = rs.getMetaData();
				int headCnt = rsmd.getColumnCount();
				while ( rs.next() )
				{
					List<String> dataRow = new ArrayList<String>();
					//dataRow.add( rs.getString(1) );
					//dataRow.add( rs.getString(2) );
					for ( int i = 1; i <= headCnt; i++ )
					{
						dataRow.add( rs.getString(i) );
					}
					dataLst.add( dataRow );
				}
			}
		}
		
		return dataLst;
	}
	
	/**
	 * SQL to get materialized view lists of schema.
	 * call by getSchemaMaterializedView
	 *************************************************
	 * @return schema
	 * @throws UnsupportedOperationException
	 */
	protected String schemaMaterializedViewSQL( String schema )
		throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}
	
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
	public List<Map<String,String>> getIndexBySchemaTable( String schema, String table )
		throws SQLException
	{
		List<Map<String,String>> dataLst = new ArrayList<Map<String,String>>();
		
		String sql = this.schemaIndexSQL( schema, table );
		if ( sql != null )
		{
			System.out.println( " -- getIndexBySchemaTable -----------" );
			System.out.println( sql );
			System.out.println( " ------------------------------------" );
			if ( sql != null )
			{
				Statement stmt = this.conn.createStatement();
				ResultSet rs = stmt.executeQuery( sql );
				while ( rs.next() )
				{
					Map<String,String> dataRow = new HashMap<String,String>();
					dataRow.put( "index_name"   , rs.getString("index_name") );
					dataRow.put( "is_unique"    , rs.getString("is_unique") );
					dataRow.put( "is_primary"   , rs.getString("is_primary") );
					//dataRow.put( "index_keys"   , rs.getString("index_keys") );
					dataRow.put( "is_functional", rs.getString("is_functional") );
					//dataRow.put( "is_partial"   , rs.getString("is_partial") );
					try
					{
						dataRow.put( "status", rs.getString("status") );
					}
					catch ( SQLException sqlEx )
					{
						// regard as "valid", if db doesn't have "status" column
						dataRow.put( "status", "VALID" );
					}
					dataLst.add( dataRow );
				}
			}
		}
		return dataLst;
	}
	
	/**
	 * SQL to get index lists of schema.
	 * call by getIndexBySchemaTable
	 *****************************************
	 * @param schema
	 * @param table
	 * @return
	 * @throws UnsupportedOperationException
	 *****************************************
	 */
	protected String schemaIndexSQL( String schema, String table )
		throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Get SchemaInfo(Table Definition)
	 ***************************************
	 * @param schema
	 * @param table
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String,String>> getTableDefBySchemaTable( String schema, String table )
			throws SQLException
	{
		List<Map<String,String>> dataLst = new ArrayList<Map<String,String>>();
		
		String sql = this.schemaTableDefSQL( schema, table );
		if ( sql != null )
		{
			System.out.println( " -- getTableDefBySchemaTable -----------" );
			System.out.println( sql );
			System.out.println( " ---------------------------------------" );
			if ( sql != null )
			{
				Statement stmt = this.conn.createStatement();
				ResultSet rs = stmt.executeQuery( sql );
				while ( rs.next() )
				{
					Map<String,String> dataRow = new HashMap<String,String>();
					dataRow.put( "column_name"   , rs.getString("column_name") );
					dataRow.put( "data_type"     , rs.getString("data_type") );
					dataRow.put( "data_size"     , rs.getString("data_size") );
					dataRow.put( "nullable"      , rs.getString("nullable") );
					dataRow.put( "data_default"  , rs.getString("data_default") );
					dataLst.add( dataRow );
				}
			}
		}
		return dataLst;
	}	
	
	/**
	 * SQL to get table definition
	 * call by getTableDefBySchemaTable
	 *****************************************
	 * @param schema
	 * @param table
	 * @return
	 * @throws UnsupportedOperationException
	 */
	protected String schemaTableDefSQL( String schema, String table )
		throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Get SchemaInfo(Function)
	 *********************************
	 * @return Lis<String>
	 * +----------+----------+
	 * + SCHEMA1  + FUNC1-1  +
	 * + SCHEMA1  + FUNC1-2  +
	 * +----------+----------+
	 * @throws SQLException
	 *********************************
	 */
	public List<List<String>> getSchemaFunc( String schema )
		throws SQLException
	{
		List<List<String>> dataLst = new ArrayList<List<String>>();
		
		String sql = this.schemaFuncSQL( schema );
		System.out.println( " -- getSchemaFunc -------------------" );
		System.out.println( sql );
		System.out.println( " ------------------------------------" );
		if ( sql != null )
		{
			try
			(
				Statement stmt = this.conn.createStatement();
				ResultSet rs = stmt.executeQuery( sql )
			)
			{
				ResultSetMetaData rsmd = rs.getMetaData();
				int headCnt = rsmd.getColumnCount();
				while ( rs.next() )
				{
					List<String> dataRow = new ArrayList<String>();
					//dataRow.add( rs.getString(1) );
					//dataRow.add( rs.getString(2) );
					for ( int i = 1; i <= headCnt; i++ )
					{
						dataRow.add( rs.getString(i) );
					}
					dataLst.add( dataRow );
				}
			}
		}
		
		return dataLst;
	}
	
	/**
	 * SQL to get function lists of schema.
	 * call by getSchemaFunc
	 *************************************
	 * @return schema
	 * @throws UnsupportedOperationException
	 */
	protected String schemaFuncSQL( String schema )
		throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Get SchemaInfo(Function Source)
	 ***************************************
	 * @param schema
	 * @param function
	 * @return
	 * @throws SQLException
	 */
	public String getFunctionSourceBySchemaFunc( String schema, String function )
		throws 
			UnsupportedOperationException,
			SQLException
	{
		throw new UnsupportedOperationException();
	}	
		
	/**
	 * Get SchemaInfo(Aggregate)
	 *********************************
	 * @return Lis<String>
	 * +----------+---------------+
	 * + SCHEMA1  + AGGREGATE1-1  +
	 * + SCHEMA1  + AGGREGATE1-2  +
	 * +----------+---------------+
	 * @throws SQLException
	 *********************************
	 */
	public List<List<String>> getSchemaAggregate( String schema )
		throws SQLException
	{
		List<List<String>> dataLst = new ArrayList<List<String>>();
		
		String sql = this.schemaAggregateSQL( schema );
		System.out.println( " -- getSchemaAggregate -------------------" );
		System.out.println( sql );
		System.out.println( " -----------------------------------------" );
		if ( sql != null )
		{
			try
			(
				Statement stmt = this.conn.createStatement();
				ResultSet rs = stmt.executeQuery( sql )
			)
			{
				ResultSetMetaData rsmd = rs.getMetaData();
				int headCnt = rsmd.getColumnCount();
				while ( rs.next() )
				{
					List<String> dataRow = new ArrayList<String>();
					//dataRow.add( rs.getString(1) );
					//dataRow.add( rs.getString(2) );
					for ( int i = 1; i <= headCnt; i++ )
					{
						dataRow.add( rs.getString(i) );
					}
					dataLst.add( dataRow );
				}
			}
		}
		
		return dataLst;
	}
	
	/**
	 * SQL to get aggregate lists of schema.
	 * call by getSchemaFunc
	 *************************************
	 * @return schema
	 * @throws UnsupportedOperationException
	 */
	protected String schemaAggregateSQL( String schema )
		throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Get SchemaInfo(Aggregate Source)
	 ***************************************
	 * @param schema
	 * @param aggregate
	 * @return
	 * @throws SQLException
	 */
	public String getAggregateSourceBySchemaAggregate( String schema, String aggregate )
		throws 
			UnsupportedOperationException,
			SQLException
	{
		throw new UnsupportedOperationException();
	}	
	
	/**
	 * Get SchemaInfo(Procedure)
	 *********************************
	 * @return Lis<String>
	 * +----------+----------+
	 * + SCHEMA1  + PROC1-1  +
	 * + SCHEMA1  + PROC1-2  +
	 * +----------+----------+
	 * @throws SQLException
	 *********************************
	 */
	public List<List<String>> getSchemaProc( String schema )
		throws SQLException
	{
		List<List<String>> dataLst = new ArrayList<List<String>>();
		
		String sql = this.schemaProcSQL( schema );
		System.out.println( " -- getSchemaProc -------------------" );
		System.out.println( sql );
		System.out.println( " ------------------------------------" );
		if ( sql != null )
		{
			try
			(
				Statement stmt = this.conn.createStatement();
				ResultSet rs = stmt.executeQuery( sql )
			)
			{
				ResultSetMetaData rsmd = rs.getMetaData();
				int headCnt = rsmd.getColumnCount();
				while ( rs.next() )
				{
					List<String> dataRow = new ArrayList<String>();
					//dataRow.add( rs.getString(1) );
					//dataRow.add( rs.getString(2) );
					for ( int i = 1; i <= headCnt; i++ )
					{
						dataRow.add( rs.getString(i) );
					}
					dataLst.add( dataRow );
				}
			}
		}
		
		return dataLst;
	}
	
	/**
	 * SQL to get procedure lists of schema.
	 * call by getSchemaProc
	 *************************************
	 * @return schema
	 * @throws UnsupportedOperationException
	 */
	protected String schemaProcSQL( String schema )
		throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}
	
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
			UnsupportedOperationException,
			SQLException
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Get SchemaInfo(Package Definition)
	 *********************************
	 * @return Lis<String>
	 * +----------+-------------+
	 * + SCHEMA1  + PACKAGE1-1  +
	 * + SCHEMA1  + PACKAGE1-2  +
	 * +----------+-------------+
	 * @throws SQLException
	 *********************************
	 */
	public List<List<String>> getSchemaPackageDef( String schema )
		throws SQLException
	{
		List<List<String>> dataLst = new ArrayList<List<String>>();
		
		String sql = this.schemaPackageDefSQL( schema );
		System.out.println( " -- getSchemaPackageDef -------------------" );
		System.out.println( sql );
		System.out.println( " ------------------------------------------" );
		if ( sql != null )
		{
			try
			(
				Statement stmt = this.conn.createStatement();
				ResultSet rs = stmt.executeQuery( sql )
			)
			{
				ResultSetMetaData rsmd = rs.getMetaData();
				int headCnt = rsmd.getColumnCount();
				while ( rs.next() )
				{
					List<String> dataRow = new ArrayList<String>();
					//dataRow.add( rs.getString(1) );
					//dataRow.add( rs.getString(2) );
					for ( int i = 1; i <= headCnt; i++ )
					{
						dataRow.add( rs.getString(i) );
					}
					dataLst.add( dataRow );
				}
			}
		}
		
		return dataLst;
	}
	
	/**
	 * SQL to get package def lists of schema.
	 * call by getSchemaPackageDef
	 *************************************
	 * @return schema
	 * @throws UnsupportedOperationException
	 */
	protected String schemaPackageDefSQL( String schema )
		throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}
	
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
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Get SchemaInfo(Package Body)
	 *********************************
	 * @return Lis<String>
	 * +----------+------------------+
	 * + SCHEMA1  + PACKAGE BODY1-1  +
	 * + SCHEMA1  + PACKAGE BODY1-2  +
	 * +----------+------------------+
	 * @throws SQLException
	 *********************************
	 */
	public List<List<String>> getSchemaPackageBody( String schema )
		throws SQLException
	{
		List<List<String>> dataLst = new ArrayList<List<String>>();
		
		String sql = this.schemaPackageBodySQL( schema );
		System.out.println( " -- getSchemaPackageBody -------------------" );
		System.out.println( sql );
		System.out.println( " -------------------------------------------" );
		if ( sql != null )
		{
			try
			(
				Statement stmt = this.conn.createStatement();
				ResultSet rs = stmt.executeQuery( sql )
			)
			{
				ResultSetMetaData rsmd = rs.getMetaData();
				int headCnt = rsmd.getColumnCount();
				while ( rs.next() )
				{
					List<String> dataRow = new ArrayList<String>();
					//dataRow.add( rs.getString(1) );
					//dataRow.add( rs.getString(2) );
					for ( int i = 1; i <= headCnt; i++ )
					{
						dataRow.add( rs.getString(i) );
					}
					dataLst.add( dataRow );
				}
			}
		}
		
		return dataLst;
	}
	
	/**
	 * SQL to get package body lists of schema.
	 * call by getSchemaPackageBody
	 *************************************
	 * @return schema
	 * @throws UnsupportedOperationException
	 */
	protected String schemaPackageBodySQL( String schema )
		throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Get SchemaInfo(Package Body Source)
	 ***************************************
	 * @param schema
	 * @param packageBody
	 * @return
	 * @throws SQLException
	 */
	public String getPackageBodySourceBySchemaPackageBody( String schema, String packageBody )
		throws 
			UnsupportedOperationException,
			SQLException
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Get SchemaInfo(TYPE)
	 *********************************
	 * @return Lis<String>
	 * +----------+----------+
	 * + SCHEMA1  + TYPE1-1  +
	 * + SCHEMA1  + TYPE1-2  +
	 * +----------+----------+
	 * @throws SQLException
	 *********************************
	 */
	public List<List<String>> getSchemaType( String schema )
		throws SQLException
	{
		List<List<String>> dataLst = new ArrayList<List<String>>();
		
		String sql = this.schemaTypeSQL( schema );
		System.out.println( " -- getSchemaType -------------------" );
		System.out.println( sql );
		System.out.println( " ------------------------------------" );
		if ( sql != null )
		{
			try
			(
				Statement stmt = this.conn.createStatement();
				ResultSet rs = stmt.executeQuery( sql )
			)
			{
				ResultSetMetaData rsmd = rs.getMetaData();
				int headCnt = rsmd.getColumnCount();
				while ( rs.next() )
				{
					List<String> dataRow = new ArrayList<String>();
					//dataRow.add( rs.getString(1) );
					//dataRow.add( rs.getString(2) );
					for ( int i = 1; i <= headCnt; i++ )
					{
						dataRow.add( rs.getString(i) );
					}
					dataLst.add( dataRow );
				}
			}
		}
		
		return dataLst;
	}
	
	/**
	 * SQL to get type lists of schema.
	 * call by getSchemaType
	 *************************************
	 * @return schema
	 * @throws UnsupportedOperationException
	 */
	protected String schemaTypeSQL( String schema )
		throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Get SchemaInfo(Type Source)
	 ***************************************
	 * @param schema
	 * @param type
	 * @return
	 * @throws SQLException
	 */
	public String getTypeSourceBySchemaType( String schema, String type )
		throws 
			UnsupportedOperationException,
			SQLException
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Get SchemaInfo(TRIGGER)
	 *********************************
	 * @return Lis<String>
	 * +----------+-------------+
	 * + SCHEMA1  + TRIGGER1-1  +
	 * + SCHEMA1  + TRIGGER1-2  +
	 * +----------+-------------+
	 * @throws SQLException
	 *********************************
	 */
	public List<List<String>> getSchemaTrigger( String schema )
		throws SQLException
	{
		List<List<String>> dataLst = new ArrayList<List<String>>();
		
		String sql = this.schemaTriggerSQL( schema );
		System.out.println( " -- getSchemaTrigger -------------------" );
		System.out.println( sql );
		System.out.println( " ---------------------------------------" );
		if ( sql != null )
		{
			try
			(
				Statement stmt = this.conn.createStatement();
				ResultSet rs = stmt.executeQuery( sql )
			)
			{
				ResultSetMetaData rsmd = rs.getMetaData();
				int headCnt = rsmd.getColumnCount();
				while ( rs.next() )
				{
					List<String> dataRow = new ArrayList<String>();
					//dataRow.add( rs.getString(1) );
					//dataRow.add( rs.getString(2) );
					for ( int i = 1; i <= headCnt; i++ )
					{
						dataRow.add( rs.getString(i) );
					}
					dataLst.add( dataRow );
				}
			}
		}
		
		return dataLst;
	}
	
	/**
	 * SQL to get trigger lists of schema.
	 * call by getSchemaTrigger
	 *************************************
	 * @return schema
	 * @throws UnsupportedOperationException
	 */
	protected String schemaTriggerSQL( String schema )
		throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}
	
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
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Get SchemaInfo(SEQUENCE)
	 *********************************
	 * @return Lis<String>
	 * +----------+--------------+
	 * + SCHEMA1  + SEQUENCE1-1  +
	 * + SCHEMA1  + SEQUENCE1-2  +
	 * +----------+--------------+
	 * @throws SQLException
	 *********************************
	 */
	/*
	public List<List<String>> getSchemaSequence( String schema )
		throws SQLException
	{
		List<List<String>> dataLst = new ArrayList<List<String>>();
		
		String sql = this.schemaSequenceSQL( schema );
		System.out.println( " -- getSchemaSequence -------------------" );
		System.out.println( sql );
		System.out.println( " ----------------------------------------" );
		if ( sql != null )
		{
			try
			(
				Statement stmt = this.conn.createStatement();
				ResultSet rs = stmt.executeQuery( sql )
			)
			{
				ResultSetMetaData rsmd = rs.getMetaData();
				int headCnt = rsmd.getColumnCount();
				while ( rs.next() )
				{
					List<String> dataRow = new ArrayList<String>();
					//dataRow.add( rs.getString(1) );
					//dataRow.add( rs.getString(2) );
					for ( int i = 1; i <= headCnt; i++ )
					{
						dataRow.add( rs.getString(i) );
					}
					dataLst.add( dataRow );
				}
			}
		}
		
		return dataLst;
	}
	*/
	
	/**
	 * SQL to get sequence lists of schema.
	 * call by getSchemaSequence
	 *************************************
	 * @return schema
	 * @throws UnsupportedOperationException
	 */
	/*
	protected String schemaSequenceSQL( String schema )
		throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}
	*/
	
}
