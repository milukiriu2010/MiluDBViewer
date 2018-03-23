package milu.gui.dlg.app;

import java.util.ResourceBundle;

import javafx.scene.control.Dialog;

import milu.ctrl.MainController;

interface PaneFactory
{
	/**
	 * 
	 * @param key
	 * @param dlg
	 * @param mainCtrl
	 * @param extLangRB
	 * @return
	 */
	public AppPaneAbstract createPane( AppSettingMenu.APPSET_TYPE key, Dialog<?> dlg, MainController mainCtrl, ResourceBundle extLangRB );
}
