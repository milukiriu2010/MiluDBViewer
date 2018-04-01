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
		System.out.println( "ExecScriptTask:start." );
		final double MAX = 100.0;
		this.updateProgress( 0.0, MAX );
		int size = -1;
		long startTimeTotal = -1;
		List<List<String>> resDataLst = new ArrayList<>();
		
		try
		{
			Thread.sleep( 100 );
			size = this.sqlBagLst.size();
			startTimeTotal = System.nanoTime();
			for ( int i = 0; i < size; i++ )
			{
				long startTime = System.nanoTime();
				this.updateProgress( i/size, MAX );
				SQLBag sqlBag = this.sqlBagLst.get(i);
				Exception eachEx = this.eachCall(i, sqlBag);
				long endTime = System.nanoTime();
				
				List<String> resData = new ArrayList<>();
				// Script
				resData.add( "Script" + (i+1) );
				// Result
				resData.add( (eachEx == null) ? "OK":"NG" );
				// Exec Time
				resData.add( String.format( "%,d", endTime - startTime ) + "nsec" );
				// SQL
				resData.add( sqlBag.getSQL() );
				resDataLst.add(resData);
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
			final long startTimeTotalF = startTimeTotal;
			if ( size > 1 )
			{
				Platform.runLater
				(
					()->
					{
						List<String>       resHeadLst = new ArrayList<>();
						resHeadLst.add("Script");
						resHeadLst.add("Result");
						resHeadLst.add("Exec Time");
						resHeadLst.add("SQL");
						
						DBResultTab dbResultTab = new DBResultTab( this.dbView );
						dbResultTab.setText( "Result" );
						dbResultTab.setDataOnTableViewSQL(resHeadLst, resDataLst);
						long endTimeTotal = System.nanoTime();
						dbResultTab.setExecTime( endTimeTotal - startTimeTotalF );
						this.tabPane.getTabs().add( 0, dbResultTab );
						this.tabPane.getSelectionModel().select( dbResultTab );
					}
				);
			}
			if ( this.taskEx != null )
			{
				this.updateValue( this.taskEx );
			}
		}
		return null;
	}
	
	protected Exception eachCall( int no, SQLBag sqlBag )
	{
		long startTime = System.nanoTime();
		AccessDB   acsDB = new AccessDB( myDBAbs );
		int procCnt = -1;
		Exception  eachEx = null;
		
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
			
			return eachEx;
		}
		catch ( MyDBOverFetchSizeException myDBEx )
		{
			eachEx = myDBEx;
			return eachEx;
		}
		catch ( SQLException sqlEx )
		{
			eachEx = sqlEx;
			return eachEx;
		}
		catch ( Exception ex )
		{
			eachEx = ex;
			return eachEx;
		}
		finally
		{
			final int procCntF = procCnt;
			final Exception eachExF = eachEx;
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
					dbResultTab.setSQL(sqlBag.getSQL());
					dbResultTab.setDataOnTableViewSQL(headLst, dataLst);
					
					if ( eachExF != null )
					{
						dbResultTab.setException(eachExF);
					}
					long endTime = System.nanoTime();
					dbResultTab.setExecTime( endTime - startTime );
					
					this.tabPane.getTabs().add( dbResultTab );
				}
			);
		}
	}

}
