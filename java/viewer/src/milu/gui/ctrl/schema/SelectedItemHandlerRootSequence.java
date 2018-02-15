package milu.gui.ctrl.schema;

import java.sql.SQLException;
import java.util.List;

import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.collections.ObservableList;

import milu.db.MyDBAbstract;
import milu.entity.schema.SchemaEntity;

/**
 * This class is invoked, when "root sequence" item is clicked on SchemaTreeView.
 * Show "Sequence List"
 * ---------------------------------------
 * [ROOT]
 * - [SCHEMA]
 *   - [ROOT_TABLE]
 *     - [TABLE]
 *       - [ROOT_INDEX]
 *         - [INDEX]
 *   - [ROOT_VIEW]
 *     - [VIEW]
 *   - [ROOT_FUNC]
 *     - [FUNC]
 *   - [ROOT_TYPE]
 *     - [TYPE]
 *   - [ROOT_TRIGGER]
 *     - [TRIGGER]
 *   - [ROOT_SEQUENCE] => selected
 *     - [SEQUENCE]    => get
 * ---------------------------------------
 * @author milu
 *
 */
public class SelectedItemHandlerRootSequence extends SelectedItemHandlerAbstract
{
	public SelectedItemHandlerRootSequence
	( 
		SchemaTreeView schemaTreeView, 
		TabPane        tabPane,
		MyDBAbstract   myDBAbs,
		SelectedItemHandlerAbstract.REFRESH_TYPE  refreshType
	)
	{
		super( schemaTreeView, tabPane, myDBAbs, refreshType );
	}
	
	@Override
	protected boolean isMyResponsible()
	{
		if ( this.itemSelected.getValue().getType() == SchemaEntity.SCHEMA_TYPE.ROOT_SEQUENCE )
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	protected void exec() 
		throws 
			UnsupportedOperationException, 
			SQLException
	{
		TreeItem<SchemaEntity> itemParent   = this.itemSelected.getParent();
		ObservableList<TreeItem<SchemaEntity>> itemChildren = this.itemSelected.getChildren();
		
		// get View List & add list as children
		if ( itemChildren.size() == 0 )
		{
			String schema = itemParent.getValue().toString();
			List<List<String>> dataLst = myDBAbs.getSchemaSequence( schema );
			this.schemaTreeView.setSequenceData( itemSelected, dataLst );
		}
	}

}
