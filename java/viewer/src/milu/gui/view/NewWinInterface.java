package milu.gui.view;

import javafx.event.Event;
import milu.entity.schema.SchemaEntity;

public interface NewWinInterface 
{
	public void createNewTab( Event event );
	public void createNewWindow( Event event );
	public void createNewDBConnection( Event event );
	public void openView( Class<?> castClazz );
	public void openView( Class<?> castClazz, SchemaEntity schemaEntity );
}
