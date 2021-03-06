package milu.gui.dlg.db;

import javafx.scene.control.Dialog;
import milu.db.MyDBAbstract;
import milu.db.MyDBPostgres;
import milu.main.MainController;
import milu.db.MyDBMySQL;
import milu.db.MyDBOracle;
import milu.db.MyDBCassandra;
import milu.db.MyDBSQLServer;
import milu.db.MyDBSQLite;
import milu.db.MyDBMongo;

class UrlPaneFactory implements AbsPaneFactory 
{
	@Override
	public UrlPaneAbstract createPane( Dialog<?> dlg, MainController mainCtrl, MyDBAbstract myDBAbs )
	{
		UrlPaneAbstract urlPaneAbs = null;
		
		if ( myDBAbs instanceof MyDBPostgres )
		{
			urlPaneAbs = new UrlPaneBasic();
		}
		else if ( myDBAbs instanceof MyDBMySQL )
		{
			urlPaneAbs = new UrlPaneBasic();
		}
		else if ( myDBAbs instanceof MyDBOracle )
		{
			urlPaneAbs = new UrlPaneOracle();
		}
		else if ( myDBAbs instanceof MyDBCassandra )
		{
			urlPaneAbs = new UrlPaneCassandra();
		}
		else if ( myDBAbs instanceof MyDBSQLServer )
		{
			urlPaneAbs = new UrlPaneBasic();
		}
		else if ( myDBAbs instanceof MyDBSQLite )
		{
			urlPaneAbs = new UrlPaneBasicFile();
		}
		else if ( myDBAbs instanceof MyDBMongo )
		{
			urlPaneAbs = new UrlPaneBasic();
		}
		else
		{
			urlPaneAbs = new UrlPaneGeneral();
		}
		urlPaneAbs.createPane( dlg, mainCtrl, myDBAbs );
		return urlPaneAbs;
	}
}
