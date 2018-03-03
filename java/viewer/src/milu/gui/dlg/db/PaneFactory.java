package milu.gui.dlg.db;

import java.util.ResourceBundle;
import javafx.scene.layout.Pane;

import milu.db.MyDBAbstract;

public interface PaneFactory
{
	public Pane createPane( MyDBAbstract myDBAbs, ResourceBundle langRB );
}
