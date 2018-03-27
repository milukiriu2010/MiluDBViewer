package milu.db.abs;

import milu.db.schema.SchemaDBFactory;
import milu.db.table.TableDBFactory;
import milu.db.view.ViewDBFactory;
import milu.db.mateview.MaterializedViewDBFactory;
import milu.db.sysview.SystemViewDBFactory;
import milu.db.func.FuncDBFactory;
import milu.db.aggregate.AggregateDBFactory;
import milu.db.proc.ProcDBFactory;
import milu.db.packagedef.PackageDefDBFactory;
import milu.db.packagebody.PackageBodyDBFactory;
import milu.db.trigger.TriggerDBFactory;
import milu.db.sequence.SequenceDBFactory;
import milu.db.fk.FKDBFactory;

public class AbsDBFactory
{
	public enum FACTORY_TYPE
	{
		SCHEMA,
		TABLE,
		VIEW,
		MATERIALIZED_VIEW,
		SYSTEM_VIEW,
		FUNC,
		AGGREGATE,
		PROC,
		PACKAGE_DEF,
		PACKAGE_BODY,
		TRIGGER,
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
		else if ( factoryType == FACTORY_TYPE.VIEW )
		{
			return new ViewDBFactory();
		}
		else if ( factoryType == FACTORY_TYPE.MATERIALIZED_VIEW )
		{
			return new MaterializedViewDBFactory();
		}
		else if ( factoryType == FACTORY_TYPE.SYSTEM_VIEW )
		{
			return new SystemViewDBFactory();
		}
		else if ( factoryType == FACTORY_TYPE.FUNC )
		{
			return new FuncDBFactory();
		}
		else if ( factoryType == FACTORY_TYPE.AGGREGATE )
		{
			return new AggregateDBFactory();
		}
		else if ( factoryType == FACTORY_TYPE.PROC )
		{
			return new ProcDBFactory();
		}
		else if ( factoryType == FACTORY_TYPE.PACKAGE_DEF )
		{
			return new PackageDefDBFactory();
		}
		else if ( factoryType == FACTORY_TYPE.PACKAGE_BODY )
		{
			return new PackageBodyDBFactory();
		}
		else if ( factoryType == FACTORY_TYPE.TRIGGER )
		{
			return new TriggerDBFactory();
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
