package milu.gui.ctrl.info;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import milu.gui.ctrl.common.inf.CloseInterface;
import milu.gui.ctrl.common.inf.ChangeLangInterface;
import milu.gui.view.DBView;
import milu.main.AppConst;
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
	// Left  => About this application
	// Right => Library Info
	private SplitPane  showPane = new SplitPane();
	
	public VersionTab( DBView dbView )
	{
		super();
		
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
	}
	
	private void setPaneBottom()
	{
		// ------------------------------------------------
		// [Pane(1) on Tab]-[Left]
		// ------------------------------------------------
		URL urlVerInfo = getClass().getResource( "/conf/html/dlg/verinfo.html" );
		WebView   webView1   = new WebView();
		WebEngine webEngine1 = webView1.getEngine();
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
		webEngine1.loadContent( MessageFormat.format( strFmt, AppConst.VER.val(), AppConst.UPDATE_DATE.val() ) );
		
		// ------------------------------------------------
		// [Pane(1) on Tab]-[Right]
		// ------------------------------------------------	
		URL urlLibInfo = getClass().getResource( "/conf/html/dlg/libinfo.html" );
		WebView   webView2   = new WebView();
		WebEngine webEngine2 = webView2.getEngine(); 
		webEngine2.load( urlLibInfo.toExternalForm() );
		
		// ------------------------------------------------
		// Show Pane
		// ------------------------------------------------
		this.showPane.setOrientation(Orientation.HORIZONTAL);
		this.showPane.getItems().addAll( webView1, webView2 );
		this.showPane.setDividerPositions( 0.5f, 0.5f );
		
		this.basePane.setBottom( this.showPane );
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
	}

}
