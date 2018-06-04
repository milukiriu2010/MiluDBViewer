package milu.net;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import milu.main.AppConf;

abstract class ProxyAbstract 
{
	protected AppConf appConf = null;
	
	void setAppConf( AppConf appConf )
	{
		this.appConf = appConf;
	}
	
	abstract void selectProxy();
	
	// http://www.rgagnon.com/javadetails/java-0085.html
	protected void callProxyAuth()
	{
		if ( ProxyType.NO_PROXY.equals(this.appConf.getProxyType()) )
		{
			return;
		}
		
		// https://stackoverflow.com/questions/41505219/unable-to-tunnel-through-proxy-proxy-returns-http-1-1-407-via-https?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
		// https://qiita.com/kaakaa_hoe/items/d4fb11a3af035a287972
		System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
		System.setProperty("jdk.http.auth.proxying.disabledSchemes", "");
		
		String proxyUser = this.appConf.getProxyUser();
		String proxyPwd  = this.appConf.getProxyPassword();
		
		if ( proxyUser == null || proxyUser.length() == 0 )
		{
			return;
		}
		
		Authenticator.setDefault
		(
			new Authenticator() 
			{
				@Override
				protected PasswordAuthentication getPasswordAuthentication() 
				{
					return new PasswordAuthentication( proxyUser, proxyPwd.toCharArray() );
				}
	        }
		);
		System.out.println( "set PasswordAuthentication" );
	}
}
