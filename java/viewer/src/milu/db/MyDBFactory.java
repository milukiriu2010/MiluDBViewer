package milu.db;

public class MyDBFactory 
{
	public static MyDBAbstract getInstance( String dbType )
	{
		MyDBAbstract myDBAbs = null;
		if ( "Oracle".equals( dbType ) )
		{
			myDBAbs = new MyDBOracle();
		}
		else if ( "PostgreSQL".equals( dbType ) )
		{
			myDBAbs = new MyDBPostgres();
		}
		else if ( "MySQL".equals( dbType ) )
		{
			myDBAbs = new MyDBMySQL();
		}
		else if ( "Cassandra".equals( dbType ) )
		{
			myDBAbs = new MyDBCassandra();
		}
		else
		{
			myDBAbs = new MyDBGeneral();
		}
		myDBAbs.init();
		return myDBAbs;
	}
}
