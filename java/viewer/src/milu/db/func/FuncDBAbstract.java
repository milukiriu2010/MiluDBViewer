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
	
	public void setMyDBAbstract( MyDBAbstract myDBAbs )
	{
		this.myDBAbs = myDBAbs;
	}
	/*
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
	*/
	
	// select Function List
	abstract public List<SchemaEntity> selectEntityLst( String schemaName ) throws SQLException;
	
	// SQL for Function List
	abstract protected String listSQL( String schemaName );
	
	// Source of Function
	abstract public String getSRC( String schemaName, String funcName ) throws SQLException;

}
