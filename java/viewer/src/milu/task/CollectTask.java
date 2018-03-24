package milu.task;

import java.util.List;
import java.util.Map;
import java.sql.SQLException;

import javafx.concurrent.Task;

import milu.db.MyDBAbstract;
import milu.db.aggregate.AggregateDBFactory;
import milu.db.mateview.MaterializedViewDBFactory;
import milu.db.packagebody.PackageBodyDBFactory;
import milu.db.packagedef.PackageDefDBFactory;
import milu.db.proc.ProcDBFactory;
import milu.db.type.TypeDBFactory;
import milu.db.view.ViewDBFactory;
import milu.db.sysview.SystemViewDBFactory;
import milu.db.trigger.TriggerDBFactory;

import milu.db.abs.AbsDBFactory;
import milu.db.abs.ObjDBFactory;
import milu.db.abs.ObjDBInterface;

import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

public class CollectTask extends Task<Double>
{
	private MyDBAbstract myDBAbs = null;
	
	public CollectTask( MyDBAbstract myDBAbs )
	{
		this.myDBAbs = myDBAbs;
	}
	
	@Override
	protected Double call() 
	{
		final double MAX = 100.0;
		double progress = 0.0;
		try
		{
			System.out.println( "Task start." );
			long startTime = System.nanoTime();
			this.updateProgress( progress, MAX );
			
			SchemaEntity schemaRoot = myDBAbs.getSchemaRoot();
			// Start to retrieve, if no child objects, 
			if ( schemaRoot.getEntityLst().size() == 0 )
			{
				System.out.println( "Schema retriving..." );
				ObjDBFactory schemaDBFactory = AbsDBFactory.getFactory( AbsDBFactory.FACTORY_TYPE.SCHEMA );
				if ( schemaDBFactory != null )
				{
					// select Schema list
					ObjDBInterface schemaDBAbs = schemaDBFactory.getInstance(myDBAbs);
					List<SchemaEntity> schemaEntityLst = schemaDBAbs.selectEntityLst( null );
					int schemaEntityLstSize = schemaEntityLst.size();
					for ( int i = 0; i < schemaEntityLstSize; i++ )
					{
						SchemaEntity schemaEntity = schemaEntityLst.get(i);
						// select Table list 
						this.selectTableLst( schemaEntity, i, schemaEntityLstSize );
						
						this.updateProgress( i*100/schemaEntityLstSize, MAX );
					}
					
					schemaRoot.addEntityAll(schemaEntityLst);
				}
			}
			// Skip retrieving, if there are child objects,
			else
			{
				System.out.println( "Schema already retrieved." );
			}
			
			
			// delete later
			{
				SchemaEntity schemaRoot2 = myDBAbs.getSchemaRoot();
				System.out.println( "schemaEntityLst:size:" + schemaRoot2.getEntityLst().size() );
			}
			
			long endTime = System.nanoTime();
			System.out.println( "Task finish:" + String.format( "%,d nsec",(endTime-startTime) ) );
			
			
			progress = MAX;
			this.updateProgress( progress, MAX );
			return progress;
		}
		catch ( SQLException sqlEx )
		{
			System.out.println( "CollectTask:SQLException." );
			progress = -2.0;
			
			this.updateProgress( progress, MAX );
			
			return progress;
		}
		catch ( Exception ex )
		{
			System.out.println( "CollectTask:Exception." );
			ex.printStackTrace();
			progress = -3.0;
			
			this.updateProgress( progress, MAX );
			
			return progress;
		}
	}
	
	// select Table List
	private void selectTableLst( SchemaEntity schemaEntity, int schemaEntityLstPos, int schemaEntityLstSize )
		throws SQLException
	{
		//TableDBAbstract tableDBAbs = TableDBFactory.getInstance(this.myDBAbs);
		ObjDBFactory tableDBFactory = AbsDBFactory.getFactory( AbsDBFactory.FACTORY_TYPE.TABLE );
		if ( tableDBFactory == null )
		{
			return;
		}
		ObjDBInterface tableDBAbs = tableDBFactory.getInstance(myDBAbs);
		if ( tableDBAbs == null )
		{
			return;
		}
		// ---------------------------------------
		// -[ROOT]
		//   -[SCHEMA]
		//     -[ROOT_TABLE]             => add
		//     -[ROOT_VIEW]              => add
		//     -[ROOT_MATERIALIZED_VIEW] => add
		//	   -[ROOT_SYSTEM_VIEW]       => add
		//     -[ROOT_FUNC]              => add
		//     -[ROOT_AGGREGATE]         => add
		//     -[ROOT_PROC]              => add
		//     -[ROOT_PACKAGE_DEF]       => add
		//     -[ROOT_PACKAGE_BODY]      => add
		//     -[ROOT_TYPE]              => add
		//     -[ROOT_TRIGGER]           => add			
		//     -[ROOT_SEQUENCE]          => add
		//     -[ROOT_ER]                => add
		// ---------------------------------------
		SchemaEntity rootTableEntity        = SchemaEntityFactory.createInstance( SchemaEntity.SCHEMA_TYPE.ROOT_TABLE );
		schemaEntity.addEntity( rootTableEntity );
		if ( ViewDBFactory.getInstance(myDBAbs) != null )
		{
			SchemaEntity rootViewEntity         = SchemaEntityFactory.createInstance( SchemaEntity.SCHEMA_TYPE.ROOT_VIEW );
			schemaEntity.addEntity( rootViewEntity );
		}
		if ( SystemViewDBFactory.getInstance(myDBAbs) != null )
		{
			SchemaEntity rootSystemViewEntity         = SchemaEntityFactory.createInstance( SchemaEntity.SCHEMA_TYPE.ROOT_SYSTEM_VIEW );
			schemaEntity.addEntity( rootSystemViewEntity );
		}
		if ( MaterializedViewDBFactory.getInstance(myDBAbs) != null )
		{
			SchemaEntity rootMaterializedViewEntity = SchemaEntityFactory.createInstance( SchemaEntity.SCHEMA_TYPE.ROOT_MATERIALIZED_VIEW );
			schemaEntity.addEntity( rootMaterializedViewEntity );
		}
		ObjDBFactory funcDBFactory = AbsDBFactory.getFactory( AbsDBFactory.FACTORY_TYPE.FUNC );
		if ( funcDBFactory.getInstance(myDBAbs) != null )
		{
			SchemaEntity rootFuncEntity             = SchemaEntityFactory.createInstance( SchemaEntity.SCHEMA_TYPE.ROOT_FUNC );
			schemaEntity.addEntity( rootFuncEntity );
		}
		if ( AggregateDBFactory.getInstance(myDBAbs) != null )
		{
			SchemaEntity rootAggregateEntity        = SchemaEntityFactory.createInstance( SchemaEntity.SCHEMA_TYPE.ROOT_AGGREGATE );
			schemaEntity.addEntity( rootAggregateEntity );
		}
		if ( ProcDBFactory.getInstance(myDBAbs) != null )
		{
			SchemaEntity rootProcEntity             = SchemaEntityFactory.createInstance( SchemaEntity.SCHEMA_TYPE.ROOT_PROC );
			schemaEntity.addEntity( rootProcEntity );
		}
		if ( PackageDefDBFactory.getInstance(myDBAbs) != null )
		{
			SchemaEntity rootPackageDefEntity   = SchemaEntityFactory.createInstance( SchemaEntity.SCHEMA_TYPE.ROOT_PACKAGE_DEF );
			schemaEntity.addEntity( rootPackageDefEntity );
		}
		if ( PackageBodyDBFactory.getInstance(myDBAbs) != null )
		{
			SchemaEntity rootPackageBodyEntity  = SchemaEntityFactory.createInstance( SchemaEntity.SCHEMA_TYPE.ROOT_PACKAGE_BODY );
			schemaEntity.addEntity( rootPackageBodyEntity );
		}
		if ( TypeDBFactory.getInstance(myDBAbs) != null )
		{
			SchemaEntity rootTypeEntity             = SchemaEntityFactory.createInstance( SchemaEntity.SCHEMA_TYPE.ROOT_TYPE );
			schemaEntity.addEntity( rootTypeEntity );
		}
		if ( TriggerDBFactory.getInstance(myDBAbs) != null )
		{
			SchemaEntity rootTriggerEntity          = SchemaEntityFactory.createInstance( SchemaEntity.SCHEMA_TYPE.ROOT_TRIGGER );
			schemaEntity.addEntity( rootTriggerEntity );
		}
		ObjDBFactory sequenceDBFactory = AbsDBFactory.getFactory( AbsDBFactory.FACTORY_TYPE.SEQUENCE );
		if ( sequenceDBFactory.getInstance(myDBAbs) != null )
		{
			SchemaEntity rootSequenceEntity     = SchemaEntityFactory.createInstance( SchemaEntity.SCHEMA_TYPE.ROOT_SEQUENCE );
			schemaEntity.addEntity( rootSequenceEntity );
		}
		ObjDBFactory fkDBFactory = AbsDBFactory.getFactory( AbsDBFactory.FACTORY_TYPE.FOREIGN_KEY );
		if ( fkDBFactory.getInstance(myDBAbs) != null )
		{
			SchemaEntity rootEREntity     = SchemaEntityFactory.createInstance( SchemaEntity.SCHEMA_TYPE.ROOT_ER );
			schemaEntity.addEntity( rootEREntity );
		}
		
		// ---------------------------------------
		// -[ROOT]
		//   -[SCHEMA]
		//     -[ROOT_TABLE]
		//       -[TABLE]    => add
		// ---------------------------------------
		String schemaName = schemaEntity.getName();
		List<SchemaEntity> tableEntityLst = tableDBAbs.selectEntityLst( schemaName );
		int tableEntityLstSize = tableEntityLst.size();
		double tableEntityLstProgressInit = schemaEntityLstPos*100/schemaEntityLstSize; 
		double tableEntityLstProgressDiv  = 100/schemaEntityLstSize;
		for ( int i = 0; i < tableEntityLstSize; i++  )
		{
			double tableEntityLstProgressNow = tableEntityLstProgressDiv*i/tableEntityLstSize+tableEntityLstProgressInit;
			
			SchemaEntity tableEntity = tableEntityLst.get(i);
			String tableName = tableEntity.getName();
			//String msg = "Loading " + schemaName + "." + tableName + "...";
			String msg = String.format( "Loading(%.3f%%) %s.%s", tableEntityLstProgressNow, schemaName, tableName );
			this.updateMessage(msg);
			List<Map<String,String>>  definitionLst = tableDBAbs.selectDefinition( schemaName, tableName ); 
			tableEntity.setDefinitionlst(definitionLst);
		}
		rootTableEntity.addEntityAll( tableEntityLst );
		
		// ---------------------------------------
		// -[ROOT]
		//   -[SCHEMA]
		//     -[ROOT_TABLE]
		//       -[TABLE]
		//         -[INDEX_ROOT] => add
		// ---------------------------------------
		for ( SchemaEntity tableEntity : tableEntityLst )
		{
			SchemaEntity rootIndexEntity = SchemaEntityFactory.createInstance( SchemaEntity.SCHEMA_TYPE.ROOT_INDEX );
			tableEntity.addEntity( rootIndexEntity );
		}
	}
	
	@Override
	protected void succeeded()
	{
		System.out.println( "Task Success." );
	}
}
