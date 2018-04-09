package milu.gui.dlg.db;

import java.util.Map;

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
	 * @param mapProp
	 * @return
	 */
	public UrlPaneAbstract createPane( Dialog<?> dlg, MainController mainCtrl, MyDBAbstract myDBAbs, Map<String,String> mapProp );
}
