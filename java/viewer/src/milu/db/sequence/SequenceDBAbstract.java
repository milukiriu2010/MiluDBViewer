package milu.db.sequence;

import java.sql.SQLException;
import java.util.List;

import milu.db.MyDBAbstract;
import milu.db.abs.ObjDBInterface;
import milu.entity.schema.SchemaEntity;

public abstract class SequenceDBAbstract implements ObjDBInterface 
{
	// DB Access Object
	protected MyDBAbstract  myDBAbs = null;
	
	@Override
	public void setMyDBAbstract( MyDBAbstract myDBAbs )
	{
		this.myDBAbs = myDBAbs;
	}
	
	/**
	 * select sequence list
	 */
	@Override
	abstract public List<SchemaEntity> selectEntityLst( String schemaName ) throws SQLException;
	
	abstract protected String listSQL( String schemaName );		
	
}
