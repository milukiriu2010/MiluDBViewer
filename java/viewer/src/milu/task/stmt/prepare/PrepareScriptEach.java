package milu.task.stmt.prepare;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.scene.control.TabPane;
import milu.ctrl.sql.parse.SQLBag;
import milu.db.MyDBAbstract;
import milu.db.access.ExecSQLAbstract;
import milu.db.access.ExecSQLFactory;
import milu.db.access.ExecSQLSelectPrepared;
import milu.db.access.MyDBOverFetchSizeException;
import milu.gui.stmt.query.DBResultSelectTab;
import milu.gui.stmt.query.DBResultTab;
import milu.gui.view.DBView;
import milu.main.AppConf;
import milu.task.ProcInterface;
import milu.task.ProgressInterface;
import milu.task.ProgressReportInterface;

public class PrepareScriptEach 
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
	private List<List<Object>>  placeHolderLst = null;
	
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
	
	public void setPlaceHolderLst( List<List<Object>> placeHolderLst )
	{
		this.placeHolderLst = placeHolderLst;
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
		
		//ExecSQLAbstract execSQLAbs = new ExecSQLFactory().createPreparedFactory( this.sqlBag, this.myDBAbs, this.appConf, this.progressInf, this.assignedSize );
		ExecSQLAbstract execSQLAbs = new ExecSQLFactory().createPreparedFactory( this.sqlBag, this.myDBAbs, this.appConf, null );
		if ( execSQLAbs == null )
		{
			return;
		}
		for ( int i = 0; i < this.placeHolderLst.size(); i++ )
		{
			List<Object> placeHolder = this.placeHolderLst.get(i);
			execSQLAbs.setPreLst(placeHolder);
		
			try
			{
				System.out.println( "PrepareScriptEach:start." );
				execSQLAbs.exec( this.appConf.getFetchMax(), this.appConf.getFetchPos() );
				System.out.println( "PrepareScriptEach:end." );
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
		
		if ( execSQLAbs instanceof ExecSQLSelectPrepared )
		{
			List<Map<String,Object>> metaInfoDataMap = ((ExecSQLSelectPrepared)execSQLAbs).getColMetaInfoDataLst();
			
			// Result Meta Data
			List<List<Object>> metaInfoDataLst = 
				metaInfoDataMap.stream()
					.map( (Map<String,Object> map) -> map.values().stream().collect(Collectors.toList()) )
					.collect(Collectors.toList());

			
			Platform.runLater( ()->{
				DBResultSelectTab  dbRSTab = new DBResultSelectTab( this.dbView );
				dbRSTab.setText( "Script " + no );
				this.tabPane.getTabs().add(dbRSTab);
				this.createDBResultTab(headLst,dataLst,dbRSTab.getChildTabPane(),PrepareScriptEach.TYPE.DATA);
				this.createDBResultTab(((ExecSQLSelectPrepared)execSQLAbs).getColMetaInfoHeadLst(),metaInfoDataLst,dbRSTab.getChildTabPane(),PrepareScriptEach.TYPE.META);
			});
		}
		else
		{
			Platform.runLater( ()->this.createDBResultTab(headLst,dataLst,this.tabPane,PrepareScriptEach.TYPE.DATA) );
		}
	}

	private void createDBResultTab( List<Object> headLst, List<List<Object>> dataLst, TabPane tabP, PrepareScriptEach.TYPE type )
	{
		DBResultTab dbResultTab = new DBResultTab( this.dbView );
		// SELECT
		if ( SQLBag.TYPE.SELECT.equals(sqlBag.getType()) )
		{
			// [SELECT]-[DATA(ResultSet)]
			if ( PrepareScriptEach.TYPE.DATA.equals(type) )
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
