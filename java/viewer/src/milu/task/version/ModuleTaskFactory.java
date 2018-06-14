package milu.task.version;

import javafx.concurrent.Task;
import milu.main.MainController;

public class ModuleTaskFactory 
{
	public enum FACTORY_TYPE
	{
		CHECK
	}
	
	public static Task<Exception> createFactory
	(
		FACTORY_TYPE   factoryType,
		MainController mainCtrl,
		String         strUrl
	)
	{
		Task<Exception> task = null;
		if (FACTORY_TYPE.CHECK.equals(factoryType))
		{
			task = new ModuleCheckTask();
		}
		else
		{
			return null;
		}
		
		((ModuleTaskInterface)task).setMainController(mainCtrl);
		
		return task;
	}
}
