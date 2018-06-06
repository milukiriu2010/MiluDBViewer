package milu.net;

import java.net.URL;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;


import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.xml.sax.SAXException;

import milu.ctrl.xml.parse.MyXMLParse;
import milu.main.AppConf;
import milu.main.AppConst;

// http://www.rgagnon.com/javadetails/java-0085.html
public class CheckUpdate 
{
	private AppConf appConf = null;
	
	private URL url = null;
	
	private String  newVersion = null;
	
	private boolean isExistNew = false;
	
	public void setAppConf( AppConf appConf )
	{
		this.appConf = appConf;
	}
	
	public String getNewVersion()
	{
		return this.newVersion;
	}
	
	public boolean isExistNew()
	{
		return this.isExistNew;
	}
	
	public boolean check() 
			throws MalformedURLException, IOException, ParserConfigurationException, SAXException, XPathExpressionException
	{
		this.url = new URL("https://sourceforge.net/projects/miludbviewer/rss?path=/");
		//this.url = new URL("http://www.livedoor.com/");
		
		System.out.println( "ProxyType:" + this.appConf.getProxyType() );
		
		ProxyAbstract proxyAbs = ProxyFactory.getInstance( this.appConf );
		proxyAbs.selectProxy();
		
		/*
		HttpURLConnection uc = null;
		if ( proxy == null )
		{
			uc = (HttpURLConnection)url.openConnection();
		}
		else
		{
			uc = (HttpURLConnection)url.openConnection(proxy);
		}
		*/
		//HttpURLConnection uc = (HttpURLConnection)url.openConnection(proxy);
		//proxyAbs.callProxyAuth(uc);
		//proxyAbs.callProxyAuth(null);
		HttpURLConnection uc = (HttpURLConnection)url.openConnection();
		uc.connect();
		
		StringBuffer page = new StringBuffer();
		BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
		String line = null;
		while ( (line = in.readLine()) != null )
		{
		   page.append( line + "\n" );
		}
		
		String strXML = page.toString();
		System.out.println( "Recv Data:" + strXML );
		
		MyXMLParse myXMLParse = new MyXMLParse();
		List<String> pathLst = myXMLParse.analyze(strXML, "/rss/channel/item/title" );
		
		// https://sourceforge.net/projects/miludbviewer/files/MiluDBViewer0.1.9/MiluDBViewer0.1.9.tar.gz/download
		
		// /MiluDBViewer0.1.9/MiluDBViewer0.1.9.tar.gz
		// /MiluDBViewer0.1.9/MiluDBViewer_Setup0.1.9.exe
		// /MiluDBViewer0.1.8/MiluDBViewer0.1.8.tar.gz
		// /MiluDBViewer0.1.8/MiluDBViewer_Setup0.1.8.exe
		this.getNewVersion(pathLst);
		System.out.println( "NewVersion:" + this.newVersion );
		
		this.compareVersion();
		
		return this.isExistNew;
	}
	
	private void getNewVersion( List<String> pathLst1 )
	{
		// System.getProperty:os.name
		//   "Windows 10"
		String osName = System.getProperty("os.name");
		String pathFirst = 
				pathLst1.stream()
					.filter
					(
						(path)->
						{
							if ( osName.contains("Windows") )
							{
								return path.endsWith("exe");
							}
							else
							{
								return !path.endsWith("exe");
							}
						}
					)
					.findFirst()
					.get();

		if ( pathFirst == null )
		{
			return;
		}
		
		System.out.println( pathFirst );
		String[] pathFirstArray = pathFirst.split("/");
		System.out.println( pathFirstArray[1] );
		this.newVersion = pathFirstArray[1].replace( "MiluDBViewer", "" );
	}
	
	private void compareVersion()
	{
		this.isExistNew = false;
		if ( this.newVersion == null )
		{
			return;
		}
		
		String[] nowVer = AppConst.VER.val().split("\\.");
		String[] newVer = this.newVersion.split("\\.");
		
		if ( nowVer.length != 3 || newVer.length != 3 )
		{
			return;
		}

		for ( int i = 0; i <= 2; i++ )
		{
			// "nowVer" is older than "newVer"
			if ( 
					( nowVer[i].compareTo(newVer[i]) < 0 ) 
					&&
					( nowVer[i].length() <= newVer[i].length() )
			)
			{
				System.out.println( "compareVersion break i:" + i );
				this.isExistNew = true;
				return;
			}
			// "nowVer" is newer than "newVer"
			else if ( nowVer[i].compareTo(newVer[i]) > 0 )
			{
				return;
			}
		}
	}
}
