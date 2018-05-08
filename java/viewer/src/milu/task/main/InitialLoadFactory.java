package milu.task.main;

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
		SECRETKEY
	}
	
	public static InitialLoadAbstract getInstance( FACTORY_TYPE type, MainController mainCtrl, ProgressInterface progressInf, double assignedSize )
	{
		InitialLoadAbstract ilAbs = null;
		
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
		else
		{
			return null;
		}
		
		ilAbs.setMainController(mainCtrl);
		ilAbs.setProgressInterface(progressInf);
		ilAbs.setAssignedSize(assignedSize);
		
		return ilAbs;
	}
}
