package milu.gui.dlg.db;

import javafx.scene.control.Dialog;
import milu.db.MyDBAbstract;
import milu.db.MyDBPostgres;
import milu.main.MainController;
import milu.db.MyDBMySQL;
import milu.db.MyDBOracle;
import milu.db.MyDBCassandra;

class UrlPaneFactory implements PaneFactory 
{
	@Override
	public UrlPaneAbstract createPane( Dialog<?> dlg, MainController mainCtrl, MyDBAbstract myDBAbs )
	{
		UrlPaneAbstract urlPaneAbs = null;
		
		if ( myDBAbs instanceof MyDBPostgres )
		{
			urlPaneAbs = new UrlPanePostgres();
		}
		else if ( myDBAbs instanceof MyDBMySQL )
		{
			urlPaneAbs = new UrlPaneMySQL();
		}
		else if ( myDBAbs instanceof MyDBOracle )
		{
			urlPaneAbs = new UrlPaneOracle();
		}
		else if ( myDBAbs instanceof MyDBCassandra )
		{
			urlPaneAbs = new UrlPaneCassandra();
		}
		else
		{
			urlPaneAbs = new UrlPaneGeneral();
		}
		urlPaneAbs.createPane( dlg, mainCtrl, myDBAbs );
		return urlPaneAbs;
	}
}
