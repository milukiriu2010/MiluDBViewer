package milu.gui.ctrl.schema.handle;

import java.sql.SQLException;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TreeItem;
import javafx.collections.ObservableList;

import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityEachFK;
import milu.db.obj.abs.AbsDBFactory;
import milu.db.obj.abs.ObjDBFactory;
import milu.db.obj.abs.ObjDBInterface;
import milu.db.obj.fk.FKDBAbstract;
import milu.gui.ctrl.schema.SchemaERViewTab;
import milu.main.MainController;
import milu.tool.MyTool;

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
 *   - [ROOT_SEQUENCE] 
 *     - [SEQUENCE]    
 *   - [ROOT_ER]       => selected
 *     - [FOREIGN_KEY] => get
 * ---------------------------------------
 * @author milu
 *
 */
public class SelectedItemHandlerRootER extends SelectedItemHandlerAbstract
{
	@Override
	public void exec() 
		throws 
			UnsupportedOperationException, 
			SQLException
	{
		TreeItem<SchemaEntity> itemParent   = this.itemSelected.getParent();
		SchemaEntity  selectedEntity = this.itemSelected.getValue();
		String schemaName = itemParent.getValue().toString();
		String id         = schemaName + "@ER@";
		ObservableList<TreeItem<SchemaEntity>> itemChildren = this.itemSelected.getChildren();
		
		final ObservableList<Tab> tabLst =  this.tabPane.getTabs();
		for ( Tab tab : tabLst )
		{
			if (
				( tab instanceof SchemaERViewTab ) &&
				//id.equals( tab.getId() )
				id.equals( tab.getUserData() )
			)
			{
				// NO Refresh
				// Activate SchemaTableViewTab, if already exists.
				if ( this.refreshType ==  SelectedItemHandlerAbstract.REFRESH_TYPE.NO_REFRESH )
				{
					this.tabPane.getSelectionModel().select( tab );
					return;
				}
				// Refresh
				// Delete SchemaTableViewTab, if already exists. 
				else
				{
					this.tabPane.getTabs().remove( tab );
					break;
				}
			}
		}		
		
		// get View List & add list as children
		if ( itemChildren.size() == 0 )
		{
			ObjDBFactory objDBFactory = AbsDBFactory.getFactory( AbsDBFactory.FACTORY_TYPE.FOREIGN_KEY );
			ObjDBInterface objDBInf = objDBFactory.getInstance(myDBAbs);
			if ( objDBInf != null )
			{
				List<SchemaEntity> fkEntityLst = objDBInf.selectEntityLst(schemaName);
				this.schemaTreeView.addEntityLst( itemSelected, fkEntityLst, true );
				FKDBAbstract fkDBAbs = (FKDBAbstract)objDBInf;
				for ( SchemaEntity seEntity : fkEntityLst )
				{
					selectedEntity.addEntity( seEntity );
					SchemaEntityEachFK fkEntity = (SchemaEntityEachFK) seEntity;
					fkDBAbs.selectSrcColumnMap(fkEntity);
					fkDBAbs.selectDstColumnMap(fkEntity);
				}
			}
		}
		
		// no fk
		if ( selectedEntity.getEntityLst().size() == 0 )
		{
			return;
		}
		
		// Create SchemaERViewTab, if it doesn't exist.
		SchemaERViewTab newTab = new SchemaERViewTab( schemaName );
		//newTab.setId( id );
		newTab.setUserData( id );
		newTab.setMyDBAbstract(myDBAbs);
		newTab.setSchemaEntityRootER(selectedEntity);
		
		// set icon on Tab
		MainController mainCtrl = this.dbView.getMainController();
		Node imageGroup = MyTool.createImageView( 16, 16, mainCtrl, selectedEntity );
		newTab.setGraphic( imageGroup );		

		// add on tabpane
		this.tabPane.getTabs().add( newTab );
		this.tabPane.getSelectionModel().select( newTab );
		
		// call after added to TabPane
		// set Label(lblTable) width to the longest Label(lblColumn) width
		newTab.calculate();
		
	}

}
