package milu.db;

import java.sql.Driver;

import milu.db.driver.DriverClassConst;
import milu.db.driver.DriverShim;

public class MyDBFactory 
{
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
			if ( DriverClassConst.CLASS_NAME_ORACLE.val().equals(driverShim.getDriverClazzName()) )
			{
				myDBAbs = new MyDBOracle();
			}
			else if ( DriverClassConst.CLASS_NAME_POSTGRESQL.val().equals(driverShim.getDriverClassName()) )
			{
				myDBAbs = new MyDBPostgres();
			}
			else if ( DriverClassConst.CLASS_NAME_MYSQL.val().equals(driverShim.getDriverClassName()) )
			{
				myDBAbs = new MyDBMySQL();
			}
			else if 
			( 
				DriverClassConst.CLASS_NAME_CASSANDRA1.val().equals(driverShim.getDriverClassName()) ||
				DriverClassConst.CLASS_NAME_CASSANDRA2.val().equals(driverShim.getDriverClassName())
			)
			{
				myDBAbs = new MyDBCassandra();
			}
			else if ( DriverClassConst.CLASS_NAME_SQLSERVER.val().equals(driverShim.getDriverClassName()) )
			{
				myDBAbs = new MyDBSQLServer();
			}
			else if ( DriverClassConst.CLASS_NAME_SQLITE.val().equals(driverShim.getDriverClassName()) )
			{
				myDBAbs = new MyDBSQLite();
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
