package milu.task.query;

import java.sql.SQLException;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.control.TabPane;
import milu.ctrl.sqlparse.SQLBag;
import milu.db.MyDBAbstract;
import milu.db.access.MyDBOverFetchSizeException;
import milu.gui.ctrl.query.DBResultTab;
import milu.gui.view.DBView;
import milu.main.AppConf;
import milu.db.access.ExecSQLAbstract;
import milu.db.access.ExecSQLExplainFactory;

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
		ExecSQLAbstract execSQLAbs = new ExecSQLExplainFactory().createFactory( this.sqlBag, this.myDBAbs, this.appConf );
		if ( execSQLAbs == null )
		{
			return;
		}
		
		try
		{
			System.out.println( "ExecExplainEach:start." );
			execSQLAbs.exec( this.appConf.getFetchMax() );
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
			List<String>       headLst = execSQLAbs.getColNameLst();
			// Result Data
			List<List<String>> dataLst = execSQLAbs.getDataLst();
			
			this.procCnt = dataLst.size();
			
			this.execTime = execSQLAbs.getExecTime();
			
			Platform.runLater
			(
				()->
				{
					DBResultTab dbResultTab = new DBResultTab( this.dbView );
					dbResultTab.setText( "Script " + no );
					dbResultTab.setSQL(sqlBag.getSQL());
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
