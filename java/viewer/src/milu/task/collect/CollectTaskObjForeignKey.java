package milu.task.collect;

import java.util.List;
import java.sql.SQLException;

import javafx.concurrent.Task;

import milu.db.MyDBAbstract;
import milu.db.obj.abs.AbsDBFactory;
import milu.db.obj.abs.ObjDBFactory;
import milu.db.obj.abs.ObjDBInterface;
import milu.db.obj.fk.FKDBAbstract;
import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityEachFK;
import milu.entity.schema.search.SearchSchemaEntityInterface;
import milu.entity.schema.search.SearchSchemaEntityVisitorFactory;
import milu.main.MainController;

import milu.task.ProgressInterface;

public class CollectTaskObjForeignKey extends Task<Exception> 
	implements 
		ProgressInterface,
		CollectTaskInterface
{
	private final double MAX = 100.0;
	
	private AbsDBFactory.FACTORY_TYPE factoryType = null;
	
	//private MainController mainCtrl = null;
	
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
		//this.mainCtrl = mainCtrl;
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

		try
		{
			this.setProgress(0.0);
			
			if ( this.selectedSchemaEntity == null )
			{
				return null;
			}
			// Start to retrieve, if no child objects, 
			if ( this.selectedSchemaEntity.getEntityLst().size() != 0 )
			{
				return null;
			}
			
			System.out.println( "Schema retriving..." );
			ObjDBFactory objDBFactory = AbsDBFactory.getFactory( this.factoryType );
			if ( objDBFactory == null )
			{
				return null;
			}
			ObjDBInterface objDBInf = objDBFactory.getInstance(this.myDBAbs);
			if ( objDBInf == null )
			{
				return null;
			}
			
			// Search [SCHEMA]
			SearchSchemaEntityInterface searchSchemaVisitor = new SearchSchemaEntityVisitorFactory().createInstance(SchemaEntity.SCHEMA_TYPE.SCHEMA);
			this.selectedSchemaEntity.acceptParent(searchSchemaVisitor);
			SchemaEntity hitEntity = searchSchemaVisitor.getHitSchemaEntity();
			// SQLite => schemaName = null
			String schemaName = null;
			if ( hitEntity != null )
			{
				schemaName = hitEntity.getName();
			}
			
			// start retrieving each schema 
			List<SchemaEntity> fkEntityLst = objDBInf.selectEntityLst( schemaName );
			FKDBAbstract fkDBAbs = (FKDBAbstract)objDBInf;
			for ( SchemaEntity seEntity : fkEntityLst )
			{
				this.setMsg(fkEntityLst.toString());
				this.selectedSchemaEntity.addEntity( seEntity );
				SchemaEntityEachFK fkEntity = (SchemaEntityEachFK) seEntity;
				fkDBAbs.selectSrcColumnMap(fkEntity);
				fkDBAbs.selectDstColumnMap(fkEntity);
				this.addProgress( MAX/fkEntityLst.size() );
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
	
	// Task
	@Override
	protected void succeeded()
	{
		System.out.println( "Task Success." );
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
	
	// ProgressInterface
	@Override
	public void cancelProc()
	{
	}
}
