package milu.db;

import java.sql.SQLException;
import java.util.Map;

import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

/**
 * 
 * @author milu
 *
 */
public class MyDBSQLServer extends MyDBAbstract 
{

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
		// URL Example
		// ------------------------------------------------------
		// jdbc:sqlserver://localhost:1433;databaseName=miludb;
		// ------------------------------------------------------
		urlTmp = 
			"jdbc:sqlserver://" + 
			dbOptMap.get( "Host" ) + ":" + dbOptMap.get( "Port" ) + ";" +
			"databaseName=" + dbOptMap.get( "DBName" ) + ";"
			;
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
		return 1433;
	}
	
	/***********************************************
	 * Get DB URL
	 ***********************************************
	 * @return URL
	 */
	public String getUrl()
	{
		if ( this.dbOpts.size() == 0 )
		{	
			return this.url;
		}
		else
		{
			StringBuffer sb = new StringBuffer("");
			this.dbOpts.forEach
			( 
				(k,v)->
				{
					if ( this.url.contains(k) == false )
					{
						if ( v != null )
						{
							sb.append(k+"="+v+";");
						}
						else
						{
							sb.append(k+";");
						}
					}
				}
			);
			return this.url + ";" + sb.toString();
		}
	}
	
	public void setUrl( String url )
	{
		this.dbOpts.clear();
		this.dbOptsAux.clear();
		this.dbOptsSpecial.clear();
		if ( url == null )
		{
			this.url = url;
			return;
		}
		else if ( url.indexOf(';') == -1 )
		{
			this.url = url;
		}
		else
		{
			int pos = url.indexOf(';');
			this.url = url.substring( 0, pos );
			String strParam = url.substring( pos+1 );
			String[] strKVs = strParam.split(";");
			for ( String strKV : strKVs )
			{
				int posK = strKV.indexOf('=');
				if ( posK != -1 )
				{
					String k = strKV.substring(0,posK);
					String v = strKV.substring(posK+1);
					if ( "user".equals(k) )
					{
						this.username = v;
					}
					else if ( "password".equals(k) )
					{
						this.password = v;
					}
					else
					{
						this.dbOpts.put( k, v );
					}
				}
				else
				{
					this.dbOpts.put( strKV, null );
				}
			}
		}
	}
	
	@Override
	public void setSchemaRoot()
	{
		this.schemaRoot = SchemaEntityFactory.createInstance( this.url, SchemaEntity.SCHEMA_TYPE.ROOT );
	}
}
