package milu.db.obj.abs;

import milu.db.obj.aggregate.AggregateDBFactory;
import milu.db.obj.fk.FKDBFactory;
import milu.db.obj.func.FuncDBFactory;
import milu.db.obj.index.IndexDBFactory;
import milu.db.obj.indexcolumn.IndexColumnDBFactory;
import milu.db.obj.mateview.MaterializedViewDBFactory;
import milu.db.obj.packagebody.PackageBodyDBFactory;
import milu.db.obj.packagedef.PackageDefDBFactory;
import milu.db.obj.proc.ProcDBFactory;
import milu.db.obj.schema.SchemaDBFactory;
import milu.db.obj.sequence.SequenceDBFactory;
import milu.db.obj.sysview.SystemViewDBFactory;
import milu.db.obj.table.TableDBFactory;
import milu.db.obj.trigger.TriggerDBFactory;
import milu.db.obj.type.TypeDBFactory;
import milu.db.obj.view.ViewDBFactory;

public class AbsDBFactory
{
	public enum FACTORY_TYPE
	{
		SCHEMA,
		TABLE,
		INDEX,
		INDEX_COLUMN,
		VIEW,
		MATERIALIZED_VIEW,
		SYSTEM_VIEW,
		FUNC,
		AGGREGATE,
		PROC,
		PACKAGE_DEF,
		PACKAGE_BODY,
		TRIGGER,
		TYPE,
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
		else if ( factoryType == FACTORY_TYPE.INDEX )
		{
			return new IndexDBFactory();
		}
		else if ( factoryType == FACTORY_TYPE.INDEX_COLUMN )
		{
			return new IndexColumnDBFactory();
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
		else if ( factoryType == FACTORY_TYPE.TYPE )
		{
			return new TypeDBFactory();
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
