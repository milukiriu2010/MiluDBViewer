package milu.db.abs;

import milu.db.schema.SchemaDBFactory;
import milu.db.table.TableDBFactory;
import milu.db.sequence.SequenceDBFactory;
import milu.db.fk.FKDBFactory;

public class AbsDBFactory
{
	public enum FACTORY_TYPE
	{
		SCHEMA,
		TABLE,
		SEQUENCE,
		FOREIGN_KEY
	}
	
	public static ObjDBFactory getFactory( FACTORY_TYPE factoryType )
	{
		if ( factoryType == FACTORY_TYPE.SCHEMA )
		{
			return new SchemaDBFactory();
		}
		else if ( factoryType == FACTORY_TYPE.TABLE )
		{
			return new TableDBFactory();
		}
		else if ( factoryType == FACTORY_TYPE.SEQUENCE )
		{
			return new SequenceDBFactory();
		}
		else if ( factoryType == FACTORY_TYPE.FOREIGN_KEY )
		{
			return new FKDBFactory();
		}
		else
		{
			return null;
		}
	}
}
