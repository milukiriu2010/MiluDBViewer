package milu.db.fk;

import java.util.List;
import java.util.Map;
import java.sql.SQLException;

import milu.db.MyDBAbstract;
import milu.db.abs.ObjDBInterface;
import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityEachFK;

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

	// columns(FK/src)
	// [KEY]
	//   column name
	// [VALUE]
	//   position
	abstract public Map<String, String> selectSrcColumnMap( SchemaEntityEachFK fkEntity ) throws SQLException;

	// columns(FK/dst)
	// [KEY]
	//   column name
	// [VALUE]
	//   position
	abstract public Map<String, String> selectDstColumnMap( SchemaEntityEachFK fkEntity ) throws SQLException;
	
	@Override
	public List<Map<String,String>> selectDefinition( String schemaName, String objName ) throws SQLException
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String getSRC( String schemaName, String objName ) throws SQLException
	{
		throw new UnsupportedOperationException();
	}
}
