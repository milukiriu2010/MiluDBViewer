package milu.gui.dlg.db;

import java.util.Map;
import java.util.ResourceBundle;
import javafx.scene.control.Dialog;

import javafx.scene.layout.Pane;
import milu.db.MyDBAbstract;
import milu.ctrl.MainController;

abstract class UrlPaneAbstract extends Pane
	implements UrlInterface
{
	abstract public void createPane( Dialog<?> dlg, MainController mainCtrl, MyDBAbstract myDBAbs, ResourceBundle extLangRB, Map<String,String> mapProp );
}
