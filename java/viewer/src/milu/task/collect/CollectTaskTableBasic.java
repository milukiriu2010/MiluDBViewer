package milu.task.collect;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javafx.concurrent.Task;
import milu.db.MyDBAbstract;
import milu.db.obj.abs.AbsDBFactory;
import milu.db.obj.abs.ObjDBFactory;
import milu.db.obj.abs.ObjDBInterface;
import milu.db.obj.abs.AbsDBFactory.FACTORY_TYPE;
import milu.entity.schema.SchemaEntity;
import milu.main.MainController;
import milu.task.ProgressInterface;

public class CollectTaskTableBasic extends Task<Exception> 
	implements 
		ProgressInterface, 
		CollectTaskInterface 
{
	private final double MAX = 100.0;
	
	private MainController mainCtrl = null;
	
	private MyDBAbstract   myDBAbs  = null;
	
	private double         progress = 0.0;
	
	// CollectTaskInterface
	@Override
	public void setAbsDBFactory(FACTORY_TYPE factoryType) 
	{
		// TODO Auto-generated method stub

	}

	// CollectTaskInterface
	@Override
	public void setCollectDataType(CollectDataType dataType) 
	{
		// TODO Auto-generated method stub

	}

	// CollectTaskInterface
	@Override
	public void setMainController(MainController mainCtrl) 
	{
		this.mainCtrl = mainCtrl;
	}

	// CollectTaskInterface
	@Override
	public void setMyDBAbstract(MyDBAbstract myDBAbs) 
	{
		this.myDBAbs = myDBAbs;
	}

	// CollectTaskInterface
	@Override
	public void setSelectedSchemaEntity(SchemaEntity selectedSchemaEntity) 
	{
		// TODO Auto-generated method stub

	}
	// Task
	@Override
	protected Exception call() throws Exception 
	{
		Exception    taskEx = null;
		System.out.println( "Task start." );
		long startTime = System.nanoTime();
		Map<AbsDBFactory.FACTORY_TYPE,SchemaEntity.SCHEMA_TYPE>  factorySchemaMap = new LinkedHashMap<>();
		factorySchemaMap.put( AbsDBFactory.FACTORY_TYPE.TABLE            , SchemaEntity.SCHEMA_TYPE.ROOT_TABLE );
		try
		{
			this.setProgress(0.0);
			
			SchemaEntity rootEntity = this.myDBAbs.getSchemaRoot();
			// Start to retrieve, if no child objects, 
			if ( rootEntity.getEntityLst().size() != 0 )
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
			rootEntity.addEntityAll(schemaEntityLst);
			int schemaEntityLstSize = schemaEntityLst.size();
			for ( int i = 0; i < schemaEntityLstSize; i++ )
			{
				SchemaEntity schemaEntity = schemaEntityLst.get(i);
				
				double assignedSize = MAX/schemaEntityLstSize/factorySchemaMap.size();
				factorySchemaMap.forEach
				( 
					(factoryType,schemaType)->
					{
						CollectSchemaFactoryAbstract csfAbs = CollectSchemaFactoryCreator.createFactory( CollectSchemaFactoryCreator.FACTORY_TYPE.CREATE_ME );
						CollectSchemaAbstract csAbs = 
							csfAbs.createInstance( factoryType, schemaType, this.mainCtrl, this.myDBAbs, schemaEntity, null, this, assignedSize );
						
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

	// ProgressInterface
	@Override
	synchronized public void addProgress( double addpos )
	{
		this.progress += addpos;
		this.updateProgress( this.progress, MAX );
	}

	// ProgressInterface
	@Override
	synchronized public void setProgress( double pos )
	{
		this.progress = pos;
		this.updateProgress( this.progress, MAX );
	}

	// ProgressInterface
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
}
