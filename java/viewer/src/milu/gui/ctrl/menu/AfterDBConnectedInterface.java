package milu.gui.ctrl.menu;

import milu.db.MyDBAbstract;

public interface AfterDBConnectedInterface 
{
	public void afterConnection( MyDBAbstract myDBAbs );
}
