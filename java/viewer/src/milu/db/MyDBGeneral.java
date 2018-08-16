package milu.db;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

public class MyDBGeneral extends MyDBAbstract 
{
	@Override
	void init()
	{
	}

	@Override
	protected void loadSpecial() 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void processAfterException() throws SQLException 
	{
	}

	@Override
	public String getDriverUrl(Map<String, String> dbOptMap, MyDBAbstract.UPDATE update ) 
	{
		return null;
	}

	@Override
	public int getDefaultPort() 
	{
		return 0;
	}
	
	@Override
	public void setSchemaRoot()
	{
		this.schemaRoot = SchemaEntityFactory.createInstance( this.url, SchemaEntity.SCHEMA_TYPE.ROOT );
	}
	
	@Override
	protected void processAfterConnection()
	{
	}
	
	@Override
	protected void addProp( Properties prop )
	{
	}
}
