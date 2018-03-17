package milu.gui.ctrl.schema.handle;

import java.sql.SQLException;
import java.util.List;

import javafx.scene.control.Tab;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import javafx.collections.ObservableList;

import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityEachFK;
import milu.ctrl.MainController;
import milu.db.abs.AbsDBFactory;
import milu.db.abs.ObjDBFactory;
import milu.db.abs.ObjDBInterface;
import milu.db.fk.FKDBAbstract;
import milu.gui.ctrl.schema.SchemaERViewTab;

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
	protected boolean isMyResponsible()
	{
		if ( this.itemSelected.getValue().getType() == SchemaEntity.SCHEMA_TYPE.ROOT_ER )
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
		SchemaEntity  selectedEntity = this.itemSelected.getValue();
		String schemaName = itemParent.getValue().toString();
		String id         = schemaName + "@ER@";
		ObservableList<TreeItem<SchemaEntity>> itemChildren = this.itemSelected.getChildren();
		
		final ObservableList<Tab> tabLst =  this.tabPane.getTabs();
		for ( Tab tab : tabLst )
		{
			if (
				( tab instanceof SchemaERViewTab ) &&
				id.equals( tab.getId() )
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
			//FKDBFactory  fkDBFactory = (FKDBFactory)objDBFactory;
			ObjDBInterface objDBInf = objDBFactory.getInstance(myDBAbs);
			if ( objDBInf != null )
			{
				List<SchemaEntity> fkEntityLst = objDBInf.selectEntityLst(schemaName);
				this.schemaTreeView.addEntityLst( itemSelected, fkEntityLst );
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
		newTab.setId( id );
		newTab.setMyDBAbstract(myDBAbs);
		newTab.setSchemaEntityRootER(selectedEntity);
		
		// set icon on Tab
		MainController mainController = this.dbView.getMainController();
		ImageView iv = new ImageView( mainController.getImage("file:resources/images/ER_root.png") );
		iv.setFitHeight( 16 );
		iv.setFitWidth( 16 );
		newTab.setGraphic( iv );

		// add on tabpane
		this.tabPane.getTabs().add( newTab );
		this.tabPane.getSelectionModel().select( newTab );
		
	}

}
