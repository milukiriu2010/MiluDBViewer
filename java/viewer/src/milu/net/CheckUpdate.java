package milu.net;

import java.net.URL;
import java.net.Proxy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import milu.main.AppConf;

// http://www.rgagnon.com/javadetails/java-0085.html
public class CheckUpdate 
{
	private AppConf appConf = null;
	
	private URL url = null;
	
	public void setAppConf( AppConf appConf )
	{
		this.appConf = appConf;
	}
	
	public String getData() throws MalformedURLException, IOException
	{
		this.url = new URL("https://sourceforge.net/projects/miludbviewer/rss?path=/");
		
		System.out.println( "ProxyType:" + this.appConf.getProxyType() );
		
		ProxyAbstract proxyAbs = ProxyFactory.getInstance( this.appConf );
		Proxy proxy = proxyAbs.selectProxy(this.url);
		
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
		HttpURLConnection uc = (HttpURLConnection)url.openConnection(proxy);
		proxyAbs.callProxyAuth(uc);
		uc.connect();
		
		StringBuffer page = new StringBuffer();
		BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
		String line;
		while ( (line = in.readLine()) != null )
		{
		   page.append( line + "\n" );
		}
		
		return page.toString();
	}
	
}
