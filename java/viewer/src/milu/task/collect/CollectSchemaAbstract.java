package milu.task.collect;

import java.sql.SQLException;

import milu.db.MyDBAbstract;
import milu.db.obj.abs.AbsDBFactory;
import milu.db.obj.abs.ObjDBFactory;
import milu.db.obj.abs.ObjDBInterface;
import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;
import milu.main.MainController;
import milu.task.CancelWrapper;
import milu.task.ProgressInterface;

abstract class CollectSchemaAbstract 
{
	protected SchemaEntity.SCHEMA_TYPE   schemaType  = null;
	
	protected MainController  mainCtrl = null;
	
	// ----------------------------------------
	// SchemaEntityEachSchema
	// ----------------------------------------
	// -[ROOT]
	//   -[SCHEMA] <=
	// ----------------------------------------
	protected SchemaEntity    schemaEntity = null;
	
	
	// ---------------------------------------
	// -[ROOT]
	//   -[SCHEMA]
	//     -[ROOT_TABLE]  <=
	//     -[ROOT_VIEW]   <=
	// ---------------------------------------
	protected SchemaEntity    meEntity     = null;
	
	protected ObjDBInterface  objDBAbs     = null;
	
	protected MyDBAbstract    myDBAbs      = null;
	
	protected ProgressInterface  progressInf = null;
	
	protected double          assignedSize = 0.0;
	
	protected CancelWrapper   cancelWrap   = null;
	
	void setSchemaType( SchemaEntity.SCHEMA_TYPE schemaType )
	{
		this.schemaType = schemaType;
	}
	
	void setMainController( MainController mainCtrl )
	{
		this.mainCtrl = mainCtrl;
	}
	
	void setSchemaEntity( SchemaEntity schemaEntity )
	{
		this.schemaEntity = schemaEntity;
	}
	
	void setMeEntity( AbsDBFactory.FACTORY_TYPE factoryType, SchemaEntity meEntity )
	{
		ObjDBFactory objDBFactory = AbsDBFactory.getFactory(factoryType);
		if ( objDBFactory == null )
		{
			//System.out.println( "createMeEntity:factory:null" );
			return;
		}
		this.objDBAbs = objDBFactory.getInstance(this.myDBAbs);
		if ( this.objDBAbs == null )
		{
			//System.out.println( "createMeEntity:obj:null" );
			return;
		}
		this.meEntity = meEntity;
	}
	
	void setMyDBAbstract( MyDBAbstract myDBAbs )
	{
		this.myDBAbs = myDBAbs;
	}
	
	void setProgressInterface( ProgressInterface progressInf )
	{
		this.progressInf = progressInf;
	}
	
	void setAssignedSize( double assignedSize )
	{
		this.assignedSize = assignedSize;
	}
	
	// ---------------------------------------
	// -[ROOT]
	//   -[SCHEMA]
	//     -[ROOT_TABLE] => me
	// ---------------------------------------
	// factoryType
	//   AbsDBFactory.FACTORY_TYPE.TABLE
	//   AbsDBFactory.FACTORY_TYPE.VIEW
	void createMeEntity( AbsDBFactory.FACTORY_TYPE factoryType )
	{
		ObjDBFactory objDBFactory = AbsDBFactory.getFactory(factoryType);
		if ( objDBFactory == null )
		{
			//System.out.println( "createMeEntity:factory:null" );
			return;
		}
		this.objDBAbs = objDBFactory.getInstance(this.myDBAbs);
		if ( this.objDBAbs == null )
		{
			//System.out.println( "createMeEntity:obj:null" );
			return;
		}
		// schemaType
		//   SchemaEntity.SCHEMA_TYPE.ROOT_TABLE
		//   SchemaEntity.SCHEMA_TYPE.ROOT_VIEW		
		this.meEntity = SchemaEntityFactory.createInstance( this.schemaType, this.mainCtrl );
		// ----------------------------------------
		// SchemaEntityEachSchema
		// ----------------------------------------
		// -[ROOT]
		//   -[SCHEMA] 
		// ----------------------------------------
		this.schemaEntity.addEntity(this.meEntity);
	}
	
	void setCancelWrapper( CancelWrapper cancelWrap )
	{
		this.cancelWrap = cancelWrap;
	}
	
	// ---------------------------------------
	// -[ROOT]
	//   -[SCHEMA]
	//     -[ROOT_TABLE]
	//       -[TABLE]    => add
	// ---------------------------------------
	abstract void retrieveChildren() throws SQLException;
}
