package milu.db.packagedef;

import milu.db.MyDBAbstract;
import milu.db.MyDBOracle;

public class PackageDefDBFactory 
{
	public static PackageDefDBAbstract getInstance( MyDBAbstract myDBAbs )
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
