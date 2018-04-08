package milu.task.query;

import javafx.application.Platform;
import javafx.concurrent.Task;

import java.util.List;
import java.util.ArrayList;

import javafx.scene.control.TabPane;

import milu.db.MyDBAbstract;
import milu.gui.ctrl.query.DBResultTab;
import milu.gui.view.DBView;
import milu.main.AppConf;
import milu.ctrl.sqlparse.SQLBag;

public class ExecScriptAllTask extends Task<Exception> 
{
	private DBView       dbView     = null;
	private MyDBAbstract myDBAbs    = null;
	private AppConf      appConf    = null;
	private List<SQLBag> sqlBagLst  = null;
	private TabPane      tabPane    = null;
	private Exception    taskEx     = null;
	
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
		System.out.println( "ExecScriptAllTask:start." );
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
				this.updateProgress( i/size, MAX );
				SQLBag sqlBag = this.sqlBagLst.get(i);
				
				ExecScriptEach execScriptEach = new ExecScriptEach();
				execScriptEach.setNo( (i+1) );
				execScriptEach.setDBView(this.dbView);
				execScriptEach.setMyDBAbstract(this.myDBAbs);
				execScriptEach.setAppConf(this.appConf);
				execScriptEach.setTabPane(this.tabPane);
				execScriptEach.setSQLBag(sqlBag);
				execScriptEach.exec();
				
				List<String> resData = new ArrayList<>();
				// Script
				resData.add( "Script" + (i+1) );
				// Result
				Exception eachEx = execScriptEach.getMyEx();
				String result = "OK";
				if ( eachEx != null )
				{
					String className = eachEx.getClass().getName();
					result = "NG(" + className.substring(className.lastIndexOf(".")+1) + ")";
				}
				resData.add( result );
				// Type
				resData.add( sqlBag.getType().toString() ); 
				// Cnt
				resData.add( String.valueOf(execScriptEach.getProcCnt()) );
				// Exec Time
				resData.add( String.format( "%,d", execScriptEach.getExecTime() ) + "nsec" );
				// SQL
				resData.add( sqlBag.getSQL() );
				resDataLst.add(resData);
			}
			this.updateProgress( MAX, MAX );
			return null;
		}
		catch ( InterruptedException interruptEx )
		{
			this.taskEx = interruptEx;
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
						resHeadLst.add("Type");
						resHeadLst.add("Row");
						resHeadLst.add("Exec Time");
						resHeadLst.add("SQL");
						
						DBResultTab dbResultTab = new DBResultTab( this.dbView );
						dbResultTab.setText( "Result" );
						dbResultTab.setSQL(null);
						dbResultTab.setDataOnTableViewSQL(resHeadLst, resDataLst);
						long endTimeTotal = System.nanoTime();
						dbResultTab.setExecTime( endTimeTotal - startTimeTotalF );
						// 0 => "..." => already remove
						// 1 => "Result"
						// 2 => "Script 1"
						// 3 => "Script 2"
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
	}

}
