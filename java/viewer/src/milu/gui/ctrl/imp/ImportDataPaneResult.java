package milu.gui.ctrl.imp;

import java.util.Map;
import java.util.ResourceBundle;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import milu.gui.view.DBView;
import milu.gui.view.FadeView;
import milu.main.MainController;
import milu.gui.ctrl.common.inf.ChangeLangInterface;
import milu.tool.MyTool;

public class ImportDataPaneResult extends Pane
	implements
		ChangeLangInterface
{
	private DBView          dbView = null;
	
	private WizardInterface wizardInf = null;
	
	private Map<String,Object> mapObj = null;
	
	private BorderPane      basePane = new BorderPane();
	
    // -----------------------------------------------------
	// [Bottom]
    // -----------------------------------------------------
	private Button btnCommit = new Button();
	private Button btnRollback = new Button();
	
	
	ImportDataPaneResult( DBView dbView, WizardInterface wizardInf, Map<String,Object> mapObj )
	{
		this.dbView    = dbView;
		this.wizardInf = wizardInf;
		this.mapObj    = mapObj;
		
		MainController mainCtrl = this.dbView.getMainController();
		
		// Button to commit
		this.btnCommit.setGraphic( MyTool.createImageView( 20, 20, mainCtrl.getImage("file:resources/images/commit.png") ) );
		
		// Button to rollback
		this.btnRollback.setGraphic( MyTool.createImageView( 20, 20, mainCtrl.getImage("file:resources/images/rollback.png") ) );
		
		HBox hBoxTran = new HBox(2);
		hBoxTran.setPadding( new Insets( 10, 10, 10, 10 ) );
		hBoxTran.setSpacing(10);
		hBoxTran.setAlignment(Pos.CENTER);
		hBoxTran.getChildren().addAll(this.btnCommit,this.btnRollback);
		this.basePane.setBottom(hBoxTran);
		
		this.getChildren().addAll(this.basePane);
		
		this.changeLang();
		
		this.setAction();
	}
	
	private void setAction()
	{
		// Commit button clicked
		this.btnCommit.setOnAction((event)->{
			this.dbView.commit();
			new FadeView( "Commit" );
		});
		
		// Rollback button clicked
		this.btnRollback.setOnAction((event)->{
			this.dbView.rollback();
			new FadeView( "Rollback" );
		});
	}
	
	// ChangeLangInterface
	@Override	
	public void changeLang()
	{
		MainController mainCtrl = this.dbView.getMainController();
		ResourceBundle langRB = mainCtrl.getLangResource("conf.lang.gui.ctrl.menu.MainToolBar");
		
		// ----------------------------------------------
		// ToolTip
		//   Button[Commit] 
		// ----------------------------------------------
		Tooltip tipCommit = new Tooltip( langRB.getString( "TIP_COMMIT" ) );
		tipCommit.getStyleClass().add("MainToolBar_MyToolTip");
		this.btnCommit.setTooltip( tipCommit );
		
		// ----------------------------------------------
		// ToolTip
		//   Button[Rollback] 
		// ----------------------------------------------
		Tooltip tipRollback = new Tooltip( langRB.getString( "TIP_ROLLBACK" ) );
		tipRollback.getStyleClass().add("MainToolBar_MyToolTip");
		this.btnRollback.setTooltip( tipRollback );
	}
}
