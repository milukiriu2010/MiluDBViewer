package milu.task.version;

import java.io.File;
import java.util.Map;
import java.io.IOException;

import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;

import milu.main.AppConf;
import milu.main.AppConst;
import milu.task.ProgressInterface;
import milu.tool.MyFileTool;

public class ModuleUpdate 
{
	private AppConf appConf = null;
	
	private ProgressInterface progressInf = null;
	
	private String downloadFileName = null;
	
	private double  assignedSize = 0.0;
	
	void setAppConf( AppConf appConf )
	{
		this.appConf = appConf;
	}
	
	void setProgressInterface( ProgressInterface progressInf )
	{
		this.progressInf = progressInf;
	}
	
	void setValue( Map<String,Object> dataMap )
	{
		this.downloadFileName = (String)dataMap.get("downloadFileName");
	}
	
	void setAssignedSize( double assignedSize )
	{
		this.assignedSize = assignedSize;
	}
	
	public void exec()
		throws IOException
	{
		String downloadFilePath = AppConst.DOWNLOAD_DIR.val() + File.separator + this.downloadFileName;
		// System.getProperty:os.name
		//   "Windows 10"
		String osName = System.getProperty("os.name");
		if ( osName.contains("Windows") )
		{
			// user.dir
			// C:\myjava\MiluDBViewer.git\java\viewer
			
			this.progressInf.setMsg( "start installer..." );
			
			Runtime.getRuntime().exec(downloadFilePath);
		}
		else
		{
			// user.dir
			// /opt/myjava/MiluDBViewer.git/java/viewer
			this.progressInf.setMsg( "extract files..." );
			
			// http://rauschig.org/jarchivelib/download.html
			File fileArchive = new File(downloadFilePath);
			File dirExtract  = new File(AppConst.DOWNLOAD_DIR.val());

			// MiluDBViewer0.2.0.tar.gz => extract => MiluDBViewer0.2.0/
			Archiver archiver = ArchiverFactory.createArchiver("tar","gz");
			archiver.extract( fileArchive, dirExtract );
			
			this.progressInf.setProgress(50);
			this.progressInf.setMsg( "copy files..." );
			
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
			
			this.progressInf.setProgress(90);
			this.progressInf.setMsg( "start new version..." );
			Runtime.getRuntime().exec( instDir + File.separator + "MiluDBViewer.sh" );
		}

	}
}
