package milu.task.imp;

import java.util.Map;

import milu.gui.ctrl.imp.ImportPreviewInterface;
import milu.gui.view.DBView;

public interface ImportTaskPreviewInterface 
{
	public void setImportPreviewInterface( ImportPreviewInterface impPreViewInf );
	public void setDBView( DBView dbView );
	public void setMapObj( Map<String,Object> mapObj );
}
