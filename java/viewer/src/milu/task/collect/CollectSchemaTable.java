package milu.task.collect;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

public class CollectSchemaTable extends CollectSchemaAbstract 
{
	@Override
	void retrieveChildren() throws SQLException
	{
		if ( this.objDBAbs == null )
		{
			return;
		}
		// ----------------------------------------
		// this.schemaEntity =
		//   SchemaEntityEachSchema
		// ----------------------------------------
		// -[ROOT]
		//   -[SCHEMA] <=
		// ----------------------------------------
		String schemaName = null;
		if ( this.schemaEntity != null )
		{
			schemaName = this.schemaEntity.getName();
		}
		List<SchemaEntity> entityLst = this.objDBAbs.selectEntityLst( schemaName );
		// ----------------------------------------
		// this.meEntity =
		//   SchemaEntityRootTable
		// ----------------------------------------
		// -[ROOT]
		//   -[SCHEMA]
		//     -[ROOT_TABLE]  <=
		// ----------------------------------------		
		this.meEntity.addEntityAll(entityLst);
		int entityLstSize = entityLst.size();
		double progressDiv = this.assignedSize/entityLstSize;
		for ( int i = 0; i < entityLstSize; i++ )
		{
			if ( this.cancelWrap != null && this.cancelWrap.getIsCancel() == true )
			{
				break;
			}
			// ----------------------------------------
			// childEntity =
			//   SchemaEntityEachTable
			// ----------------------------------------
			// -[ROOT]
			//   -[SCHEMA]
			//     -[ROOT_TABLE]
			//       -[TABLE]    <=
			// ----------------------------------------		
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
			
			// ---------------------------------------
			// -[ROOT]
			//   -[SCHEMA]
			//     -[ROOT_TABLE]
			//       -[TABLE]
			//         -[INDEX_ROOT] => add
			// ---------------------------------------
			SchemaEntity rootIndexEntity = SchemaEntityFactory.createInstance( SchemaEntity.SCHEMA_TYPE.ROOT_INDEX, this.mainCtrl );
			childEntity.addEntity(rootIndexEntity);
			
			this.progressInf.addProgress(progressDiv);
			this.progressInf.setMsg( schemaName + "." + objName );
		}

	}

}
