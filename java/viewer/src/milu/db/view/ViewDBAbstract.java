package milu.db.view;

import java.util.List;
import java.util.Map;

import java.sql.SQLException;

import milu.db.abs.ObjDBInterface;

import milu.db.MyDBAbstract;
import milu.entity.schema.SchemaEntity;

public abstract class ViewDBAbstract implements ObjDBInterface
{
	// DB Access Object
	protected MyDBAbstract  myDBAbs = null;

	@Override
	public void setMyDBAbstract( MyDBAbstract myDBAbs )
	{
		this.myDBAbs = myDBAbs;
	}
	
	@Override
	abstract public List<SchemaEntity> selectEntityLst( String schemaName ) throws SQLException;
	
	abstract protected String listSQL( String schemaName );
	
	// select View Definition
	@Override
	abstract public List<Map<String,String>> selectDefinition( String schameName, String viewName ) throws SQLException;
	
	// SQL for View Definition
	abstract protected String definitionSQL( String schemaName, String viewName );
	
	
	// Source of View
	@Override
	public String getSRC( String schemaName, String funcName ) throws SQLException
	{
		throw new UnsupportedOperationException();
	}

}
