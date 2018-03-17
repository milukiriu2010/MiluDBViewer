package milu.gui.ctrl.schema;

import javafx.scene.control.Tab;
import javafx.scene.control.ScrollPane;

import milu.entity.schema.SchemaEntity;
import milu.db.MyDBAbstract;

public class SchemaERViewTab extends Tab 
{
	private ScrollPane   root = new ScrollPane();
	
	private SchemaERView pane = new SchemaERView();
	
	public SchemaERViewTab( String tabName )
	{
		super();
		
		this.setText( tabName );
		
		this.root.setContent( this.pane );
		
		this.setContent( this.root );
	}
	
	public void setMyDBAbstract( MyDBAbstract myDBAbs )
	{
		this.pane.setMyDBAbstract(myDBAbs);
	}
	
	public void setSchemaEntityRootER( SchemaEntity erEntity )
	{
		this.pane.setSchemaEntityRootER( erEntity );
	}
}
