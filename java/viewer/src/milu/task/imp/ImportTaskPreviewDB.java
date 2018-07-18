package milu.task.imp;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.concurrent.Task;
import milu.ctrl.sql.generate.GenerateSQLAbstract;
import milu.ctrl.sql.generate.GenerateSQLFactory;
import milu.ctrl.sql.parse.SQLBag;
import milu.db.MyDBAbstract;
import milu.db.access.ExecSQLAbstract;
import milu.db.access.ExecSQLFactory;
import milu.db.access.MyDBOverFetchSizeException;
import milu.entity.schema.SchemaEntity;
import milu.gui.ctrl.imp.ImportData;
import milu.gui.ctrl.imp.ImportDataPanePreview;
import milu.gui.ctrl.imp.ImportPreviewInterface;
import milu.gui.view.DBView;
import milu.main.AppConf;
import milu.main.MainController;
import milu.task.ProgressInterface;

public class ImportTaskPreviewDB extends Task<Exception> 
	implements 
		ImportTaskPreviewInterface, 
		ProgressInterface 
{
	private ImportPreviewInterface impPreViewInf = null;
	private DBView  dbView = null;
	private Map<String,Object> mapObj = null;
	
	private final double MAX = 100.0;
	private double progress = 0.0;
	
	// ImportTaskPreviewInterface
	@Override
	public void setImportPreviewInterface(ImportPreviewInterface impPreViewInf) 
	{
		this.impPreViewInf = impPreViewInf;
	}

	// ImportTaskPreviewInterface
	@Override
	public void setDBView(DBView dbView) 
	{
		this.dbView = dbView;
	}

	// ImportTaskPreviewInterface
	@Override
	public void setMapObj(Map<String, Object> mapObj) 
	{
		this.mapObj = mapObj;
	}

	// Task
	@Override
	protected Exception call() throws Exception 
	{
		Exception taskEx = null;
		
		Thread.sleep(100);
		this.setProgress(0.0);
		
		SchemaEntity dstSchemaEntity = (SchemaEntity)this.mapObj.get(ImportData.DST_SCHEMA_ENTITY.val());
		//int columnCnt = dstSchemaEntity.getDefinitionLst().size();
		List<Object> columnLst = dstSchemaEntity.getDefinitionLst().stream()
			.map( data->data.get("column_name") )
			.collect(Collectors.toList());
		
		MainController mainCtrl = this.dbView.getMainController();
		AppConf appConf = mainCtrl.getAppConf();
		MyDBAbstract myDBAbsSrc = (MyDBAbstract)this.mapObj.get(ImportData.SRC_DB.val());
		SchemaEntity srcSchemaEntity = (SchemaEntity)this.mapObj.get(ImportData.SRC_SCHEMA_ENTITY.val());
		
		// --------------------------------------------------
		// Create SQL
		// --------------------------------------------------
		GenerateSQLAbstract gsAbs = GenerateSQLFactory.getInstance(GenerateSQLFactory.TYPE.SELECT);
		String strSQL = gsAbs.generate(srcSchemaEntity, myDBAbsSrc );
		
		SQLBag sqlBag = new SQLBag();
		sqlBag.setSQL(strSQL);
		sqlBag.setCommand(SQLBag.COMMAND.QUERY);
		sqlBag.setType(SQLBag.TYPE.SELECT);
		
		ExecSQLAbstract  execSQLAbs = 
				new ExecSQLFactory().createFactory( sqlBag, myDBAbsSrc, appConf, this, MAX );
		try
		{
			execSQLAbs.exec( appConf.getFetchMax(), appConf.getFetchPos() );
			
			return taskEx;
		}
		catch ( MyDBOverFetchSizeException myEx )
		{
			return taskEx;
		}
		catch ( SQLException sqlEx )
		{
			taskEx = sqlEx;
			return taskEx;
		}
		catch ( Exception ex )
		{
			taskEx = ex;
			return taskEx;
		}
		finally
		{
			List<Object>       headLst = execSQLAbs.getColNameLst();
			List<List<Object>> dataLst = execSQLAbs.getDataLst();

			Platform.runLater(()->{
				this.impPreViewInf.setTableViewData(headLst, dataLst);
				if ( columnLst.size() != headLst.size() )
				{
					this.impPreViewInf.setErrorType(ImportDataPanePreview.ERROR_TYPE.ERROR_COLUMN_CNT);
				}
				else
				{
					this.impPreViewInf.setErrorType(ImportDataPanePreview.ERROR_TYPE.ERROR_NO);
				}
				this.setMsg("");
			});
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
