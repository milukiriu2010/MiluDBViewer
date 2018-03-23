package milu.gui.dlg.app;

import java.util.ResourceBundle;

import javafx.scene.control.Dialog;
import milu.ctrl.MainController;
import milu.gui.dlg.app.AppSettingMenu.APPSET_TYPE;

class AppPaneFactory implements PaneFactory 
{
	@Override
	public AppPaneAbstract createPane(APPSET_TYPE key, Dialog<?> dlg, MainController mainCtrl, ResourceBundle extLangRB )
	{
		AppPaneAbstract appPaneAbs = null;
		if ( AppSettingMenu.APPSET_TYPE.TYPE_DB.equals( key ) )
		{
			appPaneAbs = new AppPaneDBConf();
		}
		else if ( AppSettingMenu.APPSET_TYPE.TYPE_DB_POSTGRESQL.equals( key ) )
		{
			appPaneAbs = new AppPaneDBConfPostgres();
		}
		else if ( AppSettingMenu.APPSET_TYPE.TYPE_GENERAL.equals( key ) )
		{
			appPaneAbs = new AppPaneGeneralConf();
		}
		
		appPaneAbs.createPane( dlg, mainCtrl, extLangRB );
		return appPaneAbs;
	}

}
