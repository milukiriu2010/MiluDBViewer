package milu.gui.dlg.db;

import java.util.ResourceBundle;
import javafx.scene.layout.Pane;

import milu.db.MyDBAbstract;
import milu.db.MyDBPostgres;
import milu.db.MyDBMySQL;
import milu.db.MyDBOracle;
import milu.db.MyDBCassandra;

public class UrlPaneFactory implements PaneFactory 
{

	@Override
	public Pane createPane(MyDBAbstract myDBAbs, ResourceBundle langRB ) 
	{
		// TODO Auto-generated method stub
		return null;
	}

}
