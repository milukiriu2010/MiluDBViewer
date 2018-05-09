package milu.gui.ctrl.schema.handle;

import javafx.scene.control.TreeItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import milu.gui.ctrl.schema.SchemaTreeView;
import milu.db.MyDBAbstract;
import milu.entity.schema.SchemaEntity;
import milu.gui.view.DBView;

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
	
	public enum MANIPULATE_TYPE
	{
		NONE,
		SELECT,
		REMOVE
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
	
	protected void removeRelatedTab( Class<?> castClazz )
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
}
