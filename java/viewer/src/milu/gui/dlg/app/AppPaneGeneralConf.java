package milu.gui.dlg.app;

import java.util.ResourceBundle;

import javafx.scene.control.Dialog;
import javafx.scene.layout.VBox;

import milu.ctrl.MainController;

public class AppPaneGeneralConf extends AppPaneAbstract 
{
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
