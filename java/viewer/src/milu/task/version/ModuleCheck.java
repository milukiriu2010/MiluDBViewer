package milu.task.version;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.Map;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import milu.task.ProgressInterface;
import milu.ctrl.xml.parse.MyXMLParse;
import milu.main.AppConf;
import milu.net.ProxyAbstract;
import milu.net.ProxyFactory;

class ModuleCheck 
{
	private AppConf appConf = null;
	
	private ProgressInterface progressInf = null;
	
	// https://sourceforge.net/projects/miludbviewer/rss?path=/
	private URL url = null;
	
	// x.x.x
	private String  newVersion = null;
	
	private String  newLink = null;
	
	private Integer fileSize = null;
	
	void setAppConf( AppConf appConf )
	{
		this.appConf = appConf;
	}
	
	void setProgressInterface( ProgressInterface progressInf )
	{
		this.progressInf = progressInf;
	}
	
	public Map<String,Object> getValue()
	{
		Map<String,Object> dataMap = new HashMap<>();
		dataMap.put( "newVersion", this.newVersion );
		dataMap.put( "newLink"   , this.newLink );
		dataMap.put( "fileSize"  , this.fileSize );
		return dataMap;
	}
	
	// ---------------------------------------------------------------------------------------
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
	// ---------------------------------------------------------------------------------------
	void check( String strUrl ) 
		throws 
			MalformedURLException, 
			IOException, 
			ParserConfigurationException, 
			SAXException, 
			XPathExpressionException
	{
		String strXML = this.accessRemoteURL( strUrl );
		
		this.progressInf.setProgress(50);
		
		Document xmlDoc = this.str2doc( strXML );
		
		this.progressInf.setProgress(60);
		
		Node nodeItem = this.searchNodeItem( xmlDoc );
		
		this.progressInf.setProgress(70);
		
		this.searchNodeLink( nodeItem );
		
		this.progressInf.setProgress(80);
		
		this.searchNodeMediaContent( nodeItem );
	}
	
	private String accessRemoteURL( String strUrl )
		throws 
			MalformedURLException, 
			IOException
	{
		this.progressInf.setMsg("Check RSS...");
		
		System.out.println( "accessRemoteURL:" + strUrl );
		
		this.url = new URL( strUrl );
		
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
		return strXML;
	}
	
	private Document str2doc( String strXML )
		throws ParserConfigurationException, IOException, SAXException
	{
		MyXMLParse myXmlParse = new MyXMLParse();
		Document xmlDoc = myXmlParse.str2doc(strXML);
		return xmlDoc;
	}
	
	// ---------------------------------------------------
	// /rss/channel/item/title
	// ---------------------------------------------------
	// /MiluDBViewer0.1.9/MiluDBViewer0.1.9.tar.gz
	// /MiluDBViewer0.1.9/MiluDBViewer_Setup0.1.9.exe
	// /MiluDBViewer0.1.8/MiluDBViewer0.1.8.tar.gz
	// /MiluDBViewer0.1.8/MiluDBViewer_Setup0.1.8.exe
	private Node searchNodeItem( Node root )
		throws XPathExpressionException
	{
		// System.getProperty:os.name
		//   "Windows 10"
		String osName = System.getProperty("os.name");
		
		MyXMLParse myXmlParse = new MyXMLParse();
		NodeList nodeListTitle = myXmlParse.searchNodeList( root, "//rss/channel/item/title" );
		Node nodeTitle = null;
		String title = null;
		for ( int i = 0; i < nodeListTitle.getLength(); i++ )
		{
			nodeTitle = nodeListTitle.item(i);
			//title = nodeTitle.getChildNodes().item(0).getNodeValue();
			//title = myXmlParse.searchNodeText( nodeTitle, "//title/text()" );
			//title = myXmlParse.searchNodeText( nodeTitle, "/title/text()" );
			//title = myXmlParse.searchNodeText( nodeTitle, "./title/text()" );
			title = myXmlParse.searchNodeText( nodeTitle, "./text()" );
			System.out.println( "title:" + title );
			
			if ( osName.contains("Windows") )
			{
				if ( title.endsWith("exe") )
				{
					break;
				}
			}
			else
			{
				if ( title.endsWith("tar.gz") )
				{
					break;
				}
			}
		}
		
		String[] titleArray = title.split("/");
		System.out.println( titleArray[1] );
		this.newVersion = titleArray[1].replace( "MiluDBViewer", "" );
		
		return nodeTitle.getParentNode();
	}
	
	// ---------------------------------------------------
	// /rss/channel/item/link
	// ---------------------------------------------------
	// https://sourceforge.net/projects/miludbviewer/files/MiluDBViewer0.1.9/MiluDBViewer_Setup0.1.9.exe/download
	// https://sourceforge.net/projects/miludbviewer/files/MiluDBViewer0.1.9/MiluDBViewer0.1.9.tar.gz/download
	private void searchNodeLink( Node root )
		throws XPathExpressionException
	{
		MyXMLParse myXmlParse = new MyXMLParse();
		Node node = myXmlParse.searchNode( root, "./link/text()" );
		this.newLink = node.getNodeValue();
		System.out.println( "link:" + this.newLink );
	}
	
	// ---------------------------------------------------
	// /rss/channel/item/media:content
	// ---------------------------------------------------
	private void searchNodeMediaContent( Node root )
		throws XPathExpressionException
	{
		MyXMLParse myXmlParse = new MyXMLParse();
		Node node = myXmlParse.searchNode( root, "./*[name()='media:content']/@filesize" );
		this.fileSize = Integer.valueOf(node.getNodeValue());
		System.out.println( "fileSize:" + this.fileSize );
	}

}
