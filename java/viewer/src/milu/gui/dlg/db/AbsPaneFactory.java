package milu.gui.dlg.db;

import javafx.scene.control.Dialog;
import milu.db.MyDBAbstract;
import milu.main.MainController;

interface AbsPaneFactory
{
	/**
	 * 
	 * @param dlg
	 * @param mainCtrl
	 * @param myDBAbs
	 * @return
	 */
	public UrlPaneAbstract createPane( Dialog<?> dlg, MainController mainCtrl, MyDBAbstract myDBAbs );
}
