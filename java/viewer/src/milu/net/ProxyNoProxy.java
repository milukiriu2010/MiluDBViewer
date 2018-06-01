package milu.net;

import java.net.Proxy;
import java.net.URL;

public class ProxyNoProxy extends ProxyAbstract 
{

	@Override
	Proxy selectProxy(URL url) 
	{
		return Proxy.NO_PROXY;
	}

}
