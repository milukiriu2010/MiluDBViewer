package milu.task.version;

import java.net.URL;

import milu.main.AppConf;

class ModuleCheck 
{
	private AppConf appConf = null;
	
	// https://sourceforge.net/projects/miludbviewer/rss?path=/
	private URL url = null;
	
	// x.x.x
	private String  newVersion = null;
	
	private Boolean isExistNew = false;
	
	private String  newLink = null;
	
	private Integer fileSize = null;
	
	public void setAppConf( AppConf appConf )
	{
		this.appConf = appConf;
	}
	
	
}
