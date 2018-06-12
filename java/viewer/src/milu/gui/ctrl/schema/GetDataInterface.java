package milu.gui.ctrl.schema;

import javafx.event.Event;

public interface GetDataInterface 
{
	public void getDataNoRefresh( Event event );
	public void getDataWithRefresh( Event event );
}
