package milu.task.collect;

import milu.db.MyDBAbstract;
import milu.main.MainController;
import milu.entity.schema.SchemaEntity;

public interface TaskInterface 
{
	public void setMainController( MainController mainCtrl );
	public void setMyDBAbstract( MyDBAbstract myDBAbs );
	public void setParentSchemaEntity( SchemaEntity parentSchemaEntity );
}
