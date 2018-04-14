package milu.main;

import java.io.File;

// https://www.mkyong.com/java/java-enum-example/
public enum AppConst 
{
	USER_DIR(System.getProperty("user.home")+File.separator+".MiluDBViewer"+File.separator),
	APP_CONF(USER_DIR.val()+"app_conf"+File.separator+"app_conf.json");
	
	private String val = null;
	
	private AppConst( String val )
	{
		this.val = val;
	}
	
	public String val()
	{
		return this.val;
	}
}
