package milu.gui.ctrl.schema.handle;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TabPane;

import java.sql.SQLException;

import milu.gui.ctrl.schema.SchemaTreeView;
import milu.db.MyDBAbstract;
import milu.entity.schema.SchemaEntity;
import milu.gui.view.DBView;

abstract public class SelectedItemHandlerAbstract 
{
	protected SelectedItemHandlerAbstract     nextHandler    = null;
	
	protected SchemaTreeView          schemaTreeView = null;
	// Root Item    on SchemaTreeView
	protected TreeItem<SchemaEntity>  itemRoot     = null;
	// SelectedItem on SchemaTreeView
	protected TreeItem<SchemaEntity>  itemSelected = null;
	
	protected TabPane       tabPane = null;
	
	protected DBView                  dbView = null;
	
	protected MyDBAbstract  myDBAbs = null;
	
	public enum REFRESH_TYPE
	{
		NO_REFRESH,
		WITH_REFRESH
	}
	
	protected REFRESH_TYPE  refreshType = REFRESH_TYPE.NO_REFRESH;
	
	public void setSchemaTreeView( SchemaTreeView schemaTreeView )
	{
		this.schemaTreeView = schemaTreeView;
		this.itemRoot = this.schemaTreeView.getRoot();
		if ( this.itemRoot != null )
		{
			this.itemSelected = this.schemaTreeView.getSelectionModel().getSelectedItem();
		}
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
	
	public void addNextHandler( SelectedItemHandlerAbstract nextHandler )
	{
		if ( this.nextHandler != null )
		{
			this.nextHandler.addNextHandler(nextHandler);
		}
		else
		{
			this.nextHandler = nextHandler;
		}
	}
	
	public SelectedItemHandlerAbstract getNextHandler()
	{
		return this.nextHandler;
	}
	
	public boolean execChain()
		throws
			UnsupportedOperationException,
			SQLException
	{
		if ( this.itemRoot != null && this.itemSelected == null )
		{
			return false;
		}
		
		if ( this.isMyResponsible() )
		{
			this.exec();
			return true;
		}
		else if ( this.nextHandler != null )
		{
			return this.nextHandler.execChain();
		}
		else
		{
			return false;
		}
	}
	
	protected abstract boolean isMyResponsible();
	
	protected abstract void exec()
		throws
			UnsupportedOperationException,
			SQLException;
}