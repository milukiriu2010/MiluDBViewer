package milu.gui.ctrl.info;

import java.util.ResourceBundle;

import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import milu.gui.ctrl.common.inf.CloseInterface;
import milu.gui.ctrl.common.inf.ChangeLangInterface;
import milu.gui.view.DBView;
import milu.main.MainController;
import milu.tool.MyGUITool;

public class VersionTab extends Tab 
	implements 
		ChangeLangInterface 
{
	private DBView          dbView = null;
	
	// ----------------------------------------
	// [Pane(1) on Tab]
	// ----------------------------------------
	// Top Pane
	private BorderPane basePane = new BorderPane();
	
    // -----------------------------------------------------
	// [Pane(1) on Tab]-[Top]
    // -----------------------------------------------------
	private Pane       updatePane = null;
	
    // -----------------------------------------------------
	// [Pane(1) on Tab]-[Center]
    // -----------------------------------------------------
	private TabPane    tabPane  = new TabPane();
	
	public VersionTab( DBView dbView )
	{
		super();
		
		System.out.println( "VersionTab:construct start" );
		
		this.dbView = dbView;
		MainController mainCtrl = this.dbView.getMainController();

	    // -----------------------------------------------------
		// [Pane(1) on Tab]-[Top]
	    // -----------------------------------------------------
		this.updatePane = new VersionPane( mainCtrl );
		
		// ------------------------------------------------
		// Show Pane
		// ------------------------------------------------
		this.basePane.setTop( this.updatePane );
		this.setPaneBottom();
		this.setContent( this.basePane );
		
		// set icon on Tab
		this.setGraphic( MyGUITool.createImageView( 16, 16, mainCtrl.getImage("file:resources/images/winicon.gif") ) );
		
		this.setAction();
		
		this.changeLang();
		
		System.out.println( "VersionTab construct end" );
	}
	
	private void setPaneBottom()
	{
		System.out.println( "VersionTab:setPaneBottom start" );
		this.tabPane.setSide(Side.LEFT);
		this.tabPane.getTabs().addAll
		(
			new VersionAboutTab( this.dbView ),
			new VersionLibraryTab( this.dbView )
		);
		this.tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		this.basePane.setBottom( this.tabPane );
		System.out.println( "VersionTab:setPaneBottom end" );
	}
	
	private void setAction()
	{
		this.setOnCloseRequest( ((CloseInterface)this.updatePane)::closeRequest	);
	}
	
	// ChangeLangInterface
	@Override
	public void changeLang() 
	{
		MainController mainCtrl = this.dbView.getMainController();
		ResourceBundle extLangRB = mainCtrl.getLangResource("conf.lang.gui.ctrl.menu.MainMenuBar");

		// Tab Title
		Node tabGraphic = this.getGraphic();
		if ( tabGraphic instanceof Label )
		{
			((Label)tabGraphic).setText( extLangRB.getString("MENU_HELP_VERSION") );
		}
		else
		{
			this.setText( extLangRB.getString("MENU_HELP_VERSION") );
		}
		
		((ChangeLangInterface)this.updatePane).changeLang();
		
		this.tabPane.getTabs().stream()
			.filter( ChangeLangInterface.class::isInstance )
			.map( ChangeLangInterface.class::cast )
			.forEach( x->x.changeLang() );
	}

}
