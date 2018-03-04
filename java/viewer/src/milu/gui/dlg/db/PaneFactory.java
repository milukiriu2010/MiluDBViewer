package milu.gui.dlg.db;

import java.util.Map;
import java.util.ResourceBundle;
import javafx.scene.layout.Pane;

import milu.db.MyDBAbstract;

public interface PaneFactory
{
	public UrlPaneAbstract createPane( MyDBAbstract myDBAbs, ResourceBundle extLangRB, Map<String,String> mapProp );
}
