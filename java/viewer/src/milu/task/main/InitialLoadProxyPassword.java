package milu.task.main;

import milu.main.AppConf;
import milu.main.AppConst;
import milu.tool.MyTool;

public class InitialLoadProxyPassword extends InitialLoadAbstract 
{

	@Override
	public void load() 
	{
		AppConf appConf = this.mainCtrl.getAppConf();
		try
		{
			System.setProperty( "http.agent", AppConst.APP_NAME.val() + "(" + System.getProperty("os.name") + ")" );
			appConf.setProxyPassword(this.mainCtrl.getSecretKey());
		}
		catch ( Exception ex )
		{
			MyTool.showException( this.mainCtrl, "conf.lang.gui.common.MyAlert", "TITLE_MISC_ERROR", ex );
		}
		finally
		{
			this.progressInf.addProgress( this.assignedSize );
			this.progressInf.setMsg( "ProxyPassword" );
		}

	}

}
