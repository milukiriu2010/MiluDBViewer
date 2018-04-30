package milu.task.collect;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.sql.SQLException;

import javafx.concurrent.Task;

import milu.db.MyDBAbstract;
import milu.db.obj.abs.AbsDBFactory;
import milu.db.obj.abs.ObjDBFactory;
import milu.db.obj.abs.ObjDBInterface;
import milu.entity.schema.SchemaEntity;

import milu.main.MainController;

import milu.task.ProgressInterface;

public class CollectTaskBasic extends Task<Exception> 
	implements 
		ProgressInterface,
		TaskInterface
{
	private final double MAX = 100.0;
	
	private MainController mainCtrl = null;
	
	private MyDBAbstract   myDBAbs  = null;
	
	private double         progress = 0.0;
	
	@Override
	public void setMainController( MainController mainCtrl )
	{
		this.mainCtrl = mainCtrl;
	}
	
	@Override
	public void setMyDBAbstract( MyDBAbstract myDBAbs )
	{
		this.myDBAbs = myDBAbs;
	}
	
	@Override
	protected Exception call() 
	{
		Exception    taskEx = null;
		System.out.println( "Task start." );
		long startTime = System.nanoTime();
		Map<AbsDBFactory.FACTORY_TYPE,SchemaEntity.SCHEMA_TYPE>  factorySchemaMap = new LinkedHashMap<>();
		factorySchemaMap.put( AbsDBFactory.FACTORY_TYPE.TABLE            , SchemaEntity.SCHEMA_TYPE.ROOT_TABLE );
		factorySchemaMap.put( AbsDBFactory.FACTORY_TYPE.VIEW             , SchemaEntity.SCHEMA_TYPE.ROOT_VIEW );
		factorySchemaMap.put( AbsDBFactory.FACTORY_TYPE.SYSTEM_VIEW      , SchemaEntity.SCHEMA_TYPE.ROOT_SYSTEM_VIEW );
		factorySchemaMap.put( AbsDBFactory.FACTORY_TYPE.MATERIALIZED_VIEW, SchemaEntity.SCHEMA_TYPE.ROOT_MATERIALIZED_VIEW );
		factorySchemaMap.put( AbsDBFactory.FACTORY_TYPE.FUNC             , SchemaEntity.SCHEMA_TYPE.ROOT_FUNC );
		factorySchemaMap.put( AbsDBFactory.FACTORY_TYPE.AGGREGATE        , SchemaEntity.SCHEMA_TYPE.ROOT_AGGREGATE );
		factorySchemaMap.put( AbsDBFactory.FACTORY_TYPE.PROC             , SchemaEntity.SCHEMA_TYPE.ROOT_PROC );
		factorySchemaMap.put( AbsDBFactory.FACTORY_TYPE.PACKAGE_DEF      , SchemaEntity.SCHEMA_TYPE.ROOT_PACKAGE_DEF );
		factorySchemaMap.put( AbsDBFactory.FACTORY_TYPE.PACKAGE_BODY     , SchemaEntity.SCHEMA_TYPE.ROOT_PACKAGE_BODY );
		factorySchemaMap.put( AbsDBFactory.FACTORY_TYPE.TYPE             , SchemaEntity.SCHEMA_TYPE.ROOT_TYPE );
		factorySchemaMap.put( AbsDBFactory.FACTORY_TYPE.TRIGGER          , SchemaEntity.SCHEMA_TYPE.ROOT_TRIGGER );
		factorySchemaMap.put( AbsDBFactory.FACTORY_TYPE.SEQUENCE         , SchemaEntity.SCHEMA_TYPE.ROOT_SEQUENCE );
		factorySchemaMap.put( AbsDBFactory.FACTORY_TYPE.FOREIGN_KEY      , SchemaEntity.SCHEMA_TYPE.ROOT_ER );

		try
		{
			this.setProgress(0.0);
			
			SchemaEntity schemaRoot = this.myDBAbs.getSchemaRoot();
			// Start to retrieve, if no child objects, 
			if ( schemaRoot.getEntityLst().size() != 0 )
			{
				return null;
			}
			
			System.out.println( "Schema retriving..." );
			ObjDBFactory objDBFactory = AbsDBFactory.getFactory( AbsDBFactory.FACTORY_TYPE.SCHEMA );
			if ( objDBFactory == null )
			{
				return null;
			}
			ObjDBInterface objDBAbs = objDBFactory.getInstance(this.myDBAbs);
			if ( objDBAbs == null )
			{
				return null;
			}
			// start retrieving each schema 
			List<SchemaEntity> schemaEntityLst = objDBAbs.selectEntityLst( null );
			schemaRoot.addEntityAll(schemaEntityLst);
			int schemaEntityLstSize = schemaEntityLst.size();
			for ( int i = 0; i < schemaEntityLstSize; i++ )
			{
				SchemaEntity schemaEntity = schemaEntityLst.get(i);
				// select Table list
				//this.selectTableLst( schemaEntity, i, schemaEntityLstSize );
				
				double assignedSize = MAX/schemaEntityLstSize/factorySchemaMap.size();
				factorySchemaMap.forEach
				( 
					(factoryType,schemaType)->
					{
						CollectSchemaAbstract csAbs = 
							CollectSchemaFactory.createInstance( factoryType, schemaType, this.mainCtrl, this.myDBAbs, schemaEntity, this, assignedSize );
						try
						{
							csAbs.retrieveChildren();
						}
						catch ( SQLException sqlEx )
						{
							throw new RuntimeException( sqlEx );
						}
					}
				);
				
				this.setProgress(i*100/schemaEntityLstSize);
			}
			
			return null;
		}
		catch ( SQLException sqlEx )
		{
			taskEx = sqlEx;
			return taskEx;
		}
		catch ( Exception ex )
		{
			taskEx = ex;
			return taskEx;
		}
		finally
		{
			this.setProgress(MAX);
			this.updateValue(taskEx);
			this.setMsg("");
			long endTime = System.nanoTime();
			System.out.println( "Task finish:" + String.format( "%,d nsec",(endTime-startTime) ) );
		}
	}
	
	@Override
	synchronized public void addProgress( double addpos )
	{
		this.progress += addpos;
		this.updateProgress( this.progress, MAX );
	}
	
	@Override
	synchronized public void setProgress( double pos )
	{
		this.progress = pos;
		this.updateProgress( this.progress, MAX );
	}
	
	@Override
	synchronized public void setMsg( String msg )
	{
		// it isn't effective
		//if ( this.progress < MAX )
		
		if ( "".equals(msg) == false )
		{
			String strMsg = String.format( "Loaded(%.3f%%) %s", this.progress, msg );
			this.updateMessage(strMsg);
		}
		else
		{
			this.updateMessage("");
		}
	}
	
	@Override
	protected void succeeded()
	{
		System.out.println( "Task Success." );
	}
}
