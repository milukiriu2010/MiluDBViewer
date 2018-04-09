package milu.gui.dlg.db;

import java.util.Map;
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
	 * @param mapProp
	 */
	abstract void createPane( Dialog<?> dlg, MainController mainCtrl, MyDBAbstract myDBAbs, Map<String,String> mapProp );
}
