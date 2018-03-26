package milu.gui.dlg.app;

import java.util.ResourceBundle;

import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.Node;

import milu.conf.AppConf;
import milu.ctrl.MainController;

public class AppPaneDBConfMySQL extends AppPaneAbstract 
{
	// Explain Type - Extended
	private ToggleGroup tglExtended     = new ToggleGroup();
	private RadioButton rbExtendedTrue  = new RadioButton("True");
	private RadioButton rbExtendedFalse = new RadioButton("False");
	
	// Explain Type - Partitions
	private ToggleGroup tglPartitions     = new ToggleGroup();
	private RadioButton rbPartitionsTrue  = new RadioButton("True");
	private RadioButton rbPartitionsFalse = new RadioButton("False");
	
	// Explain Format
	private ToggleGroup tglFormat           = new ToggleGroup();
	private RadioButton rbFormatTraditional = new RadioButton("TRADITIONAL");
	private RadioButton rbFormatJSon        = new RadioButton("JSON");

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
		this.lblTitle.setText( this.extLangRB.getString( "TITLE_DB_CONF_PANE_MYSQL" ) );
		this.lblTitle.getStyleClass().add("AppPane_Label_Title");
		
		Label lblExplain = new Label("EXPLAIN");
		
		// Explain Extended
		Label lblExtended  = new Label("EXTENDED");
		rbExtendedTrue.setToggleGroup(tglExtended);
		rbExtendedFalse.setToggleGroup(tglExtended);
		
		// select default RadioButton(explain extended)
		if ( appConf.getMySQLExplainExtended() )
		{
			rbExtendedTrue.setSelected(true);
			
			// another options are not available, when "extended" option is true.
			rbPartitionsTrue.setDisable(true);
			rbPartitionsFalse.setDisable(true);
			rbFormatTraditional.setDisable(true);
			rbFormatJSon.setDisable(true);
		}
		else
		{
			rbExtendedFalse.setSelected(true);
		}
		
		HBox hBoxExtended = new HBox(2);
		hBoxExtended.getChildren().addAll( rbExtendedTrue, rbExtendedFalse );
		
		// Explain Partitions
		Label lblPartitions  = new Label("PARTITIONS");
		rbPartitionsTrue.setToggleGroup(tglPartitions);
		rbPartitionsFalse.setToggleGroup(tglPartitions);
		
		// select default RadioButton(explain partitions)
		if ( appConf.getMySQLExplainPartitions() )
		{
			rbPartitionsTrue.setSelected(true);
			
			// another options are not available, when "partitions" option is true.
			rbExtendedTrue.setDisable(true);
			rbExtendedFalse.setDisable(true);
			rbFormatTraditional.setDisable(true);
			rbFormatJSon.setDisable(true);
		}
		else
		{
			rbPartitionsFalse.setSelected(true);
		}
		
		HBox hBoxPartitions = new HBox(2);
		hBoxPartitions.getChildren().addAll( rbPartitionsTrue, rbPartitionsFalse );
		
		// Explain Format
		Label lblFormat  = new Label("FORMAT");
		rbFormatTraditional.setToggleGroup(tglFormat);
		rbFormatJSon.setToggleGroup(tglFormat);
		
		HBox hBoxFormat = new HBox(2);
		hBoxFormat.getChildren().addAll( rbFormatTraditional, rbFormatJSon );

		// select default RadioButton(explain format)
		for ( Node nodeFormat : hBoxFormat.getChildrenUnmodifiable() )
		{
			if ( nodeFormat instanceof RadioButton )
			{
				RadioButton rb = (RadioButton)nodeFormat;
				if ( rb.getText().equals( appConf.getMySQLExplainFormat() ) )
				{
					rb.setSelected(true);
					break;
				}
			}
		}
		
		// another options are not available, when "format" option is not "TRADITIONAL".
		if ( rbFormatTraditional.isSelected() == false )
		{
			rbExtendedTrue.setDisable(true);
			rbExtendedFalse.setDisable(true);
			rbPartitionsTrue.setDisable(true);
			rbPartitionsFalse.setDisable(true);
		}
		
		// set objects
		VBox vBox = new VBox(2);
		vBox.getChildren().addAll
		( 
			this.lblTitle, 
			lblExplain,
			lblExtended,
			hBoxExtended,
			lblPartitions,
			hBoxPartitions,
			lblFormat, 
			hBoxFormat 
		);
		
		// put controls on pane
		this.getChildren().addAll( vBox );
	}
	
	private void setAction()
	{
		// "partitions","format" options are not available, when "extended" option is true. 
		this.rbExtendedTrue.selectedProperty().addListener
		(
			(obs,oldVal,newVal)->
			{
				if ( newVal.booleanValue() == true )
				{
					rbPartitionsTrue.setDisable(true);
					rbPartitionsFalse.setDisable(true);
					rbFormatTraditional.setDisable(true);
					rbFormatJSon.setDisable(true);
					
					rbPartitionsFalse.setSelected(true);
					rbFormatTraditional.setSelected(true);
				}
				else
				{
					rbPartitionsTrue.setDisable(false);
					rbPartitionsFalse.setDisable(false);
					rbFormatTraditional.setDisable(false);
					rbFormatJSon.setDisable(false);
				}
			}
		);
		
		// "extended","format" options are not available, when "partitions" option is true. 
		this.rbPartitionsTrue.selectedProperty().addListener
		(
			(obs,oldVal,newVal)->
			{
				if ( newVal.booleanValue() == true )
				{
					rbExtendedTrue.setDisable(true);
					rbExtendedFalse.setDisable(true);
					rbFormatTraditional.setDisable(true);
					rbFormatJSon.setDisable(true);
					
					rbExtendedFalse.setSelected(true);
					rbFormatTraditional.setSelected(true);
				}
				else
				{
					rbExtendedTrue.setDisable(false);
					rbExtendedFalse.setDisable(false);
					rbFormatTraditional.setDisable(false);
					rbFormatJSon.setDisable(false);
				}
			}
		);
		
		// "extended","partitions" options are not available, when "format" option is not "TRADITIONAL". 
		this.rbFormatTraditional.selectedProperty().addListener
		(
			(obs,oldVal,newVal)->
			{
				if ( newVal.booleanValue() == true )
				{
					rbExtendedTrue.setDisable(false);
					rbExtendedFalse.setDisable(false);
					rbPartitionsTrue.setDisable(false);
					rbPartitionsFalse.setDisable(false);
				}
				else
				{
					rbExtendedTrue.setDisable(true);
					rbExtendedFalse.setDisable(true);
					rbPartitionsTrue.setDisable(true);
					rbPartitionsFalse.setDisable(true);
					
					rbExtendedFalse.setSelected(true);
					rbPartitionsFalse.setSelected(true);
				}
			}
		);
		
	}

	@Override
	public boolean apply() 
	{
		AppConf  appConf   = mainCtrl.getAppConf();
		
		// Explain Extended
		if ( ((RadioButton)this.tglExtended.getSelectedToggle()) == this.rbExtendedTrue )
		{
			appConf.setMySQLExplainExtended( true );
		}
		else
		{
			appConf.setMySQLExplainExtended( false );
		}
		
		// Explain Partitions
		if ( ((RadioButton)this.tglPartitions.getSelectedToggle()) == this.rbPartitionsTrue )
		{
			appConf.setMySQLExplainPartitions( true );
		}
		else
		{
			appConf.setMySQLExplainPartitions( false );
		}
		
		// Explain Format
		appConf.setMySQLExplainFormat( ((RadioButton)this.tglFormat.getSelectedToggle()).getText() );
		
		return true;
	}

}
