package milu.db.proc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import milu.db.MyDBAbstract;
import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

abstract public class ProcDBAbstract 
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
		List<SchemaEntity>  procEntityLst = new ArrayList<>();
		for ( Map<String,String> proc : this.procLst )
		{
			SchemaEntity eachFuncEntity = SchemaEntityFactory.createInstance( proc.get("procName"), SchemaEntity.SCHEMA_TYPE.PROC );
			String strStatus = proc.get("status");
			if ( strStatus != null && "INVALID".equals(strStatus) )
			{
				eachFuncEntity.setState( SchemaEntity.STATE.INVALID );
			}
			procEntityLst.add( eachFuncEntity );
		}
		return procEntityLst;
	}	
	*/
	abstract public List<SchemaEntity> selectEntityLst( String schemaName ) throws SQLException;
	
	abstract protected String listSQL( String schemaName );

	// Source of Procedure
	abstract public String getSRC( String schemaName, String procName ) throws SQLException;

}
