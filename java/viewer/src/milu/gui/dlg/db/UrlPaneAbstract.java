package milu.gui.dlg.db;

import java.util.Map;
import java.util.ResourceBundle;

import javafx.scene.layout.Pane;
import milu.db.MyDBAbstract;
import milu.ctrl.MainController;

abstract class UrlPaneAbstract extends Pane
	implements UrlInterface
{
	abstract public void createPane( MainController mainCtrl, MyDBAbstract myDBAbs, ResourceBundle extLangRB, Map<String,String> mapProp );
}
