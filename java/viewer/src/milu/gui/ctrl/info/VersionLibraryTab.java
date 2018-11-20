package milu.gui.ctrl.info;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.control.Tab;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import milu.gui.ctrl.common.inf.ChangeLangInterface;
import milu.gui.view.DBView;
import milu.main.MainController;

public class VersionLibraryTab extends Tab 
	implements 
		ChangeLangInterface 
{
	private DBView          dbView = null;

	public VersionLibraryTab( DBView dbView )
	{
		super();
		
		this.dbView = dbView;
		
		this.setContent();
		
		this.changeLang();
	}
	
	private void setContent()
	{
		URL urlLibInfo = getClass().getResource( "/conf/html/dlg/libinfo.html" );
		WebView   webView1   = new WebView();
		WebEngine webEngine1 = webView1.getEngine(); 
		webEngine1.load( urlLibInfo.toExternalForm() );
		
		this.setContent(webView1);
		/*
		try 
		{
			String[] fileNames = new String[] {
					System.getProperty("user.dir")+"/conf/html/dlg/libinfo.html",
					System.getProperty("user.dir")+"/bin/conf/html/dlg/libinfo.html"
			};
			String fileName = null;
			File file = null;
			for (int i=0;i<fileNames.length;i++)
			{
				fileName = fileNames[i];
				file = new File(fileName);
				if (file.exists()) break;
			}
			System.out.println("fileName:"+fileName);
			URL urlLibInfo =  file.toURI().toURL();
			WebView   webView1   = new WebView();
			WebEngine webEngine1 = webView1.getEngine(); 
			webEngine1.load( urlLibInfo.toExternalForm() );
			
			this.setContent(webView1);
		}
		catch ( MalformedURLException ex )
		{
			ex.printStackTrace();
		}
		*/
	}
	
	@Override
	public void changeLang() 
	{
		MainController mainCtrl = this.dbView.getMainController();
		ResourceBundle langRB = mainCtrl.getLangResource("conf.lang.gui.common.NodeName");
		
		this.setText(langRB.getString("LABEL_LIB"));
	}
}
