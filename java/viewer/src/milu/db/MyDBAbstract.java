package milu.db;

import java.util.Map;
import java.util.HashMap;
import java.util.Properties;

import javax.crypto.SecretKey;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.google.gson.annotations.Expose;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.DatabaseMetaData;

import java.sql.SQLException;

import milu.db.driver.DriverShim;
import milu.entity.schema.SchemaEntity;
import milu.security.MySecurityKey;
import milu.main.AppConf;

abstract public class MyDBAbstract
	implements
		Comparable<MyDBAbstract>
{
	// DB Connection
	@Expose(serialize = false, deserialize = false)
	protected Connection conn = null;
	
	// DB User Name
	@Expose(serialize = true, deserialize = true)
	protected String  username = null;
	
	// DB Password
	@Expose(serialize = false, deserialize = true)
	protected String  password = null;

	// DB Password(Encrypted)
	@Expose(serialize = true, deserialize = true)
	protected String  passwordEnc = null;
	
	// Initial Vector
	@Expose(serialize = true, deserialize = true)
	protected byte[]  iv = null;
	
	// JDBC Driver
	@Expose(serialize = true, deserialize = true)
	protected DriverShim   driverShim = null;
	
	// DB URL
	@Expose(serialize = true, deserialize = true)
	protected String  url = null;
	
	// DB properties
	// -----------------------------------------------------------------------
	// jdbc:c*:datastax://localhost:9042/system_schema?consistencyLevel=ONE
	//   consistencyLevel <=> ONE
	// -----------------------------------------------------------------------
	@Expose(serialize = true, deserialize = true)
	protected Map<String,String>  dbOpts = new HashMap<>();
	
	// DB properties(special)
	// -----------------------------------------------------------------------
	// jdbc:oracle:thin:@ORCL
	//   TNSAdmin <=> C:\oracle\instantclient_12_2\network\admin
	//   TNSName  <=> ORCL
	// -----------------------------------------------------------------------
	@Expose(serialize = true, deserialize = true)
	protected Map<String,String>  dbOptsSpecial = new HashMap<>();
	
	// DB properties(auxiliary)
	@Expose(serialize = true, deserialize = true)
	protected Map<String,String>  dbOptsAux = new HashMap<>();
	
	// SchemaEntity Root
	@Expose(serialize = false, deserialize = true)
	protected SchemaEntity schemaRoot = null;
	
	// AppConf
	@Expose(serialize = false, deserialize = false)
	protected AppConf appConf = null;
	
	public enum UPDATE
	{
		WITH,
		WITHOUT
	}
	
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
	
	abstract protected void loadSpecial();
	
	abstract public void processAfterException() throws SQLException;
	
	abstract protected void processAfterConnection();
	
	/**
	 * Get Driver URL
	 ***********************************************
	 * @return URL
	 */
	abstract public String getDriverUrl( Map<String, String> dbOptMap, MyDBAbstract.UPDATE update );
	
	/**
	 * Get Default Port Number
	 **********************************************
	 * @return Port Number
	 */
	abstract public int getDefaultPort();
	
	abstract protected void setSchemaRoot();
	
	abstract protected void addProp( Properties prop );
	
	/***********************************************
	 * Get DB User name
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
	
	/**********************************************
	 * Get DB Password
	 **********************************************
	 * @return
	 */
	public String getPassword()
	{
		return this.password;
	}
	
	public void setPassword( String password )
	{
		this.password = password;
	}
	
	public void setPassword( SecretKey secretKey )
		throws 
			NoSuchAlgorithmException, 
			NoSuchPaddingException,
			InvalidKeyException,
			InvalidAlgorithmParameterException,
			BadPaddingException,
			IllegalBlockSizeException,
			UnsupportedEncodingException
	{
		if ( secretKey == null )
		{
			return;
		}
		if ( this.passwordEnc == null || this.passwordEnc.length() == 0 )
		{
			return;
		}
		if ( this.iv == null )
		{
			return;
		}
		
		MySecurityKey mySecKey = new MySecurityKey();
		this.password = mySecKey.decrypt(secretKey, this.iv, this.passwordEnc );
		//System.out.println( "Decode:before:" + this.passwordEnc );
		//System.out.println( "Decode:after :" + this.password );
	}
	
	public String getPasswordEnc()
	{
		return this.passwordEnc;
	}
	
	public void setPasswordEnc( SecretKey secretKey )
		throws 
			NoSuchAlgorithmException, 
			NoSuchPaddingException,
			InvalidKeyException,
			InvalidAlgorithmParameterException,
			BadPaddingException,
			IllegalBlockSizeException
	{
		if ( secretKey == null )
		{
			return;
		}
		if ( this.password == null || this.password.length() == 0 )
		{
			return;
		}
		
		MySecurityKey mySecKey = new MySecurityKey();
		this.iv = mySecKey.createIV();
		this.passwordEnc = mySecKey.encrypt( secretKey, this.iv, password );
		//System.out.println( "Encode:before:" + this.password );
		//System.out.println( "Encode:after :" + this.passwordEnc );
	}
	
	public byte[] getIV()
	{
		return this.iv;
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
			StringBuffer sb = ( this.url != null && this.url.contains("?") == false ) ? new StringBuffer("?"):new StringBuffer("");
			this.dbOpts.forEach
			( 
				(k,v)->
				{
					if ( this.url.contains(k) == false )
					{
						sb.append(k+"="+v+"&");
					}
				}
			);
			if ( sb.length() >= 1 )
			{
				sb.deleteCharAt(sb.length()-1);
			}
			return this.url + sb.toString();
		}
	}
	
	public void setUrl( String url )
	{
		this.dbOpts.clear();
		this.dbOptsAux.clear();
		this.dbOptsSpecial.clear();
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
	
	public void setUrl( String url, boolean analyze )
	{
		if ( analyze == false )
		{
			this.url = url;
		}
		else
		{
			this.setUrl(url);
		}
	}
	
	public Map<String,String> getDBOpts()
	{
		return this.dbOpts;
	}
	
	public void setDBOpts( Map<String,String> dbOpts )
	{
		this.dbOpts.clear();
		dbOpts.forEach( (k,v)->this.dbOpts.put(k,v) );
	}
	
	public Map<String,String> getDBOptsSpecial()
	{
		return this.dbOptsSpecial;
	}
	
	public void setDBOptsSpecial( Map<String,String> dbOptsSpecial )
	{
		this.dbOptsSpecial.clear();
		dbOptsSpecial.forEach( (k,v)->this.dbOptsSpecial.put(k,v) );
	}
	
	public Map<String,String> getDBOptsAux()
	{
		return this.dbOptsAux;
	}
	
	public void setDBOptsAux( Map<String,String> dbOptsAux )
	{
		this.dbOptsAux.clear();
		dbOptsAux.forEach( (k,v)->this.dbOptsAux.put(k,v) );
	}
	
	public synchronized SchemaEntity getSchemaRoot()
	{
		return this.schemaRoot;
	}
	
	public void setAppConf( AppConf appConf )
	{
		this.appConf = appConf;
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
		
		this.addProp(prop);
		
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
	 * @throws SQLException 
	 */
	public synchronized void connect() throws SQLException
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
		
		this.setSchemaRoot();
		
		this.processAfterConnection();
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
	
	public synchronized Statement createStatement( int resultSetType, int resultSetConcurrency )
			throws SQLException
	{
		return this.conn.createStatement( resultSetType, resultSetConcurrency );
	}
	
	public synchronized PreparedStatement createPreparedStatement( String sql )
		throws SQLException
	{
		return this.conn.prepareStatement(sql);
	}

	public synchronized PreparedStatement createPreparedStatement( String sql, int resultSetType, int resultSetConcurrency )
		throws SQLException
	{
		return this.conn.prepareStatement(sql,resultSetType,resultSetConcurrency);
	}

	public synchronized DatabaseMetaData getMetaData()
		throws SQLException
	{
		return this.conn.getMetaData();
	}
}
