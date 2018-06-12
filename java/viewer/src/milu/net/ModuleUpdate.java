package milu.net;

import java.io.File;
import java.io.IOException;

import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;

import milu.main.AppConf;
import milu.main.AppConst;
import milu.tool.MyFileTool;

public class ModuleUpdate 
{
	private AppConf appConf = null;
	
	public void setAppConf( AppConf appConf )
	{
		this.appConf = appConf;
	}	
	
	public void exec( String moduleName )
		throws IOException
	{
		// System.getProperty:os.name
		//   "Windows 10"
		String osName = System.getProperty("os.name");
		if ( osName.contains("Windows") )
		{
			Runtime.getRuntime().exec(moduleName);
			System.out.println( "exec: ModuleUpdate.");
			
			// user.dir
			// C:\myjava\MiluDBViewer.git\java\viewer
		}
		else
		{
			// user.dir
			// /opt/myjava/MiluDBViewer.git/java/viewer
			
			// http://rauschig.org/jarchivelib/download.html
			File fileArchive = new File(moduleName);
			File dirExtract  = new File(AppConst.DOWNLOAD_DIR.val());

			// MiluDBViewer0.2.0.tar.gz => extract => MiluDBViewer0.2.0/
			Archiver archiver = ArchiverFactory.createArchiver("tar","gz");
			archiver.extract( fileArchive, dirExtract );
			
			String instDir = this.appConf.getInstDir();
			if ( instDir == null || instDir.length() == 0 )
			{
				instDir = System.getProperty("user.dir");
			}
			
			int lenExt = ".tar.gz".length();
			String strFileArchive = fileArchive.getName();
			String strFileArchiveBaseName = strFileArchive.substring(0,strFileArchive.length()-lenExt);
			System.out.println( "instDir              :" + instDir );
			System.out.println( "fileArchive(Name)    :" + strFileArchive );
			System.out.println( "fileArchive(BaseName):" + strFileArchiveBaseName );
			
			String srcDir = AppConst.DOWNLOAD_DIR.val() + strFileArchiveBaseName;
			
			// not work
			//MyFileTool.copyDir( srcDir, instDir, true );
			MyFileTool.copyFolder( new File(srcDir), new File(instDir) );
		}
	}
}
