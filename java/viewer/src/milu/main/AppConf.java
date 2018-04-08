package milu.main;

/**********************************
 * Application Configuration
 ********************************** 
 * @author milu
 **********************************
 */
final public class AppConf
{
	// Max fetch rows when selecting.
	private Integer fetchMax = 100;
	
	// MySQL - Explain Extended
	private Boolean mysqlExplainExtended   = false;
	
	// MySQL - Explain Partitions
	private Boolean mysqlExplainOartitions = false;
	
	// MySQL - Explain Format
	private String  mysqlExplainFormat     = "TRADITIONAL";
	
	// Oracle - TNS Admin
	private String  oracleTnsAdmin = "";
	
	// PostgreSQL - Explain Analyze
	private Boolean postgresExplainAnalyze = false;
	
	// PostgreSQL - Explain Verbose
	private Boolean postgresExplainVerbose = false;
	
	// PostgreSQL - Explain Costs
	private Boolean postgresExplainCosts   = true;
	
	// PostgreSQL - Explain Buffers
	private Boolean postgresExplainBuffers = false;
	
	// PostgreSQL - Explain Timing
	private Boolean postgresExplainTiming  = false;
	
	// PostgreSQL - Explain Format
	private String postgresExplainFormat = "TEXT";
	
	public AppConf()
	{
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
		return this.mysqlExplainOartitions;
	}
	
	public void setMySQLExplainPartitions( Boolean mysqlExplainOartitions )
	{
		this.mysqlExplainOartitions = mysqlExplainOartitions;
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
}
