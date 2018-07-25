package milu.task.imp;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.concurrent.Task;
import milu.entity.schema.SchemaEntity;
import milu.file.table.MyFileImportAbstract;
import milu.file.table.MyFileImportFactory;
import milu.gui.ctrl.imp.ImportData;
import milu.gui.ctrl.imp.ImportDataPanePreview;
import milu.gui.ctrl.imp.ImportPreviewInterface;
import milu.gui.view.DBView;
import milu.task.ProgressInterface;

public class ImportTaskPreviewFile extends Task<Exception> 
	implements 
		ImportTaskPreviewInterface, 
		ProgressInterface 
{
	private ImportPreviewInterface impPreViewInf = null;
	//private DBView  dbView = null;
	private Map<String,Object> mapObj = null;
	
	private final double MAX = 100.0;
	private double progress = 0.0;
	
	private MyFileImportAbstract myFileAbs = null;
	
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
		//this.dbView = dbView;
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
		
		this.setProgress(0.0);
		
		SchemaEntity dstSchemaEntity = (SchemaEntity)this.mapObj.get(ImportData.DST_SCHEMA_ENTITY.val());
		int columnCnt = dstSchemaEntity.getDefinitionLst().size();
		List<Object> columnLst = dstSchemaEntity.getDefinitionLst().stream()
			.map( data->data.get("column_name") )
			.collect(Collectors.toList());
		
		//MainController mainCtrl = this.dbView.getMainController();
		String strFile = (String)this.mapObj.get(ImportData.SRC_FILE.val());
		File file = new File(strFile);
		this.myFileAbs = MyFileImportFactory.getInstance(file);
		try
		{
			Thread.sleep(100);
			this.myFileAbs.setProgressInterface(this);
			this.myFileAbs.setAssignedSize(MAX);
			this.myFileAbs.open(file);
			this.myFileAbs.load(columnCnt);
			
			return taskEx;
		}
		catch ( InterruptedException interruptEx )
		{
			taskEx = interruptEx;
			return taskEx;
		}
		catch ( Exception ex )
		{
			taskEx = ex;
			return taskEx;
		}
		finally
		{
			try
			{
				this.myFileAbs.close();
			}
			catch ( IOException ioEx )
			{
			}
			
			List<Object>       headLst = this.myFileAbs.getHeadLst();
			List<List<Object>> dataLst = this.myFileAbs.getDataLst();
			System.out.println( "columnLst.size:" + columnLst.size() );
			System.out.println( "dataLst.size  :" + dataLst.size() );

			Platform.runLater(()->{
				this.impPreViewInf.setTableViewData(headLst, dataLst);
				//this.impPreViewInf.setTableViewData(columnLst, dataLst);
				this.impPreViewInf.setErrorType(ImportDataPanePreview.ERROR_TYPE.ERROR_NO);
				this.setProgress(MAX);
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
	
	// ProgressInterface
	@Override
	public void cancelProc()
	{
		if ( this.myFileAbs != null )
		{
			this.myFileAbs.cancel();
		}
	}	
}
