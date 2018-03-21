package milu.gui.dlg.app;

import java.util.Map;
import java.util.ResourceBundle;

import javafx.scene.control.Dialog;

import milu.ctrl.MainController;
import milu.db.MyDBAbstract;
import milu.entity.AppSettingEntity;

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
	public AppPaneAbstract createPane( AppSettingEntity.APPSET_TYPE key, Dialog<?> dlg, MainController mainCtrl, ResourceBundle extLangRB );
}
