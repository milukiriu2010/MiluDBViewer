package milu.gui.ctrl.schema;

import javafx.scene.control.Tab;

import milu.entity.schema.SchemaEntity;

public class SchemaERViewTab extends Tab 
{
	private SchemaERView pane = new SchemaERView();
	
	public SchemaERViewTab( String tabName )
	{
		super();
		
		this.setText( tabName );
		
		this.setContent( this.pane );
	}
	
	public void setSchemaEntityRootER( SchemaEntity erEntity )
	{
		this.pane.setSchemaEntityRootER( erEntity );
	}
}
