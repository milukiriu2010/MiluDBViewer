package milu.ctrl.sqlgenerate;

import milu.db.MyDBAbstract;
import milu.entity.schema.SchemaEntity;

public class GenerateSQLDelete extends GenerateSQLAbstract 
{

	@Override
	public String generate(SchemaEntity selectedEntity, MyDBAbstract myDBAbs ) 
	{
		String name = selectedEntity.getName();
		String schemaName = this.getSchema(selectedEntity,myDBAbs);
		
		// DELETE FROM USERS
		String strSQL = "DELETE FROM "+((schemaName == null) ? "":schemaName+"." )+name;
		
		return strSQL;
	}

}
