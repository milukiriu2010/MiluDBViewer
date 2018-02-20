package milu.task;

import java.sql.SQLException;
import java.util.List;

import javafx.concurrent.Task;

import milu.db.MyDBAbstract;
import milu.db.func.FuncDBFactory;
import milu.db.mateview.MaterializedViewDBFactory;
import milu.db.packagebody.PackageBodyDBFactory;
import milu.db.packagedef.PackageDefDBFactory;
import milu.db.proc.ProcDBFactory;
import milu.db.schema.SchemaDBAbstract;
import milu.db.schema.SchemaDBFactory;
import milu.db.sequence.SequenceDBFactory;
import milu.db.table.TableDBAbstract;
import milu.db.table.TableDBFactory;
import milu.db.type.TypeDBFactory;
import milu.db.view.ViewDBFactory;
import milu.db.sysview.SystemViewDBFactory;
import milu.db.trigger.TriggerDBFactory;
import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;
import milu.gui.view.DBView;

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
			this.updateProgress( progress, MAX );
			
			SchemaEntity schemaRoot = myDBAbs.getSchemaRoot();
			if ( schemaRoot.getEntityLst().size() == 0 )
			{
				System.out.println( "Schema retriving..." );
				SchemaDBAbstract schemaDBAbs = SchemaDBFactory.getInstance( myDBAbs );
				if ( schemaDBAbs != null )
				{
					// select Schema list
					schemaDBAbs.selectSchemaLst();
					List<SchemaEntity> schemaEntityLst = schemaDBAbs.getSchemaEntityLst();
					int schemaEntityLstSize = schemaEntityLst.size();
					for ( int i = 0; i < schemaEntityLstSize; i++ )
					{
						SchemaEntity schemaEntity = schemaEntityLst.get(i);
						// select Table list 
						this.selectTableLst( schemaEntity );
						
						this.updateProgress( i*100/schemaEntityLstSize, MAX );
					}
					
					schemaRoot.addEntityAll(schemaEntityLst);
					//System.out.println( "schemaEntityLst:size:" + schemaEntityLst.size() );
				}
			}
			else
			{
				System.out.println( "Schema already retrieved." );
			}
			
			
			// delete later
			{
				SchemaEntity schemaRoot2 = myDBAbs.getSchemaRoot();
				System.out.println( "schemaEntityLst:size:" + schemaRoot2.getEntityLst().size() );
			}
			
			System.out.println( "Task finish." );
			
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
	private void selectTableLst( SchemaEntity schemaEntity )
		throws SQLException
	{
		TableDBAbstract tableDBAbs = TableDBFactory.getInstance(this.myDBAbs);
		if ( tableDBAbs != null )
		{
			// ---------------------------------------
			// -[ROOT]
			//   -[SCHEMA]
			//     -[ROOT_TABLE]             => add
			//     -[ROOT_VIEW]              => add
			//     -[ROOT_MATERIALIZED_VIEW] => add
			//	   -[ROOT_SYSTEM_VIEW]       => add
			//     -[ROOT_FUNC]              => add
			//     -[ROOT_PROC]              => add
			//     -[ROOT_PACKAGE_DEF]       => add
			//     -[ROOT_PACKAGE_BODY]      => add
			//     -[ROOT_TYPE]              => add
			//     -[ROOT_TRIGGER]           => add			
			//     -[ROOT_SEQUENCE]          => add
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
			if ( FuncDBFactory.getInstance(myDBAbs) != null )
			{
				SchemaEntity rootFuncEntity             = SchemaEntityFactory.createInstance( SchemaEntity.SCHEMA_TYPE.ROOT_FUNC );
				schemaEntity.addEntity( rootFuncEntity );
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
			if ( SequenceDBFactory.getInstance(myDBAbs) != null )
			{
				SchemaEntity rootSequenceEntity     = SchemaEntityFactory.createInstance( SchemaEntity.SCHEMA_TYPE.ROOT_SEQUENCE );
				schemaEntity.addEntity( rootSequenceEntity );
			}
			
			// ---------------------------------------
			// -[ROOT]
			//   -[SCHEMA]
			//     -[ROOT_TABLE]
			//       -[TABLE]    => add
			// ---------------------------------------
			String schemaName = schemaEntity.getName();
			tableDBAbs.selectTableLst( schemaName );
			List<SchemaEntity> tableEntityLst = tableDBAbs.getTableEntityLst();
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
	}
	
	@Override
	protected void succeeded()
	{
		System.out.println( "Task Success." );
	}
}
