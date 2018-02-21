package milu.db.mateview;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import milu.db.MyDBAbstract;
import milu.entity.schema.SchemaEntity;

abstract public class MaterializedViewDBAbstract 
{
	// DB Access Object
	protected MyDBAbstract  myDBAbs = null;
	
	public void setMyDBAbstract( MyDBAbstract myDBAbs )
	{
		this.myDBAbs = myDBAbs;
	}
	/*
	public List<SchemaEntity> getEntityLst()
	{
		List<SchemaEntity>  viewEntityLst = new ArrayList<>();
		for ( Map<String,String> view : this.viewLst )
		{
			SchemaEntity eachViewEntity = SchemaEntityFactory.createInstance( view.get("viewName"), SchemaEntity.SCHEMA_TYPE.MATERIALIZED_VIEW );
			String strStatus = view.get("status");
			if ( strStatus != null && "INVALID".equals(strStatus) )
			{
				eachViewEntity.setState( SchemaEntity.STATE.INVALID );
			}
			viewEntityLst.add( eachViewEntity );
		}
		return viewEntityLst;
	}	
	*/
	abstract public List<SchemaEntity> selectEntityLst( String schemaName ) throws SQLException;
	
	abstract protected String listSQL( String schemaName );
	
	// select Table Definition
	abstract public List<Map<String,String>> selectDefinition( String schameName, String viewName ) throws SQLException;
	
	// SQL for Table Definition
	abstract protected String definitionSQL( String schemaName, String viewName );

}
