package milu.gui.dlg.app;

import java.util.ResourceBundle;

import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Toggle;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.Node;

import milu.conf.AppConf;
import milu.ctrl.MainController;

public class AppPaneDBConfPostgres extends AppPaneAbstract 
{
	private ToggleGroup tglFormat = new ToggleGroup();
	private RadioButton rbFormatText = new RadioButton("TEXT");
	private RadioButton rbFormatXML  = new RadioButton("XML");
	private RadioButton rbFormatJSon = new RadioButton("JSON");
	private RadioButton rbFormatYaml = new RadioButton("YAML");

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
		
		// set objects
		this.lblTitle.setText( this.extLangRB.getString( "TITLE_DB_CONF_PANE_POSTGRES" ) );
		this.lblTitle.getStyleClass().add("label-title");
		
		Label lblExplain = new Label("EXPLAIN");
		
		Label lblFormat  = new Label("FORMAT");
		rbFormatText.setToggleGroup(tglFormat);
		rbFormatXML.setToggleGroup(tglFormat);
		rbFormatJSon.setToggleGroup(tglFormat);
		rbFormatYaml.setToggleGroup(tglFormat);
		
		HBox hBoxFormat = new HBox(2);
		hBoxFormat.getChildren().addAll( rbFormatText, rbFormatXML, rbFormatJSon, rbFormatYaml );

		AppConf  appConf   = mainCtrl.getAppConf();
		// select defautl radiobutton(explain format)
		for ( Node nodeFormat : hBoxFormat.getChildrenUnmodifiable() )
		{
			if ( nodeFormat instanceof RadioButton )
			{
				RadioButton rb = (RadioButton)nodeFormat;
				if ( rb.getText().equals( appConf.getPostgresExplainFormat() ) )
				{
					rb.setSelected(true);
					break;
				}
			}
		}
		
		VBox vBox = new VBox(2);
		vBox.getChildren().addAll( this.lblTitle, lblExplain, lblFormat, hBoxFormat );
		
		// put controls on pane
		this.getChildren().addAll( vBox );
	}
	
	private void setAction()
	{
		this.tglFormat.selectedToggleProperty().addListener
		(
			(obs,oldVal,newVal)->
			{
				/*
				if ( newVal instanceof RadioButton )
				{
					RadioButton rb = (RadioButton)newVal;
				}
				*/
			}
		);
	}

	@Override
	public boolean apply() 
	{
		AppConf  appConf   = mainCtrl.getAppConf();
		Toggle tgl = this.tglFormat.getSelectedToggle();
		if ( tgl instanceof RadioButton )
		{
			appConf.setPostgresExplainFormat( ((RadioButton)tgl).getText() );
		}
		
		return true;
	}

}
