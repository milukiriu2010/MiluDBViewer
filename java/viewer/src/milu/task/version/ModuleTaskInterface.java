package milu.task.version;

import milu.main.MainController;
import milu.gui.ctrl.info.MapInterface;

public interface ModuleTaskInterface 
{
	public void setMainController( MainController mainCtrl );
	public void setUrl( String strUrl );
	public void setMapInterface( MapInterface mapInf );
}
