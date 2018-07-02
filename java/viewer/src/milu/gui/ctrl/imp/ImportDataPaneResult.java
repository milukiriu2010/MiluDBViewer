package milu.gui.ctrl.imp;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;

import milu.db.MyDBAbstract;
import milu.db.access.ExecSQLAbstract;
import milu.db.access.ExecSQLFactory;
import milu.entity.schema.SchemaEntity;
import milu.ctrl.sql.generate.GenerateSQLAbstract;
import milu.ctrl.sql.generate.GenerateSQLFactory;
import milu.ctrl.sql.parse.SQLBag;
import milu.gui.ctrl.common.inf.ChangeLangInterface;
import milu.gui.ctrl.common.table.ObjTableView;
import milu.gui.view.DBView;
import milu.gui.view.FadeView;
import milu.main.AppConf;
import milu.main.MainController;
import milu.tool.MyTool;

public class ImportDataPaneResult extends Pane
	implements
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
		
		HBox hBoxRS = new HBox(2);
		hBoxRS.setPadding( new Insets( 10, 10, 10, 10 ) );
		hBoxRS.setSpacing(10);
		hBoxRS.setAlignment(Pos.CENTER);
		hBoxRS.getChildren().addAll(this.lblTotal,this.txtTotal,this.lblOK,this.txtOK,this.lblNG,this.txtNG);
		this.basePane.setTop(hBoxRS);
		
	    // -----------------------------------------------------
		// [Center]
	    // -----------------------------------------------------
		this.objTableView = new ObjTableView( this.dbView );
		this.objTableView.setPrefWidth(500);
		this.objTableView.setPrefHeight(200);
		this.basePane.setCenter(this.objTableView);
		
	    // -----------------------------------------------------
		// [Bottom]
	    // -----------------------------------------------------		
		this.btnCommit.setGraphic( MyTool.createImageView( 20, 20, mainCtrl.getImage("file:resources/images/commit.png") ) );
		this.btnRollback.setGraphic( MyTool.createImageView( 20, 20, mainCtrl.getImage("file:resources/images/rollback.png") ) );
		this.btnBack.setGraphic( MyTool.createImageView( 20, 20, mainCtrl.getImage("file:resources/images/back.png") ) );
		this.btnClose.setGraphic( MyTool.createImageView( 20, 20, mainCtrl.getImage("file:resources/images/close.png") ) );
		
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
		// --------------------------------------------------
		// Create SQL
		// --------------------------------------------------
		MyDBAbstract myDBAbs = this.dbView.getMyDBAbstract();
		GenerateSQLAbstract gsAbs = GenerateSQLFactory.getInstance(GenerateSQLFactory.TYPE.INSERT_BY_SIMPLE);
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
		this.objTableView.setTableViewData(ngHeadLst,ngDataLst);
	}
	
	// ChangeLangInterface
	@Override	
	public void changeLang()
	{
		MainController mainCtrl = this.dbView.getMainController();
		ResourceBundle langRB    = mainCtrl.getLangResource("conf.lang.gui.ctrl.imp.ImportDataTab");
		ResourceBundle extLangRB = mainCtrl.getLangResource("conf.lang.gui.ctrl.menu.MainToolBar");
		
		this.lblTotal.setText(langRB.getString( "LABEL_TOTAL" ));
		this.lblOK.setText(langRB.getString( "LABEL_OK" ));
		this.lblNG.setText(langRB.getString( "LABEL_NG" ));
		
		// ----------------------------------------------
		// ToolTip
		//   Button[Commit] 
		// ----------------------------------------------
		Tooltip tipCommit = new Tooltip( extLangRB.getString( "TIP_COMMIT" ) );
		tipCommit.getStyleClass().add("MainToolBar_MyToolTip");
		this.btnCommit.setTooltip( tipCommit );
		
		// ----------------------------------------------
		// ToolTip
		//   Button[Rollback] 
		// ----------------------------------------------
		Tooltip tipRollback = new Tooltip( extLangRB.getString( "TIP_ROLLBACK" ) );
		tipRollback.getStyleClass().add("MainToolBar_MyToolTip");
		this.btnRollback.setTooltip( tipRollback );
	}
}
