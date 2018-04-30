package milu.db;

import java.sql.SQLException;
import java.util.Map;

import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

public class MyDBSQLite extends MyDBAbstract {

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
		urlTmp = "jdbc:sqlite:" + dbOptMap.get( "DBName" );
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
		return 0;
	}

	@Override
	protected void setSchemaRoot() 
	{
		this.schemaRoot = SchemaEntityFactory.createInstance( this.url, SchemaEntity.SCHEMA_TYPE.ROOT );
	}

}
