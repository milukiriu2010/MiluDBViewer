package milu.gui.ctrl.imp;

import java.io.File;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.HashMap;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import milu.gui.ctrl.common.inf.ChangeLangInterface;
import milu.gui.view.DBView;
import milu.main.AppConf;
import milu.main.MainController;
import milu.tool.MyFileTool;
import milu.tool.MyTool;

public class ImportDataPaneFile extends Pane 
	implements ChangeLangInterface 
{
	private DBView          dbView = null;
	
	private WizardInterface wizardInf = null;
	
	private Map<String,Object> mapObj = null;
	
	private BorderPane      basePane = new BorderPane();
	
    // -----------------------------------------------------
	// [Center]
    // ----------------------------------------------------- 
	private Label      lblSrcFile = new Label();
	private TextField  txtSrcFile = new TextField();
	private Button     btnOpen    = new Button();
	
    // -----------------------------------------------------
	// [Bottom]
    // ----------------------------------------------------- 
	private Button     btnNext    = new Button();
	
	ImportDataPaneFile( DBView dbView, WizardInterface wizardInf, Map<String,Object> mapObj )
	{
		this.dbView    = dbView;
		this.wizardInf = wizardInf;
		this.mapObj    = mapObj;
		
	    // -----------------------------------------------------
		// [Center]
	    // -----------------------------------------------------
		this.txtSrcFile.setPrefWidth(500);
		HBox hBoxSrc = new HBox(2);
		hBoxSrc.setPadding( new Insets( 10, 10, 10, 10 ) );
		hBoxSrc.setSpacing(10);
		hBoxSrc.getChildren().addAll( this.lblSrcFile, this.txtSrcFile, this.btnOpen );
		
		this.basePane.setCenter(hBoxSrc);
		
	    // -----------------------------------------------------
		// [Bottom]
	    // ----------------------------------------------------- 
		HBox hBoxBtm = new HBox(2);
		hBoxBtm.setPadding( new Insets( 10, 10, 10, 10 ) );
		hBoxBtm.setSpacing(10);
		Pane spacer = new Pane();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		spacer.setMinSize(10,1);
		
		hBoxBtm.getChildren().addAll( spacer, this.btnNext );
		
		this.basePane.setBottom(hBoxBtm);
		
		this.getChildren().add(this.basePane);
		
		//this.basePane.prefHeightProperty().bind(this.heightProperty());
		//this.basePane.prefWidthProperty().bind(this.widthProperty());
		
		this.setAction();
		
		this.changeLang();
	}
	
	private void setAction()
	{
		MainController mainCtrl = this.dbView.getMainController();
		AppConf appConf = mainCtrl.getAppConf();
		this.btnOpen.setOnAction((event)->{
			FileChooser fc = new FileChooser();
			// Initial Directory
			if ( appConf.getInitDirImportFile().isEmpty() != true )
			{
				fc.setInitialDirectory( new File(appConf.getInitDirImportFile()) );
			}
			if ( (this.txtSrcFile.getText() != null) && 
				 (this.txtSrcFile.getText().isEmpty() != true ) )
			{
				fc.setInitialDirectory( new File(this.txtSrcFile.getText()).getParentFile() );
			}
			// Extension Filter
			FileChooser.ExtensionFilter  extFilter = 
				new FileChooser.ExtensionFilter( "Excel(*.xlsx)", "*.xlsx" );
			fc.getExtensionFilters().add(extFilter);
			File file = fc.showOpenDialog(this.getScene().getWindow());
			if ( file == null )
			{
				return;
			}
			this.txtSrcFile.setText(file.getAbsolutePath());
			appConf.setInitDirImportFile(file.getParentFile().getAbsolutePath());
			
			MyFileTool.save( mainCtrl, appConf );
		});
		
		this.btnNext.setOnAction((event)->{
			this.mapObj.put( ImportData.SRC_FILE.val(), this.txtSrcFile.getText() );
			this.wizardInf.next( this, this.mapObj);
		});
	}
	
	// ChangeLangInterface
	@Override
	public void changeLang() 
	{
		MainController mainCtrl = this.dbView.getMainController();
		ResourceBundle langRB = mainCtrl.getLangResource("conf.lang.gui.ctrl.imp.ImportDataTab");
		ResourceBundle extLangRB = mainCtrl.getLangResource("conf.lang.gui.common.NodeName");
		
		this.lblSrcFile.setText(langRB.getString("LABEL_SRC_FILE"));
		
		// "select folder" button
		this.btnOpen.setGraphic( MyTool.createImageView( 16, 16, mainCtrl.getImage("file:resources/images/folder.png") ));
		Tooltip tipOpen = new Tooltip(extLangRB.getString( "TOOLTIP_OPEN_FILE" ));
		tipOpen.getStyleClass().add("MainToolBar_MyToolTip");
		this.btnOpen.setTooltip( tipOpen );
		
		this.btnNext.setText(extLangRB.getString("TOOLTIP_NEXT"));

	}
}
