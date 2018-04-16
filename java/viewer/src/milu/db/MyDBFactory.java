package milu.db;

import java.sql.Driver;

import milu.db.driver.DriverConst;
import milu.db.driver.DriverShim;

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
	
	public static MyDBAbstract getInstance( Driver driver )
	{
		MyDBAbstract myDBAbs = null;
		if ( driver == null )
		{
			myDBAbs = new MyDBGeneral();
		}
		else if ( driver instanceof DriverShim )
		{
			DriverShim driverShim = (DriverShim)driver;
			if ( DriverConst.CLASS_NAME_ORACLE.val().equals(driverShim.getDriverClazzName()) )
			{
				myDBAbs = new MyDBOracle();
			}
			else if ( DriverConst.CLASS_NAME_POSTGRESQL.val().equals(driverShim.getDriverClassName()) )
			{
				myDBAbs = new MyDBPostgres();
			}
			else if ( DriverConst.CLASS_NAME_MYSQL.val().equals(driverShim.getDriverClassName()) )
			{
				myDBAbs = new MyDBMySQL();
			}
			else if 
			( 
				DriverConst.CLASS_NAME_CASSANDRA1.val().equals(driverShim.getDriverClassName()) ||
				DriverConst.CLASS_NAME_CASSANDRA2.val().equals(driverShim.getDriverClassName())
			)
			{
				myDBAbs = new MyDBCassandra();
			}
			else
			{
				myDBAbs = new MyDBGeneral();
			}
			myDBAbs.setDriverShim(driverShim);
		}
		else
		{
			myDBAbs = new MyDBGeneral();
		}
		myDBAbs.init();
		return myDBAbs;
	}
}
