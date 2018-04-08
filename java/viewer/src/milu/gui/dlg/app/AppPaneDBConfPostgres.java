package milu.gui.dlg.app;

import java.util.ResourceBundle;

import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.Node;
import milu.main.AppConf;
import milu.main.MainController;

public class AppPaneDBConfPostgres extends AppPaneAbstract 
{
	// Explain Analyze
	private ToggleGroup tglAnalyze     = new ToggleGroup();
	private RadioButton rbAnalyzeTrue  = new RadioButton("True");
	private RadioButton rbAnalyzeFalse = new RadioButton("False");
	
	// Explain Verbose
	private ToggleGroup tglVerbose     = new ToggleGroup();
	private RadioButton rbVerboseTrue  = new RadioButton("True");
	private RadioButton rbVerboseFalse = new RadioButton("False");
	
	// Explain Costs
	private ToggleGroup tglCosts       = new ToggleGroup();
	private RadioButton rbCostsTrue    = new RadioButton("True");
	private RadioButton rbCostsFalse   = new RadioButton("False");
	
	// Explain Buffers
	private ToggleGroup tglBuffers     = new ToggleGroup();
	private RadioButton rbBuffersTrue  = new RadioButton("True");
	private RadioButton rbBuffersFalse = new RadioButton("False");
	
	// Explain Timing
	private ToggleGroup tglTiming      = new ToggleGroup();
	private RadioButton rbTimingTrue   = new RadioButton("True");
	private RadioButton rbTimingFalse  = new RadioButton("False");
	
	// Explain Format
	private ToggleGroup tglFormat    = new ToggleGroup();
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
		
		AppConf  appConf   = mainCtrl.getAppConf();
		
		// set title
		this.lblTitle.setText( this.extLangRB.getString( "TITLE_DB_CONF_PANE_POSTGRES" ) );
		this.lblTitle.getStyleClass().add("AppPane_Label_Title");
		
		Label lblExplain = new Label("EXPLAIN");
		
		// Explain Analyze
		Label lblAnalyze  = new Label("ANALYZE");
		rbAnalyzeTrue.setToggleGroup(tglAnalyze);
		rbAnalyzeFalse.setToggleGroup(tglAnalyze);
		
		// select default RadioButton(explain analyze)
		if ( appConf.getPostgresExplainAnalyze() )
		{
			rbAnalyzeTrue.setSelected(true);
		}
		else
		{
			rbAnalyzeFalse.setSelected(true);
			
			// "buffers","timing" options are available, when "analyze" option is true.
			rbBuffersTrue.setDisable(true);
			rbBuffersFalse.setDisable(true);
			rbTimingTrue.setDisable(true);
			rbTimingFalse.setDisable(true);
		}
		
		HBox hBoxAnalyze = new HBox(2);
		hBoxAnalyze.getChildren().addAll( rbAnalyzeTrue, rbAnalyzeFalse );
		
		// Explain Verbose
		Label lblVerbose  = new Label("VERBOSE");
		rbVerboseTrue.setToggleGroup(tglVerbose);
		rbVerboseFalse.setToggleGroup(tglVerbose);
		
		// select default RadioButton(explain verbose)
		if ( appConf.getPostgresExplainVerbose() )
		{
			rbVerboseTrue.setSelected(true);
		}
		else
		{
			rbVerboseFalse.setSelected(true);
		}
		
		HBox hBoxVerbose = new HBox(2);
		hBoxVerbose.getChildren().addAll( rbVerboseTrue, rbVerboseFalse );
		
		// Explain Costs
		Label lblCosts  = new Label("COSTS");
		rbCostsTrue.setToggleGroup(tglCosts);
		rbCostsFalse.setToggleGroup(tglCosts);
		
		// select default RadioButton(explain costs)
		if ( appConf.getPostgresExplainCosts() )
		{
			rbCostsTrue.setSelected(true);
		}
		else
		{
			rbCostsFalse.setSelected(true);
		}
		
		HBox hBoxCosts = new HBox(2);
		hBoxCosts.getChildren().addAll( rbCostsTrue, rbCostsFalse );
		
		// Explain Buffers
		Label lblBuffers  = new Label("BUFFERS");
		rbBuffersTrue.setToggleGroup(tglBuffers);
		rbBuffersFalse.setToggleGroup(tglBuffers);
		
		// select default RadioButton(explain buffers)
		if ( appConf.getPostgresExplainBuffers() )
		{
			rbBuffersTrue.setSelected(true);
		}
		else
		{
			rbBuffersFalse.setSelected(true);
		}
		
		HBox hBoxBuffers = new HBox(2);
		hBoxBuffers.getChildren().addAll( rbBuffersTrue, rbBuffersFalse );
		
		// Explain Timing
		Label lblTiming  = new Label("TIMING");
		rbTimingTrue.setToggleGroup(tglTiming);
		rbTimingFalse.setToggleGroup(tglTiming);
		
		// select default RadioButton(explain timing)
		if ( appConf.getPostgresExplainTiming() )
		{
			rbTimingTrue.setSelected(true);
		}
		else
		{
			rbTimingFalse.setSelected(true);
		}
		
		HBox hBoxTiming = new HBox(2);
		hBoxTiming.getChildren().addAll( rbTimingTrue, rbTimingFalse );

		// Explain Format
		Label lblFormat  = new Label("FORMAT");
		rbFormatText.setToggleGroup(tglFormat);
		rbFormatXML.setToggleGroup(tglFormat);
		rbFormatJSon.setToggleGroup(tglFormat);
		rbFormatYaml.setToggleGroup(tglFormat);
		
		HBox hBoxFormat = new HBox(2);
		hBoxFormat.getChildren().addAll( rbFormatText, rbFormatXML, rbFormatJSon, rbFormatYaml );

		// select default RadioButton(explain format)
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
		
		// set objects
		VBox vBox = new VBox(2);
		vBox.getChildren().addAll
		( 
			this.lblTitle, 
			lblExplain,
			lblAnalyze,
			hBoxAnalyze,
			lblVerbose,
			hBoxVerbose,
			lblCosts,
			hBoxCosts,
			lblBuffers,
			hBoxBuffers,
			lblTiming,
			hBoxTiming,
			lblFormat, 
			hBoxFormat 
		);
		
		// put controls on pane
		this.getChildren().addAll( vBox );
	}
	
	private void setAction()
	{
		// "buffers","timing" options are available, when "analyze" option is true. 
		this.rbAnalyzeTrue.selectedProperty().addListener
		(
			(obs,oldVal,newVal)->
			{
				if ( newVal.booleanValue() == true )
				{
					this.rbBuffersTrue.setDisable(false);
					this.rbBuffersFalse.setDisable(false);
					this.rbTimingTrue.setDisable(false);
					this.rbTimingFalse.setDisable(false);
				}
				else
				{
					this.rbBuffersTrue.setDisable(true);
					this.rbBuffersFalse.setDisable(true);
					this.rbTimingTrue.setDisable(true);
					this.rbTimingFalse.setDisable(true);
					
					this.rbBuffersFalse.setSelected(true);
					this.rbTimingFalse.setSelected(true);
				}
			}
		);
	}

	@Override
	public boolean apply() 
	{
		AppConf  appConf   = mainCtrl.getAppConf();
		
		// Explain Analyze
		if ( ((RadioButton)this.tglAnalyze.getSelectedToggle()) == this.rbAnalyzeTrue )
		{
			appConf.setPostgresExplainAnalyze( true );
		}
		else
		{
			appConf.setPostgresExplainAnalyze( false );
		}
		
		// Explain Verbose
		if ( ((RadioButton)this.tglVerbose.getSelectedToggle()) == this.rbVerboseTrue )
		{
			appConf.setPostgresExplainVerbose( true );
		}
		else
		{
			appConf.setPostgresExplainVerbose( false );
		}
		
		// Explain Costs
		if ( ((RadioButton)this.tglCosts.getSelectedToggle()) == this.rbCostsTrue )
		{
			appConf.setPostgresExplainCosts( true );
		}
		else
		{
			appConf.setPostgresExplainCosts( false );
		}
		
		// Explain Buffers
		if ( ((RadioButton)this.tglBuffers.getSelectedToggle()) == this.rbBuffersTrue )
		{
			appConf.setPostgresExplainBuffers( true );
		}
		else
		{
			appConf.setPostgresExplainBuffers( false );
		}
		
		// Explain Timing
		if ( ((RadioButton)this.tglTiming.getSelectedToggle()) == this.rbTimingTrue )
		{
			appConf.setPostgresExplainTiming( true );
		}
		else
		{
			appConf.setPostgresExplainTiming( false );
		}
		
		// Explain Format
		appConf.setPostgresExplainFormat( ((RadioButton)this.tglFormat.getSelectedToggle()).getText() );
		
		return true;
	}

}
