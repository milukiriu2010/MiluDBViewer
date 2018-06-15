package milu.net;

public class ProxySystem extends ProxyAbstract 
{

	@Override
	public void selectProxy() 
	{
		// jdk-10.0.1/conf/net.properties
		System.clearProperty( "http.proxyHost" );
		System.clearProperty( "http.proxyPort" );
		System.clearProperty( "https.proxyHost" );
		System.clearProperty( "https.proxyPort" );
		System.setProperty( "java.net.useSystemProxies", "true" );
		
		this.callProxyAuth();
	}

}
