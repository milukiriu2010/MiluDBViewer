package milu.task.version;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import milu.main.AppConf;
import milu.main.AppConst;
import milu.net.ProxyAbstract;
import milu.net.ProxyFactory;
import milu.task.ProgressInterface;

class ModuleDownload 
{
	private AppConf appConf = null;
	
	private ProgressInterface progressInf = null;
	
	private URL url = null;
	
	private String downloadFileName = null;
	
	private Integer fileSize = 0;
	
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
		this.fileSize = (Integer)dataMap.get("fileSize");
	}
	
	void setAssignedSize( double assignedSize )
	{
		this.assignedSize = assignedSize;
	}

	void check( String strUrl )
		throws 
			MalformedURLException, 
			IOException
	{
		this.progressInf.setMsg( "start download..." );
		File downloadDir = new File( AppConst.DOWNLOAD_DIR.val() );
		downloadDir.mkdirs();
		
		this.url = new URL( strUrl );
		
		ProxyAbstract proxyAbs = ProxyFactory.getInstance( this.appConf );
		proxyAbs.selectProxy();
		
		HttpURLConnection uc = (HttpURLConnection)url.openConnection();
		uc.connect();

		// https://www.mkyong.com/java/how-to-write-to-file-in-java-fileoutputstream-example/
		String downloadFilePath = downloadDir.getAbsolutePath() + File.separator + this.downloadFileName;
		File downloadFile = new File( downloadFilePath );
		try(
			InputStream is = uc.getInputStream();
			FileOutputStream fop = new FileOutputStream(downloadFile); 
		)
		{
			final int BUF_SIZE = 1024;
			int readByte = -1;
			byte[] readBuf = new byte[BUF_SIZE];
			int dlByte = 0;
			while ( (readByte=is.read(readBuf)) != -1 )
			{
				double progress = (double)dlByte*this.assignedSize/(double)this.fileSize;
				String strMsg = String.format( "%d/%d bytes - %.3f%%", dlByte, this.fileSize, progress );
				this.progressInf.setMsg( strMsg );
				this.progressInf.setProgress( progress );
				
				fop.write(readBuf,0,readByte);
				fop.flush();
				dlByte += readByte;
			}
		}
		
	}
}
