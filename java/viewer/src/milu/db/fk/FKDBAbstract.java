package milu.db.fk;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import milu.db.MyDBAbstract;
import milu.db.abs.ObjDBInterface;
import milu.entity.schema.SchemaEntity;

public abstract class FKDBAbstract implements ObjDBInterface 
{
	// DB Access Object
	protected MyDBAbstract  myDBAbs = null;

	@Override
	public void setMyDBAbstract(MyDBAbstract myDBAbs) 
	{
		this.myDBAbs = myDBAbs;
	}

	/**
	 * select Foreign Key List
	 */
	@Override
	abstract public List<SchemaEntity> selectEntityLst(String schemaName) throws SQLException;
	
	// SQL for Table List
	abstract protected String listSQL( String schemaName );	
}
