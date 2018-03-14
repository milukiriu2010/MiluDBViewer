package milu.db.fk;

import milu.db.MyDBAbstract;
import milu.db.abs.ObjDBInterface;
import milu.db.abs.ObjDBFactory;

import milu.db.MyDBMySQL;
import milu.db.MyDBOracle;

public class FKDBFactory implements ObjDBFactory 
{
	@Override
	public ObjDBInterface getInstance( MyDBAbstract myDBAbs ) 
	{
		FKDBAbstract fkDBAbs = null;
		if ( myDBAbs instanceof MyDBMySQL )
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
