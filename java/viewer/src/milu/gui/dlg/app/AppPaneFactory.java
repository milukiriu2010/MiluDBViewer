package milu.gui.dlg.app;

import java.util.Map;
import java.util.ResourceBundle;

import javafx.scene.control.Dialog;
import milu.ctrl.MainController;
import milu.entity.AppSettingEntity;
import milu.entity.AppSettingEntity.APPSET_TYPE;

class AppPaneFactory implements PaneFactory 
{
	@Override
	public AppPaneAbstract createPane(APPSET_TYPE key, Dialog<?> dlg, MainController mainCtrl, ResourceBundle extLangRB )
	{
		AppPaneAbstract appPaneAbs = null;
		if ( AppSettingEntity.APPSET_TYPE.TYPE_DB.equals( key ) )
		{
			appPaneAbs = new AppPaneDBConf();
		}
		else if ( AppSettingEntity.APPSET_TYPE.TYPE_GENERAL.equals( key ) )
		{
			appPaneAbs = new AppPaneGeneralConf();
		}
		
		appPaneAbs.createPane( dlg, mainCtrl, extLangRB );
		return appPaneAbs;
	}

}
