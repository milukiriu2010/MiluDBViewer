package milu.gui.ctrl.imp;

import java.util.List;

public interface ImportPreviewInterface 
{
	public void setTableViewData( List<Object> columnLst, List<List<Object>> dataLst );
	public void setErrorType( ImportDataPanePreview.ERROR_TYPE errorType );
}
