package milu.gui.dlg.app;

import java.util.ResourceBundle;

import javafx.scene.control.Dialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

import milu.ctrl.MainController;
import milu.db.MyDBAbstract;

public class AppPaneGeneralConf extends AppPaneAbstract 
{
	// Language Resource(from External Class)
	private ResourceBundle extLangRB = null;
	
	private Dialog<?>      dlg            = null;
	
	private MainController mainCtrl       = null;
	
	// Label of title
	private Label  lblTitle = new Label();
	
	@Override
	public void createPane( Dialog<?> dlg, MainController mainCtrl, ResourceBundle extLangRB )
	{
		this.dlg       = dlg;
		this.mainCtrl  = mainCtrl;
		this.extLangRB = extLangRB;
		
		this.setPane();
	}

	private void setPane()
	{
		this.getChildren().removeAll( this.getChildren() );
		
		// set objects
		this.lblTitle.setText( this.extLangRB.getString( "TITLE_GENERAL_CONF_PANE" ) );
		this.lblTitle.getStyleClass().add("label-title");
		
		VBox vBox = new VBox(2);
		vBox.getChildren().addAll( this.lblTitle );
		
		this.getChildren().addAll( vBox );
	}
	
	@Override
	public boolean apply() 
	{
		return true;
	}

}
