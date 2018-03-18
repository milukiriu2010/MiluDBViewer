package milu.db.abs;

import milu.db.fk.FKDBFactory;

public class AbsDBFactory
{
	public enum FACTORY_TYPE
	{
		FOREIGN_KEY
	}
	public static ObjDBFactory getFactory( FACTORY_TYPE factoryType )
	{
		if ( factoryType == FACTORY_TYPE.FOREIGN_KEY )
		{
			return new FKDBFactory();
		}
		else
		{
			return null;
		}
	}
}
