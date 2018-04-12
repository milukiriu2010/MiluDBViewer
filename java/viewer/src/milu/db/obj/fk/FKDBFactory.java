package milu.db.obj.fk;

import milu.db.MyDBAbstract;
import milu.db.MyDBPostgres;
import milu.db.obj.abs.ObjDBFactory;
import milu.db.obj.abs.ObjDBInterface;
import milu.db.MyDBMySQL;
import milu.db.MyDBOracle;

public class FKDBFactory implements ObjDBFactory 
{
	@Override
	public ObjDBInterface getInstance( MyDBAbstract myDBAbs ) 
	{
		FKDBAbstract fkDBAbs = null;
		if ( myDBAbs instanceof MyDBPostgres )
		{
			fkDBAbs = new FKDBPostgres();
		}
		else if ( myDBAbs instanceof MyDBMySQL )
		{
			fkDBAbs = new FKDBMySQL();
		}
		else if ( myDBAbs instanceof MyDBOracle )
		{
			fkDBAbs = new FKDBOracle();
		}
		else
		{
			return null;
		}
		
		fkDBAbs.setMyDBAbstract(myDBAbs);
		return fkDBAbs;
	}

}
