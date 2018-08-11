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

// same speed as no thread pool. umm.
public class CollectSchemaTableWithExecutorService extends CollectSchemaAbstract 
{
	@Override
	void retrieveChildren() throws SQLException
	{
		ExecutorService executorService = Executors.newFixedThreadPool(10);
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
			
			if ( this.cancelWrap != null && this.cancelWrap.getIsCancel() == true )
			{
				break;
			}

			Callable<String> callable = ()->{
				this.progressInf.addProgress(progressDiv);
				this.progressInf.setMsg( schemaNameFinal + "." + objName );
				if ( this.cancelWrap != null && this.cancelWrap.getIsCancel() == true )
				{
					return objName;
				}
				List<Map<String,String>>  definitionLst = this.objDBAbs.selectDefinition( schemaNameFinal, objName ); 
				childEntity.setDefinitionlst(definitionLst);
				System.out.println
				( 
					"schema[" + schemaNameFinal + 
					"]schemaType[" + schemaType + 
					"]obj[" + objName + 
					"]Thread[" + Thread.currentThread().getName() + 
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
				
				/*
				// ---------------------------------------
				// -[ROOT]
				//   -[SCHEMA]
				//     -[ROOT_TABLE]
				//       -[TABLE]
				//         -[INDEX_ROOT]
				//           -[INDEX]    => add
				// ---------------------------------------
				ObjDBFactory objDBFactory = AbsDBFactory.getFactory( AbsDBFactory.FACTORY_TYPE.INDEX );
				if ( objDBFactory == null )
				{
					return null;
				}
				ObjDBInterface objDBInf = objDBFactory.getInstance(this.myDBAbs);
				if ( objDBInf == null )
				{
					return null;
				}
				
				List<SchemaEntity> indexEntityLst = ((IndexDBAbstract)objDBInf).selectEntityLst( schemaNameFinal, objName );
				childEntity.addEntityAll(indexEntityLst);
				*/
				
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
					String objName = future.get();
					System.out.println( "Future: " + objName );
				}
				catch ( InterruptedException intEx )
				{
					//throw new RuntimeException( intEx );
					intEx.printStackTrace();
				}
				catch ( ExecutionException execEx )
				{
					//throw new RuntimeException( execEx );
					execEx.printStackTrace();
				}
			});
		}
		catch ( InterruptedException intEx )
		{
			//throw intEx;
			intEx.printStackTrace();
		}
	}

}
