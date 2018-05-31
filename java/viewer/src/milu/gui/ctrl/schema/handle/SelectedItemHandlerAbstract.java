package milu.gui.ctrl.schema.handle;

import javafx.scene.control.TreeItem;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.lang.reflect.Constructor;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import milu.gui.ctrl.common.inf.SetTableViewDataInterface;
import milu.gui.ctrl.schema.SchemaTreeView;
import milu.gui.ctrl.schema.SetSrcTextInterface;
import milu.gui.dlg.MyAlertDialog;
import milu.db.MyDBAbstract;
import milu.db.obj.abs.AbsDBFactory;
import milu.entity.schema.SchemaEntity;
import milu.gui.view.DBView;
import milu.main.MainController;
import milu.task.collect.CollectDataType;
import milu.task.collect.CollectTaskFactory;
import milu.tool.MyTool;

abstract public class SelectedItemHandlerAbstract 
{
	protected SchemaTreeView          schemaTreeView = null;
	// Root Item    on SchemaTreeView
	protected TreeItem<SchemaEntity>  itemRoot     = null;
	// SelectedItem on SchemaTreeView
	protected TreeItem<SchemaEntity>  itemSelected = null;
	// Part of UserData on Tab
	//   "@table@"
	//   "@func@"
	protected String        strPartUserData = null;
	
	protected TabPane       tabPane = null;
	
	protected DBView        dbView = null;
	
	protected MyDBAbstract  myDBAbs = null;
	
	public enum REFRESH_TYPE
	{
		NO_REFRESH,
		WITH_REFRESH
	}
	
	enum MANIPULATE_TYPE
	{
		NONE,
		SELECT,
		REMOVE
	}
	
	enum DEFINITION_TYPE
	{
		WITH_DEFAULT,
		NO_DEFAULT
	}
	
	protected REFRESH_TYPE  refreshType = REFRESH_TYPE.NO_REFRESH;
	
	// Thread Pool
	protected ExecutorService service = Executors.newSingleThreadExecutor();
	
	public void setSchemaTreeView( SchemaTreeView schemaTreeView )
	{
		this.schemaTreeView = schemaTreeView;
		this.itemRoot = this.schemaTreeView.getRoot();
		if ( this.itemRoot != null )
		{
			this.itemSelected = this.schemaTreeView.getSelectionModel().getSelectedItem();
		}
	}
	
	public void setStrPartUserData( String strPartUserData )
	{
		this.strPartUserData = strPartUserData;
	}
	
	public void setTabPane( TabPane tabPane )
	{
		this.tabPane = tabPane;
	}
	
	public void setDBView( DBView dbView )
	{
		this.dbView = dbView;
	}
	
	public void setMyDBAbs( MyDBAbstract myDBAbs )
	{
		this.myDBAbs     = myDBAbs;
	}
	
	public void setRefreshType( SelectedItemHandlerAbstract.REFRESH_TYPE refreshType )
	{
		this.refreshType = refreshType;
	}
	
    protected void serviceShutdown()
    {
		try
		{
			System.out.println( "shutdown executor start(" + this.getClass().toString() + ")." );
			this.service.shutdown();
			this.service.awaitTermination( 3, TimeUnit.SECONDS );
		}
		catch ( InterruptedException intEx )
		{
			System.out.println( "tasks interrupted(" + this.getClass().toString() + ")." );
		}
		finally
		{
			if ( !this.service.isTerminated() )
			{
				System.out.println( "executor still working...(" + this.getClass().toString() + ")." );
			}
			this.service.shutdownNow();
			System.out.println( "executor finished(" + this.getClass().toString() + ")." );
		}
    }
	
	public abstract void exec()
		throws
			UnsupportedOperationException,
			SQLException;
	
	private void removeRelatedTab( Class<?> castClazz )
	{
		if ( this.refreshType == SelectedItemHandlerAbstract.REFRESH_TYPE.WITH_REFRESH )
		{
			final ObservableList<Tab> tabLst =  this.tabPane.getTabs();
			ObservableList<Tab> relatedTabLst = FXCollections.observableArrayList();
			for ( Tab tab : tabLst )
			{
				if (
					( castClazz.isInstance(tab) ) &&
					//( tab instanceof SchemaProcViewTab ) &&
					( ((String)tab.getUserData()).contains(this.strPartUserData) == true )
				)
				{
					relatedTabLst.add( tab );
				}
			}
			this.tabPane.getTabs().removeAll( relatedTabLst );
		}
	}
	
	protected MANIPULATE_TYPE manipulateSpecifiedTab( Class<?> castClazz, String id )
	{
		final ObservableList<Tab> tabLst =  this.tabPane.getTabs();
		for ( Tab tab : tabLst )
		{
			if (
				( castClazz.isInstance(tab) ) &&
				//( tab instanceof SchemaProcViewTab ) &&
				//id.equals(tab.getId())
				id.equals(tab.getUserData())
			)
			{
				// Activate DBSchemaTableViewTab, if already exists.
				if ( this.refreshType ==  SelectedItemHandlerAbstract.REFRESH_TYPE.NO_REFRESH )
				{
					this.tabPane.getSelectionModel().select( tab );
					return MANIPULATE_TYPE.SELECT;
				}
				// Delete DBSchemaTableViewTab, if already exists. 
				else
				{
					this.tabPane.getTabs().remove( tab );
					return MANIPULATE_TYPE.REMOVE;
				}
			}
		}
		
		return MANIPULATE_TYPE.NONE;
	}
	
	// Call by SelectedItemHandlerRootXX
	protected void loadChildLst( AbsDBFactory.FACTORY_TYPE factoryType, Class<?> castClazz, CollectDataType dataType )
	{
		SchemaEntity selectedEntity = this.itemSelected.getValue();
		ObservableList<TreeItem<SchemaEntity>> itemChildren = this.itemSelected.getChildren();
		if ( itemChildren.size() > 0 )
		{
			return;
		}
		
		MainController mainCtrl = this.dbView.getMainController();
		final Task<Exception> collectTask = CollectTaskFactory.getInstance( factoryType,  dataType , mainCtrl, this.myDBAbs, selectedEntity );
		if ( collectTask == null )
		{
			return;
		}
		
		// execute task
		this.service.submit( collectTask );
		
		collectTask.progressProperty().addListener
		(
			(obs,oldVal,newVal)->
			{
				System.out.println( "CollectTask:Progress[" + obs.getClass() + "]oldVal[" + oldVal + "]newVal[" + newVal + "]" );
				if ( newVal.doubleValue() == 0.0 )
				{
					this.schemaTreeView.setIsLoading(true);
					this.schemaTreeView.scrollToSelectedItem(this.itemSelected);
				}
				// Task Done.
				else if ( newVal.doubleValue() == 1.0 )
				{
					System.out.println( "CollectTask:Done[" + newVal + "]" );
					this.schemaTreeView.addEntityLst( itemSelected, selectedEntity.getEntityLst(), true );
					this.schemaTreeView.setChildrenCnt();
					this.schemaTreeView.setIsLoading(false);
					this.schemaTreeView.scrollToSelectedItem(this.itemSelected);
					this.dbView.setBottomMsg(null);
					// Delete Related Tab, if already exists. 
					if ( castClazz != null )
					{
						this.removeRelatedTab( castClazz );
					}
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
	
	// Call by SelectedItemHandlerEachXX
	protected void loadSource( AbsDBFactory.FACTORY_TYPE factoryType, Class<?> castClazz )
	{
		SchemaEntity selectedEntity = this.itemSelected.getValue();
		TreeItem<SchemaEntity> itemParent = itemSelected.getParent();
		String schemaName = itemParent.getParent().getValue().toString();
		String objName   = itemSelected.getValue().getName();
		String id         = schemaName + this.strPartUserData + objName;
		
		// Activate Tab, if already exists.
		// Delete Tab, if already exists.
		if ( MANIPULATE_TYPE.SELECT.equals(this.manipulateSpecifiedTab( castClazz , id )) )
		{
			return;
		}
		
		Object obj = null;
		Constructor<?>[] constructors = castClazz.getDeclaredConstructors();
		for ( int i = 0; i < constructors.length; i++ )
		{
			try
			{
				// exit loop 
				// when match "new XXViewTab( DBView dbView )"
				obj = castClazz.cast(constructors[i].newInstance(this.dbView));
				break;
			}
			catch ( Exception ex )
			{
			}
		}
		if ( obj == null )
		{
			return;
		}
		
		Tab newTab = null;
		if ( obj instanceof Tab )
		{
			newTab = (Tab)obj;
		}
		
		newTab.setUserData( id );
		newTab.setText( objName );
		this.tabPane.getTabs().add( newTab );
		this.tabPane.getSelectionModel().select( newTab );
		
		// set icon on Tab
		MainController mainCtrl = this.dbView.getMainController();
		Node imageGroup = MyTool.createImageView( 16, 16, mainCtrl, selectedEntity );
		newTab.setGraphic( imageGroup );
		
		// get function ddl
		String strSrc = selectedEntity.getSrcSQL();
		
		if ( ( strSrc != null ) && 
				( this.refreshType ==  SelectedItemHandlerAbstract.REFRESH_TYPE.NO_REFRESH )
		)
		{
			// set source in SqlTextArea
			((SetSrcTextInterface)newTab).setSrcText( strSrc );
			return;
		}

		final Task<Exception> collectTask = CollectTaskFactory.getInstance( factoryType,  CollectDataType.SOURCE, mainCtrl, this.myDBAbs, selectedEntity );
		if ( collectTask == null )
		{
			return;
		}
		
		// execute task
		this.service.submit( collectTask );
		
		final SetSrcTextInterface sstInf = (SetSrcTextInterface)newTab;
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
					this.schemaTreeView.setIsLoading(false);
					this.dbView.setBottomMsg(null);
					// set source in SqlTextArea
					sstInf.setSrcText( selectedEntity.getSrcSQL() );
					this.serviceShutdown();
				}
			}
		);
		
		this.setValueProperty(collectTask);
	}
	
	// Call by SelectedItemHandlerEachXX
	protected void loadDefinition( AbsDBFactory.FACTORY_TYPE factoryType, Class<?> castClazz, DEFINITION_TYPE defType )
	{
		SchemaEntity selectedEntity = this.itemSelected.getValue();
		TreeItem<SchemaEntity> itemParent   = this.itemSelected.getParent();
		String schemaName = itemParent.getParent().getValue().toString();
		String objName    = itemSelected.getValue().getName();
		String id         = schemaName + this.strPartUserData + objName;
		
		// Activate Tab, if already exists.
		// Delete Tab, if already exists.
		if ( MANIPULATE_TYPE.SELECT.equals(this.manipulateSpecifiedTab( castClazz , id )) )
		{
			return;
		}		
		Object obj = null;
		Constructor<?>[] constructors = castClazz.getDeclaredConstructors();
		for ( int i = 0; i < constructors.length; i++ )
		{
			try
			{
				// exit loop 
				// when match "new XXViewTab( DBView dbView )"
				obj = castClazz.cast(constructors[i].newInstance(this.dbView));
				break;
			}
			catch ( Exception ex )
			{
			}
		}
		if ( obj == null )
		{
			return;
		}
		
		Tab newTab = null;
		if ( obj instanceof Tab )
		{
			newTab = (Tab)obj;
		}
		
		newTab.setUserData( id );
		newTab.setText( objName );
		this.tabPane.getTabs().add( newTab );
		this.tabPane.getSelectionModel().select( newTab );
		
		// set icon on Tab
		MainController mainCtrl = this.dbView.getMainController();
		Node imageGroup = MyTool.createImageView( 16, 16, mainCtrl, selectedEntity );
		newTab.setGraphic( imageGroup );
		
		// get view definition
		List<Map<String,String>>  dataLst = selectedEntity.getDefinitionLst();
		if ( ( dataLst.size() != 0 ) &&
				( this.refreshType ==  SelectedItemHandlerAbstract.REFRESH_TYPE.NO_REFRESH )
		)
		{
			((SetTableViewDataInterface)newTab).setTableViewData
			(
				this.createHeadLst(defType), 
				this.createDataLst(defType, dataLst)
			);
			return;
		}
		
		final Task<Exception> collectTask = CollectTaskFactory.getInstance( factoryType,  CollectDataType.DEFINITION, mainCtrl, this.myDBAbs, selectedEntity );
		if ( collectTask == null )
		{
			return;
		}
		
		// execute task
		this.service.submit( collectTask );
		
		final SetTableViewDataInterface stvInf = (SetTableViewDataInterface)newTab;
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
					this.schemaTreeView.setIsLoading(false);
					this.dbView.setBottomMsg(null);
					// set source in SqlTextArea
					stvInf.setTableViewData
					(
						this.createHeadLst(defType), 
						this.createDataLst(defType, selectedEntity.getDefinitionLst() )
					);

					this.serviceShutdown();
				}
			}
		);
		
		this.setValueProperty(collectTask);
	}
	
	protected List<Object> createHeadLst( DEFINITION_TYPE defType )
	{
		List<Object> headLst = new ArrayList<>();
		headLst.add( "COLUMN" );
		headLst.add( "TYPE" );
		headLst.add( "NULL?" );
		if ( DEFINITION_TYPE.WITH_DEFAULT.equals(defType) )
		{
			headLst.add( "DEFAULT" );
		}
		return headLst;
	}
	
	protected List<List<Object>> createDataLst( DEFINITION_TYPE defType, List<Map<String,String>>  dataLst )
	{
		List<List<Object>>  data2Lst = new ArrayList<>();
		
		for ( Map<String,String> dataRow1 : dataLst )
		{
			List<Object> dataRow2 = new ArrayList<>();
			dataRow2.add( dataRow1.get("column_name") );
			String dataType = dataRow1.get("data_type");
			String dataSize = dataRow1.get("data_size");
			String dataType2 = "";
			if ( dataSize == null )
			{
				dataType2 = dataType;
			}
			else
			{
				// Oracle => TIMESTAMP(*)
				if ( dataType.contains("(") )
				{
					dataType2 = dataType;
				}
				else
				{
					dataType2 = dataType + "(" + dataSize + ")";
				}
			}
			dataRow2.add( dataType2 );
			dataRow2.add( dataRow1.get("nullable") );
			if ( DEFINITION_TYPE.WITH_DEFAULT.equals(defType) )
			{
				dataRow2.add( dataRow1.get("data_default") );
			}
			
			data2Lst.add( dataRow2 );
		}
		
		return data2Lst;
	}
	
	protected void setValueProperty( Task<Exception> collectTask )
	{
		collectTask.valueProperty().addListener
		(
			(obs,oldVal,ex)->
			{
				if ( ex == null )
				{
					return;
				}
				else if ( ex instanceof SQLException )
				{
					SQLException sqlEx = (SQLException)ex;
					ResourceBundle langRB = this.dbView.getMainController().getLangResource("conf.lang.gui.common.MyAlert");
					MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING, this.dbView.getMainController() );
					alertDlg.setHeaderText( langRB.getString("TITLE_EXEC_QUERY_ERROR") );
		    		alertDlg.setTxtExp( sqlEx, myDBAbs );
		    		alertDlg.showAndWait();
		    		alertDlg = null;
				}
				else
				{
					ResourceBundle langRB = this.dbView.getMainController().getLangResource("conf.lang.gui.common.MyAlert");
					MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING, this.dbView.getMainController() );
					alertDlg.setHeaderText( langRB.getString("TITLE_MISC_ERROR") );
		    		alertDlg.setTxtExp( ex );
		    		alertDlg.showAndWait();
		    		alertDlg = null;
				}
			}
		);
	}
}
