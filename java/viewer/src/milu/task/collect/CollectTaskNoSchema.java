package milu.task.collect;

import java.util.Map;
import java.util.LinkedHashMap;
import java.sql.SQLException;

import javafx.concurrent.Task;

import milu.db.MyDBAbstract;
import milu.db.obj.abs.AbsDBFactory;
import milu.entity.schema.SchemaEntity;

import milu.main.MainController;

import milu.task.ProgressInterface;

public class CollectTaskNoSchema extends Task<Exception> 
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
			double assignedSize = MAX/factorySchemaMap.size();
			factorySchemaMap.forEach
			( 
				(factoryType,schemaType)->
				{
					CollectSchemaAbstract csAbs = 
						CollectSchemaFactory.createInstance( factoryType, schemaType, this.mainCtrl, this.myDBAbs, schemaRoot, this, assignedSize );
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
				
			return null;
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