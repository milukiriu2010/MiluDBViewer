package milu.gui.ctrl.info;

import java.util.ResourceBundle;

import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.BorderPane;
import milu.gui.ctrl.common.inf.FocusInterface;
import milu.gui.ctrl.common.inf.ChangeLangInterface;
import milu.gui.view.DBView;
import milu.main.MainController;
import milu.tool.MyTool;

public class SystemTab extends Tab implements ChangeLangInterface
{
	private DBView      dbView = null;
	
	private TabPane     tabPane = new TabPane();
	
	private BorderPane  basePane = new BorderPane();
	
	public SystemTab( DBView dbView )
	{
		super();
		
		this.dbView = dbView;
		MainController mainCtrl = this.dbView.getMainController();
		
		this.tabPane.setSide(Side.LEFT);
		this.tabPane.setTabClosingPolicy( TabClosingPolicy.UNAVAILABLE );
		
		this.tabPane.getTabs().addAll( 
				new SystemPropertyTab(this.dbView),
				new SystemEnvTab(this.dbView),
				new SystemDisplayTab(this.dbView),
				new SystemMemTab(this.dbView)
			);
		
		//this.setContent(this.tabPane);
		//this.setContent(new Label("XXX"));
		
		this.basePane.setCenter(this.tabPane);
		this.setContent(this.basePane);
		
		// set icon on Tab
		this.setGraphic( MyTool.createImageView( 16, 16, mainCtrl.getImage("file:resources/images/sysinfo.png") ) );
		
		this.setAction();
		
		this.changeLang();
	}
	
	private void setAction()
	{
		this.tabPane.getSelectionModel().selectedItemProperty().addListener((obs,oldVal,tab)->{
			if ( tab instanceof FocusInterface )
			{
				((FocusInterface)tab).setFocus();
			}
		});
		
		Tab tab = this.tabPane.getTabs().get(0);
		if ( tab instanceof FocusInterface )
		{
			((FocusInterface)tab).setFocus();
		}
	}
	
	// ChangeLangInterface
	@Override
	public void changeLang() 
	{
		MainController mainCtrl = this.dbView.getMainController();
		ResourceBundle langRB = mainCtrl.getLangResource("conf.lang.gui.ctrl.info.SystemTab");
		
		// Tab Title
		Node tabGraphic = this.getGraphic();
		if ( tabGraphic instanceof Label )
		{
			((Label)tabGraphic).setText( langRB.getString("TITLE_SYSINFO") );
		}
		else
		{
			this.setText(langRB.getString("TITLE_SYSINFO"));
		}
		
		this.tabPane.getTabs().forEach((tab)->{
			if ( tab instanceof ChangeLangInterface )
			{
				((ChangeLangInterface)tab).changeLang();
			}
		});
	}	
}
