package milu.db.obj.packagedef;

import milu.db.MyDBAbstract;
import milu.db.MyDBOracle;
import milu.db.obj.abs.ObjDBFactory;
import milu.db.obj.abs.ObjDBInterface;

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
