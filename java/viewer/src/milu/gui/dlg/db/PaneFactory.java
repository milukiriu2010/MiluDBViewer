package milu.gui.dlg.db;

import java.util.Map;
import java.util.ResourceBundle;

import javafx.scene.control.Dialog;
import milu.db.MyDBAbstract;
import milu.main.MainController;

interface PaneFactory
{
	/**
	 * 
	 * @param dlg
	 * @param mainCtrl
	 * @param myDBAbs
	 * @param extLangRB
	 * @param mapProp
	 * @return
	 */
	public UrlPaneAbstract createPane( Dialog<?> dlg, MainController mainCtrl, MyDBAbstract myDBAbs, ResourceBundle extLangRB, Map<String,String> mapProp );
}
