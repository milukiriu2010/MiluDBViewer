package milu.gui.dlg.app;

import java.util.ResourceBundle;

import javafx.beans.property.StringProperty;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import milu.conf.AppConf;
import milu.main.MainController;

public class AppPaneDBConf extends AppPaneAbstract 
{	
	// TextField for "Fetch Row Size"
	TextField  txtRowMax = new TextField();

	@Override
	public void createPane( Dialog<?> dlg, MainController mainCtrl, ResourceBundle extLangRB) 
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
		
		// set objects
		this.lblTitle.setText( this.extLangRB.getString( "TITLE_DB_CONF_PANE" ) );
		this.lblTitle.getStyleClass().add("AppPane_Label_Title");
		
		Label    lblRowMax = new Label( this.extLangRB.getString( "LABEL_ROW_MAX" ) );
		AppConf  appConf   = mainCtrl.getAppConf();
		this.txtRowMax.setText( String.valueOf( appConf.getFetchMax() ) );
		HBox     hbxRowMax = new HBox( 2 );
		hbxRowMax.getChildren().addAll( lblRowMax, this.txtRowMax );
		
		VBox vBox = new VBox(2);
		vBox.getChildren().addAll( this.lblTitle, hbxRowMax );
		
		// put controls on pane
		this.getChildren().addAll( vBox );
	}
	
	private void setAction()
	{
		// restriction for TextField "Fetch Row Size"
		// https://stackoverflow.com/questions/15615890/recommended-way-to-restrict-input-in-javafx-textfield
		this.txtRowMax.textProperty().addListener
		(
			(obs, oldVal, newVal) ->
			{
				// "Numeric" or "No Input" are allowed.
				if ( newVal.length() == 0 )
				{
					
				}
				// if alphabets or marks are input, back to previous input.
				else if ( newVal.matches( "^[0-9]+$" ) == false )
				{
					((StringProperty)obs).setValue( oldVal );
				}
			}
		);
	}

	@Override
	public boolean apply() 
	{
		String strRowMax = this.txtRowMax.getText();
		if ( strRowMax.length() > 0 )
		{
			AppConf  appConf   = mainCtrl.getAppConf();
			appConf.setFetchMax( Integer.valueOf( strRowMax ) );
		}
		return true;
	}

}
