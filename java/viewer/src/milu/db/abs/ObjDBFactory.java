package milu.db.abs;

import milu.db.MyDBAbstract;

public interface ObjDBFactory 
{
	public ObjDBInterface getInstance( MyDBAbstract myDBAbs );
}
