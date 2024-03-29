package milu.db.obj.type;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import milu.db.MyDBAbstract;
import milu.db.obj.abs.ObjDBInterface;
import milu.entity.schema.SchemaEntity;

abstract public class TypeDBAbstract implements ObjDBInterface
{
	// DB Access Object
	protected MyDBAbstract  myDBAbs = null;
	
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
	
	// Source of Type
	@Override
	abstract public String getSRC( String schemaName, String typeName ) throws SQLException;

}
