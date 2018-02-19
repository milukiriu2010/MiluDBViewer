package milu.task;

import java.sql.SQLException;
import java.util.List;

import javafx.concurrent.Task;

import milu.db.MyDBAbstract;
import milu.db.schema.SchemaDBAbstract;
import milu.db.schema.SchemaDBFactory;
import milu.db.table.TableDBAbstract;
import milu.db.table.TableDBFactory;
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
			//     -[ROOT_FUNC]              => add
			//     -[ROOT_PROC]              => add
			//     -[ROOT_PACKAGE_DEF]       => add
			//     -[ROOT_PACKAGE_BODY]      => add
			//     -[ROOT_TYPE]              => add
			//     -[ROOT_SEQUENCE]          => add
			// ---------------------------------------
			SchemaEntity rootTableEntity        = SchemaEntityFactory.createInstance( SchemaEntity.SCHEMA_TYPE.ROOT_TABLE );
			schemaEntity.addEntity( rootTableEntity );
			SchemaEntity rootViewEntity         = SchemaEntityFactory.createInstance( SchemaEntity.SCHEMA_TYPE.ROOT_VIEW );
			schemaEntity.addEntity( rootViewEntity );
			SchemaEntity rootMaterializedViewEntity = SchemaEntityFactory.createInstance( SchemaEntity.SCHEMA_TYPE.ROOT_MATERIALIZED_VIEW );
			schemaEntity.addEntity( rootMaterializedViewEntity );
			SchemaEntity rootFuncEntity             = SchemaEntityFactory.createInstance( SchemaEntity.SCHEMA_TYPE.ROOT_FUNC );
			schemaEntity.addEntity( rootFuncEntity );
			SchemaEntity rootProcEntity             = SchemaEntityFactory.createInstance( SchemaEntity.SCHEMA_TYPE.ROOT_PROC );
			schemaEntity.addEntity( rootProcEntity );
			SchemaEntity rootPackageDefEntity   = SchemaEntityFactory.createInstance( SchemaEntity.SCHEMA_TYPE.ROOT_PACKAGE_DEF );
			schemaEntity.addEntity( rootPackageDefEntity );
			SchemaEntity rootPackageBodyEntity  = SchemaEntityFactory.createInstance( SchemaEntity.SCHEMA_TYPE.ROOT_PACKAGE_BODY );
			schemaEntity.addEntity( rootPackageBodyEntity );
			SchemaEntity rootTypeEntity             = SchemaEntityFactory.createInstance( SchemaEntity.SCHEMA_TYPE.ROOT_TYPE );
			schemaEntity.addEntity( rootTypeEntity );
			SchemaEntity rootSequenceEntity     = SchemaEntityFactory.createInstance( SchemaEntity.SCHEMA_TYPE.ROOT_SEQUENCE );
			schemaEntity.addEntity( rootSequenceEntity );
			
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
