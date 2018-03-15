package milu.gui.ctrl.schema.handle;

import java.sql.SQLException;

import javafx.scene.control.TabPane;
import milu.gui.ctrl.schema.SchemaTreeView;
import milu.db.MyDBAbstract;
import milu.gui.view.DBView;

public class SelectedItemHandlerChooser
{
	public static void exec
	( 
		SchemaTreeView schemaTreeView, 
		TabPane        tabPane,
		DBView         dbView,
		MyDBAbstract   myDBAbs,
		SelectedItemHandlerAbstract.REFRESH_TYPE  refreshType
	)
		throws
			SQLException
	{
		
		SelectedItemHandlerAbstract handleRoot                 = new SelectedItemHandlerRoot();
		SelectedItemHandlerAbstract handleRootTable            = new SelectedItemHandlerRootTable();
		SelectedItemHandlerAbstract handleEachTable            = new SelectedItemHandlerEachTable();
		SelectedItemHandlerAbstract handleRootIndex            = new SelectedItemHandlerRootIndex();
		SelectedItemHandlerAbstract handleEachIndex            = new SelectedItemHandlerEachIndex();
		SelectedItemHandlerAbstract handleRootView             = new SelectedItemHandlerRootView();
		SelectedItemHandlerAbstract handleEachView             = new SelectedItemHandlerEachView();
		SelectedItemHandlerAbstract handleRootSystemView       = new SelectedItemHandlerRootSystemView();
		SelectedItemHandlerAbstract handleEachSystemView       = new SelectedItemHandlerEachSystemView();
		SelectedItemHandlerAbstract handleRootMaterializedView = new SelectedItemHandlerRootMaterializedView();
		SelectedItemHandlerAbstract handleEachMaterializedView = new SelectedItemHandlerEachMaterializedView();
		SelectedItemHandlerAbstract handleRootFunc             = new SelectedItemHandlerRootFunc();
		SelectedItemHandlerAbstract handleEachFunc             = new SelectedItemHandlerEachFunc();
		SelectedItemHandlerAbstract handleRootAggregate        = new SelectedItemHandlerRootAggregate();
		SelectedItemHandlerAbstract handleEachAggregate        = new SelectedItemHandlerEachAggregate();
		SelectedItemHandlerAbstract handleRootProc             = new SelectedItemHandlerRootProc();
		SelectedItemHandlerAbstract handleEachProc             = new SelectedItemHandlerEachProc();
		SelectedItemHandlerAbstract handleRootPackageDef       = new SelectedItemHandlerRootPackageDef();
		SelectedItemHandlerAbstract handleEachPackageDef       = new SelectedItemHandlerEachPackageDef();
		SelectedItemHandlerAbstract handleRootPackageBody      = new SelectedItemHandlerRootPackageBody();
		SelectedItemHandlerAbstract handleEachPackageBody      = new SelectedItemHandlerEachPackageBody();
		SelectedItemHandlerAbstract handleRootType             = new SelectedItemHandlerRootType();
		SelectedItemHandlerAbstract handleEachType             = new SelectedItemHandlerEachType();
		SelectedItemHandlerAbstract handleRootTrigger          = new SelectedItemHandlerRootTrigger();
		SelectedItemHandlerAbstract handleEachTrigger          = new SelectedItemHandlerEachTrigger();
		SelectedItemHandlerAbstract handleRootSequence         = new SelectedItemHandlerRootSequence();
		SelectedItemHandlerAbstract handleRootER               = new SelectedItemHandlerRootER();
		
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
		handleRoot.addNextHandler( handleRootER );
		
		SelectedItemHandlerAbstract handleNext = handleRoot;
		
		while ( handleNext != null )
		{
			handleNext.setSchemaTreeView(schemaTreeView);
			handleNext.setTabPane(tabPane);
			handleNext.setDBView(dbView);
			handleNext.setMyDBAbs(myDBAbs);
			handleNext.setRefreshType(refreshType);
			
			handleNext = handleNext.getNextHandler();
		}
		
		handleRoot.execChain();
	}
}
