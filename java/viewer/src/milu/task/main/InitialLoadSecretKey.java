package milu.task.main;

import java.io.File;

import javax.crypto.SecretKey;

import milu.file.ext.MyFileExtAbstract;
import milu.file.ext.MyFileExtFactory;
import milu.main.AppConst;
import milu.security.MySecurityKey;
import milu.tool.MyGUITool;

public class InitialLoadSecretKey extends InitialLoadAbstract 
{

	@Override
	public void load() 
	{
		File keyFile = new File(AppConst.KEY_FILE.val());
		MyFileExtAbstract<SecretKey> myFileExtAbs = 
			MyFileExtFactory.getInstance(MyFileExtFactory.TYPE.SERIALIZE);
		try
		{
			SecretKey secretKey = myFileExtAbs.load(keyFile, SecretKey.class );
			if ( secretKey != null )
			{
				this.mainCtrl.setSecretKey(secretKey);
			}
			else
			{
				SecretKey keyTmp = new MySecurityKey().createKey();
				myFileExtAbs.save( keyFile, keyTmp );
				this.mainCtrl.setSecretKey(keyTmp);
			}
		}
		catch ( Exception ex )
		{
			//this.showException(ex);
			MyGUITool.showException( this.mainCtrl, "conf.lang.gui.common.MyAlert", "TITLE_MISC_ERROR", ex );
		}
		finally
		{
			this.progressInf.addProgress( this.assignedSize );
			this.progressInf.setMsg( "SecretKey" );
		}
	}

}
