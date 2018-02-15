package milu.gui.ctrl.schema;

import java.sql.SQLException;

import javafx.scene.control.TabPane;
import milu.gui.ctrl.schema.SchemaTreeView;
import milu.db.MyDBAbstract;

public class SelectedItemHandlerChooser
{
	public static void exec
	( 
		SchemaTreeView schemaTreeView, 
		TabPane        tabPane,
		MyDBAbstract   myDBAbs,
		SelectedItemHandlerAbstract.REFRESH_TYPE  refreshType
	)
		throws
			UnsupportedOperationException,
			SQLException
	{
		
		SelectedItemHandlerAbstract handleRoot = 
			new SelectedItemHandlerRoot( schemaTreeView, tabPane, myDBAbs, refreshType );
		SelectedItemHandlerAbstract handleRootTable = 
				new SelectedItemHandlerRootTable( schemaTreeView, tabPane, myDBAbs, refreshType );
		SelectedItemHandlerAbstract handleEachTable = 
			new SelectedItemHandlerEachTable( schemaTreeView, tabPane, myDBAbs, refreshType );
		SelectedItemHandlerAbstract handleRootIndex = 
			new SelectedItemHandlerRootIndex( schemaTreeView, tabPane, myDBAbs, refreshType );
		SelectedItemHandlerAbstract handleEachIndex = 
			new SelectedItemHandlerEachIndex( schemaTreeView, tabPane, myDBAbs, refreshType );
		SelectedItemHandlerAbstract handleRootView = 
			new SelectedItemHandlerRootView( schemaTreeView, tabPane, myDBAbs, refreshType );
		SelectedItemHandlerAbstract handleEachView = 
			new SelectedItemHandlerEachView( schemaTreeView, tabPane, myDBAbs, refreshType );
		SelectedItemHandlerAbstract handleRootSystemView = 
			new SelectedItemHandlerRootSystemView( schemaTreeView, tabPane, myDBAbs, refreshType );
		SelectedItemHandlerAbstract handleEachSystemView = 
			new SelectedItemHandlerEachSystemView( schemaTreeView, tabPane, myDBAbs, refreshType );
		SelectedItemHandlerAbstract handleRootMaterializedView = 
			new SelectedItemHandlerRootMaterializedView( schemaTreeView, tabPane, myDBAbs, refreshType );
		SelectedItemHandlerAbstract handleEachMaterializedView = 
			new SelectedItemHandlerEachMaterializedView( schemaTreeView, tabPane, myDBAbs, refreshType );
		SelectedItemHandlerAbstract handleRootFunc = 
			new SelectedItemHandlerRootFunc( schemaTreeView, tabPane, myDBAbs, refreshType );
		SelectedItemHandlerAbstract handleEachFunc = 
			new SelectedItemHandlerEachFunc( schemaTreeView, tabPane, myDBAbs, refreshType );
		SelectedItemHandlerAbstract handleRootAggregate = 
			new SelectedItemHandlerRootAggregate( schemaTreeView, tabPane, myDBAbs, refreshType );
		SelectedItemHandlerAbstract handleEachAggregate = 
			new SelectedItemHandlerEachAggregate( schemaTreeView, tabPane, myDBAbs, refreshType );
		SelectedItemHandlerAbstract handleRootProc = 
			new SelectedItemHandlerRootProc( schemaTreeView, tabPane, myDBAbs, refreshType );
		SelectedItemHandlerAbstract handleEachProc = 
			new SelectedItemHandlerEachProc( schemaTreeView, tabPane, myDBAbs, refreshType );
		SelectedItemHandlerAbstract handleRootPackageDef = 
			new SelectedItemHandlerRootPackageDef( schemaTreeView, tabPane, myDBAbs, refreshType );
		SelectedItemHandlerAbstract handleEachPackageDef = 
			new SelectedItemHandlerEachPackageDef( schemaTreeView, tabPane, myDBAbs, refreshType );
		SelectedItemHandlerAbstract handleRootPackageBody = 
			new SelectedItemHandlerRootPackageBody( schemaTreeView, tabPane, myDBAbs, refreshType );
		SelectedItemHandlerAbstract handleEachPackageBody = 
			new SelectedItemHandlerEachPackageBody( schemaTreeView, tabPane, myDBAbs, refreshType );
		SelectedItemHandlerAbstract handleRootType = 
			new SelectedItemHandlerRootType( schemaTreeView, tabPane, myDBAbs, refreshType );
		SelectedItemHandlerAbstract handleEachType = 
			new SelectedItemHandlerEachType( schemaTreeView, tabPane, myDBAbs, refreshType );
		SelectedItemHandlerAbstract handleRootTrigger = 
			new SelectedItemHandlerRootTrigger( schemaTreeView, tabPane, myDBAbs, refreshType );
		SelectedItemHandlerAbstract handleEachTrigger = 
			new SelectedItemHandlerEachTrigger( schemaTreeView, tabPane, myDBAbs, refreshType );
		SelectedItemHandlerAbstract handleRootSequence = 
			new SelectedItemHandlerRootSequence( schemaTreeView, tabPane, myDBAbs, refreshType );
		
		handleRoot.addNextHandler( handleEachTable );
		handleRoot.addNextHandler( handleRootTable );
		handleRoot.addNextHandler( handleRootIndex );
		handleRoot.addNextHandler( handleEachIndex );
		handleRoot.addNextHandler( handleRootView );
		handleRoot.addNextHandler( handleEachView );
		handleRoot.addNextHandler( handleRootSystemView );
		handleRoot.addNextHandler( handleEachSystemView );
		handleRoot.addNextHandler( handleRootMaterializedView );
		handleRoot.addNextHandler( handleEachMaterializedView );
		handleRoot.addNextHandler( handleRootFunc );
		handleRoot.addNextHandler( handleEachFunc );
		handleRoot.addNextHandler( handleRootAggregate );
		handleRoot.addNextHandler( handleEachAggregate );
		handleRoot.addNextHandler( handleRootProc );
		handleRoot.addNextHandler( handleEachProc );
		handleRoot.addNextHandler( handleRootPackageDef );
		handleRoot.addNextHandler( handleEachPackageDef );
		handleRoot.addNextHandler( handleRootPackageBody );
		handleRoot.addNextHandler( handleEachPackageBody );
		handleRoot.addNextHandler( handleRootType );
		handleRoot.addNextHandler( handleEachType );
		handleRoot.addNextHandler( handleRootTrigger );
		handleRoot.addNextHandler( handleEachTrigger );
		handleRoot.addNextHandler( handleRootSequence );
		
		handleRoot.execChain();
	}
}
