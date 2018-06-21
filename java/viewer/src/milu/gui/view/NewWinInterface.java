package milu.gui.view;

import javafx.event.Event;

public interface NewWinInterface 
{
	public void createNewTab( Event event );
	public void createNewWindow( Event event );
	public void createNewDBConnection( Event event );
	public void openView( Class<?> castClazz );
}
