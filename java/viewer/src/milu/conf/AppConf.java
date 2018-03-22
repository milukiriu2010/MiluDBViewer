package milu.conf;

/**********************************
 * Application Configuration
 ********************************** 
 * @author milu
 **********************************
 */
final public class AppConf
{
	// Max fetch rows when selecting.
	private int fetchMax = 100;
	
	// PostgreSQL - Explain Format
	private String postgresExplainFormat = "TEXT";
	
	public AppConf()
	{
	}
	
	/**
	 * get max fetch rows
	 */
	public int getFetchMax()
	{
		return this.fetchMax;
	}
	
	/**
	 * set max fetch rows
	 */
	public void setFetchMax( int fetchMax )
	{
		this.fetchMax = fetchMax;
	}
	
	public String getPostgresExplainFormat()
	{
		return this.postgresExplainFormat;
	}
	
	public void setPostgresExplainFormat( String postgresExplainFormat )
	{
		this.postgresExplainFormat = postgresExplainFormat;
	}
}
