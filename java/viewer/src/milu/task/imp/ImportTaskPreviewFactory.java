package milu.task.imp;

import java.util.Map;
import javafx.concurrent.Task;
import milu.gui.ctrl.imp.ImportDataPane;
import milu.gui.ctrl.imp.ImportPreviewInterface;
import milu.gui.view.DBView;

public class ImportTaskPreviewFactory 
{
	public static Task<Exception> createFactory
	( 
		ImportDataPane.SRC_TYPE srcType,
		ImportPreviewInterface  impPreviewInf,
		DBView                  dbView,
		Map<String,Object>      mapObj
	)
	{
		Task<Exception> task = null;
		
		if ( ImportDataPane.SRC_TYPE.FILE.equals(srcType) )
		{
			task = new ImportTaskPreviewFile();
		}
		else if ( ImportDataPane.SRC_TYPE.DB.equals(srcType) )
		{
			task = new ImportTaskPreviewDB();
		}
		else
		{
			return null;
		}
		
		((ImportTaskPreviewInterface)task).setImportPreviewInterface(impPreviewInf);
		((ImportTaskPreviewInterface)task).setDBView(dbView);
		((ImportTaskPreviewInterface)task).setMapObj(mapObj);
		
		return task;
	}
}
