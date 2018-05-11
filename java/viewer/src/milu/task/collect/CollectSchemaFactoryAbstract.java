package milu.task.collect;

import milu.db.MyDBAbstract;
import milu.db.obj.abs.AbsDBFactory;
import milu.entity.schema.SchemaEntity;
import milu.main.MainController;
import milu.task.ProgressInterface;

abstract class CollectSchemaFactoryAbstract 
{
	abstract CollectSchemaAbstract createInstance
	( 
		AbsDBFactory.FACTORY_TYPE  factoryType,
		SchemaEntity.SCHEMA_TYPE   schemaType,
		MainController             mainCtrl,
		MyDBAbstract               myDBAbs,
		SchemaEntity               schemaEntity,
		SchemaEntity               meEntity,
		ProgressInterface          progressInf,
		double                     assignedSize
	);
}
