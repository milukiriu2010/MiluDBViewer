package milu.db.abs;

import java.sql.SQLException;
import java.util.List;

import milu.db.MyDBAbstract;
import milu.entity.schema.SchemaEntity;

public interface ObjDBInterface 
{
	public void setMyDBAbstract( MyDBAbstract myDBAbs );
	
	public List<SchemaEntity> selectEntityLst( String schemaName ) throws SQLException;
}
