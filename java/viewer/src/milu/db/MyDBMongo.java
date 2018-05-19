package milu.db;

import java.sql.SQLException;
import java.util.Map;

import milu.db.driver.DriverClassConst;
import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

public class MyDBMongo extends MyDBAbstract {

	@Override
	void init()
	{
	}

	@Override
	protected void loadSpecial() 
	{
	}

	@Override
	public void processAfterException() throws SQLException 
	{
	}

	@Override
	public String getDriverUrl(Map<String, String> dbOptMap, UPDATE update) 
	{
		String urlTmp = "";
		if ( DriverClassConst.CLASS_NAME_MONGODB1.val().equals(this.driverShim.getDriverClazzName()))
		{
			urlTmp = 
					"jdbc:mongo://"+
					dbOptMap.get( "Host" )+":"+dbOptMap.get( "Port" )+"/"+
					dbOptMap.get( "DBName" );
		}
		if ( update.equals(MyDBAbstract.UPDATE.WITH) )
		{
			this.dbOptsAux.clear();
			dbOptMap.forEach( (k,v)->this.dbOptsAux.put(k,v) );
			this.url = urlTmp;
		}
		return urlTmp;
	}

	@Override
	public int getDefaultPort() 
	{
		return 27017;
	}

	@Override
	protected void setSchemaRoot()
	{
		this.schemaRoot = SchemaEntityFactory.createInstance( this.url, SchemaEntity.SCHEMA_TYPE.ROOT );
	}

}
