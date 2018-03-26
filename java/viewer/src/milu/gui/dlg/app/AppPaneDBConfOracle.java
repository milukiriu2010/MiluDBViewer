package milu.gui.dlg.app;

import java.io.File;
import java.util.ResourceBundle;

import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.scene.layout.HBox;
import javafx.scene.Node;

import milu.conf.AppConf;
import milu.ctrl.MainController;

public class AppPaneDBConfOracle extends AppPaneAbstract 
{
	// ----------------------------------------------------
	// Items for "TNS"
	// ----------------------------------------------------
	private TextField tnsAdminTextField    = new TextField();
	
	private Button    folderBtn            = new Button();

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
		
		Label lblTnsAdmin = new Label("TNS Admin:");
		
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
		
		HBox hBoxTnsAdmin = new HBox(2);
		hBoxTnsAdmin.getChildren().addAll( lblTnsAdmin, this.tnsAdminTextField, this.folderBtn );
		
		// set objects
		VBox vBox = new VBox(2);
		vBox.getChildren().addAll
		( 
			this.lblTitle, 
			hBoxTnsAdmin
		);
		
		// put controls on pane
		this.getChildren().addAll( vBox );
	}
	
	private void setAction()
	{
		this.folderBtn.setOnAction
		(
			(event)->
			{
				DirectoryChooser dc = new DirectoryChooser();
				File dir = dc.showDialog(null);
				if ( dir != null )
				{
					this.tnsAdminTextField.setText( dir.getAbsolutePath() );
				}
			}
		);
	}

	@Override
	public boolean apply() 
	{
		//System.out.println( "AppPaneDBConfOracle:apply[" + this.tnsAdminTextField.getText() + "]" );
		AppConf  appConf   = mainCtrl.getAppConf();
		
		appConf.setOracleTnsAdmin( this.tnsAdminTextField.getText() );
		
		return true;
	}

}
