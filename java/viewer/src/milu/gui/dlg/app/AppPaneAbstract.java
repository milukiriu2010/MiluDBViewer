package milu.gui.dlg.app;

import java.util.ResourceBundle;

import javafx.scene.control.Dialog;
import javafx.scene.layout.Pane;
import milu.ctrl.MainController;

abstract class AppPaneAbstract extends Pane 
	implements ApplyInterface 
{
	/**
	 * 
	 * @param dlg
	 * @param mainCtrl
	 * @param extLangRB
	 */
	abstract public void createPane( Dialog<?> dlg, MainController mainCtrl, ResourceBundle extLangRB );
}
