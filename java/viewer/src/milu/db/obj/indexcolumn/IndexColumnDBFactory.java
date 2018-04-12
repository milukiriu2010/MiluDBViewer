package milu.db.obj.indexcolumn;

import milu.db.MyDBAbstract;
import milu.db.MyDBPostgres;
import milu.db.obj.abs.ObjDBFactory;
import milu.db.obj.abs.ObjDBInterface;
import milu.db.MyDBMySQL;
import milu.db.MyDBOracle;
import milu.db.MyDBCassandra;

public class IndexColumnDBFactory implements ObjDBFactory
{
	public ObjDBInterface getInstance( MyDBAbstract myDBAbs )
	{
		IndexColumnDBAbstract indexColumnDBAbs = null;
		
		if ( myDBAbs instanceof MyDBPostgres )
		{
			indexColumnDBAbs = new IndexColumnDBPostgres();
		}
		else if ( myDBAbs instanceof MyDBMySQL )
		{
			indexColumnDBAbs = new IndexColumnDBMySQL();
		}
		else if ( myDBAbs instanceof MyDBOracle )
		{
			indexColumnDBAbs = new IndexColumnDBOracle();
		}
		else if ( myDBAbs instanceof MyDBCassandra )
		{
			indexColumnDBAbs = new IndexColumnDBCassandra();
		}
		else
		{
			return null;
		}
		
		indexColumnDBAbs.setMyDBAbstract(myDBAbs);
		return indexColumnDBAbs;
	}
}

