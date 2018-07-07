package milu.db.driver;

import com.google.gson.annotations.Expose;

public class DriverInfo 
{
	@Expose(serialize = true, deserialize = true)
	private String       driverClassName = null;
	
	@Expose(serialize = true, deserialize = true)
	private String       tmplateUrl = null;
	
	@Expose(serialize = true, deserialize = true)
	private String       referenceUrl = null;

	// get ClassName by TextField
	public String getDriverClassName()
	{
		return this.driverClassName;
	}
	
	public void setDriverClassName( String driverClassName )
	{
		this.driverClassName = driverClassName;
	}
	
	public String getTemplateUrl()
	{
		return this.tmplateUrl;
	}
	
	public void setTemplateUrl( String templateUrl )
	{
		this.tmplateUrl = templateUrl;
	}
	
	public String getReferenceUrl()
	{
		return this.referenceUrl;
	}
	
	public void setReferenceUrl( String referenceUrl )
	{
		this.referenceUrl = referenceUrl;
	}	
}
