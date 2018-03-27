package milu.db.packagedef;

import milu.db.abs.ObjDBInterface;
import milu.db.abs.ObjDBFactory;

import milu.db.MyDBAbstract;
import milu.db.MyDBOracle;

public class PackageDefDBFactory implements ObjDBFactory 
{
	@Override
	public ObjDBInterface getInstance( MyDBAbstract myDBAbs )
	{
		PackageDefDBAbstract packageDefDBAbs = null;
		if ( myDBAbs instanceof MyDBOracle )
		{
			packageDefDBAbs = new PackageDefDBOracle();
		}
		else
		{
			return null;
		}
		
		packageDefDBAbs.setMyDBAbstract( myDBAbs );
		return packageDefDBAbs;
	}
}
