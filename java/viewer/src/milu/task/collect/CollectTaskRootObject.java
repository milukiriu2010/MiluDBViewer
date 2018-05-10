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
import milu.entity.schema.search.SearchSchemaEntityInterface;
import milu.entity.schema.search.SearchSchemaEntityVisitorFactory;
import milu.main.MainController;

import milu.task.ProgressInterface;

public class CollectTaskRootObject extends Task<Exception> 
	implements 
		ProgressInterface,
		TaskInterface
{
	private final double MAX = 100.0;
	
	private AbsDBFactory.FACTORY_TYPE factoryType = null;
	
	private MainController mainCtrl = null;
	
	private MyDBAbstract   myDBAbs  = null;
	
	private double         progress = 0.0;
	
	private SchemaEntity   selectedSchemaEntity = null;
	
	@Override
	public void setAbsDBFactory( AbsDBFactory.FACTORY_TYPE factoryType )
	{
		this.factoryType = factoryType;
	}
	
	@Override
	public void setCollectDataType( CollectDataType dataType )
	{
		
	}
	
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
	public void setSelectedSchemaEntity( SchemaEntity selectedSchemaEntity )
	{
		this.selectedSchemaEntity = selectedSchemaEntity;
	}
	
	@Override
	protected Exception call() 
	{
		Exception    taskEx = null;
		System.out.println( "Task start." );
		long startTime = System.nanoTime();
		/*
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
		*/
		
		try
		{
			this.setProgress(0.0);
			
			// Start to retrieve, if no child objects, 
			if ( this.selectedSchemaEntity.getEntityLst().size() != 0 )
			{
				return null;
			}
			
			// Search [SCHEMA]
			SearchSchemaEntityInterface searchSchemaVisitor = new SearchSchemaEntityVisitorFactory().createInstance(SchemaEntity.SCHEMA_TYPE.SCHEMA);
			this.selectedSchemaEntity.acceptParent(searchSchemaVisitor);
			SchemaEntity hitEntity = searchSchemaVisitor.getHitSchemaEntity();
			
			System.out.println( "Schema retriving..." );
			
			double assignedSize = MAX;
			CollectSchemaAbstract csAbs = 
				CollectSchemaFactory.createInstance( factoryType, this.selectedSchemaEntity.getType(), this.mainCtrl, this.myDBAbs, hitEntity, this, assignedSize );
			csAbs.retrieveChildren();
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
