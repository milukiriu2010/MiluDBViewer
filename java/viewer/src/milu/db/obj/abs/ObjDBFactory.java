package milu.db.obj.abs;

import milu.db.MyDBAbstract;

public interface ObjDBFactory 
{
	public ObjDBInterface getInstance( MyDBAbstract myDBAbs );
}
