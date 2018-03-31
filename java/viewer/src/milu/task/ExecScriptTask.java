package milu.task;

import javafx.application.Platform;
import javafx.concurrent.Task;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import javafx.scene.control.TabPane;

import milu.db.MyDBAbstract;
import milu.db.access.AccessDB;
import milu.db.access.MyDBOverFetchSizeException;
import milu.gui.ctrl.query.DBResultTab;
import milu.gui.ctrl.query.SqlTableView;
import milu.gui.view.DBView;

import milu.conf.AppConf;
import milu.ctrl.sqlparse.SQLBag;

public class ExecScriptTask extends Task<Exception> 
{
	private DBView       dbView  = null;
	private MyDBAbstract myDBAbs = null;
	private AppConf      appConf = null;
	private List<SQLBag> sqlBagLst  = null;
	//private SqlTableView tableViewSQL = null;
	private TabPane      tabPane = null;
	private Exception    taskEx = null;
	
	public void setDBView( DBView dbView )
	{
		this.dbView = dbView;
	}
	
	public void setMyDBAbstract( MyDBAbstract myDBAbs )
	{
		this.myDBAbs = myDBAbs;
	}
	
	public void setAppConf( AppConf appConf )
	{
		this.appConf = appConf;
	}
	
	public void setTabPane( TabPane tabPane )
	{
		this.tabPane = tabPane;
	}
	
	public void setSQLBagLst( List<SQLBag> sqlBagLst )
	{
		this.sqlBagLst = sqlBagLst;
	}

	@Override
	protected Exception call()
	{
		final double MAX = 100.0;
		this.updateProgress( 0.0, MAX );
		
		try
		{
			Thread.sleep( 100 );
			System.out.println( "ExecScriptTask:start." );
			int size = this.sqlBagLst.size();
			for ( int i = 0; i < size; i++ )
			{
				this.updateProgress( i/size, MAX );
				SQLBag sqlBag = this.sqlBagLst.get(i);
				this.eachCall(i, sqlBag);
			}
			this.updateProgress( MAX, MAX );
		}
		catch ( InterruptedException interruptEx )
		{
			this.taskEx = interruptEx;
			this.updateValue( this.taskEx );
			return interruptEx;
		}
		finally
		{
			if ( this.taskEx != null )
			{
				this.updateValue( this.taskEx );
			}
		}
		return null;
	}
	
	protected void eachCall( int no, SQLBag sqlBag )
	{
		AccessDB   acsDB = new AccessDB( myDBAbs );
		int procCnt = -1;
		
		try
		{
			if ( SQLBag.COMMAND.QUERY.equals( sqlBag.getCommand() ) )
			{
				acsDB.select( sqlBag.getSQL(), appConf.getFetchMax() );
			}
			else if ( SQLBag.COMMAND.TRANSACTION.equals( sqlBag.getCommand() ) )
			{
				procCnt = acsDB.transaction( sqlBag.getSQL(), -1 );
			}
		}
		catch ( MyDBOverFetchSizeException myDBEx )
		{
			this.taskEx = myDBEx;
		}
		catch ( SQLException sqlEx )
		{
			this.taskEx = sqlEx;
		}
		catch ( Exception ex )
		{
			this.taskEx = ex;
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
					
					if ( SQLBag.COMMAND.QUERY.equals( sqlBag.getCommand() ) )
					{
						headLst = acsDB.getColNameLst();
						dataLst = acsDB.getDataLst();
					}
					else if ( SQLBag.COMMAND.TRANSACTION.equals( sqlBag.getCommand() ) )
					{
						headLst = new ArrayList<>();
						headLst.add( "Result" );
						headLst.add( "SQL" );
						
						dataLst = new ArrayList<>();
						List<String> data = new ArrayList<>();
						if ( SQLBag.TYPE.INSERT.equals(sqlBag.getType()) )
						{
							data.add( procCntF + " row insert" );
							data.add( sqlBag.getSQL() );
						}
						else if ( SQLBag.TYPE.UPDATE.equals(sqlBag.getType()) )
						{
							data.add( procCntF + " row update" );
							data.add( sqlBag.getSQL() );
						}
						else if ( SQLBag.TYPE.DELETE.equals(sqlBag.getType()) )
						{
							data.add( procCntF + " row delete" );
							data.add( sqlBag.getSQL() );
						}
						dataLst.add( data );
						
					}
					else
					{
						headLst = new ArrayList<>();
						dataLst = new ArrayList<>();
					}
					
					DBResultTab dbResultTab = new DBResultTab( this.dbView );
					dbResultTab.setText( "Script " + (no+1) );
					System.out.println( "headLst.size:" + headLst.size() );
					System.out.println( "dataLst.size:" + dataLst.size() );
					dbResultTab.setDataOnTableViewSQL(headLst, dataLst);
					this.tabPane.getTabs().add( dbResultTab );
				}
			);
		}
	}

}
