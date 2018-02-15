package milu.entity;

public class AppSettingEntity
{
	public enum APPSET_TYPE
	{
		TYPE_DB,
		TYPE_GENERAL
	}
	
	private String name = null;
	
	private APPSET_TYPE type = null;
	
	public AppSettingEntity( String name, APPSET_TYPE type )
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
