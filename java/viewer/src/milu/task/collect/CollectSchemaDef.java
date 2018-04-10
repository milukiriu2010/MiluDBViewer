package milu.task.collect;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import milu.entity.schema.SchemaEntity;

public class CollectSchemaDef extends CollectSchemaAbstract 
{
	@Override
	void retrieveChildren() throws SQLException
	{
		if ( this.objDBAbs == null )
		{
			return;
		}
		String schemaName = this.schemaEntity.getName();
		List<SchemaEntity> entityLst = this.objDBAbs.selectEntityLst( schemaName );
		this.meEntity.addEntityAll(entityLst);
		int entityLstSize = entityLst.size();
		double progressDiv = this.assignedSize/entityLstSize;
		for ( int i = 0; i < entityLstSize; i++ )
		{
			SchemaEntity childEntity = entityLst.get(i);
			String objName = childEntity.getName();
			List<Map<String,String>>  definitionLst = this.objDBAbs.selectDefinition( schemaName, objName ); 
			childEntity.setDefinitionlst(definitionLst);
			System.out.println
			( 
				"schema[" + schemaName + 
				"]schemaType[" + schemaType + 
				"]obj[" + objName + 
				"]assignedSize[" + this.assignedSize + 
				"]progressDiv[" + progressDiv + 
				"]" 
			);
			this.progressInf.addProgress(progressDiv);
			this.progressInf.setMsg( schemaName + "." + objName );
		}

	}

}
