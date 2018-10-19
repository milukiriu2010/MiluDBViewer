package milu.task.prepare;

import java.util.ArrayList;
import java.util.List;

import javafx.concurrent.Task;
import javafx.geometry.Orientation;
import javafx.scene.control.TabPane;
import milu.ctrl.sql.parse.SQLBag;
import milu.db.MyDBAbstract;
import milu.gui.view.DBView;
import milu.main.AppConf;
import milu.task.ProcInterface;
import milu.task.ProgressInterface;

public class PrepareScriptAllTask extends Task<Exception> 
	implements 
		PrepareTaskInterface,
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
	private List<List<Object>>  placeHolderLst = null;
	
	private final double MAX = 100.0;
	private double progress = 0.0;

	// PrepareTaskInterface
	@Override
	public void setDBView( DBView dbView )
	{
		this.dbView = dbView;
	}
	
	// PrepareTaskInterface
	@Override
	public void setMyDBAbstract( MyDBAbstract myDBAbs )
	{
		this.myDBAbs = myDBAbs;
	}
	
	// PrepareTaskInterface
	@Override
	public void setAppConf( AppConf appConf )
	{
		this.appConf = appConf;
	}
	
	// PrepareTaskInterface
	@Override
	public void setTabPane( TabPane tabPane )
	{
		this.tabPane = tabPane;
	}
	
	// PrepareTaskInterface
	@Override
	public void setSQLBagLst( List<SQLBag> sqlBagLst )
	{
		this.sqlBagLst = sqlBagLst;
	}
	
	// PrepareTaskInterface
	@Override
	public void setProcInf( ProcInterface procInf )
	{
		this.procInf = procInf;
	}
	
	// PrepareTaskInterface
	@Override
	public void setOrientation( Orientation orientation )
	{
		this.orientation = orientation;
	}
	
	// PrepareTaskInterface
	@Override
	public void setPlaceHolderLst( List<List<Object>> placeHolderLst )
	{
		this.placeHolderLst = placeHolderLst;
	}

	// Task
	@Override
	protected Exception call() throws Exception {
		System.out.println( "PrepareScriptAllTask:start." );
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
				this.setMsg(".");
				SQLBag sqlBag = this.sqlBagLst.get(i);
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
	
	// ProgressInterface
	@Override
	public void cancelProc()
	{
	}
}
