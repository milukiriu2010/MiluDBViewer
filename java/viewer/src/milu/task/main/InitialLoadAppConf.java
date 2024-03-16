package milu.task.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;

import milu.file.json.MyJsonEachAbstract;
import milu.file.json.MyJsonEachFactory;
import milu.main.AppConf;
import milu.main.AppConst;
import milu.tool.MyGUITool;

public class InitialLoadAppConf extends InitialLoadAbstract 
{

	@Override
	public void load() 
	{
		/*
		MyJsonHandleAbstract myJsonAbs =
				new MyJsonHandleFactory().createInstance(AppConf.class);
		try
		{
			myJsonAbs.open(AppConst.APP_CONF.val());
			Object obj = myJsonAbs.load();
			if ( obj instanceof AppConf )
			{
				AppConf appConf = (AppConf)obj;
				this.mainCtrl.setAppConf(appConf);
			}
		}
		catch ( FileNotFoundException nfEx )
		{
			// When this application starts at the first time,
			// "app_conf.json" doesn't exists yet.
			// So it always enters this logic.
			System.out.println( "Not Found:" + AppConst.APP_CONF.val() );
		}
		catch ( Exception ex )
		{
			// "app_conf.json" exists
			// but, cannot read.
			MyGUITool.showException( this.mainCtrl, "conf.lang.gui.common.MyAlert", "TITLE_MISC_ERROR", ex );
		}
		finally
		{
			AppConf appConf = this.mainCtrl.getAppConf();
			if ( this.propMap.containsKey("instDir") )
			{
				appConf.setInstDir(this.propMap.get("instDir"));
			}
			{
				String langCode = appConf.getLangCode();
				if ( langCode != null && langCode.isEmpty() == false )
				{
					Locale nextLocale = new Locale( langCode );
					Locale.setDefault( nextLocale );
				}
			}
			
			this.progressInf.addProgress( this.assignedSize );
			this.progressInf.setMsg( "AppConf" );
		}
		*/
		MyJsonEachAbstract<AppConf> myJsonAbs =
			MyJsonEachFactory.<AppConf>getInstance(MyJsonEachFactory.factoryType.APP_CONF);
		try
		{
			AppConf appConf = myJsonAbs.load( new File(AppConst.APP_CONF.val()) );
			this.mainCtrl.setAppConf(appConf);
		}
		catch ( FileNotFoundException nfEx )
		{
			// When this application starts at the first time,
			// "app_conf.json" doesn't exists yet.
			// So it always enters this logic.
			System.out.println( "Not Found:" + AppConst.APP_CONF.val() );
		}
		catch ( Exception ex )
		{
			// "app_conf.json" exists
			// but, cannot read.
			MyGUITool.showException( this.mainCtrl, "conf.lang.gui.common.MyAlert", "TITLE_MISC_ERROR", ex );
		}
		finally
		{
			AppConf appConf = this.mainCtrl.getAppConf();
			if ( this.propMap.containsKey("instDir") )
			{
				appConf.setInstDir(this.propMap.get("instDir"));
			}
			{
				String langCode = appConf.getLangCode();
				if ( langCode != null && langCode.isEmpty() == false )
				{
					// deprecated version 19
					//Locale nextLocale = new Locale( langCode );
					Locale nextLocale = Locale.of( langCode );
					Locale.setDefault( nextLocale );
				}
			}
			
			this.progressInf.addProgress( this.assignedSize );
			this.progressInf.setMsg( "AppConf" );
		}
	}

}
