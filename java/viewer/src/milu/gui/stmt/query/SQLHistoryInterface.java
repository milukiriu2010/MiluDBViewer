package milu.gui.stmt.query;

import javafx.event.Event;

public interface SQLHistoryInterface 
{
	public void prevSQL( Event event );
	public void nextSQL( Event event );
}
