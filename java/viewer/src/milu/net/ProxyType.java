package milu.net;

public enum ProxyType 
{
	NO_PROXY("Direct"),
	SYSTEM("System"),
	MANUAL("Manual")
	;
	
	private String val = null;
	
	private ProxyType( String val )
	{
		this.val = val;
	}

	public String val()
	{
		return this.val;
	}
}
