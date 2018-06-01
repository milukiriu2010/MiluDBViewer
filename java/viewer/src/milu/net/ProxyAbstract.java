package milu.net;

import java.net.URL;
import java.net.URLConnection;
import java.net.Proxy;
import java.util.Base64;

import milu.main.AppConf;

abstract class ProxyAbstract 
{
	protected AppConf appConf = null;
	
	void setAppConf( AppConf appConf )
	{
		this.appConf = appConf;
	}
	
	abstract Proxy selectProxy( URL url );
	
	// http://www.rgagnon.com/javadetails/java-0085.html
	void callProxyAuth( URLConnection uc )
	{
		if ( ProxyType.NO_PROXY.equals(this.appConf.getProxyType()) )
		{
			return;
		}
		
		String proxyUser = this.appConf.getProxyUser();
		String proxyPwd  = this.appConf.getProxyPassword();
		String proxyStr  = proxyUser + ":" + proxyPwd;
		
		Base64.Encoder base64Encoder = Base64.getUrlEncoder();
		
		String strEncoded = base64Encoder.encodeToString(proxyStr.getBytes());
		uc.setRequestProperty( "Proxy-Authorization", "Basic " + strEncoded );
	}
}
