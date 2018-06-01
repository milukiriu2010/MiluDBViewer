package milu.gui.dlg.app;

class AppSettingMenu
{
	public enum APPSET_TYPE
	{
		TYPE_ROOT,
		TYPE_DB,
		TYPE_DB_MYSQL,
		TYPE_DB_ORACLE,
		TYPE_DB_POSTGRESQL,
		TYPE_PROXY,
		TYPE_GENERAL
	}
	
	private String name = null;
	
	private APPSET_TYPE type = null;
	
	public AppSettingMenu( String name, APPSET_TYPE type )
	{
		this.name = name;
		this.type = type;
	}

	public String toString()
	{
		return this.name;
	}

	public void setName( String name )
	{
		this.name = name;
	}

	public APPSET_TYPE getType()
	{
		return this.type;
	}
}
