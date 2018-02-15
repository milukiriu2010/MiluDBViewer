package milu.conf;

/**********************************
 * Application Configuration
 ********************************** 
 * @author milu
 **********************************
 */
public class AppConf
{
	// Singleton Instance
	private static AppConf instance = null;
	
	// Max fetch rows when selecting.
	protected int fetchMax = 100;
	
	public static AppConf getInstance()
	{
		if ( instance == null )
		{
			instance = new AppConf();
		}
		return instance;
	}
	
	protected AppConf()
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
}
