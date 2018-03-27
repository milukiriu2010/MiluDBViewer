package milu.db.proc;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import milu.db.abs.ObjDBInterface;

import milu.db.MyDBAbstract;
import milu.entity.schema.SchemaEntity;

abstract public class ProcDBAbstract implements ObjDBInterface
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
	
	// Source of Procedure
	@Override
	abstract public String getSRC( String schemaName, String procName ) throws SQLException;

}
