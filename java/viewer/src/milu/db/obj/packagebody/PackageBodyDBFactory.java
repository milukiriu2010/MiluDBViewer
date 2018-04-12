package milu.db.obj.packagebody;

import milu.db.MyDBAbstract;
import milu.db.MyDBOracle;
import milu.db.obj.abs.ObjDBFactory;
import milu.db.obj.abs.ObjDBInterface;

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
