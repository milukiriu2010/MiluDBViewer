package milu.gui.dlg.app;

import java.util.ResourceBundle;

import javafx.beans.property.StringProperty;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import milu.main.AppConf;
import milu.main.MainController;

public class AppPaneDBConf extends AppPaneAbstract 
{	
	// TextField for "Fetch Row Size"
	TextField  txtRowMax = new TextField();
	
	// collect database objects after connection?
	private ToggleGroup tglCollectDBObj   = new ToggleGroup();
	private RadioButton rbCollectDBObjYes = new RadioButton();
	private RadioButton rbCollectDBObjNo  = new RadioButton();

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
		
		ResourceBundle cmnLangRB = this.mainCtrl.getLangResource("conf.lang.gui.common.NodeName");
		
		// set objects
		this.lblTitle.setText( this.extLangRB.getString( "TITLE_DB_CONF_PANE" ) );
		this.lblTitle.getStyleClass().add("AppPane_Label_Title");
		
		// ---------------------------------------------------------------------------
		// Fetch Row Size
		// ---------------------------------------------------------------------------
		Label    lblRowMax = new Label( this.extLangRB.getString( "LABEL_ROW_MAX" ) );
		AppConf  appConf   = mainCtrl.getAppConf();
		this.txtRowMax.setText( String.valueOf( appConf.getFetchMax() ) );
		HBox     hbxRowMax = new HBox( 2 );
		hbxRowMax.getChildren().addAll( lblRowMax, this.txtRowMax );

		// ---------------------------------------------------------------------------
		// collect database objects after connection?
		// ---------------------------------------------------------------------------
		Label    lblCollectDBObj = new Label( this.extLangRB.getString( "LABEL_COLLECT_DB_OBJ" ) );
		rbCollectDBObjYes.setToggleGroup(tglCollectDBObj);
		rbCollectDBObjNo.setToggleGroup(tglCollectDBObj);
		rbCollectDBObjYes.setText(cmnLangRB.getString("LABEL_YES"));
		rbCollectDBObjNo.setText(cmnLangRB.getString("LABEL_NO"));
		
		if ( appConf.isCollectDBObj() )
		{
			rbCollectDBObjYes.setSelected(true);
		}
		else
		{
			rbCollectDBObjNo.setSelected(true);
		}
		HBox     hbxCollectDBObj = new HBox( 2 );
		hbxCollectDBObj.getChildren().addAll( rbCollectDBObjYes, rbCollectDBObjNo );
		
		VBox vBox = new VBox(2);
		vBox.getChildren().addAll
		( 
			this.lblTitle, 
			hbxRowMax,
			lblCollectDBObj,
			hbxCollectDBObj
		);
		
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
		AppConf  appConf   = mainCtrl.getAppConf();
		// ---------------------------------------------------------------------------
		// Fetch Row Size
		// ---------------------------------------------------------------------------
		String strRowMax = this.txtRowMax.getText();
		if ( strRowMax.length() > 0 )
		{
			appConf.setFetchMax( Integer.valueOf( strRowMax ) );
		}
		
		// ---------------------------------------------------------------------------
		// collect database objects after connection?
		// ---------------------------------------------------------------------------
		if ( ((RadioButton)this.tglCollectDBObj.getSelectedToggle()) == this.rbCollectDBObjYes )
		{
			appConf.setCollectDBObj(true);
		}
		else
		{
			appConf.setCollectDBObj(false);
		}
		
		
		return true;
	}

}
