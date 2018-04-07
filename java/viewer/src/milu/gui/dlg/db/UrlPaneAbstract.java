package milu.gui.dlg.db;

import java.util.Map;
import java.util.ResourceBundle;
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
	 * @param extLangRB
	 * @param mapProp
	 */
	abstract public void createPane( Dialog<?> dlg, MainController mainCtrl, MyDBAbstract myDBAbs, ResourceBundle extLangRB, Map<String,String> mapProp );
}
