package milu.gui.ctrl.info;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import javafx.scene.control.Tab;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import milu.gui.ctrl.common.inf.ChangeLangInterface;
import milu.gui.view.DBView;
import milu.main.AppConst;
import milu.main.MainController;

public class VersionAboutTab extends Tab
	implements 
		ChangeLangInterface 
{
	private DBView          dbView = null;

	public VersionAboutTab( DBView dbView )
	{
		super();
		
		this.dbView = dbView;
		
		this.setContent();
		
		this.changeLang();
	}
	
	private void setContent()
	{
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
		}
		webEngine1.loadContent( MessageFormat.format( strFmt, AppConst.VER.val(), AppConst.UPDATE_DATE.val() ) );
		
		this.setContent(webView1);
	}
	
	// ChangeLangInterface
	@Override
	public void changeLang() 
	{
		MainController mainCtrl = this.dbView.getMainController();
		ResourceBundle langRB = mainCtrl.getLangResource("conf.lang.gui.common.NodeName");
		
		this.setText(langRB.getString("LABEL_ABOUT"));
	}
}
