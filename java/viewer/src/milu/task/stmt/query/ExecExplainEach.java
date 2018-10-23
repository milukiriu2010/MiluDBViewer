package milu.task.stmt.query;

import java.sql.SQLException;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.control.TabPane;
import milu.ctrl.sql.parse.SQLBag;
import milu.db.MyDBAbstract;
import milu.db.access.MyDBOverFetchSizeException;
import milu.db.access.ExecSQLAbstract;
import milu.db.access.ExecSQLExplainFactory;
import milu.gui.stmt.query.DBResultTab;
import milu.gui.view.DBView;
import milu.main.AppConf;
import milu.task.ProcInterface;

public class ExecExplainEach 
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
	private ProcInterface procInf    = null;
	
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
	
	public void exec()
	{
		ExecSQLAbstract execSQLAbs = new ExecSQLExplainFactory().createFactory( this.sqlBag, this.myDBAbs, this.appConf, null, 0.0 );
		if ( execSQLAbs == null )
		{
			return;
		}
		
		try
		{
			System.out.println( "ExecExplainEach:start." );
			execSQLAbs.exec( this.appConf.getFetchMax(), this.appConf.getFetchPos() );
			System.out.println( "ExecExplainEach:end." );
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
			
			Platform.runLater
			(
				()->
				{
					DBResultTab dbResultTab = new DBResultTab( this.dbView );
					dbResultTab.setText( "Script " + no );
					dbResultTab.setSQL(sqlBag.getSQL());
					dbResultTab.setProcInf(this.procInf);
					dbResultTab.setDataOnTableViewSQL(headLst, dataLst);
					
					if ( this.myEx != null )
					{
						dbResultTab.setException(this.myEx);
					}
					dbResultTab.setExecTime( this.execTime );
					
					this.tabPane.getTabs().add( dbResultTab );
				}
			);
		}
		
	}
}
