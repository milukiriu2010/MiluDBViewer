package milu.db.abs;

import java.util.List;
import java.util.Map;
import java.sql.SQLException;

import milu.db.MyDBAbstract;
import milu.entity.schema.SchemaEntity;

public interface ObjDBInterface 
{
	public void setMyDBAbstract( MyDBAbstract myDBAbs );
	
	public List<SchemaEntity> selectEntityLst( String objName ) throws SQLException;
	
	public List<Map<String,String>> selectDefinition( String schemaName, String objName ) throws SQLException;
	
	// Source of Function
	public String getSRC( String schemaName, String objName ) throws SQLException;
}
