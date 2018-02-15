package milu.db;

public class MyDBFactory 
{
	public static MyDBAbstract getInstance( String dbType )
	{
		if ( "Oracle".equals( dbType ) )
		{
			return new MyDBOracle();
		}
		else if ( "PostgreSQL".equals( dbType ) )
		{
			return new MyDBPostgres();
		}
		else if ( "MySQL".equals( dbType ) )
		{
			return new MyDBMySQL();
		}
		else if ( "Cassandra".equals( dbType ) )
		{
			return new MyDBCassandra();
		}
		else
		{
			return null;
		}
	}
}
