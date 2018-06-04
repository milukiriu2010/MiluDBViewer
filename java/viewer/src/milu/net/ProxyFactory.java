package milu.net;

import milu.main.AppConf;

public class ProxyFactory 
{
	static ProxyAbstract getInstance( AppConf appConf )
	{
		ProxyAbstract proxyAbs = null;
		ProxyType proxyType = appConf.getProxyType();
		if ( ProxyType.NO_PROXY.equals(proxyType) )
		{
			proxyAbs = new ProxyNoProxy();
		}
		else if ( ProxyType.SYSTEM.equals(proxyType) )
		{
			proxyAbs = new ProxySystem();
		}
		else if ( ProxyType.MANUAL.equals(proxyType) )
		{
			proxyAbs = new ProxyManual();
		}
		else
		{
			return null;
		}
		
		proxyAbs.setAppConf(appConf);
		return proxyAbs;
	}
}
