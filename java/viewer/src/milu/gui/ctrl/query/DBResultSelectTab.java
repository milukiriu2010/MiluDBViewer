package milu.gui.ctrl.query;

import javafx.event.Event;
import javafx.geometry.Orientation;
import javafx.geometry.Side;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.BorderPane;
import milu.gui.ctrl.common.table.CopyTableInterface;
import milu.gui.ctrl.common.table.DirectionSwitchInterface;
import milu.gui.view.DBView;
import milu.main.MainController;
import milu.tool.MyGUITool;

public class DBResultSelectTab extends Tab
	implements
		CopyTableInterface,
		DirectionSwitchInterface
{
	private DBView      dbView = null;
	
	private TabPane     tabPane = new TabPane();
	
	private BorderPane  basePane = new BorderPane();
	
	public DBResultSelectTab( DBView dbView )
	{
		super();
		
		this.dbView = dbView;
		
		this.tabPane.setSide(Side.LEFT);
		this.tabPane.setTabClosingPolicy( TabClosingPolicy.UNAVAILABLE );
		
		this.basePane.setCenter(this.tabPane);
		
		MainController mainCtrl = this.dbView.getMainController();
		
		// set icon on Tab
		this.setGraphic( MyGUITool.createImageView( 16, 16, mainCtrl.getImage("file:resources/images/result.png") ) );

		this.setContent(this.basePane);
	}
	
	public TabPane getChildTabPane()
	{
		return this.tabPane;
	}
	
	
	// CopyTableInterface
	@Override
	public void copyTableNoHead( Event event )
	{
		Tab selectedTab = this.tabPane.getSelectionModel().getSelectedItem();
		if ( selectedTab instanceof CopyTableInterface )
		{
			((CopyTableInterface)selectedTab).copyTableNoHead( event );
		}
	}
	
	// CopyTableInterface
	@Override
	public void copyTableWithHead( Event event )
	{
		Tab selectedTab = this.tabPane.getSelectionModel().getSelectedItem();
		if ( selectedTab instanceof CopyTableInterface )
		{
			((CopyTableInterface)selectedTab).copyTableWithHead( event );
		}
	}
	
	// DirectionSwitchInterface
	@Override
	public Orientation getOrientation()
	{
		Tab selectedTab = this.tabPane.getSelectionModel().getSelectedItem();
		if ( selectedTab instanceof DirectionSwitchInterface )
		{
			return ((DirectionSwitchInterface)selectedTab).getOrientation();
		}
		else
		{
			throw new UnsupportedOperationException();
		}
	}
	
	// DirectionSwitchInterface
	@Override
	public void setOrientation( Orientation orientation )
	{
		throw new UnsupportedOperationException();
	}
	
	// DirectionSwitchInterface
	@Override
	public void switchDirection( Event event )
	{
		Tab selectedTab = this.tabPane.getSelectionModel().getSelectedItem();
		if ( selectedTab instanceof DirectionSwitchInterface )
		{
			((DirectionSwitchInterface)selectedTab).switchDirection( event );
		}
	}
}
