package milu.gui.ctrl.common.inf;

import milu.db.driver.DriverShim;

public interface PaneSwitchDriverInterface 
{
	public void driverAdd( DriverShim driver );
	public void driverCancel();
}
