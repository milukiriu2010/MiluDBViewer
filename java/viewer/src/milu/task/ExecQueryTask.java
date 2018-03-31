package milu.task;

import javafx.application.Platform;
import javafx.concurrent.Task;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import milu.db.MyDBAbstract;
import milu.db.access.AccessDB;
import milu.db.access.MyDBOverFetchSizeException;
import milu.gui.ctrl.query.SqlTableView;

import milu.conf.AppConf;
import milu.ctrl.sqlparse.SQLBag;

public class ExecQueryTask extends Task<Exception> 
{
	private MyDBAbstract myDBAbs = null;
	private AppConf      appConf = null;
	//private String       sql     = null;
	private SQLBag       sqlBag  = null;
	private SqlTableView tableViewSQL = null;
	private Exception    taskEx = null;
	
	/*
	public ExecQueryTask( MyDBAbstract myDBAbs, AppConf appConf, String sql, SqlTableView tableViewSQL )
	{
		super();
		this.myDBAbs = myDBAbs;
		this.appConf = appConf;
		this.sql     = sql;
		this.tableViewSQL = tableViewSQL;
	}
	*/
	
	public void setMyDBAbstract( MyDBAbstract myDBAbs )
	{
		this.myDBAbs = myDBAbs;
	}
	
	public void setAppConf( AppConf appConf )
	{
		this.appConf = appConf;
	}
	
	public void setSqlTableView( SqlTableView tableViewSQL )
	{
		this.tableViewSQL = tableViewSQL;
	}
	
	public void setSQL( String sql )
	{
		this.sqlBag = new SQLBag();
		this.sqlBag.setSQL(sql);
		this.sqlBag.setCommand(SQLBag.COMMAND.QUERY);
		this.sqlBag.setType(SQLBag.TYPE.SELECT);
	}
	
	public void setSQLBag ( SQLBag sqlBag )
	{
		this.sqlBag = sqlBag;
	}

	@Override
	protected Exception call() 
	{
		final double MAX = 100.0;
		
		AccessDB   acsDB = new AccessDB( myDBAbs );
		int procCnt = -1;
		
		try
		{
			System.out.println( "ExecQueryTask:start." );
			this.updateProgress( 0.0, MAX );
			Thread.sleep( 100 );
			
			if ( SQLBag.COMMAND.QUERY.equals( this.sqlBag.getCommand() ) )
			{
				acsDB.select( this.sqlBag.getSQL(), appConf.getFetchMax() );
			}
			else if ( SQLBag.COMMAND.TRANSACTION.equals( this.sqlBag.getCommand() ) )
			{
				procCnt = acsDB.transaction( this.sqlBag.getSQL(), -1 );
			}
			
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
			final int procCntF = procCnt;
			Platform.runLater
			(
				()->
				{
					// Result ColumnName
					List<String>       headLst = null;
					// Result Data
					List<List<String>> dataLst = null;
					
					if ( SQLBag.COMMAND.QUERY.equals( this.sqlBag.getCommand() ) )
					{
						headLst = acsDB.getColNameLst();
						dataLst = acsDB.getDataLst();
					}
					else if ( SQLBag.COMMAND.TRANSACTION.equals( this.sqlBag.getCommand() ) )
					{
						headLst = new ArrayList<>();
						headLst.add( "Result" );
						headLst.add( "SQL" );
						
						dataLst = new ArrayList<>();
						List<String> data = new ArrayList<>();
						if ( SQLBag.TYPE.INSERT.equals(this.sqlBag.getType()) )
						{
							data.add( procCntF + " row insert" );
							data.add( this.sqlBag.getSQL() );
						}
						else if ( SQLBag.TYPE.UPDATE.equals(this.sqlBag.getType()) )
						{
							data.add( procCntF + " row update" );
							data.add( this.sqlBag.getSQL() );
						}
						else if ( SQLBag.TYPE.DELETE.equals(this.sqlBag.getType()) )
						{
							data.add( procCntF + " row delete" );
							data.add( this.sqlBag.getSQL() );
						}
						dataLst.add( data );
						
					}
					else
					{
						headLst = new ArrayList<>();
						dataLst = new ArrayList<>();
					}
					
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
