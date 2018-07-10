package milu.main;

import java.io.File;

// https://www.mkyong.com/java/java-enum-example/
public enum AppConst 
{
	APP_NAME("MiluDBViewer"),
	VER("0.2.5"),
	UPDATE_DATE("2018/07/11"),
	USER_DIR(System.getProperty("user.home")+File.separator+".MiluDBViewer"+File.separator),
	APP_CONF(USER_DIR.val()+"app_conf"+File.separator+"app_conf.json"),
	DRIVER_DIR(USER_DIR.val()+"driver"+File.separator),
	DB_DIR(USER_DIR.val()+"bookmark"+File.separator),
	DOWNLOAD_DIR(USER_DIR.val()+"download"+File.separator),
	KEY_FILE(USER_DIR.val()+"key"+File.separator+"enc.key");
	
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
