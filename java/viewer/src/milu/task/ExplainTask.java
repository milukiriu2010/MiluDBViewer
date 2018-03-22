package milu.task;

import javafx.concurrent.Task;

import java.sql.SQLException;
import java.util.List;

import milu.db.MyDBAbstract;
import milu.db.explain.ExplainDBFactory;
import milu.db.explain.ExplainDBAbstract;

import milu.ctrl.MainController;

import milu.gui.ctrl.query.SqlTableView;


public class ExplainTask extends Task<Exception> 
{
	private MyDBAbstract   myDBAbs      = null;
	private MainController mainCtrl     = null;
	private String         sql          = null;
	private SqlTableView   tableViewSQL = null;
	private Exception    taskEx = null;
	
	public ExplainTask( MyDBAbstract myDBAbs, MainController mainCtrl, String sql, SqlTableView tableViewSQL )
	{
		super();
		this.myDBAbs  = myDBAbs;
		this.mainCtrl = mainCtrl;
		this.sql      = sql;
		this.tableViewSQL = tableViewSQL;
	}	

	@Override
	protected Exception call() 
	{
		final double MAX = 100.0;
		
		ExplainDBAbstract explainDBAbs = ExplainDBFactory.getInstance(this.myDBAbs,this.mainCtrl);
		try
		{
			System.out.println( "ExplainTask." );
			this.updateProgress( 0.0, MAX );
			Thread.sleep( 100 );
			
			if ( explainDBAbs != null )
			{
				explainDBAbs.explain( sql );
			}
			
			return null;
		}
		catch ( SQLException sqlEx )
		{
			this.taskEx = sqlEx;
			return sqlEx;	
		}
		catch ( Exception ex )
		{
			this.taskEx = ex;			
			return ex;	
		}
		finally
		{
			if ( explainDBAbs != null )
			{
				// Result ColumnName
				List<String>       headLst = explainDBAbs.getColNameLst();
				// Result Data
				List<List<String>> dataLst = explainDBAbs.getDataLst();
				
				this.tableViewSQL.setTableViewSQL( headLst, dataLst );
				
				if ( taskEx != null )
				{
					this.updateValue( taskEx );
				}
				else
				{
					this.updateProgress( MAX, MAX );
				}				
				
				explainDBAbs = null;
			}
		}
	}

}
