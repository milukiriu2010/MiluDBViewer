package milu.net;

public class ProxyManual extends ProxyAbstract 
{

	@Override
	void selectProxy() 
	{
		// jdk-10.0.1/conf/net.properties
		/**/
		//System.setProperty( "proxySet", "true");
		System.setProperty( "http.proxyHost", this.appConf.getProxyHost() );
		System.setProperty( "http.proxyPort", String.valueOf(this.appConf.getProxyPort()) );
		System.setProperty( "https.proxyHost", this.appConf.getProxyHost() );
		System.setProperty( "https.proxyPort", String.valueOf(this.appConf.getProxyPort()) );
		System.setProperty( "java.net.useSystemProxies", "false" );
		/**/
		
		this.callProxyAuth();
		
		/*
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress( this.appConf.getProxyHost(), this.appConf.getProxyPort() ));
		return proxy;
		*/
	}

}
