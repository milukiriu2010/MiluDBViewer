package milu.db;

import java.util.Map;

import milu.entity.schema.SchemaEntity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import java.sql.SQLException;

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
	
	/*
	// Supported Type List
	protected List<SchemaEntity.SCHEMA_TYPE>  suppoertedTypeLst = 
			new ArrayList<SchemaEntity.SCHEMA_TYPE>();
	*/	
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
	
	public void setUsername( String username )
	{
		this.username = username;
	}
	
	public void setPassword( String password )
	{
		this.password = password;
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
	
	public void setUrl( String url )
	{
		this.url = url;
	}
	
	public synchronized SchemaEntity getSchemaRoot()
	{
		return this.schemaRoot;
	}
	
	/*
	public List<SchemaEntity.SCHEMA_TYPE> getSupportedTypeLst()
	{
		return this.suppoertedTypeLst;
	}
	*/
	
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
	//abstract protected void createConnectionParameter( Map<String,String> dbOptMap );
	
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
	//public synchronized void connect( Map<String,String> dbOptMap ) 
	public synchronized void connect() 
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
	    
	    /*
	    // set username
	    this.username = dbOptMap.get( "UserName" );
	    
	    // set password
	    this.password = dbOptMap.get( "Password" );
	    */
	    
	    /*
	    // Create URL for DB Connection
		this.createConnectionParameter( dbOptMap );
	    */
	    
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
	public synchronized void reconnect() 
		 throws SQLException
	{
		/*
		// close connection, if already connected.
		try
		{
			this.close();
		}
		catch ( SQLException sqlEx )
		{
			// suppress error
		}
		*/
		
		
		this.conn = null;
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
	public synchronized void close()
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
	public synchronized boolean isConnected()
	{
		return ( this.conn != null );
	}
	
	/**
	 * Commit
	 ************************************* 
	 * @throws SQLException
	 */
	public synchronized void commit()
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
	public synchronized void rollback()
		throws SQLException
	{
		if ( this.conn != null )
		{
			this.conn.rollback();
		}
	}
	
	public synchronized Statement createStatement()
		throws SQLException
	{
		return this.conn.createStatement();
	}
}
