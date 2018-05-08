package milu.gui.ctrl.schema.handle;

import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import milu.db.MyDBAbstract;
import milu.entity.schema.SchemaEntity;
import milu.gui.ctrl.schema.SchemaTreeView;
import milu.gui.view.DBView;

public class SelectedItemHandlerFactory 
{
	public static SelectedItemHandlerAbstract getInstance
	(
		SchemaTreeView schemaTreeView, 
		TabPane        tabPane,
		DBView         dbView,
		MyDBAbstract   myDBAbs,
		SelectedItemHandlerAbstract.REFRESH_TYPE  refreshType
	)
	{
		TreeItem<SchemaEntity>  itemRoot     = schemaTreeView.getRoot();
		TreeItem<SchemaEntity>  itemSelected = null;
		if ( itemRoot != null )
		{
			itemSelected = schemaTreeView.getSelectionModel().getSelectedItem();
		}
		
		SelectedItemHandlerAbstract handleAbs = null;
		if ( itemRoot == null )
		{
			handleAbs = new SelectedItemHandlerRoot();
		}
		else if ( itemSelected == null )
		{
			return null;
		}
		else if ( SchemaEntity.SCHEMA_TYPE.ROOT.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerRoot();
		}
		else if ( SchemaEntity.SCHEMA_TYPE.ROOT_TABLE.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerRootTable();
		}
		else if ( SchemaEntity.SCHEMA_TYPE.TABLE.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerEachTable();
		}
		else if ( SchemaEntity.SCHEMA_TYPE.ROOT_INDEX.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerRootIndex();
		}
		else if ( SchemaEntity.SCHEMA_TYPE.INDEX.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerEachIndex();
		}
		else if ( SchemaEntity.SCHEMA_TYPE.ROOT_VIEW.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerRootView();
		}
		else if ( SchemaEntity.SCHEMA_TYPE.VIEW.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerEachView();
		}
		else if ( SchemaEntity.SCHEMA_TYPE.ROOT_SYSTEM_VIEW.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerRootSystemView();
		}
		else if ( SchemaEntity.SCHEMA_TYPE.SYSTEM_VIEW.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerEachSystemView();
		}
		else if ( SchemaEntity.SCHEMA_TYPE.ROOT_MATERIALIZED_VIEW.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerRootMaterializedView();
		}
		else if ( SchemaEntity.SCHEMA_TYPE.MATERIALIZED_VIEW.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerEachMaterializedView();
		}
		else if ( SchemaEntity.SCHEMA_TYPE.ROOT_FUNC.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerRootFunc();
		}
		else if ( SchemaEntity.SCHEMA_TYPE.FUNC.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerEachFunc();
		}
		else if ( SchemaEntity.SCHEMA_TYPE.ROOT_AGGREGATE.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerRootAggregate();
		}
		else if ( SchemaEntity.SCHEMA_TYPE.AGGREGATE.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerEachAggregate();
		}
		else if ( SchemaEntity.SCHEMA_TYPE.ROOT_PROC.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerRootProc();
		}
		else if ( SchemaEntity.SCHEMA_TYPE.PROC.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerEachProc();
		}
		else if ( SchemaEntity.SCHEMA_TYPE.ROOT_PACKAGE_DEF.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerRootPackageDef();
		}
		else if ( SchemaEntity.SCHEMA_TYPE.PACKAGE_DEF.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerEachPackageDef();
		}
		else if ( SchemaEntity.SCHEMA_TYPE.ROOT_PACKAGE_BODY.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerRootPackageBody();
		}
		else if ( SchemaEntity.SCHEMA_TYPE.PACKAGE_BODY.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerEachPackageBody();
		}
		else if ( SchemaEntity.SCHEMA_TYPE.ROOT_TYPE.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerRootType();
		}
		else if ( SchemaEntity.SCHEMA_TYPE.TYPE.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerEachType();
		}
		else if ( SchemaEntity.SCHEMA_TYPE.ROOT_TRIGGER.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerRootTrigger();
		}
		else if ( SchemaEntity.SCHEMA_TYPE.TRIGGER.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerEachTrigger();
		}
		else if ( SchemaEntity.SCHEMA_TYPE.ROOT_SEQUENCE.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerRootSequence();
		}
		else if ( SchemaEntity.SCHEMA_TYPE.ROOT_ER.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerRootER();
		}
		else
		{
			return null;
		}
		
		
		
		
		handleAbs.setSchemaTreeView(schemaTreeView);
		handleAbs.setTabPane(tabPane);
		handleAbs.setDBView(dbView);
		handleAbs.setMyDBAbs(myDBAbs);
		handleAbs.setRefreshType(refreshType);
		
		return handleAbs;
	}
}
