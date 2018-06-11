package milu.net;

import java.net.URL;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import milu.main.AppConf;
import milu.main.AppConst;

public class DownloadModule 
{
	private AppConf appConf = null;
	
	// // https://sourceforge.net/projects/miludbviewer/files/MiluDBViewer0.1.9/MiluDBViewer_Setup0.1.9.exe/download
	private URL url = null;
	
	public void setAppConf( AppConf appConf )
	{
		this.appConf = appConf;
	}
	
	public boolean exec( String strUrl )
		throws 
			MalformedURLException, 
			IOException
	{
		File downloadDir = new File( AppConst.DOWNLOAD_DIR.val() );
		downloadDir.mkdirs();
		
		this.url = new URL( strUrl );
		
		String[] strPaths = strUrl.split("/");
		System.out.println( "DownloadModule:strPaths:" + strPaths.length );
		String   strFile = null;
		for ( String strPath: strPaths )
		{
			System.out.println( "DownloadModule:strPath:" + strPath );
			if ( strPath.endsWith("exe") || strPath.endsWith("tar.gz") )
			{
				strFile = strPath;
			}
		}
		
		ProxyAbstract proxyAbs = ProxyFactory.getInstance( this.appConf );
		proxyAbs.selectProxy();
		
		HttpURLConnection uc = (HttpURLConnection)url.openConnection();
		uc.connect();
		
		// https://www.mkyong.com/java/how-to-write-to-file-in-java-fileoutputstream-example/
		File downloadFile = new File( downloadDir.getAbsolutePath() + File.separator + strFile );
		try(
			InputStream is = uc.getInputStream();
			FileOutputStream fop = new FileOutputStream(downloadFile); 
		)
		{
			//fop.write(is.readAllBytes());
			//fop.flush();
			final int BUF_SIZE = 1024;
			int readByte = -1;
			byte[] readBuf = new byte[BUF_SIZE];
			int dlByte = 0;
			while ( (readByte=is.read(readBuf)) != -1 )
			{
				fop.write(readBuf,0,readByte);
				fop.flush();
				dlByte += readByte;
				System.out.println( "Downloaded:" + dlByte );
			}
		}
		System.out.println( "Download done." );
		
		return true;
	}
}
