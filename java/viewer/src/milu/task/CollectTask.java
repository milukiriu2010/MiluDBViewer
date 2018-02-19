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
						SchemaEntity rootTableEntity = SchemaEntityFactory.createInstance( SchemaEntity.SCHEMA_TYPE.ROOT_TABLE );
						this.selectTableLst( schemaEntity, rootTableEntity );
						schemaEntity.addEntity( rootTableEntity );
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
	
	private void selectTableLst( SchemaEntity schemaEntity, SchemaEntity rootTableEntity )
		throws SQLException
	{
		TableDBAbstract tableDBAbs = TableDBFactory.getInstance(this.myDBAbs);
		if ( tableDBAbs != null )
		{
			String schemaName = schemaEntity.getName();
			tableDBAbs.selectTableLst( schemaName );
			List<SchemaEntity> tableEntityLst = tableDBAbs.getTableEntityLst();
			rootTableEntity.addEntityAll( tableEntityLst );
		}
	}
	
	@Override
	protected void succeeded()
	{
		System.out.println( "Task Success." );
	}
}
