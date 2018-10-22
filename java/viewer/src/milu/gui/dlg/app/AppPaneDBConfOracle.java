package milu.gui.dlg.app;

import java.io.File;
import java.util.ResourceBundle;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import milu.main.AppConf;
import milu.main.MainController;

class AppPaneDBConfOracle extends AppPaneAbstract 
{
	// ----------------------------------------------------
	// Items for "TNS"
	// ----------------------------------------------------
	private TextField tnsAdminTextField    = new TextField();
	
	private Button    folderBtn            = new Button();
	
	// ----------------------------------------------------
	// v$session.osuser
	// ----------------------------------------------------
	private TextField sessionOsuserTextField = new TextField();
	
	// ----------------------------------------------------
	// v$session.machine
	// ----------------------------------------------------
	private TextField sessionMachineTextField = new TextField();
	
	// ----------------------------------------------------
	// v$session.program
	// ----------------------------------------------------
	private TextField sessionProgramTextField = new TextField();

	@Override
	public void createPane(Dialog<?> dlg, MainController mainCtrl, ResourceBundle extLangRB) 
	{
		this.dlg       = dlg;
		this.mainCtrl  = mainCtrl;
		this.extLangRB = extLangRB;

		this.setPane();
		
		this.setAction();
	}
	
	private void setPane()
	{
		this.getChildren().removeAll( this.getChildren() );
		
		AppConf  appConf   = mainCtrl.getAppConf();
		
		// set title
		this.lblTitle.setText( this.extLangRB.getString( "TITLE_DB_CONF_PANE_ORACLE" ) );
		this.lblTitle.getStyleClass().add("AppPane_Label_Title");
		
		// ----------------------------------------------------
		// Items for "TNS"
		// ----------------------------------------------------
		// TNS_ADMIN(Default Value)
		if ( appConf.getOracleTnsAdmin().length() > 0 )
		{
			String tns_admin = appConf.getOracleTnsAdmin();
			this.tnsAdminTextField.setText( tns_admin );
		}
		else if ( System.getProperty("oracle.net.tns_admin") != null )
		{
			String tns_admin = System.getProperty("oracle.net.tns_admin");
			if ( new File(tns_admin).exists() )
			{
				this.tnsAdminTextField.setText( tns_admin );
			}
		}
		else if ( System.getenv("TNS_ADMIN") != null )
		{
			String tns_admin = System.getenv("TNS_ADMIN");
			if ( new File(tns_admin).exists() )
			{
				this.tnsAdminTextField.setText( tns_admin );
			}
		}
		else if ( System.getenv("ORACLE_HOME") != null )
		{
			String oracle_home = System.getenv("ORACLE_HOME");
			String tns_admin1 = oracle_home + java.io.File.separator + "network" + java.io.File.separator + "admin";
			String tns_admin2 = oracle_home + java.io.File.separator + "NETWORK" + java.io.File.separator + "ADMIN";
			if ( new File(tns_admin1).exists() )
			{
				this.tnsAdminTextField.setText( tns_admin1 );
			}
			else if ( new File(tns_admin2).exists() )
			{
				this.tnsAdminTextField.setText( tns_admin2 );
			}
		}
		
		// TNS_ADMIN(Default Prompt)
		if ( System.getProperty("os.name").contains("Windows") )
		{
			this.tnsAdminTextField.setPromptText("%ORACLE_HOME%\\network\\admin");
		}
		else
		{
			this.tnsAdminTextField.setPromptText("$ORACLE_HOME/network/admin");
		}
		
		// "select folder" button
		ImageView   ivFolder = new ImageView( this.mainCtrl.getImage("file:resources/images/folder.png") );
		ivFolder.setFitWidth(16);
		ivFolder.setFitHeight(16);
		this.folderBtn.setGraphic( ivFolder );
		Tooltip tipFolder = new Tooltip(this.extLangRB.getString( "TOOLTIP_NEW_FOLDER" ));
		tipFolder.getStyleClass().add("Common_MyToolTip");		
		this.folderBtn.setTooltip( tipFolder );
		
		// ----------------------------------------------------
		// v$session.osuser
		// ----------------------------------------------------
		this.sessionOsuserTextField.setText(appConf.getOracleSessionOsuser());		
		// ----------------------------------------------------
		// v$session.machine
		// ----------------------------------------------------
		this.sessionMachineTextField.setText(appConf.getOracleSessionMachine());		
		// ----------------------------------------------------
		// v$session.program
		// ----------------------------------------------------
		this.sessionProgramTextField.setText(appConf.getOracleSessionProgram());		
		
		// set objects
		GridPane paneGrid = new GridPane();
		paneGrid.setHgap(5);
		paneGrid.setVgap(2);
		paneGrid.setPadding( new Insets( 10, 10, 10, 10 ) );
		paneGrid.add( new Label("TNS Admin")        , 0, 1 );
		paneGrid.add( this.tnsAdminTextField        , 1, 1 );
		paneGrid.add( this.folderBtn                , 2, 1 );
		paneGrid.add( new Label("v$session.osuser") , 0, 2 );
		paneGrid.add( this.sessionOsuserTextField   , 1, 2 );
		paneGrid.add( new Label("v$session.machine"), 0, 3 );
		paneGrid.add( this.sessionMachineTextField  , 1, 3 );
		paneGrid.add( new Label("v$session.program"), 0, 4 );
		paneGrid.add( this.sessionProgramTextField  , 1, 4 );
		
		VBox vBox = new VBox(2);
		vBox.getChildren().addAll
		( 
			this.lblTitle, 
			paneGrid
		);
		
		// put controls on pane
		this.getChildren().addAll( vBox );
	}
	
	private void setAction()
	{
		this.folderBtn.setOnAction((event)->{
			DirectoryChooser dc = new DirectoryChooser();
			if ( ( this.tnsAdminTextField.getText() != null ) &&
				 ( this.tnsAdminTextField.getText().isEmpty() != true ) )
			{
				dc.setInitialDirectory( new File(this.tnsAdminTextField.getText()).getParentFile() );
			}
			File dir = dc.showDialog(this.dlg.getDialogPane().getScene().getWindow());
			if ( dir != null )
			{
				this.tnsAdminTextField.setText( dir.getAbsolutePath() );
			}
		});
	}

	@Override
	public boolean apply() 
	{
		//System.out.println( "AppPaneDBConfOracle:apply[" + this.tnsAdminTextField.getText() + "]" );
		AppConf  appConf   = mainCtrl.getAppConf();
		
		appConf.setOracleTnsAdmin( this.tnsAdminTextField.getText() );
		// ----------------------------------------------------
		// v$session.osuser
		// ----------------------------------------------------
		appConf.setOracleSessionOsuser(this.sessionOsuserTextField.getText());
		// ----------------------------------------------------
		// v$session.machine
		// ----------------------------------------------------
		appConf.setOracleSessionMachine(this.sessionMachineTextField.getText());
		// ----------------------------------------------------
		// v$session.program
		// ----------------------------------------------------
		appConf.setOracleSessionProgram(this.sessionProgramTextField.getText());
		
		return true;
	}

}
