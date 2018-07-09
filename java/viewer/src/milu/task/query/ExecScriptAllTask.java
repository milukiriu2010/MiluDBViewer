package milu.task.query;

import javafx.application.Platform;
import javafx.concurrent.Task;

import java.util.List;
import java.util.ArrayList;

import javafx.scene.control.TabPane;
import javafx.geometry.Orientation;

import milu.ctrl.sql.parse.SQLBag;
import milu.db.MyDBAbstract;
import milu.gui.ctrl.common.inf.ProcInterface;
import milu.gui.ctrl.query.DBResultTab;
import milu.gui.view.DBView;
import milu.main.AppConf;
import milu.task.ProgressInterface;

public class ExecScriptAllTask extends Task<Exception>
	implements 
		ExecTaskInterface,
		ProgressInterface
{
	private DBView        dbView      = null;
	private MyDBAbstract  myDBAbs     = null;
	private AppConf       appConf     = null;
	private List<SQLBag>  sqlBagLst   = null;
	private TabPane       tabPane     = null;
	private Exception     taskEx      = null;
	private ProcInterface procInf     = null;
	private Orientation   orientation = null;
	
	private final double MAX = 100.0;
	private double progress = 0.0;
	
	// ExecTaskInterface
	@Override
	public void setDBView( DBView dbView )
	{
		this.dbView = dbView;
	}
	
	// ExecTaskInterface
	@Override
	public void setMyDBAbstract( MyDBAbstract myDBAbs )
	{
		this.myDBAbs = myDBAbs;
	}
	
	// ExecTaskInterface
	@Override
	public void setAppConf( AppConf appConf )
	{
		this.appConf = appConf;
	}
	
	// ExecTaskInterface
	@Override
	public void setTabPane( TabPane tabPane )
	{
		this.tabPane = tabPane;
	}
	
	// ExecTaskInterface
	@Override
	public void setSQLBagLst( List<SQLBag> sqlBagLst )
	{
		this.sqlBagLst = sqlBagLst;
	}
	
	// ExecTaskInterface
	@Override
	public void setProcInf( ProcInterface procInf )
	{
		this.procInf = procInf;
	}
	
	// ExecTaskInterface
	@Override
	public void setOrientation( Orientation orientation )
	{
		this.orientation = orientation;
	}
	
	// Task
	@Override
	protected Exception call()
	{
		System.out.println( "ExecScriptAllTask:start." );
		this.setProgress(0.0);
		int size = -1;
		long startTimeTotal = -1;
		List<List<Object>> resDataLst = new ArrayList<>();
		
		try
		{
			Thread.sleep( 100 );
			size = this.sqlBagLst.size();
			startTimeTotal = System.nanoTime();
			double assignedSize = (double)size*MAX;
			for ( int i = 0; i < size; i++ )
			{
				this.setProgress( (double)i*assignedSize );
				SQLBag sqlBag = this.sqlBagLst.get(i);
				
				ExecScriptEach execScriptEach = new ExecScriptEach();
				execScriptEach.setNo( (i+1) );
				execScriptEach.setDBView(this.dbView);
				execScriptEach.setMyDBAbstract(this.myDBAbs);
				execScriptEach.setAppConf(this.appConf);
				execScriptEach.setTabPane(this.tabPane);
				execScriptEach.setSQLBag(sqlBag);
				execScriptEach.setProcInf(this.procInf);
				execScriptEach.setOrientation(this.orientation);
				execScriptEach.setProgressInterface(this);
				execScriptEach.setAssignedSize(assignedSize);
				execScriptEach.exec();
				
				List<Object> resData = new ArrayList<>();
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
			this.setProgress(MAX);
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
						List<Object>       resHeadLst = new ArrayList<>();
						resHeadLst.add("Script");
						resHeadLst.add("Result");
						resHeadLst.add("Type");
						resHeadLst.add("Row");
						resHeadLst.add("Exec Time");
						resHeadLst.add("SQL");
						
						DBResultTab dbResultTab = new DBResultTab( this.dbView );
						dbResultTab.setText( "Result" );
						dbResultTab.setSQL(null);
						dbResultTab.setProcInf(this.procInf);
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
	
	// ProgressInterface
	@Override
	synchronized public void addProgress( double addpos )
	{
		this.progress += addpos;
		this.updateProgress( this.progress, MAX );
	}
	
	// ProgressInterface
	@Override
	synchronized public void setProgress( double pos )
	{
		this.progress = pos;
		this.updateProgress( this.progress, MAX );
	}
	
	// ProgressInterface
	@Override
	synchronized public void setMsg( String msg )
	{
		if ( "".equals(msg) == false )
		{
			String strMsg = String.format( "Loaded(%.3f%%) %s", this.progress, msg );
			this.updateMessage(strMsg);
		}
		else
		{
			this.updateMessage("");
		}
	}

}
