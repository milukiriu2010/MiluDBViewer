package milu.task.collect;

import java.sql.SQLException;

import milu.db.MyDBAbstract;
import milu.db.obj.abs.AbsDBFactory;
import milu.db.obj.abs.ObjDBFactory;
import milu.db.obj.abs.ObjDBInterface;
import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;
import milu.main.MainController;
import milu.task.ProgressInterface;

abstract class CollectSchemaAbstract 
{
	//protected AbsDBFactory.FACTORY_TYPE  factoryType = null;
	
	protected SchemaEntity.SCHEMA_TYPE   schemaType  = null;
	
	protected MainController  mainCtrl = null;
	
	// ---------------------------------------
	// -[ROOT]
	//   -[SCHEMA] <=
	// ---------------------------------------
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
		this.meEntity = SchemaEntityFactory.createInstance( this.schemaType, this.mainCtrl );
		this.schemaEntity.addEntity(this.meEntity);
	}
	
	// ---------------------------------------
	// -[ROOT]
	//   -[SCHEMA]
	//     -[ROOT_TABLE]
	//       -[TABLE]    => add
	// ---------------------------------------
	abstract void retrieveChildren() throws SQLException;
}
