package milu.db.packagebody;

import milu.db.abs.ObjDBInterface;
import milu.db.abs.ObjDBFactory;

import milu.db.MyDBAbstract;
import milu.db.MyDBOracle;

public class PackageBodyDBFactory implements ObjDBFactory
{
	public ObjDBInterface getInstance( MyDBAbstract myDBAbs )
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
