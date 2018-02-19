package milu.db.sysview;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import milu.db.MyDBAbstract;
import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

abstract public class SystemViewDBAbstract 
{
	// DB Access Object
	protected MyDBAbstract  myDBAbs = null;
	
	// Materialized View List
	protected List<Map<String,String>> viewLst = new ArrayList<>();
	
	public void setMyDBAbstract( MyDBAbstract myDBAbs )
	{
		this.myDBAbs = myDBAbs;
	}
	
	protected void clear()
	{
		this.viewLst.clear();
	}
	
	public List<SchemaEntity> getEntityLst()
	{
		List<SchemaEntity>  viewEntityLst = new ArrayList<>();
		for ( Map<String,String> view : this.viewLst )
		{
			SchemaEntity eachViewEntity = SchemaEntityFactory.createInstance( view.get("viewName"), SchemaEntity.SCHEMA_TYPE.SYSTEM_VIEW );
			String strStatus = view.get("status");
			if ( strStatus != null && "INVALID".equals(strStatus) )
			{
				eachViewEntity.setState( SchemaEntity.STATE.INVALID );
			}
			viewEntityLst.add( eachViewEntity );
		}
		return viewEntityLst;
	}	
	
	abstract public void selectEntityLst( String schemaName ) throws SQLException;
	
	abstract protected String listSQL( String schemaName );

}
