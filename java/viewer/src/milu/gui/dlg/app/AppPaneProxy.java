package milu.gui.dlg.app;

import java.util.ResourceBundle;

import javafx.geometry.Insets;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;

import milu.main.AppConf;
import milu.main.MainController;
import milu.net.ProxyType;
import milu.tool.MyTool;

public class AppPaneProxy extends AppPaneAbstract 
{
	private	ChoiceBox<ProxyType> cbxProxyType = new ChoiceBox<>();
	
	private TextField txtHost = new TextField();
	
	private TextField txtPort = new TextField();
	
	private TextField txtUser = new TextField();
	
	private TextField txtPwd  = new PasswordField();

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
		
		ResourceBundle  langRB = this.mainCtrl.getLangResource("conf.lang.gui.dlg.db.DBSettingDialog");
		
		// set title
		this.lblTitle.setText( this.extLangRB.getString( "TITLE_PROXY_PANE" ) );
		this.lblTitle.getStyleClass().add("AppPane_Label_Title");
		
		Label     lblProxyType = new Label(this.extLangRB.getString( "LABEL_PROXY_TYPE" ));
		ObservableList<ProxyType> proxyTypeLst = 
			FXCollections.observableArrayList( ProxyType.NO_PROXY, ProxyType.SYSTEM, ProxyType.MANUAL );
		cbxProxyType.setItems(proxyTypeLst);
		cbxProxyType.getSelectionModel().select(appConf.getProxyType());
		this.setDisableInput(appConf.getProxyType());
		cbxProxyType.setConverter
		(
			new StringConverter<ProxyType>()
			{
				@Override
				public String toString( ProxyType proxyType )
				{
					if ( ProxyType.NO_PROXY.equals(proxyType) )
					{
						return extLangRB.getString( "PROXY_TYPE_NO_PROXY" );
					}
					else if ( ProxyType.SYSTEM.equals(proxyType) )
					{
						return extLangRB.getString( "PROXY_TYPE_SYSTEM" );
					}
					else
					{
						return extLangRB.getString( "PROXY_TYPE_MANUAL" );
					}
				}
				
				@Override
				public ProxyType fromString( String str )
				{
					return null;
				}
			}
		);
		
		
		Label     lblHost = new Label(langRB.getString( "LABEL_HOST_OR_IPADDRESS" ));
		this.txtHost.setText( appConf.getProxyHost() );
		Label     lblPort = new Label(langRB.getString( "LABEL_PORT" ));
		this.txtPort.setText( appConf.getProxyPort().toString() );
		Label     lblUser = new Label(langRB.getString( "LABEL_USERNAME" ));
		this.txtUser.setText( appConf.getProxyUser() );
		Label     lblPwd  = new Label(langRB.getString( "LABEL_PASSWORD" ));
		this.txtPwd.setText( appConf.getProxyPassword() );
		
		GridPane  pane = new GridPane();
		pane.setHgap(5);
		pane.setVgap(2);
		pane.setPadding( new Insets( 10, 10, 10, 10 ) );
		
		pane.add( lblProxyType, 0, 1 );
		pane.add( cbxProxyType, 1, 1 );
		pane.add( lblHost, 0, 2 );
		pane.add( txtHost, 1, 2 );
		pane.add( lblPort, 0, 3 );
		pane.add( txtPort, 1, 3 );
		pane.add( lblUser, 0, 4 );
		pane.add( txtUser, 1, 4 );
		pane.add( lblPwd , 0, 5 );
		pane.add( txtPwd , 1, 5 );
		
		VBox vBox = new VBox(2);
		vBox.getChildren().addAll( this.lblTitle, pane );
		
		// put controls on pane
		this.getChildren().addAll( vBox );
	}
	
	private void setAction()
	{
		this.cbxProxyType.valueProperty().addListener( (obs,oldVal,newVal)->this.setDisableInput(newVal) );
	}
	
	private void setDisableInput( ProxyType proxyType )
	{
		if ( ProxyType.NO_PROXY.equals(proxyType) )
		{
			this.txtHost.setDisable(true);
			this.txtPort.setDisable(true);
			this.txtUser.setDisable(true);
			this.txtPwd.setDisable(true);
		}
		else if ( ProxyType.SYSTEM.equals(proxyType) )
		{
			this.txtHost.setDisable(true);
			this.txtPort.setDisable(true);
			this.txtUser.setDisable(false);
			this.txtPwd.setDisable(false);
		}
		else
		{
			this.txtHost.setDisable(false);
			this.txtPort.setDisable(false);
			this.txtUser.setDisable(false);
			this.txtPwd.setDisable(false);
		}
	}

	@Override
	public boolean apply() 
	{
		AppConf appConf = this.mainCtrl.getAppConf();
		
		try
		{
			appConf.setProxyType( this.cbxProxyType.getValue() );
			appConf.setProxyHost( this.txtHost.getText() );
			appConf.setProxyPort( Integer.valueOf(this.txtPort.getText()) );
			appConf.setProxyUser( this.txtUser.getText() );
			appConf.setProxyPassword( this.txtPwd.getText() );
			appConf.setProxyPasswordEnc( this.mainCtrl.getSecretKey() );
		}
		catch ( Exception ex )
		{
			MyTool.showException( this.mainCtrl, "conf.lang.gui.common.MyAlert", "TITLE_MISC_ERROR", ex );
		}
		
		return true;
	}

}
