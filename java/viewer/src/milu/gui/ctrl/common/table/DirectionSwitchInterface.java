package milu.gui.ctrl.common.table;

import javafx.event.Event;
import javafx.geometry.Orientation;

public interface DirectionSwitchInterface 
{
	public Orientation getOrientation();
	public void setOrientation( Orientation orientation );
	public void switchDirection( Event event );
}
