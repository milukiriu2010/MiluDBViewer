package milu.gui.ctrl.schema;

import java.util.List;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityEachFK;

import milu.gui.ctrl.common.LabelTable;

public class SchemaERView extends ScrollPane 
{
	private AnchorPane root = new AnchorPane();
	
	private SchemaEntity erEntity = null;
	
	public SchemaERView()
	{
		super();
		
		this.setPrefSize( 640, 480 );
		this.setContent( root );
		
	}
	
	public void setSchemaEntityRootER( SchemaEntity erEntity )
	{
		this.erEntity = erEntity;
		
		List<SchemaEntity> fkEntityLst = this.erEntity.getEntityLst();
		for ( SchemaEntity seEntity : fkEntityLst )
		{
			SchemaEntityEachFK fkEntity = (SchemaEntityEachFK)seEntity;
			
			String srcTableSchema = fkEntity.getSrcTableSchema();
			String srcTableName   = fkEntity.getSrcTableName();
			
		}
	}
}
