package milu.task.main;

import java.io.FileNotFoundException;

import milu.file.json.MyJsonHandleAbstract;
import milu.file.json.MyJsonHandleFactory;
import milu.main.AppConf;
import milu.main.AppConst;

public class InitialLoadAppConf extends InitialLoadAbstract 
{

	@Override
	public void load() 
	{
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
			this.showException(ex);
		}
		finally
		{
			this.progressInf.addProgress( this.assignedSize );
			this.progressInf.setMsg( "AppConf" );
		}
	}

}