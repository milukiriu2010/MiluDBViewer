package milu.gui.dlg.app;

import java.util.ResourceBundle;

import javafx.scene.control.Dialog;
import milu.gui.dlg.app.AppSettingMenu.APPSET_TYPE;
import milu.main.MainController;

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
		else if ( AppSettingMenu.APPSET_TYPE.TYPE_DB_MYSQL.equals( key ) )
		{
			appPaneAbs = new AppPaneDBConfMySQL();
		}
		else if ( AppSettingMenu.APPSET_TYPE.TYPE_DB_ORACLE.equals( key ) )
		{
			appPaneAbs = new AppPaneDBConfOracle();
		}
		else if ( AppSettingMenu.APPSET_TYPE.TYPE_DB_POSTGRESQL.equals( key ) )
		{
			appPaneAbs = new AppPaneDBConfPostgres();
		}
		else if ( AppSettingMenu.APPSET_TYPE.TYPE_PROXY.equals( key ) )
		{
			appPaneAbs = new AppPaneProxy();
		}
		else if ( AppSettingMenu.APPSET_TYPE.TYPE_GENERAL.equals( key ) )
		{
			appPaneAbs = new AppPaneGeneralConf();
		}
		else
		{
			appPaneAbs = new AppPaneGeneralConf();
		}
		
		appPaneAbs.createPane( dlg, mainCtrl, extLangRB );
		return appPaneAbs;
	}

}
