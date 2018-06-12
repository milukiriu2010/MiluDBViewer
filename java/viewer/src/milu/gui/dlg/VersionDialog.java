package milu.gui.dlg;

import java.net.URL;
import java.util.ResourceBundle;
import java.text.MessageFormat;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.layout.BorderPane;
import javafx.scene.input.KeyEvent;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import milu.main.AppConst;
import milu.main.MainController;
import milu.net.CheckUpdate;
import milu.net.DownloadModule;
import milu.net.ModuleUpdate;
import milu.tool.MyTool;

public class VersionDialog extends Dialog<Boolean>
{
	// Main Controller
	MainController mainCtrl = null;
	
	// TabPane
	TabPane  tabPane = new TabPane();
	
	// Tab for Version Information
	Tab      verTab  = new Tab();
	
	// Tab for library Information
	Tab      libTab  = new Tab();
	
	public VersionDialog( MainController mainCtrl )
	{
		super();
		
		this.mainCtrl = mainCtrl;
		
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
		stage.getIcons().add( this.mainCtrl.getImage( "file:resources/images/winicon.gif" ) );
		
		// set size
		this.setResizable( true );
		this.getDialogPane().setPrefSize( 640, 320 );
		
		// set location
		Platform.runLater( ()->MyTool.setWindowLocation( stage, stage.getWidth(), stage.getHeight() ) );
		
		// set Dialog Title
		ResourceBundle langRB = this.mainCtrl.getLangResource("conf.lang.gui.dlg.VersionDialog");
		this.setTitle( langRB.getString( "TITLE_ABOUT" ) );
		
		this.setAction();
	}
	
	private void setAction()
	{
		// result when clicking on "Close".
		this.setResultConverter( dialogButton->Boolean.TRUE );
	}
	
	private void setContentOnVerTab()
	{
		ResourceBundle langRB = this.mainCtrl.getLangResource("conf.lang.gui.dlg.VersionDialog");
		
		URL urlVerInfo = getClass().getResource( "/conf/html/dlg/verinfo.html" );
		WebView   webView   = new WebView();
		WebEngine webEngine = webView.getEngine();
		//webEngine.load( urlVerInfo.toExternalForm() );
		StringBuffer sb = new StringBuffer();
		String strFmt = null;
		try
		(
			InputStream is = urlVerInfo.openStream();
			DataInputStream     dis = new DataInputStream(is);
		)
		{
			int readSize = 0;
			int pos = 0;
			while ( ( readSize = dis.available() ) > 0 )
			{
				byte[] b = new byte [readSize];
				dis.read( b, pos, readSize );
				pos += readSize;
				sb.append( new String( b ) );
			}
			strFmt = sb.toString();
		}
		catch ( IOException ioEx )
		{
			ioEx.printStackTrace();
			return;
		}
		webEngine.loadContent( MessageFormat.format( strFmt, AppConst.VER.val(), AppConst.UPDATE_DATE.val() ) );
		
		Button btnCheck = new Button( langRB.getString("BTN_CHECK_UPDATE") );
		
		Label  lblCheck = new Label();
		
		TextField txtErrMsg = new TextField();
		txtErrMsg.setEditable(false);
		txtErrMsg.visibleProperty().setValue(false);
		
		Button btnDownload = new Button( "Download" );
		//btnDownload.setDisable(true);
		
		
		HBox hBoxCheck = new HBox(2);
		hBoxCheck.setPadding( new Insets( 10, 10, 10, 10 ) );
		hBoxCheck.setSpacing(10);
		hBoxCheck.getChildren().addAll( btnCheck, lblCheck );
		
		VBox vBox = new VBox(2);
		vBox.setPadding( new Insets( 10, 10, 10, 10 ) );
		//vBox.getChildren().addAll( webView, hBoxCheck, txtErrMsg, btnDownload );
		vBox.getChildren().addAll( webView, hBoxCheck, txtErrMsg );
		
		this.verTab.setContent( vBox );
		this.verTab.setText( langRB.getString( "TITLE_ABOUT" ) );
		
		//btnCheck.setOnKeyPressed(value);
		
		btnCheck.setOnAction
		//btnCheck.setOnKeyPressed
		(
			(event)->
			{
				//btnDownload.setDisable(true);
				CheckUpdate checkUpdate = new CheckUpdate();
				checkUpdate.setAppConf(this.mainCtrl.getAppConf());
				try
				{
					boolean isExistNew = checkUpdate.check();
					String newVersion = checkUpdate.getNewVersion();
					if ( isExistNew )
					{
						//lblCheck.setText( "New Verion is Found.(" + newVersion + ")" );
						lblCheck.setText( MessageFormat.format( langRB.getString("LABEL_FOUND_NEW_VERSION"), newVersion ) );
						String newLink = checkUpdate.getNewLink();
						System.out.println( "NewLink:" + newLink );
						System.out.println( "FileSize:" + checkUpdate.getFileSize() );
						if ( event instanceof KeyEvent )
						{
							KeyEvent keyEvent = (KeyEvent)event;
							if ( keyEvent.isControlDown() == false )
							{
								return;
							}
						}
						else
						{
							return;
						}
						vBox.getChildren().add(btnDownload);
						btnDownload.setOnAction
						((event2)->{
							try
							{
								DownloadModule dlModule = new DownloadModule();
								dlModule.setAppConf(this.mainCtrl.getAppConf());
								dlModule.exec(newLink);
								
								String downloadFileName = dlModule.getDownloadFileName();
								ModuleUpdate mdlUpdate = new ModuleUpdate();
								mdlUpdate.setAppConf(this.mainCtrl.getAppConf());
								mdlUpdate.exec(downloadFileName);
								
							}
							catch ( Exception ex2 )
							{
								throw new RuntimeException( ex2 );
							}
						});
					}
					else
					{
						//lblCheck.setText( "This version is up-to-date." );
						lblCheck.setText( MessageFormat.format( langRB.getString("LABEL_THIS_VERSION"), newVersion ) );
					}
					txtErrMsg.visibleProperty().setValue(false);
				}
				catch ( Exception ex )
				{
					// Unable to tunnel through proxy. Proxy returns "HTTP/1.1 407 Proxy Authentication Required"
					ex.printStackTrace();
					//lblCheck.setText( "Check proxy configuration." );
					lblCheck.setText( langRB.getString("LABEL_PROXY_ERROR") );
					txtErrMsg.setText( ex.getMessage() );
					txtErrMsg.visibleProperty().setValue(true);
				}
			}
		);
		
	}
	
	private void setContentOnLibTab()
	{
		URL urlLibInfo = getClass().getResource( "/conf/html/dlg/libinfo.html" );
		WebView   webView   = new WebView();
		WebEngine webEngine = webView.getEngine(); 
		webEngine.load( urlLibInfo.toExternalForm() );
		this.libTab.setContent( webView );
		ResourceBundle langRB = this.mainCtrl.getLangResource("conf.lang.gui.dlg.VersionDialog");
		this.libTab.setText( langRB.getString( "TITLE_LIBRARY" ) );
	}
}
