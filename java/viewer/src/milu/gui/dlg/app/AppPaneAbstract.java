package milu.gui.dlg.app;

import java.util.ResourceBundle;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import milu.file.json.MyJsonHandleAbstract;
import milu.file.json.MyJsonHandleFactory;
import milu.main.AppConf;
import milu.main.AppConst;
import milu.main.MainController;

abstract class AppPaneAbstract extends Pane 
	implements ApplyInterface 
{
	// Language Resource(from External Class)
	protected ResourceBundle extLangRB = null;
	
	protected Dialog<?>      dlg       = null;
	
	protected MainController mainCtrl  = null;
	
	// Label of title
	protected Label  lblTitle = new Label();
	
	/**
	 * 
	 * @param dlg
	 * @param mainCtrl
	 * @param extLangRB
	 */
	abstract public void createPane( Dialog<?> dlg, MainController mainCtrl, ResourceBundle extLangRB );
	
	protected void save() throws FileNotFoundException, IOException
	{
		MyJsonHandleAbstract myJsonAbs =
			new MyJsonHandleFactory().createInstance(AppConf.class);
			
		myJsonAbs.open(AppConst.APP_CONF.val());
		myJsonAbs.save( this.mainCtrl.getAppConf() );
	}
}
