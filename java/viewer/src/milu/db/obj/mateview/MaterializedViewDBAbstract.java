package milu.db.obj.mateview;

import java.util.List;
import java.util.Map;
import java.sql.SQLException;

import milu.db.MyDBAbstract;
import milu.db.obj.abs.ObjDBInterface;
import milu.entity.schema.SchemaEntity;

abstract public class MaterializedViewDBAbstract implements ObjDBInterface
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
	
	// select Table Definition
	@Override
	abstract public List<Map<String,String>> selectDefinition( String schameName, String viewName ) throws SQLException;
	
	// SQL for Table Definition
	abstract protected String definitionSQL( String schemaName, String viewName );

	@Override
	public String getSRC( String schemaName, String objName ) throws SQLException
	{
		throw new UnsupportedOperationException();
	}
}
