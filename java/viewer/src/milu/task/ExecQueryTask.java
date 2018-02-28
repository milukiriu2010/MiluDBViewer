package milu.task;

import javafx.application.Platform;
import javafx.concurrent.Task;

import java.sql.SQLException;
import java.util.List;

import milu.db.MyDBAbstract;
import milu.db.AccessDB;
import milu.db.MyDBOverFetchSizeException;

import milu.gui.ctrl.query.SqlTableView;

import milu.conf.AppConf;

public class ExecQueryTask extends Task<Exception> 
{
	private MyDBAbstract myDBAbs = null;
	private AppConf      appConf = null;
	private String       sql     = null;
	private SqlTableView tableViewSQL = null;
	private Exception  taskEx = null;
	
	public ExecQueryTask( MyDBAbstract myDBAbs, AppConf appConf, String sql, SqlTableView tableViewSQL )
	{
		super();
		this.myDBAbs = myDBAbs;
		this.appConf = appConf;
		this.sql     = sql;
		this.tableViewSQL = tableViewSQL;
	}

	@Override
	protected Exception call() 
	{
		final double MAX = 100.0;
		
		AccessDB   acsDB = new AccessDB( myDBAbs );
		
		try
		{
			System.out.println( "ExecQueryTask:start." );
			this.updateProgress( 0.0, MAX );
			Thread.sleep( 100 );
			acsDB.select( sql, appConf.getFetchMax() );
			
			return null;
		}
		catch ( MyDBOverFetchSizeException myDBEx )
		{
			this.taskEx = myDBEx;			
			return myDBEx;
		}
		catch ( SQLException sqlEx )
		{
			this.taskEx = sqlEx;			
			return sqlEx;
		}
		catch ( Exception ex )
		{
			this.taskEx = ex;			
			return ex;
		}
		finally
		{
			Platform.runLater
			(
				()->
				{
					// Result ColumnName
					List<String>       headLst = acsDB.getColNameLst();
					// Result Data
					List<List<String>> dataLst = acsDB.getDataLst();
					
					this.tableViewSQL.setTableViewSQL( headLst, dataLst );

					if ( taskEx != null )
					{
						this.updateValue( taskEx );
					}
					else
					{
						this.updateProgress( MAX, MAX );
					}
				}
			);
		}
	}

}
