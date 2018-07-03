package milu.gui.ctrl.imp;

import java.util.Map;
import java.util.ResourceBundle;
import java.util.HashMap;
import java.util.Stack;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import milu.entity.schema.SchemaEntity;
import milu.entity.schema.search.SearchSchemaEntityInterface;
import milu.entity.schema.search.SearchSchemaEntityVisitorFactory;
import milu.gui.ctrl.common.inf.CloseInterface;
import milu.gui.ctrl.common.inf.ChangeLangInterface;
import milu.gui.view.DBView;
import milu.main.MainController;

class ImportDataPane extends Pane
	implements 
		WizardInterface,
		ChangeLangInterface 
{
	private DBView          dbView = null;
	
	private CloseInterface  closeInf = null;
	
	private Stack<Pane>     paneStack = new Stack<>();
	
	// -------------------------------------------------
	// [1:ImportDataPane]
	//   <Put>
	//     DST_SCHEMA_ENTITY => SchemaEntity
	//     TABLE_NAME_PATH   => String
	// -------------------------------------------------
	// [2:ImportDataPaneFile]
	//   <Put>
	//     SRC_FILE          => String
	// -------------------------------------------------
	// [3:ImportDataPaneFileTableView]
	//   <Get>
	//     SRC_FILE          => String
	//   <Put>
	//     IMPORT_HEAD_LST   => List<Object>
	//     IMPORT_DATA_LST   => List<List<Object>>
	//     SKIP_ROW_COUNT    => Integer
	// -------------------------------------------------
	// [4:ImportDataPaneResult]
	//   <Get>
	//     DST_SCHEMA_ENTITY => SchemaEntity
	//     IMPORT_DATA_LST   => List<List<Object>>
	//     SKIP_ROW_COUNT    => Integer
	// -------------------------------------------------
	private Map<String,Object> mapObj = new HashMap<>();
	
	private BorderPane      basePane = new BorderPane();
	
    // -----------------------------------------------------
	// [Top]
    // ----------------------------------------------------- 
	private ToggleGroup tglSrc     = new ToggleGroup();
	private RadioButton rbSrcFile  = new RadioButton();
	private RadioButton rbSrcDB    = new RadioButton();
	private Label       lblArrow   = new Label();
	private Label       lblDstDB   = new Label();
	
    // -----------------------------------------------------
	// [Center]
    // -----------------------------------------------------
	private Pane  selectedPane = null;
	
	ImportDataPane( DBView dbView, SchemaEntity dstSchemaEntity, CloseInterface closeInf )
	{
		this.dbView   = dbView;
		this.closeInf = closeInf;
		this.mapObj.put( ImportData.DST_SCHEMA_ENTITY.val(), dstSchemaEntity );
		
		// ---------------------------------------------------
		// Get Table Name
		//---------------------------------------------------
		String tableName = dstSchemaEntity.getName();
		// Search [SCHEMA]
		SearchSchemaEntityInterface searchSchemaVisitor = new SearchSchemaEntityVisitorFactory().createInstance(SchemaEntity.SCHEMA_TYPE.SCHEMA);
		dstSchemaEntity.acceptParent(searchSchemaVisitor);
		SchemaEntity hitSchemaEntity = searchSchemaVisitor.getHitSchemaEntity();
		String schemaName = "";
		if ( hitSchemaEntity != null )
		{
			schemaName = hitSchemaEntity.getName() + ".";
		}
		String tableNamePath = schemaName + tableName;
		this.mapObj.put( ImportData.TABLE_NAME_PATH.val(), tableNamePath );
		this.lblDstDB.setText( tableNamePath );
		
		this.setPane();
		
		this.getChildren().add(this.basePane);
		
		this.basePane.prefHeightProperty().bind(this.heightProperty());
		this.basePane.prefWidthProperty().bind(this.widthProperty());
		
		this.setAction();
		
		this.changeLang();
	}
	
	private void setPane()
	{
		this.rbSrcFile.setToggleGroup(tglSrc);
		this.rbSrcDB.setToggleGroup(tglSrc);
		this.rbSrcDB.setDisable(true);
		this.rbSrcFile.setSelected(true);
		
		HBox hBoxSrc = new HBox(2);
		hBoxSrc.setPadding( new Insets( 10, 10, 10, 10 ) );
		hBoxSrc.setSpacing(10);
		hBoxSrc.getChildren().addAll(this.rbSrcFile,this.rbSrcDB,this.lblArrow,this.lblDstDB);
		
		this.basePane.setTop(hBoxSrc);
		
		this.selectedPane = new ImportDataPaneFile( this.dbView, this, this.mapObj );
		this.basePane.setCenter(this.selectedPane);
	}
	
	private void setAction()
	{
		this.tglSrc.selectedToggleProperty().addListener((obs,oldVal,newVal)->{
			if ( newVal == this.rbSrcFile )
			{
				this.selectedPane = new ImportDataPaneFile( this.dbView, this, this.mapObj );
			}
			else if ( newVal == this.rbSrcDB )
			{
				this.selectedPane = new ImportDataPaneDB( this.dbView, this, this.mapObj );
			}
			this.basePane.setCenter(this.selectedPane);
			this.paneStack.clear();
		});
	}
	
	// WizardInterface
	@Override
	public void next( Pane pane, Map<String,Object> mapObj )
	{
		this.paneStack.push(pane);
		//this.basePane.setTop(null);
		this.basePane.setCenter(null);
		if ( pane instanceof ImportDataPaneFile )
		{
			this.selectedPane = new ImportDataPaneFileTableView( this.dbView, this, mapObj );
		}
		else if ( pane instanceof ImportDataPaneFileTableView )
		{
			this.selectedPane = new ImportDataPaneResult( this.dbView, this, mapObj );
		}
		this.basePane.setCenter(this.selectedPane);
	}
	
	// WizardInterface
	@Override
	public void prev()
	{
		Pane prevPane = this.paneStack.pop();
		this.basePane.setCenter(prevPane);
	}
	
	// WizardInterface
	@Override
	public void close()
	{
		this.closeInf.closeRequest(null);
	}
	
	// ChangeLangInterface
	@Override
	public void changeLang() 
	{
		MainController mainCtrl = this.dbView.getMainController();
		ResourceBundle langRB = mainCtrl.getLangResource("conf.lang.gui.ctrl.imp.ImportDataTab");
		ResourceBundle extLangRB = mainCtrl.getLangResource("conf.lang.gui.common.NodeName");
		
		this.rbSrcFile.setText(extLangRB.getString("LABEL_FILE"));
		this.rbSrcDB.setText(extLangRB.getString("LABEL_DB"));
		this.lblArrow.setText(langRB.getString("LABEL_IMPORT_ARROW"));

		if ( selectedPane instanceof ChangeLangInterface )
		{
			((ChangeLangInterface)selectedPane).changeLang();
		}
	}
}
