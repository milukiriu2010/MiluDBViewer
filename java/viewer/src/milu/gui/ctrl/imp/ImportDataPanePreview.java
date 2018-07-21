package milu.gui.ctrl.imp;

import java.util.Map;
import java.util.ResourceBundle;
import java.util.List;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import milu.gui.ctrl.common.inf.ChangeLangInterface;
import milu.gui.ctrl.common.inf.WatchInterface;
import milu.gui.ctrl.common.table.ObjTableView;
import milu.gui.dlg.TaskDialog;
import milu.gui.view.DBView;
import milu.main.MainController;
import milu.task.imp.ImportTaskPreviewFactory;
import milu.tool.MyGUITool;

public class ImportDataPanePreview extends Pane
	implements
		ImportPreviewInterface,
		WatchInterface,
		ChangeLangInterface 
{
	private DBView          dbView = null;
	
	private WizardInterface wizardInf = null;
	
	private Map<String,Object> mapObj = null;
	
	private Integer  skipRowCount = Integer.valueOf(0);
	
	private BorderPane      basePane = new BorderPane();
	
    // -----------------------------------------------------
	// [Top]
    // -----------------------------------------------------
	private Label      lblSkipRowCnt = new Label();
	private TextField  txtSkipRowCnt = new TextField(String.valueOf(this.skipRowCount));
	
    // -----------------------------------------------------
	// [Center]
    // -----------------------------------------------------
	private TextField    txtWarn      = new TextField();
	private ObjTableView objTableView = null;

    // -----------------------------------------------------
	// [Bottom]
    // -----------------------------------------------------
	private Button btnImport = new Button();
	private Button btnBack   = new Button();
	
	/*
	// Thread Pool
	private ExecutorService service = Executors.newSingleThreadExecutor();
	*/
	
	public enum ERROR_TYPE
	{
		ERROR_NO,
		ERROR_COLUMN_CNT
	}

	ImportDataPanePreview( DBView dbView, WizardInterface wizardInf, Map<String,Object> mapObj )
	{
		this.dbView    = dbView;
		this.wizardInf = wizardInf;
		this.mapObj    = mapObj;
		
		MainController mainCtrl = this.dbView.getMainController();
		
		
	    // -----------------------------------------------------
		// [Top]
	    // -----------------------------------------------------
		HBox hBoxSkip = new HBox(2);
		hBoxSkip.setPadding( new Insets( 10, 10, 10, 10 ) );
		hBoxSkip.setSpacing(10);
		//hBoxSkip.setAlignment(Pos.BOTTOM_LEFT);
		hBoxSkip.getChildren().addAll(this.lblSkipRowCnt,this.txtSkipRowCnt);
		this.basePane.setTop(hBoxSkip);
		
	    // -----------------------------------------------------
		// [Center]
	    // -----------------------------------------------------
		this.txtWarn.setEditable(false);
		this.txtWarn.prefWidthProperty().bind(this.dbView.widthProperty().multiply(0.8));
		this.objTableView = new ObjTableView( this.dbView );
		//this.objTableView.setPrefWidth(500);
		//this.objTableView.setPrefHeight(200);
		this.objTableView.prefWidthProperty().bind(this.dbView.widthProperty().multiply(0.9));
		this.objTableView.prefHeightProperty().bind(this.dbView.heightProperty().multiply(0.5));
		
		VBox vBoxCenter = new VBox(2);
		vBoxCenter.setPadding( new Insets( 10, 10, 10, 10 ) );
		vBoxCenter.setSpacing(10);
		vBoxCenter.getChildren().addAll(this.txtWarn,this.objTableView);
		this.basePane.setCenter(vBoxCenter);
		
	    // -----------------------------------------------------
		// [Bottom]
	    // -----------------------------------------------------
		this.btnImport.setDisable(true);
		this.btnBack.setGraphic( MyGUITool.createImageView( 20, 20, mainCtrl.getImage("file:resources/images/back.png") ) );
		
		HBox hBoxNext = new HBox(2);
		hBoxNext.setPadding( new Insets( 10, 10, 10, 10 ) );
		hBoxNext.setSpacing(10);
		hBoxNext.setAlignment(Pos.BOTTOM_RIGHT);
		hBoxNext.getChildren().addAll(this.btnBack,this.btnImport);
		this.basePane.setBottom(hBoxNext);
		
		this.getChildren().addAll(this.basePane);
		
		this.setAction();
		
		this.loadData();
		
		this.changeLang();
	}
	
	private void setAction()
	{
		this.txtSkipRowCnt.textProperty().addListener((obs,oldVal,newVal)->{
			if ( newVal == null )
			{
				this.skipRowCount = Integer.valueOf(0);
			}
			// "Numeric" or "No Input" are allowed.
			else if ( newVal.length() == 0 )
			{
				this.skipRowCount = Integer.valueOf(0);
				this.objTableView.setSkipRowCnt(0);
			}
			// if alphabets or marks are input, back to previous input.
			else if ( newVal.matches( "^[0-9]+$" ) == false )
			{
				((StringProperty)obs).setValue( oldVal );
			}
			else
			{
				System.out.println( "change skipRowCnt" );
				this.skipRowCount = Integer.valueOf(this.txtSkipRowCnt.getText());
				this.objTableView.setSkipRowCnt(this.skipRowCount);
			}
		});
		
		this.btnImport.setOnAction((event)->{
			this.mapObj.put( ImportData.IMPORT_HEAD_LST.val(), this.objTableView.getHeadList() );
			this.mapObj.put( ImportData.IMPORT_DATA_LST.val(), this.objTableView.getDataList() );
			this.mapObj.put( ImportData.SKIP_ROW_COUNT.val(), this.skipRowCount );
			this.wizardInf.next( this, mapObj );
		});
		
		this.btnBack.setOnAction((event)->{
			this.wizardInf.prev();
		});
		/*
		Platform.runLater(()->{
			this.getScene().getWindow().setOnCloseRequest((event)->{
				MyServiceTool.shutdownService(this.service);
			});
		});
		*/
	}
	
	private void loadData()
	{
		MainController mainCtrl = this.dbView.getMainController();
		ImportDataPane.SRC_TYPE srcType = (ImportDataPane.SRC_TYPE)this.mapObj.get(ImportData.SRC_TYPE.val());
		final Task<Exception> task = 
				ImportTaskPreviewFactory.createFactory( srcType, this, this.dbView, this.mapObj );
		if ( task == null )
		{
			return;
		}
		TaskDialog taskDlg = new TaskDialog(task,mainCtrl,null);
		taskDlg.showAndWait();
		/*
		// execute task
		this.service.submit(task);
		
		task.progressProperty().addListener((obs,oldVal,newVal)->{
			if ( newVal.doubleValue() == 1.0 )
			{
			}
		});
		
		task.messageProperty().addListener((obs,oldVal,msg)->{
			this.wizardInf.setMsg(msg);
		});	

		task.valueProperty().addListener((obs,oldVal,ex)->{
			MyGUITool.showException( mainCtrl, "conf.lang.gui.common.MyAlert", "TITLE_MISC_ERROR", ex );
		});
		*/
	}
	
	/*
	private void loadData2()
	{
		//mapObj.forEach( (k,v)->System.out.println( "mapObj:k[" + k + "]v[" + v + "]" ));
		ImportDataPane.SRC_TYPE srcType = (ImportDataPane.SRC_TYPE)this.mapObj.get(ImportData.SRC_TYPE.val());
		if ( ImportDataPane.SRC_TYPE.FILE.equals(srcType) )
		{
			this.loadDataFile();
		}
		else if ( ImportDataPane.SRC_TYPE.DB.equals(srcType) )
		{
			this.loadDataDB();
		}
		else
		{
			System.out.println( "SRC_TYPE not recognized." );
		}
	}
	*/
	/*
	private void loadDataFile()
	{
		System.out.println( "Load data from file." );
		
		SchemaEntity dstSchemaEntity = (SchemaEntity)this.mapObj.get(ImportData.DST_SCHEMA_ENTITY.val());
		int columnCnt = dstSchemaEntity.getDefinitionLst().size();
		List<Object> columnLst = dstSchemaEntity.getDefinitionLst().stream()
			.map( data->data.get("column_name") )
			.collect(Collectors.toList());
		
		MainController mainCtrl = this.dbView.getMainController();
		String strFile = (String)this.mapObj.get(ImportData.SRC_FILE.val());
		File file = new File(strFile);
		MyFileImportAbstract myFileAbs = MyFileImportFactory.getInstance(file);
		try
		{
			myFileAbs.open(file);
			myFileAbs.load(columnCnt);
			List<List<Object>> dataLst = myFileAbs.getDataLst();
			System.out.println( "columnLst.size:" + columnLst.size() );
			System.out.println( "dataLst.size  :" + dataLst.size() );
			
			this.objTableView.setTableViewData(columnLst, dataLst);
		}
		catch ( Exception ex )
		{
			MyGUITool.showException( mainCtrl, "conf.lang.gui.common.MyAlert", "TITLE_MISC_ERROR", ex );
		}
		finally
		{
			try
			{
				myFileAbs.close();
			}
			catch ( IOException ioEx )
			{
			}
		}
	}
	*/
	/*
	private void loadDataDB()
	{
		System.out.println( "Load data from DB." );
		
		SchemaEntity dstSchemaEntity = (SchemaEntity)this.mapObj.get(ImportData.DST_SCHEMA_ENTITY.val());
		//int columnCnt = dstSchemaEntity.getDefinitionLst().size();
		List<Object> columnLst = dstSchemaEntity.getDefinitionLst().stream()
			.map( data->data.get("column_name") )
			.collect(Collectors.toList());
		
		MainController mainCtrl = this.dbView.getMainController();
		AppConf appConf = mainCtrl.getAppConf();
		MyDBAbstract myDBAbsSrc = (MyDBAbstract)this.mapObj.get(ImportData.SRC_DB.val());
		SchemaEntity srcSchemaEntity = (SchemaEntity)this.mapObj.get(ImportData.SRC_SCHEMA_ENTITY.val());
		
		// --------------------------------------------------
		// Create SQL
		// --------------------------------------------------
		GenerateSQLAbstract gsAbs = GenerateSQLFactory.getInstance(GenerateSQLFactory.TYPE.SELECT);
		String strSQL = gsAbs.generate(srcSchemaEntity, myDBAbsSrc );
		
		SQLBag sqlBag = new SQLBag();
		sqlBag.setSQL(strSQL);
		sqlBag.setCommand(SQLBag.COMMAND.QUERY);
		sqlBag.setType(SQLBag.TYPE.SELECT);
		
		ExecSQLAbstract  execSQLAbs = 
				new ExecSQLFactory().createFactory( sqlBag, myDBAbsSrc, appConf, null, 0.0 );
		try
		{
			execSQLAbs.exec(appConf.getFetchMax());
		}
		catch ( MyDBOverFetchSizeException myEx )
		{
		}
		catch ( SQLException sqlEx )
		{
			MyGUITool.showException( mainCtrl, "conf.lang.gui.common.MyAlert", "TITLE_MISC_ERROR", sqlEx );
		}
		catch ( Exception ex )
		{
			MyGUITool.showException( mainCtrl, "conf.lang.gui.common.MyAlert", "TITLE_MISC_ERROR", ex );			
		}
		finally
		{
			List<Object>       headLst = execSQLAbs.getColNameLst();
			List<List<Object>> dataLst = execSQLAbs.getDataLst();
			//this.objTableView.setTableViewData(columnLst, dataLst);
			this.objTableView.setTableViewData(headLst, dataLst);
			
			if ( columnLst.size() != headLst.size() ) 
			{
				this.btnImport.setDisable(true);
				this.txtWarn.setText("Column count doesn't match.");
			}
			else
			{
				this.btnImport.setDisable(false);
			}
		}

	}
	*/
	// ImportPreviewInterface
	@Override
	public void setTableViewData( List<Object> columnLst, List<List<Object>> dataLst )
	{
		this.objTableView.setTableViewData(columnLst, dataLst);
	}
	
	// ImportPreviewInterface
	@Override
	public void setErrorType( ImportDataPanePreview.ERROR_TYPE errorType )
	{
		MainController mainCtrl = this.dbView.getMainController();
		ResourceBundle langRB = mainCtrl.getLangResource("conf.lang.gui.ctrl.imp.ImportDataTab");
		
		if ( ERROR_TYPE.ERROR_NO.equals(errorType) )
		{
			this.btnImport.setDisable(false);
			this.txtWarn.setText("");
		}
		else if ( ERROR_TYPE.ERROR_COLUMN_CNT.equals(errorType) ) 
		{
			this.btnImport.setDisable(true);
			this.txtWarn.setText( langRB.getString("MSG_ERROR_COLUMN_CNT") );
		}
	}
	
	// WatchInterface
	@Override
	public void notify( Event event )
	{
		System.out.println( "ImportDataPaneFileTableView:notify" );
		//MyServiceTool.shutdownService(this.service);
	}
	
	// ChangeLangInterface
	@Override
	public void changeLang() 
	{
		MainController mainCtrl = this.dbView.getMainController();
		ResourceBundle langRB = mainCtrl.getLangResource("conf.lang.gui.ctrl.imp.ImportDataTab");
		ResourceBundle extLangRB = mainCtrl.getLangResource("conf.lang.gui.common.NodeName");
		
		this.lblSkipRowCnt.setText(langRB.getString("LABEL_SKIP_ROW_CNT"));
		
		// "import" button
		this.btnImport.setGraphic( MyGUITool.createImageView( 16, 16, mainCtrl.getImage("file:resources/images/next.png") ));
		Tooltip tipImport = new Tooltip(langRB.getString( "TOOLTIP_IMPORT" ));
		tipImport.getStyleClass().add("Common_MyToolTip");
		this.btnImport.setTooltip(tipImport);
		
		// "back" button
		this.btnBack.setGraphic( MyGUITool.createImageView( 16, 16, mainCtrl.getImage("file:resources/images/back.png") ));
		Tooltip tipBack = new Tooltip(extLangRB.getString( "TOOLTIP_BACK" ));
		tipBack.getStyleClass().add("Common_MyToolTip");
		this.btnBack.setTooltip(tipBack);
		
	}

}
