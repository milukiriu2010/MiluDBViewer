package milu.gui.dlg;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.image.Image;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class VersionDialog extends Dialog<Boolean>
{
	// Property File for this class 
	private static final String PROPERTY_FILENAME = 
			"conf.lang.gui.dlg.VersionDialog";

	// Language Resource
	private ResourceBundle langRB = ResourceBundle.getBundle( PROPERTY_FILENAME );
	
	// TabPane
	TabPane  tabPane = new TabPane();
	
	// Tab for Version Infomation
	Tab      verTab  = new Tab();
	
	// Tab for library Information
	Tab      libTab  = new Tab();
	
	public VersionDialog()
	{
		super();
		
		// Set Content on Version Information Tab
		this.setContentOnVerTab();
		
		// Set Content on Library Information Tab
		this.setContentOnLibTab();
		
		this.tabPane.getTabs().addAll( this.verTab, this.libTab );
		this.tabPane.setTabClosingPolicy( TabClosingPolicy.UNAVAILABLE );
		
		BorderPane pane = new BorderPane();
		pane.setCenter( this.tabPane );
		
		// set pane on dialog
		this.getDialogPane().setContent( pane );
		
		this.getDialogPane().getButtonTypes().add( ButtonType.CLOSE );
		
		// Window Icon
		Stage     stage   = (Stage)this.getDialogPane().getScene().getWindow();
		stage.getIcons().add( new Image( "file:resources/images/winicon.gif" ) );
		
		// set size
		this.setResizable( true );
		this.getDialogPane().setPrefSize( 640, 320 );
		
		// set Dialog Title
		this.setTitle( langRB.getString( "TITLE_ABOUT" ) );
		
		this.setAction();
	}
	
	private void setAction()
	{
		// result when clicking on "Close".
		this.setResultConverter( (dialogButton)->{ return Boolean.TRUE; } );
	}
	
	private void setContentOnVerTab()
	{
		URL urlVerInfo = getClass().getResource( "/conf/html/dlg/verinfo.html" );
		WebView   webView   = new WebView();
		WebEngine webEngine = webView.getEngine(); 
		webEngine.load( urlVerInfo.toExternalForm() );
		this.verTab.setContent( webView );
		this.verTab.setText( langRB.getString( "TITLE_ABOUT" ) );
	}
	
	private void setContentOnLibTab()
	{
		URL urlLibInfo = getClass().getResource( "/conf/html/dlg/libinfo.html" );
		WebView   webView   = new WebView();
		WebEngine webEngine = webView.getEngine(); 
		webEngine.load( urlLibInfo.toExternalForm() );
		this.libTab.setContent( webView );
		this.libTab.setText( langRB.getString( "TITLE_LIBRARY" ) );
	}
}
