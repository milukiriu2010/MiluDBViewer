package milu.task.imp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import javafx.concurrent.Task;
import milu.ctrl.sql.generate.GenerateSQLAbstract;
import milu.ctrl.sql.generate.GenerateSQLFactory;
import milu.ctrl.sql.parse.SQLBag;
import milu.db.MyDBAbstract;
import milu.db.access.ExecSQLAbstract;
import milu.db.access.ExecSQLFactory;
import milu.entity.schema.SchemaEntity;
import milu.gui.ctrl.imp.ImportData;
import milu.gui.ctrl.imp.ImportResultInterface;
import milu.gui.view.DBView;
import milu.main.AppConf;
import milu.main.MainController;
import milu.task.ProgressInterface;

public class ImportTaskResult extends Task<Exception> 
	implements
		ProgressInterface
{
	private ImportResultInterface impResultInf = null;
	private DBView  dbView = null;
	private Map<String,Object> mapObj = null;
	
	private final double MAX = 100.0;
	private double progress = 0.0;
	
	public void setImportResultInterface( ImportResultInterface impResultInf )
	{
		this.impResultInf = impResultInf;
	}
	
	public void setDBView(DBView dbView) 
	{
		this.dbView = dbView;
	}
	
	public void setMapObj(Map<String, Object> mapObj) 
	{
		this.mapObj = mapObj;
	}
	
	// Task
	@Override
	@SuppressWarnings("unchecked")
	protected Exception call() throws Exception 
	{
		Thread.sleep(100);
		this.setProgress(0.0);
		
		// --------------------------------------------------
		// Create SQL
		// --------------------------------------------------
		MyDBAbstract myDBAbs = this.dbView.getMyDBAbstract();
		GenerateSQLAbstract gsAbs = GenerateSQLFactory.getInstance(GenerateSQLFactory.TYPE.INSERT_BY_SIMPLE_WITHOUT_COMMENT);
		SchemaEntity schemaEntity = (SchemaEntity)this.mapObj.get(ImportData.DST_SCHEMA_ENTITY.val());
		String strSQL = gsAbs.generate( schemaEntity, myDBAbs );
		System.out.println( "strSQL:" + strSQL );
		
		// --------------------------------------------------
		// Create Import Data
		// --------------------------------------------------
		List<List<Object>> dataLst = (List<List<Object>>)this.mapObj.get(ImportData.IMPORT_DATA_LST.val());
		Integer skipRowCnt = (Integer)this.mapObj.get(ImportData.SKIP_ROW_COUNT.val());
		List<List<Object>> dataFilterLst = dataLst.subList(skipRowCnt.intValue(), dataLst.size());
		
		// --------------------------------------------------
		// Create ExecSQL Factory
		// --------------------------------------------------
		MainController mainCtrl = this.dbView.getMainController();
		AppConf appConf = mainCtrl.getAppConf();
		
		SQLBag sqlBag = new SQLBag();
		sqlBag.setSQL(strSQL);
		sqlBag.setCommand(SQLBag.COMMAND.TRANSACTION);
		sqlBag.setType(SQLBag.TYPE.INSERT);
		int cntOK = 0;
		int cntNG = 0;
		List<Object> ngHeadLst = new ArrayList<>();
		ngHeadLst.add("Error Message");
		ngHeadLst.addAll((List<Object>)this.mapObj.get(ImportData.IMPORT_HEAD_LST.val()));
		
		double assignedSizeDiv = MAX/(double)dataFilterLst.size();
		List<List<Object>> ngDataLst = new ArrayList<>();
		for ( List<Object> preLst : dataFilterLst )
		{
			ExecSQLAbstract  execSQLAbs = 
				new ExecSQLFactory().createPreparedFactory( sqlBag, myDBAbs, appConf, preLst );
			try
			{
				execSQLAbs.exec(1,-1);
				cntOK++;
				System.out.println( "OK:" + preLst );
			}
			catch ( Exception ex )
			{
				ex.printStackTrace();
				cntNG++;
				System.out.println( "NG:" + preLst );
				List<Object>  ngRow = new ArrayList<>();
				ngRow.add(ex.getMessage());
				ngRow.addAll(preLst);
				ngDataLst.add(ngRow);
			}
			
			this.addProgress(assignedSizeDiv);
			this.setMsg(".");
		}
		
		final int cntOKF = cntOK;
		final int cntNGF = cntNG;
		Platform.runLater(()->{
			this.impResultInf.setTotal(dataFilterLst.size());
			this.impResultInf.setOK(cntOKF);
			this.impResultInf.setNG(cntNGF);
			this.impResultInf.setSQL(strSQL);
			this.impResultInf.setTableViewData(ngHeadLst, ngDataLst);
			this.setProgress(MAX);
			this.setMsg("");
		});
		
		return null;
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
