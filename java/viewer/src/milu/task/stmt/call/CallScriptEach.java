package milu.task.stmt.call;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.control.TabPane;
import milu.ctrl.sql.parse.CallObj;
import milu.ctrl.sql.parse.SQLBag;
import milu.db.MyDBAbstract;
import milu.db.access.ExecSQLAbstract;
import milu.db.access.ExecSQLFactory;
import milu.db.access.MyDBOverFetchSizeException;
import milu.gui.stmt.query.DBResultTab;
import milu.gui.view.DBView;
import milu.main.AppConf;
import milu.task.ProcInterface;
import milu.task.ProgressInterface;
import milu.task.ProgressReportInterface;

public class CallScriptEach 
	implements 
		ProgressReportInterface 
{
	private int           no      = -1;
	private DBView        dbView  = null;
	private MyDBAbstract  myDBAbs = null;
	private AppConf       appConf = null;
	private SQLBag        sqlBag  = null;
	private TabPane       tabPane = null;
	
	private int           procCnt  = -1;
	private long          execTime = -1;
	private Exception     myEx     = null;
	private ProcInterface procInf     = null;
	private Orientation   orientation = null;
	private ObservableList<CallObj> placeHolderParamLst = null;
	private List<List<Object>>  placeHolderInLst = null;
	
	private ProgressInterface  progressInf  = null;
	private double             assignedSize = 0.0;
	
	enum TYPE
	{
		DATA,
		META
	}
	
	
	public void setNo( int no )
	{
		this.no = no;
	}
	
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
	
	public void setSQLBag( SQLBag sqlBag )
	{
		this.sqlBag = sqlBag;
	}
	
	public void setProcInf( ProcInterface procInf )
	{
		this.procInf = procInf;
	}
	
	public void setOrientation( Orientation orientation )
	{
		this.orientation = orientation;
	}
	
	public void setPlaceHolderParamLst( ObservableList<CallObj> placeHolderParamLst )
	{
		this.placeHolderParamLst = placeHolderParamLst;
	}
	
	public void setPlaceHolderInLst( List<List<Object>> placeHolderInLst )
	{
		this.placeHolderInLst = placeHolderInLst;
	}
	
	public int getProcCnt()
	{
		return this.procCnt;
	}
	
	public long getExecTime()
	{
		return this.execTime;
	}
	
	public Exception getMyEx()
	{
		return this.myEx;
	}

	@Override
	public void setProgressInterface(ProgressInterface progressInf) 
	{
		this.progressInf = progressInf;
	}

	@Override
	public void setAssignedSize(double assignedSize) 
	{
		this.assignedSize = assignedSize;
	}

	@Override
	public void cancel() 
	{
	}

	public void exec()
	{
		// Result ColumnName
		List<Object>       headLst = new ArrayList<>();
		// Result Data
		List<List<Object>> dataLst = new ArrayList<>();
		
		ExecSQLAbstract execSQLAbs = new ExecSQLFactory().createCallableFactory( this.sqlBag, this.myDBAbs, this.appConf, this.placeHolderParamLst, null );
		if ( execSQLAbs == null )
		{
			return;
		}
		for ( int i = 0; i < this.placeHolderInLst.size(); i++ )
		{
			List<Object> placeHolder = this.placeHolderInLst.get(i);
			execSQLAbs.setPreLst(placeHolder);
		
			try
			{
				System.out.println( "CallScriptEach:start." );
				execSQLAbs.exec( this.appConf.getFetchMax(), this.appConf.getFetchPos() );
				System.out.println( "CallScriptEach:end." );
			}
			catch ( MyDBOverFetchSizeException myDBEx )
			{
				this.myEx = myDBEx;
			}
			catch ( SQLException sqlEx )
			{
				this.myEx = sqlEx;
			}
			catch ( Exception ex )
			{
				this.myEx = ex;
			}
			finally
			{
				// Result ColumnName
				if ( i == 0 )
				{
					headLst.addAll( execSQLAbs.getColNameLst() );
				}
				// Result Data
				dataLst.addAll( execSQLAbs.getDataLst() );
				
				this.procCnt = dataLst.size();
				
				this.execTime = execSQLAbs.getExecTime();
			}
		}
		
		Platform.runLater( ()->this.createDBResultTab(headLst,dataLst,this.tabPane, CallScriptEach.TYPE.DATA) );

	}

	private void createDBResultTab( List<Object> headLst, List<List<Object>> dataLst, TabPane tabP, CallScriptEach.TYPE type )
	{
		DBResultTab dbResultTab = new DBResultTab( this.dbView );
		// SELECT
		if ( SQLBag.TYPE.SELECT.equals(sqlBag.getType()) )
		{
			// [SELECT]-[DATA(ResultSet)]
			if ( CallScriptEach.TYPE.DATA.equals(type) )
			{
				dbResultTab.setText( "Data" );
				dbResultTab.setGraphic(null);
				dbResultTab.setSQL(sqlBag.getSQL());
				dbResultTab.setProcInf(this.procInf);
				dbResultTab.setOrientation(this.orientation);
				dbResultTab.setDataOnTableViewSQL(headLst, dataLst);
				
				if ( this.myEx != null )
				{
					dbResultTab.setException(this.myEx);
				}
				dbResultTab.setExecTime( this.execTime );
			}
			// [SELECT]-[META(ResultSetMetaData)]
			else
			{
				dbResultTab.setText( "Meta" );
				dbResultTab.setGraphic(null);
				dbResultTab.setSQL(sqlBag.getSQL());
				dbResultTab.setDataOnTableViewSQL(headLst, dataLst);
			}
		}
		// INSERT,UPDATE,DELETE...
		else
		{
			dbResultTab.setText( "Script " + no );
			dbResultTab.setSQL(sqlBag.getSQL());
			dbResultTab.setProcInf(this.procInf);
			dbResultTab.setOrientation(this.orientation);
			dbResultTab.setDataOnTableViewSQL(headLst, dataLst);
			
			if ( this.myEx != null )
			{
				dbResultTab.setException(this.myEx);
			}
			dbResultTab.setExecTime( this.execTime );
		}
		
		tabP.getTabs().add( dbResultTab );
	}

}
