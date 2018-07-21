package milu.task.query;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.scene.control.TabPane;
import javafx.geometry.Orientation;

import milu.ctrl.sql.parse.SQLBag;
import milu.db.MyDBAbstract;
import milu.db.access.MyDBOverFetchSizeException;
import milu.db.access.ExecSQLFactory;
import milu.db.access.ExecSQLAbstract;
import milu.db.access.ExecSQLSelect;
import milu.gui.ctrl.query.DBResultSelectTab;
import milu.gui.ctrl.query.DBResultTab;
import milu.gui.view.DBView;
import milu.main.AppConf;
import milu.task.ProcInterface;
import milu.task.ProgressInterface;
import milu.task.ProgressReportInterface;

public class ExecScriptEach 
	implements ProgressReportInterface
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
	
	// ProgressReportInterface
	@Override
	public void setProgressInterface( ProgressInterface progressInf )
	{
		this.progressInf = progressInf;
	}
	
	// ProgressReportInterface
	@Override
	public void setAssignedSize( double assignedSize )
	{
		this.assignedSize = assignedSize;
	}
	
	// ProgressReportInterface
	@Override
	public void cancel()
	{
		
	}
	
	public void exec()
	{
		ExecSQLAbstract execSQLAbs = new ExecSQLFactory().createFactory( this.sqlBag, this.myDBAbs, this.appConf, this.progressInf, this.assignedSize );
		if ( execSQLAbs == null )
		{
			return;
		}
		
		try
		{
			System.out.println( "ExecScriptEach:start." );
			execSQLAbs.exec( this.appConf.getFetchMax(), this.appConf.getFetchPos() );
			System.out.println( "ExecScriptEach:end." );
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
			List<Object>       headLst = execSQLAbs.getColNameLst();
			// Result Data
			List<List<Object>> dataLst = execSQLAbs.getDataLst();
			
			this.procCnt = dataLst.size();
			
			this.execTime = execSQLAbs.getExecTime();
			
			if ( execSQLAbs instanceof ExecSQLSelect )
			{
				List<Map<String,Object>> metaInfoDataMap = ((ExecSQLSelect)execSQLAbs).getColMetaInfoDataLst();
				
				// Result Meta Data
				List<List<Object>> metaInfoDataLst = 
					metaInfoDataMap.stream()
						.map( (Map<String,Object> map) -> map.values().stream().collect(Collectors.toList()) )
						.collect(Collectors.toList());
				
				/*
				System.out.println( "====== Meta Data ======");
				metaInfoDataLst.forEach(
					(metaLst)->
					{
						System.out.println( "*******************");
						metaLst.forEach(System.out::println);
					}
				);
				System.out.println( "======+++++++++++======");
				*/
				
				Platform.runLater( ()->{
					DBResultSelectTab  dbRSTab = new DBResultSelectTab( this.dbView );
					dbRSTab.setText( "Script " + no );
					this.tabPane.getTabs().add(dbRSTab);
					this.createDBResultTab(headLst,dataLst,dbRSTab.getChildTabPane(),ExecScriptEach.TYPE.DATA);
					this.createDBResultTab(((ExecSQLSelect)execSQLAbs).getColMetaInfoHeadLst(),metaInfoDataLst,dbRSTab.getChildTabPane(),ExecScriptEach.TYPE.META);
					//this.createDBResultTab(headLst,dataLst,this.tabPane);	
				});
			}
			else
			{
				Platform.runLater( ()->this.createDBResultTab(headLst,dataLst,this.tabPane,ExecScriptEach.TYPE.DATA) );
			}
		}
		
	}
	
	private void createDBResultTab( List<Object> headLst, List<List<Object>> dataLst, TabPane tabP, ExecScriptEach.TYPE type )
	{
		DBResultTab dbResultTab = new DBResultTab( this.dbView );
		// SELECT
		if ( SQLBag.TYPE.SELECT.equals(sqlBag.getType()) )
		{
			// [SELECT]-[DATA(ResultSet)]
			if ( ExecScriptEach.TYPE.DATA.equals(type) )
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
