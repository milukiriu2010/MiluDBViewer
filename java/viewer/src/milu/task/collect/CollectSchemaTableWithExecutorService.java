package milu.task.collect;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;

import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

public class CollectSchemaTableWithExecutorService extends CollectSchemaAbstract 
{
	@Override
	void retrieveChildren() throws SQLException
	{
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		try
		{
			this.execRetrieveChildren(executorService);
		}
		catch ( SQLException sqlEx )
		{
			throw sqlEx;
		}
		finally
		{
			executorService.shutdown();
		}
	}
	
	private void execRetrieveChildren(ExecutorService executorService) throws SQLException
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
		final String schemaNameFinal = schemaName;
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
		List<Callable<String>> callableLst = new ArrayList<>();
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

			Callable<String> callable = ()->{
				if ( this.cancelWrap != null && this.cancelWrap.getIsCancel() == true )
				{
					return "<NULL>";
				}
				List<Map<String,String>>  definitionLst = this.objDBAbs.selectDefinition( schemaNameFinal, objName ); 
				childEntity.setDefinitionlst(definitionLst);
				System.out.println
				( 
					"schema[" + schemaNameFinal + 
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
				
				return objName;
			};
			callableLst.add(callable);
		}
		
		try
		{
			List<Future<String>> futureLst = executorService.invokeAll(callableLst);
			futureLst.forEach((future)->{
				try
				{
					this.progressInf.addProgress(progressDiv);
					this.progressInf.setMsg( schemaNameFinal + "." + future.get() );
				}
				catch ( InterruptedException intEx )
				{
					//throw new RuntimeException( intEx );
				}
				catch ( ExecutionException execEx )
				{
					//throw new RuntimeException( execEx );
				}
			});
		}
		catch ( InterruptedException intEx )
		{
			//throw intEx;
		}
	}

}
