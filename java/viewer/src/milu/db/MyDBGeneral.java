package milu.db;
import java.sql.SQLException;
import java.util.Map;

public class MyDBGeneral extends MyDBAbstract 
{
	@Override
	void init()
	{
	}

	@Override
	protected void loadSpecial() 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void processAfterException() throws SQLException 
	{
	}

	@Override
	public String getDriverUrl(Map<String, String> dbOptMap) 
	{
		return null;
	}

	@Override
	public int getDefaultPort() 
	{
		return 0;
	}
	
	/**
	 * Name on GUI Items
	 */
	@Override
	public String toString()
	{
		return "[<<Any>>]";
	}

}