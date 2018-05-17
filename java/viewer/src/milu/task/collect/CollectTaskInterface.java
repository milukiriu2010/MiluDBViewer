package milu.task.collect;

import milu.db.MyDBAbstract;
import milu.db.obj.abs.AbsDBFactory;
import milu.main.MainController;
import milu.entity.schema.SchemaEntity;

public interface CollectTaskInterface 
{
	public void setAbsDBFactory( AbsDBFactory.FACTORY_TYPE factoryType );
	public void setCollectDataType( CollectDataType dataType );
	public void setMainController( MainController mainCtrl );
	public void setMyDBAbstract( MyDBAbstract myDBAbs );
	public void setSelectedSchemaEntity( SchemaEntity selectedSchemaEntity );
}
