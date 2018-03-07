package milu.gui.dlg.db;

import java.util.Map;
import java.util.ResourceBundle;

import javafx.scene.control.Dialog;

import milu.ctrl.MainController;
import milu.db.MyDBAbstract;

public interface PaneFactory
{
	public UrlPaneAbstract createPane( Dialog<?> dlg, MainController mainCtrl, MyDBAbstract myDBAbs, ResourceBundle extLangRB, Map<String,String> mapProp );
}
