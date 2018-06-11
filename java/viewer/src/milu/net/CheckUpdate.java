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
	
	// https://sourceforge.net/projects/miludbviewer/rss?path=/
	private URL url = null;
	
	// x.x.x
	private String  newVersion = null;
	
	private boolean isExistNew = false;
	
	private String  newLink = null;
	
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
	
	public String getNewLink()
	{
		return this.newLink;
	}
	
	// <item>
	//     <title><![CDATA[/MiluDBViewer0.2.0/MiluDBViewer_Setup0.2.0.exe]]></title>
	//     <link>https://sourceforge.net/projects/miludbviewer/files/MiluDBViewer0.2.0/MiluDBViewer_Setup0.2.0.exe/download</link>
	//     <guid>https://sourceforge.net/projects/miludbviewer/files/MiluDBViewer0.2.0/MiluDBViewer_Setup0.2.0.exe/download</guid>
	//     <pubDate>Wed, 06 Jun 2018 23:14:06 UT</pubDate>
	//     <description><![CDATA[/MiluDBViewer0.2.0/MiluDBViewer_Setup0.2.0.exe]]></description>
	//     <files:sf-file-id xmlns:files="https://sourceforge.net/api/files.rdf#">28362746</files:sf-file-id>
	//     <files:extra-info xmlns:files="https://sourceforge.net/api/files.rdf#">PE32 executable</files:extra-info>
	//     <media:content xmlns:media="http://video.search.yahoo.com/mrss/" type="application/x-dosexec; charset=binary" url="https://sourceforge.net/projects/miludbviewer/files/MiluDBViewer0.2.0/MiluDBViewer_Setup0.2.0.exe/download" filesize="37500847"><media:hash algo="md5">d949b66fd32ce1b5811cccc61a28307b</media:hash></media:content>
	// </item>
	public boolean check() 
		throws 
			MalformedURLException, 
			IOException, 
			ParserConfigurationException, 
			SAXException, 
			XPathExpressionException
	{
		this.url = new URL("https://sourceforge.net/projects/miludbviewer/rss?path=/");
		
		System.out.println( "ProxyType:" + this.appConf.getProxyType() );
		
		ProxyAbstract proxyAbs = ProxyFactory.getInstance( this.appConf );
		proxyAbs.selectProxy();
		
		HttpURLConnection uc = (HttpURLConnection)url.openConnection();
		uc.connect();
		
		StringBuffer page = new StringBuffer();
		try( BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream())); )
		{
			String line = null;
			while ( (line = in.readLine()) != null )
			{
			   page.append( line + "\n" );
			}
		}
		
		String strXML = page.toString();
		System.out.println( "Recv Data:" + strXML );
		
		MyXMLParse myXMLParse = new MyXMLParse();
		List<String> titlePathLst = myXMLParse.analyze(strXML, "/rss/channel/item/title" );
		
		// ---------------------------------------------------
		// /rss/channel/item/title
		// ---------------------------------------------------
		// /MiluDBViewer0.1.9/MiluDBViewer0.1.9.tar.gz
		// /MiluDBViewer0.1.9/MiluDBViewer_Setup0.1.9.exe
		// /MiluDBViewer0.1.8/MiluDBViewer0.1.8.tar.gz
		// /MiluDBViewer0.1.8/MiluDBViewer_Setup0.1.8.exe
		String titlePathFirst = this.getNewVersion(titlePathLst);
		System.out.println( "NewVersion:" + this.newVersion );
		
		this.compareVersion();
		
		if ( this.isExistNew == true )
		{
			List<String> titleLinkLst = myXMLParse.analyze(strXML, "/rss/channel/item/link" );
			
			// ---------------------------------------------------
			// /rss/channel/item/link
			// ---------------------------------------------------
			// https://sourceforge.net/projects/miludbviewer/files/MiluDBViewer0.1.9/MiluDBViewer_Setup0.1.9.exe/download
			// https://sourceforge.net/projects/miludbviewer/files/MiluDBViewer0.1.9/MiluDBViewer0.1.9.tar.gz/download
			this.getNewLink( titleLinkLst, titlePathFirst );
		}
		
		return this.isExistNew;
	}
	
	private String getNewVersion( List<String> pathLst1 )
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
			return null;
		}
		
		System.out.println( pathFirst );
		String[] pathFirstArray = pathFirst.split("/");
		System.out.println( pathFirstArray[1] );
		this.newVersion = pathFirstArray[1].replace( "MiluDBViewer", "" );
		
		return pathFirst;
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
	
	private void getNewLink( List<String> pathLst1, String link )
	{
		this.newLink = 
			pathLst1.stream()
				.filter( (path)->path.contains(link) )
				.findFirst()
				.get();
	}	
}
