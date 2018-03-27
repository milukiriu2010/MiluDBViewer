package milu.db.trigger;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import milu.db.abs.ObjDBInterface;

import milu.db.MyDBAbstract;
import milu.entity.schema.SchemaEntity;

public abstract class TriggerDBAbstract implements ObjDBInterface
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
	
	@Override
	public List<Map<String,String>> selectDefinition( String schemaName, String objName ) throws SQLException
	{
		throw new UnsupportedOperationException();
	}

	// Source of Trigger
	@Override
	abstract public String getSRC( String schemaName, String triggerName ) throws SQLException;
}
