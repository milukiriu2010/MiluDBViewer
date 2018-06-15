package milu.net;

public class ProxyNoProxy extends ProxyAbstract 
{

	@Override
	public void selectProxy() 
	{
		System.clearProperty( "http.proxyHost" );
		System.clearProperty( "http.proxyPort" );
		System.clearProperty( "https.proxyHost" );
		System.clearProperty( "https.proxyPort" );
		System.setProperty( "java.net.useSystemProxies", "false" );
		
		//return Proxy.NO_PROXY;
	}

}
