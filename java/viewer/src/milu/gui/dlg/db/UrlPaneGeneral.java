package milu.gui.dlg.db;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Dialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import milu.db.MyDBAbstract;
import milu.gui.ctrl.common.PersistentButtonToggleGroup;
import milu.main.MainController;

public class UrlPaneGeneral extends UrlPaneAbstract
{	
	private Dialog<?>      dlg          = null;
	
	private MainController mainCtrl     = null;
	
	private MyDBAbstract   myDBAbs      = null;
	
	private HBox           hBoxToggle     = new HBox(2);
	
	private ToggleGroup    tglGroup       = new PersistentButtonToggleGroup();
	
	// ToggleButton for Free Hand
	private ToggleButton   tglBtnFreeHand = new ToggleButton();
	
	// ----------------------------------------------------
	// Items for "All"
	// ----------------------------------------------------
	// field for URL
	private TextArea   urlTextArea = new TextArea();
	
	@Override
	public void createPane( Dialog<?> dlg, MainController mainCtrl, MyDBAbstract myDBAbs, Map<String,String> mapProp )
	{
		this.dlg       = dlg;
		this.mainCtrl  = mainCtrl;
		this.myDBAbs   = myDBAbs;
		
		ResourceBundle extLangRB = this.mainCtrl.getLangResource("conf.lang.gui.dlg.db.DBSettingDialog");
		
		// ToggleButton for Free Hand
		this.tglBtnFreeHand.setText(extLangRB.getString("TOGGLE_FREE"));
		this.tglBtnFreeHand.setToggleGroup( this.tglGroup );
		
		this.hBoxToggle.getChildren().addAll( this.tglBtnFreeHand );
		
		this.tglBtnFreeHand.setSelected(true);
		this.setPaneFreeHand();
		
		this.setAction();
		
		this.setUrlTextArea();
	}
	
	private void setAction()
	{
		// --------------------------------------------
		// Selected Toggle Button is changed
		// --------------------------------------------
		this.tglGroup.selectedToggleProperty().addListener
		(
			(obs,oldVal,newVal)->
			{
				if ( newVal == this.tglBtnFreeHand )
				{
					this.setPaneFreeHand();
				}
				Stage stage = (Stage)this.dlg.getDialogPane().getScene().getWindow();
				stage.sizeToScene();
			}
		);
	}
	
	private void setPaneFreeHand()
	{
		this.getChildren().removeAll( this.getChildren() );
		
		this.urlTextArea.setEditable( true );
		this.urlTextArea.setWrapText(true);
		
		Label lblUrl = new Label("JDBC URL");
		
		VBox vBox = new VBox(2);
		vBox.getChildren().addAll
		( 
			this.hBoxToggle,
			lblUrl,
			this.urlTextArea 
		);
		
		this.getChildren().addAll( vBox );
	}
	
	@Override
	public Map<String,String> getProp()
	{
		Map<String,String> dbOptMap = new HashMap<String,String>();
		
		return dbOptMap;
	}

	@Override
	public void setUrl() 
	{
		if ( this.tglBtnFreeHand.isSelected() )
		{
			this.myDBAbs.setUrl( this.urlTextArea.getText() );
		}
	}
	
	private void setUrlTextArea()
	{
		this.setUrl();
		this.urlTextArea.setText( this.myDBAbs.getUrl() );
	}
	
}
