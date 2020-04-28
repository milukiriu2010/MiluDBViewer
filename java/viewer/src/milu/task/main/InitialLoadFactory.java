package milu.task.main;

import java.util.Map;

import milu.main.MainController;
import milu.task.ProgressInterface;

public class InitialLoadFactory 
{
	public enum FACTORY_TYPE
	{
		IMAGE,
		LANG,
		APPCONF,
		DRIVER,
		SECRETKEY,
		PROXYPASSWORD
	}
	
	public static InitialLoadAbstract getInstance
	( 
		FACTORY_TYPE        type, 
		MainController      mainCtrl,
		Map<String,String>  propMap,
		ProgressInterface   progressInf, 
		double             assignedSize 
	)
	{
		InitialLoadAbstract ilAbs = null;
		
		System.out.println("===================================");
		System.out.println("InitialLoadFactory:"+type.toString());
		
		if ( FACTORY_TYPE.IMAGE.equals(type) )
		{
			ilAbs = new InitialLoadImage();
		}
		else if ( FACTORY_TYPE.LANG.equals(type) )
		{
			ilAbs = new InitialLoadLangResource();
		}
		else if ( FACTORY_TYPE.APPCONF.equals(type) )
		{
			ilAbs = new InitialLoadAppConf();
		}
		else if ( FACTORY_TYPE.DRIVER.equals(type) )
		{
			ilAbs = new InitialLoadDriver();
		}
		else if ( FACTORY_TYPE.SECRETKEY.equals(type) )
		{
			ilAbs = new InitialLoadSecretKey();
		}
		else if ( FACTORY_TYPE.PROXYPASSWORD.equals(type) )
		{
			ilAbs = new InitialLoadProxyPassword();
		}
		else
		{
			return null;
		}
		
		ilAbs.setMainController(mainCtrl);
		ilAbs.setPropMap(propMap);
		ilAbs.setProgressInterface(progressInf);
		ilAbs.setAssignedSize(assignedSize);
		
		return ilAbs;
	}
}
