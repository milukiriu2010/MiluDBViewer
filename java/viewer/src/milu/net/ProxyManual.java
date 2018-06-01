package milu.net;

import java.net.Proxy;
import java.net.URL;
import java.net.InetSocketAddress;

public class ProxyManual extends ProxyAbstract 
{

	@Override
	Proxy selectProxy(URL url) 
	{
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress( this.appConf.getProxyHost(), this.appConf.getProxyPort() ));
		return proxy;
	}

}
