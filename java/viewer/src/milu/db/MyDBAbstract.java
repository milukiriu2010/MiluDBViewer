package milu.db;

import java.util.Map;
import java.util.HashMap;
import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import java.sql.SQLException;

import milu.db.driver.DriverShim;

import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;
import java.lang.reflect.InvocationTargetException;

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
	
	// JDBC Driver
	protected DriverShim   driverShim = null;
	
	// DB URL
	protected String  url = null;
	
	// DB properties
	protected Map<String,String>  dbOpts = new HashMap<>();
	
	// DB properties(special)
	protected Map<String,String>  dbOptsSpecial = new HashMap<>();
	
	// SchemaEntity Root
	protected SchemaEntity schemaRoot = null;
	
	abstract void init();
	
	@Override
	public int compareTo( MyDBAbstract obj )
	{
		if ( this.driverShim != null )
		{
			return this.driverShim.getDBName().compareTo( obj.driverShim.getDBName() );
		}
		else
		{
			return this.toString().compareTo( obj.toString() );
		}
	}
	
	public DriverShim getDriveShim()
	{
		return this.driverShim;
	}
	
	public void setDriverShim( DriverShim driverShim )
	{
		this.driverShim = driverShim;
	}
	
	/**
	 * Load JDBC Driver
	 ***********************************************
	 * @throws ClassNotFoundException
	 */
	abstract protected void loadDriver() 
			throws ClassNotFoundException, 
					SQLException, 
					InstantiationException,
					InvocationTargetException, 
					IllegalAccessException,
					NoSuchMethodException;
	
	abstract protected void loadSpecial();
	
	abstract public void processAfterException() throws SQLException;
	
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
		if ( this.dbOpts.size() == 0 )
		{	
			return this.url;
		}
		else
		{
			StringBuffer sb = new StringBuffer("?");
			this.dbOpts.forEach( (k,v)->sb.append(k+"="+v) );
			return this.url + sb.toString();
		}
	}
	
	public void setUrl( String url )
	{
		this.dbOpts.clear();
		if ( url == null )
		{
			this.url = url;
			return;
		}
		else if ( url.indexOf('?') == -1 )
		{
			this.url = url;
		}
		else
		{
			int pos = url.indexOf('?');
			this.url = url.substring( 0, pos );
			String strParam = url.substring( pos+1 );
			String[] strKVs = strParam.split("&");
			for ( String strKV : strKVs )
			{
				int posK = strKV.indexOf('=');
				if ( posK != -1 )
				{
					String k = strKV.substring(0,posK);
					String v = strKV.substring(posK+1);
					if ( "user".equals(k) )
					{
						this.username = v;
					}
					else if ( "password".equals(k) )
					{
						this.password = v;
					}
					else
					{
						this.dbOpts.put( k, v );
					}
				}
				else
				{
					this.dbOpts.put( strKV, null );
				}
			}
		}
	}
	
	public synchronized SchemaEntity getSchemaRoot()
	{
		return this.schemaRoot;
	}
	
	private Properties createProp()
	{
		Properties prop = new Properties();
		
		if ( this.username != null && this.username.length() != 0 )
		{
			prop.setProperty( "user", this.username );
		}
		if ( this.password != null && this.username.length() != 0 )
		{
			prop.setProperty( "password", this.password );
		}
		
		this.dbOpts.forEach( (k,v)->prop.setProperty(k,v) );
		
		return prop;
	}
	
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
	public synchronized void connect() 
		 throws ClassNotFoundException, 
			 	SQLException,
				InstantiationException,
				InvocationTargetException, 
				IllegalAccessException,
				NoSuchMethodException
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
	    //this.loadDriver();
	    
	    // Load special environment
	    this.loadSpecial();
	    
		System.out.println( "URL     :" + this.url );
		System.out.println( "UserName:" + this.username );
		
		// Connection
		Properties prop = this.createProp();
		if ( prop.isEmpty() == true )
		{
			this.conn =	DriverManager.getConnection( this.url );
		}
		else
		{
			this.conn =	DriverManager.getConnection( this.url, prop );
		}
		
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
		//this.conn = DriverManager.getConnection( this.url, this.username, this.password );
		this.conn =	DriverManager.getConnection( this.url, this.createProp() );
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
			System.out.println( "commit" );
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
			System.out.println( "rollback" );
		}
	}
	
	public synchronized Statement createStatement()
		throws SQLException
	{
		return this.conn.createStatement();
	}
}
