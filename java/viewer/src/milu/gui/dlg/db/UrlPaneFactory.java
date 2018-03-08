package milu.gui.dlg.db;

import java.util.Map;
import java.util.ResourceBundle;
import javafx.scene.layout.Pane;
import javafx.scene.control.Dialog;

import milu.ctrl.MainController;

import milu.db.MyDBAbstract;
import milu.db.MyDBPostgres;
import milu.db.MyDBMySQL;
import milu.db.MyDBOracle;
import milu.db.MyDBCassandra;

public class UrlPaneFactory implements PaneFactory 
{
	@Override
	public UrlPaneAbstract createPane( Dialog<?> dlg, MainController mainCtrl, MyDBAbstract myDBAbs, ResourceBundle extLangRB, Map<String,String> mapProp )
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
			urlPaneAbs = new UrlPaneCommon();
		}
		urlPaneAbs.createPane( dlg, mainCtrl, myDBAbs, extLangRB, mapProp);
		return urlPaneAbs;
	}
}
