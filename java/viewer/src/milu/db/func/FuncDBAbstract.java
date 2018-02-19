package milu.db.func;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import milu.db.MyDBAbstract;
import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

abstract public class FuncDBAbstract 
{
	// DB Access Object
	protected MyDBAbstract  myDBAbs = null;
	
	// Function List
	protected List<Map<String,String>> funcLst = new ArrayList<>();
	
	public void setMyDBAbstract( MyDBAbstract myDBAbs )
	{
		this.myDBAbs = myDBAbs;
	}
	
	protected void clear()
	{
		this.funcLst.clear();
	}
	
	public List<SchemaEntity> getEntityLst()
	{
		List<SchemaEntity>  funcEntityLst = new ArrayList<>();
		for ( Map<String,String> func : this.funcLst )
		{
			SchemaEntity eachFuncEntity = SchemaEntityFactory.createInstance( func.get("funcName"), SchemaEntity.SCHEMA_TYPE.FUNC );
			String strStatus = func.get("status");
			if ( strStatus != null && "INVALID".equals(strStatus) )
			{
				eachFuncEntity.setState( SchemaEntity.STATE.INVALID );
			}
			funcEntityLst.add( eachFuncEntity );
		}
		return funcEntityLst;
	}	
	
	abstract public void selectEntityLst( String schemaName ) throws SQLException;
	
	abstract protected String listSQL( String schemaName );

}
