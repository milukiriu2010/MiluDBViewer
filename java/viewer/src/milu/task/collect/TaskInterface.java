package milu.task.collect;

import milu.db.MyDBAbstract;
import milu.main.MainController;

public interface TaskInterface 
{
	public void setMainController( MainController mainCtrl );
	public void setMyDBAbstract( MyDBAbstract myDBAbs );
}
