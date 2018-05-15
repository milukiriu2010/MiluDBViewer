package milu.task.collect;

import milu.db.MyDBAbstract;
import milu.db.obj.abs.AbsDBFactory;
import milu.entity.schema.SchemaEntity;
import milu.main.MainController;
import milu.task.ProgressInterface;

public class CollectSchemaFactorySetMe extends CollectSchemaFactoryAbstract {

	@Override
	CollectSchemaAbstract createInstance
	( 
			AbsDBFactory.FACTORY_TYPE  factoryType,
			SchemaEntity.SCHEMA_TYPE   schemaType,
			MainController             mainCtrl,
			MyDBAbstract               myDBAbs,
			SchemaEntity               schemaEntity,
			SchemaEntity               meEntity,
			ProgressInterface          progressInf,
			double                     assignedSize
	)
	{
		CollectSchemaAbstract csAbs = null;
		//System.out.println( "schema[" + schemaEntity.getName() + "]factory[" + factoryType + "]" );
		if ( AbsDBFactory.FACTORY_TYPE.TABLE.equals(factoryType) )
		{
			csAbs = new CollectSchemaTable();
		}
		else if (
			( AbsDBFactory.FACTORY_TYPE.VIEW.equals(factoryType) ) ||
			( AbsDBFactory.FACTORY_TYPE.MATERIALIZED_VIEW.equals(factoryType) ) ||
			( AbsDBFactory.FACTORY_TYPE.SYSTEM_VIEW.equals(factoryType) )
		)
		{
			csAbs = new CollectSchemaDef();
		}
		else
		{
			csAbs = new CollectSchemaSkip();
		}
		
		csAbs.setSchemaType(schemaType);
		csAbs.setMainController(mainCtrl);
		csAbs.setMyDBAbstract(myDBAbs);
		csAbs.setSchemaEntity(schemaEntity);
		csAbs.setProgressInterface(progressInf);
		csAbs.setAssignedSize(assignedSize);
		csAbs.setMeEntity(factoryType,meEntity);
		return csAbs;
	}

}
