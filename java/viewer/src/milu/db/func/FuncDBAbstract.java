package milu.db.func;

import java.sql.SQLException;
import java.util.List;

import milu.db.MyDBAbstract;
import milu.entity.schema.SchemaEntity;

abstract public class FuncDBAbstract 
{
	// DB Access Object
	protected MyDBAbstract  myDBAbs = null;
	
	public void setMyDBAbstract( MyDBAbstract myDBAbs )
	{
		this.myDBAbs = myDBAbs;
	}
	
	// select Function List
	abstract public List<SchemaEntity> selectEntityLst( String schemaName ) throws SQLException;
	
	// SQL for Function List
	abstract protected String listSQL( String schemaName );
	
	// Source of Function
	abstract public String getSRC( String schemaName, String funcName ) throws SQLException;

}
