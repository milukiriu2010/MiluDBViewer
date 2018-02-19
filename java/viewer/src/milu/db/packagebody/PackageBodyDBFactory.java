package milu.db.packagebody;

import milu.db.MyDBAbstract;
import milu.db.MyDBOracle;

public class PackageBodyDBFactory 
{
	public static PackageBodyDBAbstract getInstance( MyDBAbstract myDBAbs )
	{
		PackageBodyDBAbstract packageBodyDBAbs = null;
		if ( myDBAbs instanceof MyDBOracle )
		{
			packageBodyDBAbs = new PackageBodyDBOracle();
		}
		else
		{
			return null;
		}
		
		packageBodyDBAbs.setMyDBAbstract( myDBAbs );
		return packageBodyDBAbs;		
	}
}
