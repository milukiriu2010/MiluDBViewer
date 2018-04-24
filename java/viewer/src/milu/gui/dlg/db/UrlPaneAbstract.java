package milu.gui.dlg.db;

import javafx.scene.control.Dialog;

import javafx.scene.layout.Pane;
import milu.db.MyDBAbstract;
import milu.main.MainController;

abstract class UrlPaneAbstract extends Pane
	implements UrlInterface
{
	/**
	 * 
	 * @param dlg
	 * @param mainCtrl
	 * @param myDBAbs
	 */
	abstract void createPane( Dialog<?> dlg, MainController mainCtrl, MyDBAbstract myDBAbs );
	
	abstract void init();
}
