package milu.db.aggregate;

import java.sql.SQLException;
import java.util.List;

import milu.db.MyDBAbstract;
import milu.entity.schema.SchemaEntity;

abstract public class AggregateDBAbstract 
{
	// DB Access Object
	protected MyDBAbstract  myDBAbs = null;
	
	public void setMyDBAbstract( MyDBAbstract myDBAbs )
	{
		this.myDBAbs = myDBAbs;
	}
	
	// select Aggregate List
	abstract public List<SchemaEntity> selectEntityLst( String schemaName ) throws SQLException;
	
	// SQL for Aggregate List
	abstract protected String listSQL( String schemaName );
	
	// Source of Aggregate
	abstract public String getSRC( String schemaName, String aggregateName ) throws SQLException;

}
