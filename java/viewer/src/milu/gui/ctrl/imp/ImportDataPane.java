package milu.gui.ctrl.imp;

import javafx.scene.layout.Pane;

import java.util.Map;

import javafx.geometry.Insets;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import milu.entity.schema.SchemaEntity;
import milu.gui.view.DBView;

class ImportDataPane extends Pane
	implements WizardInterface
{
	private DBView          dbView = null;
	
	SchemaEntity            dstSchemaEntity = null;
	
	private BorderPane      basePane = new BorderPane();
	
    // -----------------------------------------------------
	// [Top]
    // ----------------------------------------------------- 
	private ToggleGroup tglSrc     = new ToggleGroup();
	private RadioButton rbSrcFile  = new RadioButton("File");
	private RadioButton rbSrcDB    = new RadioButton("DB");
	
    // -----------------------------------------------------
	// [Center]
    // -----------------------------------------------------
	private Pane  selectedPane = null;
	
	ImportDataPane( DBView dbView, SchemaEntity dstSchemaEntity )
	{
		this.dbView = dbView;
		this.dstSchemaEntity = dstSchemaEntity;
		
		this.setPane();
		
		this.getChildren().add(this.basePane);
		
		this.setAction();
	}
	
	private void setPane()
	{
		rbSrcFile.setToggleGroup(tglSrc);
		rbSrcDB.setToggleGroup(tglSrc);
		rbSrcFile.setSelected(true);
		
		HBox hBoxSrc = new HBox(2);
		hBoxSrc.setPadding( new Insets( 10, 10, 10, 10 ) );
		hBoxSrc.setSpacing(10);
		hBoxSrc.getChildren().addAll(rbSrcFile,rbSrcDB);
		
		this.basePane.setTop(hBoxSrc);
		
		this.selectedPane = new ImportDataPaneFile( this.dbView, this );
		this.basePane.setCenter(this.selectedPane);
	}
	
	private void setAction()
	{
		this.tglSrc.selectedToggleProperty().addListener((obs,oldVal,newVal)->{
			if ( newVal == this.rbSrcFile )
			{
				this.selectedPane = new ImportDataPaneFile( this.dbView, this );
			}
			else if ( newVal == this.rbSrcDB )
			{
				this.selectedPane = new ImportDataPaneDB( this.dbView, this );
			}
			this.basePane.setCenter(this.selectedPane);
		});
	}
	
	// WizardInterface
	@Override
	public void next( Pane pane, Map<String,Object> mapObj )
	{
		this.basePane.setTop(null);
		this.basePane.setCenter(null);
		if ( pane instanceof ImportDataPaneFile )
		{
			this.selectedPane = new ImportDataPaneFileTableView( this.dbView, this, mapObj );
		}
		this.basePane.setCenter(this.selectedPane);
	}
}
