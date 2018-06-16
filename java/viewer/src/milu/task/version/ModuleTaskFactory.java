package milu.task.version;

import javafx.concurrent.Task;
import milu.main.MainController;
import milu.gui.ctrl.info.MapInterface;

public class ModuleTaskFactory 
{
	public enum FACTORY_TYPE
	{
		CHECK,
		DOWNLOAD,
		UPDATE
	}
	
	public static Task<Exception> createFactory
	(
		FACTORY_TYPE   factoryType,
		MainController mainCtrl,
		String         strUrl,
		MapInterface   mapInf
	)
	{
		Task<Exception> task = null;
		if (FACTORY_TYPE.CHECK.equals(factoryType))
		{
			task = new ModuleCheckTask();
		}
		else if (FACTORY_TYPE.DOWNLOAD.equals(factoryType))
		{
			task = new ModuleDownloadTask();
		}
		else if (FACTORY_TYPE.UPDATE.equals(factoryType))
		{
			task = new ModuleUpdateTask();
		}
		else
		{
			return null;
		}
		
		((ModuleTaskInterface)task).setMainController(mainCtrl);
		((ModuleTaskInterface)task).setUrl(strUrl);
		((ModuleTaskInterface)task).setMapInterface(mapInf);
		
		return task;
	}
}
