package milu.gui.ctrl.schema.handle;

import java.sql.SQLException;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import milu.db.obj.abs.AbsDBFactory;
import milu.entity.schema.SchemaEntity;
import milu.entity.schema.search.SearchSchemaEntityInterface;
import milu.entity.schema.search.SearchSchemaEntityVisitorFactory;
import milu.gui.ctrl.schema.SchemaERViewTab;
import milu.main.MainController;
import milu.task.collect.CollectDataType;
import milu.task.collect.CollectTaskFactory;
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
		//TreeItem<SchemaEntity> itemParent   = this.itemSelected.getParent();
		SchemaEntity  selectedEntity = this.itemSelected.getValue();
		//String schemaName = itemParent.getValue().toString();
		
		// Search [SCHEMA]
		SearchSchemaEntityInterface searchSchemaVisitor = new SearchSchemaEntityVisitorFactory().createInstance(SchemaEntity.SCHEMA_TYPE.SCHEMA);
		selectedEntity.acceptParent(searchSchemaVisitor);
		SchemaEntity hitEntity = searchSchemaVisitor.getHitSchemaEntity();
		String schemaName = null;
		// SQLite => [SCHEMA]=null
		if ( hitEntity != null )
		{
			schemaName = hitEntity.getName();
		}
		
		String id         = (( schemaName == null ) ? "":schemaName) + "@ER@";
		ObservableList<TreeItem<SchemaEntity>> itemChildren = this.itemSelected.getChildren();
		
		/*
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
		*/		
		// Activate Tab, if already exists.
		// Delete Tab, if already exists.
		if ( MANIPULATE_TYPE.SELECT.equals(this.manipulateSpecifiedTab( SchemaERViewTab.class , id )) )
		{
			return;
		}
		
		// get View List & add list as children
		if ( itemChildren.size() > 0 )
		{
			return;
		}
		
		MainController mainCtrl = this.dbView.getMainController();
		final Task<Exception> collectTask = CollectTaskFactory.getInstance( AbsDBFactory.FACTORY_TYPE.FOREIGN_KEY,  CollectDataType.LIST,mainCtrl, this.myDBAbs, selectedEntity );
		if ( collectTask == null )
		{
			return;
		}
		// execute task
		this.service.submit( collectTask );
		
		final String schemaNameFinal = schemaName;
		collectTask.progressProperty().addListener
		(
			(obs,oldVal,newVal)->
			{
				System.out.println( "CollectTask:Progress[" + obs.getClass() + "]oldVal[" + oldVal + "]newVal[" + newVal + "]" );
				if ( newVal.doubleValue() == 0.0 )
				{
					this.schemaTreeView.setIsLoading(true);
				}
				// Task Done.
				else if ( newVal.doubleValue() == 1.0 )
				{
					System.out.println( "CollectTask:Done[" + newVal + "]" );
					this.schemaTreeView.addEntityLst( itemSelected, selectedEntity.getEntityLst(), true );
					this.schemaTreeView.setChildrenCnt();
					this.drawER( selectedEntity, schemaNameFinal, id );
					this.schemaTreeView.setIsLoading(false);
					this.dbView.setBottomMsg(null);
					this.serviceShutdown();
				}
			}
		);
		
		collectTask.messageProperty().addListener
		(
			(obs,oldVal,newVal)->
			{
				System.out.println( "CollectTask:Message[" + newVal + "]" );
				this.dbView.setBottomMsg(newVal);
			}
		);
		
		this.setValueProperty(collectTask);
	}
	
	private void drawER( SchemaEntity selectedEntity, String schemaName, String id )
	{
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
