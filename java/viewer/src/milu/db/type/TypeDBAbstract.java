package milu.db.type;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import milu.db.MyDBAbstract;
import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

abstract public class TypeDBAbstract 
{
	// DB Access Object
	protected MyDBAbstract  myDBAbs = null;
	
	// Type List
	protected List<Map<String,String>> typeLst = new ArrayList<>();
	
	public void setMyDBAbstract( MyDBAbstract myDBAbs )
	{
		this.myDBAbs = myDBAbs;
	}
	
	protected void clear()
	{
		this.typeLst.clear();
	}
	
	public List<SchemaEntity> getEntityLst()
	{
		List<SchemaEntity>  typeEntityLst = new ArrayList<>();
		for ( Map<String,String> type : this.typeLst )
		{
			SchemaEntity eachFuncEntity = SchemaEntityFactory.createInstance( type.get("typeName"), SchemaEntity.SCHEMA_TYPE.TYPE );
			String strStatus = type.get("status");
			if ( strStatus != null && "INVALID".equals(strStatus) )
			{
				eachFuncEntity.setState( SchemaEntity.STATE.INVALID );
			}
			typeEntityLst.add( eachFuncEntity );
		}
		return typeEntityLst;
	}	
	
	abstract public void selectEntityLst( String schemaName ) throws SQLException;
	
	abstract protected String listSQL( String schemaName );

}
