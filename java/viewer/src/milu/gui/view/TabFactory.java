package milu.gui.view;

import javafx.scene.control.Tab;

import milu.gui.ctrl.schema.DBSchemaTab;
import milu.gui.ctrl.jdbc.DBJdbcTab;

class TabFactory 
{
	static <T> Tab getInstance( DBView dbView, Class<T> clazz )
	{
		Tab tab = null;
		if ( clazz.equals(DBSchemaTab.class) )
		{
			tab = new DBSchemaTab( dbView );
		}
		else if ( clazz.equals(DBJdbcTab.class) )
		{
			tab = new DBJdbcTab( dbView );
		}
		else
		{
			return null;
		}
		
		return tab;
	}
}
