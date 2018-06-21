package milu.main;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import com.google.gson.annotations.Expose;

import milu.net.ProxyType;
import milu.security.MySecurityKey;

/**********************************
 * Application Configuration
 ********************************** 
 * @author milu
 **********************************
 */
final public class AppConf
{
	// Install Directory(for Development)
	@Expose(serialize = false, deserialize = false)
	private String  instDir = "";
	
	// Initial Directory for JDBC Driver
	@Expose(serialize = true, deserialize = true)
	private String  initDirJDBC = "";
	
	// Initial Directory for File DB(SQLite)
	@Expose(serialize = true, deserialize = true)
	private String  initDirFileDB = "";

	// Language Code
	@Expose(serialize = true, deserialize = true)
	private String  langCode = "";
	
	// Max fetch rows when selecting.
	@Expose(serialize = true, deserialize = true)
	private Integer fetchMax = 100;
	
	// MySQL - Explain Extended
	@Expose(serialize = true, deserialize = true)
	private Boolean mysqlExplainExtended   = false;
	
	// MySQL - Explain Partitions
	@Expose(serialize = true, deserialize = true)
	private Boolean mysqlExplainPartitions = false;
	
	// MySQL - Explain Format
	@Expose(serialize = true, deserialize = true)
	private String  mysqlExplainFormat     = "TRADITIONAL";
	
	// Oracle - TNS Admin
	@Expose(serialize = true, deserialize = true)
	private String  oracleTnsAdmin = "";
	
	// PostgreSQL - Explain Analyze
	@Expose(serialize = true, deserialize = true)
	private Boolean postgresExplainAnalyze = false;
	
	// PostgreSQL - Explain Verbose
	@Expose(serialize = true, deserialize = true)
	private Boolean postgresExplainVerbose = false;
	
	// PostgreSQL - Explain Costs
	@Expose(serialize = true, deserialize = true)
	private Boolean postgresExplainCosts   = true;
	
	// PostgreSQL - Explain Buffers
	@Expose(serialize = true, deserialize = true)
	private Boolean postgresExplainBuffers = false;
	
	// PostgreSQL - Explain Timing
	@Expose(serialize = true, deserialize = true)
	private Boolean postgresExplainTiming  = false;
	
	// PostgreSQL - Explain Format
	@Expose(serialize = true, deserialize = true)
	private String  postgresExplainFormat = "TEXT";
	
	// Proxy Type
	@Expose(serialize = true, deserialize = true)
	private ProxyType proxyType = ProxyType.NO_PROXY;
	
	// Proxy Host/IP
	@Expose(serialize = true, deserialize = true)
	private String  proxyHost = "";
	
	// Proxy Port
	@Expose(serialize = true, deserialize = true)
	private Integer proxyPort = 8080;
	
	// Proxy User
	@Expose(serialize = true, deserialize = true)
	private String  proxyUser = "";
	
	// Proxy Password
	@Expose(serialize = false, deserialize = true)
	private String  proxyPassword = "";
	
	// Proxy Password(Encrypted)
	@Expose(serialize = true, deserialize = true)
	private String  proxyPasswordEnc = "";
	
	// Initial Vector
	@Expose(serialize = true, deserialize = true)
	private byte[]  proxyIV = null;
	
	// Install Directory(for Development)
	public void setInstDir( String instDir )
	{
		this.instDir = instDir;
	}
	
	public String getInstDir()
	{
		return this.instDir;
	}
	
	// Initial Directory for JDBC Driver
	public String getInitDirJDBC()
	{
		return this.initDirJDBC;
	}
	
	public void setInitDirJDBC( String initDirJDBC )
	{
		this.initDirJDBC = initDirJDBC;
	}
	
	// Initial Directory for File DB(SQLite)
	public String getInitDirFileDB()
	{
		return this.initDirFileDB;
	}
	
	public void setInitDirFileDB( String initDirFileDB )
	{
		this.initDirFileDB = initDirFileDB;
	}
	
	// Language Code
	public String getLangCode()
	{
		return this.langCode;
	}
	
	public void setLangCode( String langCode )
	{
		this.langCode = langCode;
	}
	
	/**
	 * get max fetch rows
	 */
	public Integer getFetchMax()
	{
		return this.fetchMax;
	}
	
	/**
	 * set max fetch rows
	 */
	public void setFetchMax( Integer fetchMax )
	{
		this.fetchMax = fetchMax;
	}
	
	// MySQL - Explain Extended
	public Boolean getMySQLExplainExtended()
	{
		return this.mysqlExplainExtended;
	}
	
	public void setMySQLExplainExtended( Boolean mysqlExplainExtended )
	{
		this.mysqlExplainExtended = mysqlExplainExtended;
	}
	
	// MySQL - Explain Partitions
	public Boolean getMySQLExplainPartitions()
	{
		return this.mysqlExplainPartitions;
	}
	
	public void setMySQLExplainPartitions( Boolean mysqlExplainPartitions )
	{
		this.mysqlExplainPartitions = mysqlExplainPartitions;
	}
	
	// MySQL - Explain Format
	public String getMySQLExplainFormat()
	{
		return this.mysqlExplainFormat;
	}
	
	public void setMySQLExplainFormat( String mysqlExplainFormat )
	{
		this.mysqlExplainFormat = mysqlExplainFormat;
	}
	
	// Oracle - TNS Admin
	public String getOracleTnsAdmin()
	{
		return this.oracleTnsAdmin;
	}
	
	public void setOracleTnsAdmin( String oracleTnsAdmin )
	{
		this.oracleTnsAdmin = oracleTnsAdmin;
		if ( oracleTnsAdmin == null || oracleTnsAdmin.length() == 0 )
		{
			System.clearProperty("oracle.net.tns_admin");
		}
	}
	
	// PostgreSQL - Explain Analyze
	public Boolean getPostgresExplainAnalyze()
	{
		return postgresExplainAnalyze;
	}
	
	public void setPostgresExplainAnalyze( Boolean postgresExplainAnalyze )
	{
		this.postgresExplainAnalyze = postgresExplainAnalyze;
	}
	
	// PostgreSQL - Explain Verbose
	public Boolean getPostgresExplainVerbose()
	{
		return this.postgresExplainVerbose;
	}
	
	public void setPostgresExplainVerbose( Boolean postgresExplainVerbose )
	{
		this.postgresExplainVerbose = postgresExplainVerbose;
	}
	
	// PostgreSQL - Explain Costs
	public Boolean getPostgresExplainCosts()
	{
		return this.postgresExplainCosts;
	}
	
	public void setPostgresExplainCosts( Boolean postgresExplainCosts )
	{
		this.postgresExplainCosts = postgresExplainCosts;
	}
	
	// PostgreSQL - Explain Buffers
	public Boolean getPostgresExplainBuffers()
	{
		return this.postgresExplainBuffers;
	}
	
	public void setPostgresExplainBuffers( Boolean postgresExplainBuffers )
	{
		this.postgresExplainBuffers = postgresExplainBuffers;
	}
	
	// PostgreSQL - Explain Timing
	public Boolean getPostgresExplainTiming()
	{
		return this.postgresExplainTiming;
	}
	
	public void setPostgresExplainTiming( Boolean postgresExplainTiming )
	{
		this.postgresExplainTiming = postgresExplainTiming;
	}
	
	// PostgreSQL - Explain Format
	public String getPostgresExplainFormat()
	{
		return this.postgresExplainFormat;
	}
	
	public void setPostgresExplainFormat( String postgresExplainFormat )
	{
		this.postgresExplainFormat = postgresExplainFormat;
	}
	
	// Proxy Type
	public ProxyType getProxyType()
	{
		return this.proxyType;
	}
	
	public void setProxyType( ProxyType proxyType )
	{
		this.proxyType = proxyType;
	}
	
	// Proxy Host/IP
	public String  getProxyHost()
	{
		return this.proxyHost;
	}
	
	public void setProxyHost( String  proxyHost )
	{
		this.proxyHost = proxyHost;
	}
	
	// Proxy Port
	public Integer getProxyPort()
	{
		return this.proxyPort;
	}
	
	public void setProxyPort( Integer proxyPort )
	{
		this.proxyPort = proxyPort;
	}
	
	// Proxy User
	public String getProxyUser()
	{
		return this.proxyUser;
	}
	
	public void setProxyUser( String proxyUser )
	{
		this.proxyUser = proxyUser;
	}
	
	// Proxy Password
	public String  getProxyPassword()
	{
		return this.proxyPassword;
	}
	
	public void setProxyPassword( String proxyPassword )
	{
		this.proxyPassword = proxyPassword;
	}
	
	public void setProxyPassword( SecretKey secretKey )
		throws 
			NoSuchAlgorithmException, 
			NoSuchPaddingException,
			InvalidKeyException,
			InvalidAlgorithmParameterException,
			BadPaddingException,
			IllegalBlockSizeException,
			UnsupportedEncodingException
	{
		//System.out.println( "AppConf.setProxyPassword" );
		if ( secretKey == null )
		{
			return;
		}
		if ( this.proxyPasswordEnc == null || this.proxyPasswordEnc.length() == 0 )
		{
			return;
		}
		if ( this.proxyIV == null )
		{
			return;
		}
		
		MySecurityKey mySecKey = new MySecurityKey();
		this.proxyPassword = mySecKey.decrypt( secretKey, this.proxyIV, this.proxyPasswordEnc );
		//System.out.println( "Decode(setProxyPassword):before:" + this.proxyPasswordEnc );
		//System.out.println( "Decode(setProxyPassword):after :" + this.proxyPassword );
	}
	
	public String getProxyPasswordEnc()
	{
		return this.proxyPasswordEnc;
	}
	
	
	public void setProxyPasswordEnc( SecretKey secretKey )
		throws 
			NoSuchAlgorithmException, 
			NoSuchPaddingException,
			InvalidKeyException,
			InvalidAlgorithmParameterException,
			BadPaddingException,
			IllegalBlockSizeException
	{
		//System.out.println( "AppConf.setProxyPasswordEnc" );
		if ( secretKey == null )
		{
			return;
		}
		if ( this.proxyPassword == null || this.proxyPassword.length() == 0 )
		{
			return;
		}
		
		MySecurityKey mySecKey = new MySecurityKey();
		this.proxyIV = mySecKey.createIV();
		this.proxyPasswordEnc = mySecKey.encrypt( secretKey, this.proxyIV, this.proxyPassword );
		//System.out.println( "Encode(AppConf.setProxyPasswordEnc):before:" + this.proxyPassword );
		//System.out.println( "Encode(AppConf.setProxyPasswordEnc):after :" + this.proxyPasswordEnc );
	}
	
	public byte[] getProxyIV()
	{
		return this.proxyIV;
	}	
}
