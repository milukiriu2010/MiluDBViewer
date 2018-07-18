package milu.gui.ctrl.imp;

import java.util.Map;
import java.util.List;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import milu.db.MyDBAbstract;
import milu.gui.ctrl.common.inf.ChangeLangInterface;
import milu.gui.ctrl.common.inf.WatchInterface;
import milu.gui.ctrl.common.table.ObjTableView;
import milu.gui.dlg.TaskDialog;
import milu.gui.view.DBView;
import milu.gui.view.FadeView;
import milu.main.MainController;
import milu.task.imp.ImportTaskResult;
import milu.tool.MyGUITool;
import milu.tool.MyServiceTool;

public class ImportDataPaneResult extends Pane
	implements
		ImportResultInterface,
		WatchInterface,
		ChangeLangInterface
{
	private DBView          dbView = null;
	
	private WizardInterface wizardInf = null;
	
	private Map<String,Object> mapObj = null;
	
	private BorderPane      basePane = new BorderPane();

    // -----------------------------------------------------
	// [Top]
    // -----------------------------------------------------
	private Label     lblTotal = new Label();
	private TextField txtTotal = new TextField();
	private Label     lblOK    = new Label();
	private TextField txtOK    = new TextField();
	private Label     lblNG    = new Label();
	private TextField txtNG    = new TextField();
	private TextField txtSQL   = new TextField();
	
    // -----------------------------------------------------
	// [Center]
    // -----------------------------------------------------
	private ObjTableView objTableView = null;
	
    // -----------------------------------------------------
	// [Bottom]
    // -----------------------------------------------------
	private Button btnCommit   = new Button();
	private Button btnRollback = new Button();
	private Button btnBack     = new Button();
	private Button btnClose    = new Button();

	// Thread Pool
	private ExecutorService service = Executors.newSingleThreadExecutor();
	
	ImportDataPaneResult( DBView dbView, WizardInterface wizardInf, Map<String,Object> mapObj )
	{
		this.dbView    = dbView;
		this.wizardInf = wizardInf;
		this.mapObj    = mapObj;
		
		MainController mainCtrl = this.dbView.getMainController();
		
	    // -----------------------------------------------------
		// [Top]
	    // -----------------------------------------------------
		this.txtTotal.setEditable(false);
		this.txtOK.setEditable(false);
		this.txtNG.setEditable(false);
		this.txtSQL.setEditable(false);
		
		HBox hBoxRS = new HBox(2);
		hBoxRS.setPadding( new Insets( 10, 10, 10, 10 ) );
		hBoxRS.setSpacing(10);
		hBoxRS.setAlignment(Pos.CENTER);
		hBoxRS.getChildren().addAll(this.lblTotal,this.txtTotal,this.lblOK,this.txtOK,this.lblNG,this.txtNG);
		this.basePane.setTop(hBoxRS);
		/*
		VBox vBoxRS = new VBox(2);
		vBoxRS.setPadding( new Insets( 10, 10, 10, 10 ) );
		vBoxRS.setSpacing(10);
		vBoxRS.getChildren().addAll(hBoxRS,this.txtSQL);
		this.basePane.setTop(vBoxRS);
		*/
		
	    // -----------------------------------------------------
		// [Center]
	    // -----------------------------------------------------
		this.objTableView = new ObjTableView( this.dbView );
		//this.objTableView.setPrefWidth(500);
		//this.objTableView.setPrefHeight(200);
		this.objTableView.prefWidthProperty().bind(this.dbView.widthProperty().multiply(0.9));
		this.objTableView.prefHeightProperty().bind(this.dbView.heightProperty().multiply(0.3));
		//this.basePane.setCenter(this.objTableView);
		
		VBox vBoxCenter = new VBox(2);
		vBoxCenter.setPadding( new Insets( 10, 10, 10, 10 ) );
		vBoxCenter.setSpacing(10);
		vBoxCenter.getChildren().addAll(this.txtSQL,this.objTableView);
		this.basePane.setTop(vBoxCenter);

		
	    // -----------------------------------------------------
		// [Bottom]
	    // -----------------------------------------------------		
		this.btnCommit.setGraphic( MyGUITool.createImageView( 20, 20, mainCtrl.getImage("file:resources/images/commit.png") ) );
		this.btnRollback.setGraphic( MyGUITool.createImageView( 20, 20, mainCtrl.getImage("file:resources/images/rollback.png") ) );
		this.btnBack.setGraphic( MyGUITool.createImageView( 20, 20, mainCtrl.getImage("file:resources/images/back.png") ) );
		this.btnClose.setGraphic( MyGUITool.createImageView( 20, 20, mainCtrl.getImage("file:resources/images/close.png") ) );
		
		HBox hBoxTran = new HBox(2);
		hBoxTran.setPadding( new Insets( 10, 10, 10, 10 ) );
		hBoxTran.setSpacing(10);
		hBoxTran.setAlignment(Pos.CENTER);
		hBoxTran.getChildren().addAll(this.btnCommit,this.btnRollback,this.btnBack,this.btnClose);
		this.basePane.setBottom(hBoxTran);
		
		this.getChildren().addAll(this.basePane);
		
		this.changeLang();
		
		this.setAction();
		
		this.importData();
	}
	
	private void setAction()
	{
		// Commit button clicked
		this.btnCommit.setOnAction((event)->{
			this.dbView.commit();
			new FadeView( "Commit" );
		});
		
		// Rollback button clicked
		this.btnRollback.setOnAction((event)->{
			this.dbView.rollback();
			new FadeView( "Rollback" );
		});
		
		// Back button clicked
		this.btnBack.setOnAction((event)->{
			this.wizardInf.prev();
		});		
		
		// Close button clicked
		this.btnClose.setOnAction((event)->{
			this.wizardInf.close();
		});
	}
	
	private void importData()
	{
		MainController mainCtrl = this.dbView.getMainController();
		
		final ImportTaskResult task = new ImportTaskResult();
		task.setImportResultInterface(this);
		task.setDBView(this.dbView);
		task.setMapObj(this.mapObj);
		
		TaskDialog taskDlg = new TaskDialog(task,mainCtrl);
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
	@SuppressWarnings("unchecked")
	private void importData2()
	{
		// --------------------------------------------------
		// Create SQL
		// --------------------------------------------------
		MyDBAbstract myDBAbs = this.dbView.getMyDBAbstract();
		GenerateSQLAbstract gsAbs = GenerateSQLFactory.getInstance(GenerateSQLFactory.TYPE.INSERT_BY_SIMPLE_WITHOUT_COMMENT);
		SchemaEntity schemaEntity = (SchemaEntity)this.mapObj.get(ImportData.DST_SCHEMA_ENTITY.val());
		String strSQL = gsAbs.generate( schemaEntity, myDBAbs );
		System.out.println( "strSQL:" + strSQL );
		
		// --------------------------------------------------
		// Create Import Data
		// --------------------------------------------------
		List<List<Object>> dataLst = (List<List<Object>>)this.mapObj.get(ImportData.IMPORT_DATA_LST.val());
		Integer skipRowCnt = (Integer)this.mapObj.get(ImportData.SKIP_ROW_COUNT.val());
		List<List<Object>> dataFilterLst = dataLst.subList(skipRowCnt.intValue(), dataLst.size());
		
		// --------------------------------------------------
		// Create ExecSQL Factory
		// --------------------------------------------------
		MainController mainCtrl = this.dbView.getMainController();
		AppConf appConf = mainCtrl.getAppConf();
		
		SQLBag sqlBag = new SQLBag();
		sqlBag.setSQL(strSQL);
		sqlBag.setCommand(SQLBag.COMMAND.TRANSACTION);
		sqlBag.setType(SQLBag.TYPE.INSERT);
		int cntOK = 0;
		int cntNG = 0;
		List<Object>       ngHeadLst = new ArrayList<>();
		ngHeadLst.add("Error Message");
		ngHeadLst.addAll((List<Object>)this.mapObj.get(ImportData.IMPORT_HEAD_LST.val()));
		List<List<Object>> ngDataLst = new ArrayList<>();
		for ( List<Object> preLst : dataFilterLst )
		{
			ExecSQLAbstract  execSQLAbs = 
				new ExecSQLFactory().createPreparedFactory( sqlBag, myDBAbs, appConf, preLst );
			try
			{
				execSQLAbs.exec(1);
				cntOK++;
				System.out.println( "OK:" + preLst );
			}
			catch ( Exception ex )
			{
				ex.printStackTrace();
				cntNG++;
				System.out.println( "NG:" + preLst );
				List<Object>  ngRow = new ArrayList<>();
				ngRow.add(ex.getMessage());
				ngRow.addAll(preLst);
				ngDataLst.add(ngRow);
			}
		}
		
		this.txtTotal.setText(String.valueOf(dataFilterLst.size()));
		this.txtOK.setText(String.valueOf(cntOK));
		this.txtNG.setText(String.valueOf(cntNG));
		this.txtSQL.setText(strSQL.replaceAll("\n|\t", " "));
		this.objTableView.setTableViewData(ngHeadLst,ngDataLst);
	}
	*/
	
	// ImportResultInterface
	@Override
	public void setTotal( int cntTotal )
	{
		this.txtTotal.setText(String.valueOf(cntTotal));
	}
	
	// ImportResultInterface
	@Override
	public void setOK( int cntOK )
	{
		this.txtOK.setText(String.valueOf(cntOK));
	}
	
	// ImportResultInterface
	@Override
	public void setNG( int cntNG )
	{
		this.txtNG.setText(String.valueOf(cntNG));
	}
	
	// ImportResultInterface
	@Override
	public void setSQL( String strSQL )
	{
		this.txtSQL.setText(strSQL.replaceAll("\n|\t", " "));
	}
	
	// ImportResultInterface
	@Override
	public void setTableViewData( List<Object> columnLst, List<List<Object>> dataLst )
	{
		this.objTableView.setTableViewData(columnLst,dataLst);
	}
	
	// WatchInterface
	@Override
	public void notify( Event event )
	{
		System.out.println( "ImportDataPaneFileTableView:notify" );
		MyServiceTool.shutdownService(this.service);
		MyDBAbstract myDBAbsSrc = (MyDBAbstract)this.mapObj.get(ImportData.SRC_DB.val());
		if ( myDBAbsSrc != null )
		{
			try
			{
				myDBAbsSrc.close();
			}
			catch ( SQLException sqlEx )
			{
			}
		}
		
	}
	
	// ChangeLangInterface
	@Override	
	public void changeLang()
	{
		MainController mainCtrl = this.dbView.getMainController();
		ResourceBundle extLangRB = mainCtrl.getLangResource("conf.lang.gui.common.NodeName");
		
		this.lblTotal.setText(extLangRB.getString( "LABEL_TOTAL" ));
		this.lblOK.setText(extLangRB.getString( "LABEL_OK" ));
		this.lblNG.setText(extLangRB.getString( "LABEL_NG" ));
		
		// ----------------------------------------------
		// ToolTip
		//   Button[Commit] 
		// ----------------------------------------------
		Tooltip tipCommit = new Tooltip( extLangRB.getString( "TOOLTIP_COMMIT" ) );
		tipCommit.getStyleClass().add("Common_MyToolTip");
		this.btnCommit.setTooltip( tipCommit );
		
		// ----------------------------------------------
		// ToolTip
		//   Button[Rollback] 
		// ----------------------------------------------
		Tooltip tipRollback = new Tooltip( extLangRB.getString( "TOOLTIP_ROLLBACK" ) );
		tipRollback.getStyleClass().add("Common_MyToolTip");
		this.btnRollback.setTooltip( tipRollback );
		
		// ----------------------------------------------
		// ToolTip
		//   Button[Back] 
		// ----------------------------------------------
		Tooltip tipBack = new Tooltip(extLangRB.getString( "TOOLTIP_BACK" ));
		tipBack.getStyleClass().add("Common_MyToolTip");
		this.btnBack.setTooltip(tipBack);
		
		// ----------------------------------------------
		// ToolTip
		//   Button[Close] 
		// ----------------------------------------------
		Tooltip tipClose = new Tooltip(extLangRB.getString( "TOOLTIP_CLOSE" ));
		tipClose.getStyleClass().add("Common_MyToolTip");
		this.btnClose.setTooltip(tipClose);
	}
}
