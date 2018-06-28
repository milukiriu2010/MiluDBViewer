package milu.gui.ctrl.imp;

import java.util.ResourceBundle;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import milu.entity.schema.SchemaEntity;
import milu.gui.ctrl.common.inf.CloseInterface;
import milu.gui.ctrl.common.inf.ChangeLangInterface;
import milu.gui.view.DBView;
import milu.main.MainController;
import milu.tool.MyTool;

public class ImportDataTab extends Tab 
	implements
		CloseInterface,
		ChangeLangInterface 
{
	private DBView          dbView = null;
	
    // -----------------------------------------------------
	// [Pane(1) on Tab]
    // -----------------------------------------------------
	private Pane       basePane = null;
	
	public ImportDataTab( DBView dbView, SchemaEntity schemaEntity )
	{
		super();
		this.dbView = dbView;
		
		MainController mainCtrl = this.dbView.getMainController();
		
		this.basePane = new ImportDataPane(dbView,schemaEntity,this);
		this.setContent(this.basePane);
		
		//this.basePane.prefHeightProperty().bind(this.getTabPane().heightProperty());
		//this.basePane.prefWidthProperty().bind(this.getTabPane().widthProperty());
		
		// set icon on Tab
		this.setGraphic( MyTool.createImageView( 16, 16, mainCtrl.getImage("file:resources/images/import.png") ) );
		
		this.changeLang();
	}
	
	// CloseInterface
	@Override
	public void closeRequest( Event event )
	{
		this.getTabPane().getTabs().remove(this);
	}
	
	// ChangeLangInterface
	@Override
	public void changeLang() 
	{
		MainController mainCtrl = this.dbView.getMainController();
		ResourceBundle extLangRB = mainCtrl.getLangResource("conf.lang.gui.ctrl.imp.ImportDataTab");

		// Tab Title
		Node tabGraphic = this.getGraphic();
		if ( tabGraphic instanceof Label )
		{
			((Label)tabGraphic).setText( extLangRB.getString("TITLE_TAB_IMPORT_DATA") );
		}
		else
		{
			this.setText( extLangRB.getString("TITLE_TAB_IMPORT_DATA") );
		}
	}

}
