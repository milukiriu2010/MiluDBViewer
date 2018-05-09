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
		String                  strPartUserData = null;
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
			strPartUserData = "@table@";
		}
		else if ( SchemaEntity.SCHEMA_TYPE.TABLE.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerEachTable();
			strPartUserData = "@table@";
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
			strPartUserData = "@view@";
		}
		else if ( SchemaEntity.SCHEMA_TYPE.VIEW.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerEachView();
			strPartUserData = "@view@";
		}
		else if ( SchemaEntity.SCHEMA_TYPE.ROOT_SYSTEM_VIEW.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerRootSystemView();
			strPartUserData = "@system_view@";
		}
		else if ( SchemaEntity.SCHEMA_TYPE.SYSTEM_VIEW.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerEachSystemView();
			strPartUserData = "@system_view@";
		}
		else if ( SchemaEntity.SCHEMA_TYPE.ROOT_MATERIALIZED_VIEW.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerRootMaterializedView();
			strPartUserData = "@materialized_view@";
		}
		else if ( SchemaEntity.SCHEMA_TYPE.MATERIALIZED_VIEW.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerEachMaterializedView();
			strPartUserData = "@materialized_view@";
		}
		else if ( SchemaEntity.SCHEMA_TYPE.ROOT_FUNC.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerRootFunc();
			strPartUserData = "@func@";
		}
		else if ( SchemaEntity.SCHEMA_TYPE.FUNC.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerEachFunc();
			strPartUserData = "@func@";
		}
		else if ( SchemaEntity.SCHEMA_TYPE.ROOT_AGGREGATE.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerRootAggregate();
			strPartUserData = "@aggregate@";
		}
		else if ( SchemaEntity.SCHEMA_TYPE.AGGREGATE.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerEachAggregate();
			strPartUserData = "@aggregate@";
		}
		else if ( SchemaEntity.SCHEMA_TYPE.ROOT_PROC.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerRootProc();
			strPartUserData = "@proc@";
		}
		else if ( SchemaEntity.SCHEMA_TYPE.PROC.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerEachProc();
			strPartUserData = "@proc@";
		}
		else if ( SchemaEntity.SCHEMA_TYPE.ROOT_PACKAGE_DEF.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerRootPackageDef();
			strPartUserData = "@package_def@";
		}
		else if ( SchemaEntity.SCHEMA_TYPE.PACKAGE_DEF.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerEachPackageDef();
			strPartUserData = "@package_def@";
		}
		else if ( SchemaEntity.SCHEMA_TYPE.ROOT_PACKAGE_BODY.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerRootPackageBody();
			strPartUserData = "@package_body@";
		}
		else if ( SchemaEntity.SCHEMA_TYPE.PACKAGE_BODY.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerEachPackageBody();
			strPartUserData = "@package_body@";
		}
		else if ( SchemaEntity.SCHEMA_TYPE.ROOT_TYPE.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerRootType();
			strPartUserData = "@type@";
		}
		else if ( SchemaEntity.SCHEMA_TYPE.TYPE.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerEachType();
			strPartUserData = "@type@";
		}
		else if ( SchemaEntity.SCHEMA_TYPE.ROOT_TRIGGER.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerRootTrigger();
			strPartUserData = "@trigger@";
		}
		else if ( SchemaEntity.SCHEMA_TYPE.TRIGGER.equals(itemSelected.getValue().getType()) )
		{
			handleAbs = new SelectedItemHandlerEachTrigger();
			strPartUserData = "@trigger@";
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
		handleAbs.setStrPartUserData(strPartUserData);
		
		return handleAbs;
	}
}
