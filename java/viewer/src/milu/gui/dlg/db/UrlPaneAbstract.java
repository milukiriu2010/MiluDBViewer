package milu.gui.dlg.db;

import java.util.Map;
import java.util.ResourceBundle;

import javafx.scene.layout.Pane;
import milu.db.MyDBAbstract;

abstract class UrlPaneAbstract extends Pane
	implements UrlInterface
{
	abstract public void createPane( MyDBAbstract myDBAbs, ResourceBundle extLangRB, Map<String,String> mapProp );
}
