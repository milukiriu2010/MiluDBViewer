package milu.gui.ctrl.schema.handle;

import java.sql.SQLException;
import java.util.List;

import javafx.scene.control.Tab;
import javafx.scene.control.TreeItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import milu.db.abs.AbsDBFactory;
import milu.db.abs.ObjDBFactory;
import milu.db.abs.ObjDBInterface;
import milu.entity.schema.SchemaEntity;
import milu.gui.ctrl.schema.SchemaProcViewTab;

/**
 * This class is invoked, when "root func" item is clicked on SchemaTreeView.
 * Show "Function List"
 * ---------------------------------------
 * [ROOT]
 * - [SCHEMA]
 *   - [ROOT_TABLE]
 *     - [TABLE]
 *       - [ROOT_INDEX]
 *         - [INDEX]
 *   - [ROOT_VIEW]
 *     - [VIEW]
 *   - [ROOT_FUNC] => selected
 *     - [FUNC]    => get
 * ---------------------------------------
 * @author milu
 *
 */
public class SelectedItemHandlerRootFunc extends SelectedItemHandlerAbstract
{
	/*
	public SelectedItemHandlerRootFunc
	( 
		SchemaTreeView schemaTreeView, 
		TabPane        tabPane,
		MyDBAbstract   myDBAbs,
		SelectedItemHandlerAbstract.REFRESH_TYPE  refreshType
	)
	{
		super( schemaTreeView, tabPane, myDBAbs, refreshType );
	}
	*/
	@Override
	protected boolean isMyResponsible()
	{
		if ( this.itemSelected.getValue().getType() == SchemaEntity.SCHEMA_TYPE.ROOT_FUNC )
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
			ObjDBFactory objDBFactory = AbsDBFactory.getFactory( AbsDBFactory.FACTORY_TYPE.FUNC );
			ObjDBInterface objDBInf = objDBFactory.getInstance(myDBAbs);
			if ( objDBInf != null )
			{
				String schemaName = itemParent.getValue().toString();
				List<SchemaEntity> funcEntityLst = objDBInf.selectEntityLst(schemaName);
				this.schemaTreeView.addEntityLst( itemSelected, funcEntityLst );
			}
		}
		
		// Delete DBSchemaTableViewTab, if already exists. 
		if ( this.refreshType == SelectedItemHandlerAbstract.REFRESH_TYPE.WITH_REFRESH )
		{
			final ObservableList<Tab> tabLst =  this.tabPane.getTabs();
			ObservableList<Tab> relatedTabLst = FXCollections.observableArrayList();
			for ( Tab tab : tabLst )
			{
				if (
					( tab instanceof SchemaProcViewTab ) &&
					( tab.getId().contains("@func@") == true )
				)
				{
					relatedTabLst.add( tab );
				}
			}
			this.tabPane.getTabs().removeAll( relatedTabLst );
		}
		
	}

}
