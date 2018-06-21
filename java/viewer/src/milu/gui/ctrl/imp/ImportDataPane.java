package milu.gui.ctrl.imp;

import javafx.scene.layout.Pane;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import milu.gui.view.DBView;

class ImportDataPane extends Pane 
{
	private DBView          dbView = null;
	
	private BorderPane      basePane = new BorderPane();
	
    // -----------------------------------------------------
	// [Top]
    // ----------------------------------------------------- 
	private ToggleGroup tglSrc     = new ToggleGroup();
	private RadioButton rbSrcFile  = new RadioButton("File");
	private RadioButton rbSrcDB    = new RadioButton("DB");
	
	ImportDataPane( DBView dbView )
	{
		this.dbView = dbView;
		
		this.setPane();
		
		this.getChildren().add(this.basePane);
	}
	
	private void setPane()
	{
		rbSrcFile.setToggleGroup(tglSrc);
		rbSrcDB.setToggleGroup(tglSrc);
		
		HBox hBoxSrc = new HBox(2);
		hBoxSrc.getChildren().addAll(rbSrcFile,rbSrcDB);
		
		this.basePane.setTop(hBoxSrc);
	}
}
