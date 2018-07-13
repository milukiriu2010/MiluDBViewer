package milu.gui.ctrl.imp;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javafx.scene.control.MenuBar;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.event.Event;

import milu.db.MyDBAbstract;
import milu.entity.schema.SchemaEntity;
import milu.entity.schema.search.SearchSchemaEntityInterface;
import milu.entity.schema.search.SearchSchemaEntityVisitorFactory;
import milu.gui.ctrl.common.inf.WatchInterface;
import milu.gui.ctrl.common.inf.ChangeLangInterface;
import milu.gui.ctrl.menu.AfterDBConnectedInterface;
import milu.main.AppConst;
import milu.main.MainController;
import milu.task.collect.CollectTaskFactory;
import milu.tool.MyFileTool;
import milu.tool.MyGUITool;
import milu.tool.MyServiceTool;
import milu.gui.view.DBView;

public class ImportDataPaneDB extends Pane 
	implements
		AfterDBConnectedInterface,
		WatchInterface,
		ChangeLangInterface
{
	private DBView          dbView = null;
	
	private WizardInterface wizardInf = null;
	
	private Map<String,Object> mapObj = null;
	
	private BorderPane      basePane = new BorderPane();
	
    // -----------------------------------------------------
	// [Center]
    // -----------------------------------------------------
	
	private MenuBar          menuBar = new MenuBar();
	private Menu             menuBookMark = new Menu("Select DB");
	private TextField        txtUrl = new TextField();
	private Label            lblTable  = new Label("Table");
	private ComboBox<SchemaEntity> cmbSchema = new ComboBox<>();
	private ComboBox<SchemaEntity> cmbTable  = new ComboBox<>();
	
    // -----------------------------------------------------
	// [Bottom]
    // ----------------------------------------------------- 
	private Button     btnNext    = new Button();
	
	private MyDBAbstract  myDBAbs = null;
	
	// Thread Pool
	private ExecutorService service = Executors.newSingleThreadExecutor();	
	
	ImportDataPaneDB( DBView dbView, WizardInterface wizardInf, Map<String,Object> mapObj )
	{
		this.dbView = dbView;
		this.wizardInf = wizardInf;
		this.mapObj = mapObj;
				
	    // -----------------------------------------------------
		// [Center]
	    // ----------------------------------------------------- 
		this.menuBar.getMenus().add(this.menuBookMark);
		this.menuBookMark.getItems().addAll( new MenuItem("dummy") );
		this.txtUrl.setEditable(false);
		//this.txtUrl.setPrefWidth(500);
		this.txtUrl.prefWidthProperty().bind(this.dbView.widthProperty().multiply(0.75));
		
		HBox hBoxTbl = new HBox(2);
		hBoxTbl.getChildren().addAll( this.cmbSchema, this.cmbTable );
		
		GridPane gridSrc = new GridPane();
		gridSrc.setHgap(5);
		gridSrc.setVgap(2);
		gridSrc.setPadding( new Insets( 10, 10, 10, 10 ) );
		gridSrc.add( this.menuBar , 0, 1 );
		gridSrc.add( this.txtUrl  , 1, 1 );
		gridSrc.add( this.lblTable, 0, 2 );
		gridSrc.add( hBoxTbl      , 1, 2 );
		
		this.basePane.setCenter(gridSrc);
		
	    // -----------------------------------------------------
		// [Bottom]
	    // ----------------------------------------------------- 
		HBox hBoxBtm = new HBox(2);
		hBoxBtm.setPadding( new Insets( 10, 10, 10, 10 ) );
		hBoxBtm.setSpacing(10);
		Pane spacer = new Pane();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		spacer.setMinSize(10,1);
		
		this.btnNext.setDisable(true);
		hBoxBtm.getChildren().addAll( spacer, this.btnNext );
		
		this.basePane.setBottom(hBoxBtm);
		
		this.setAction();
		
		this.getChildren().add(this.basePane);
		
		this.changeLang();
	}
	
	private void setAction()
	{
	    this.menuBookMark.setOnShowing((event)->{
	    	this.menuBookMark.getItems().removeAll(this.menuBookMark.getItems());
	    	try
	    	{
	    		MyFileTool.createMenuBookMark( Paths.get(AppConst.DB_DIR.val()), this.menuBookMark, this.dbView, this );
	    	}
	    	catch ( IOException ioEx )
	    	{
	    		throw new RuntimeException(ioEx);
	    	}
	    });
	    
		this.btnNext.setOnAction((event)->{
			SchemaEntity schemaEntity = this.cmbSchema.getSelectionModel().getSelectedItem();
			SchemaEntity tableEntity  = this.cmbTable.getSelectionModel().getSelectedItem();
			String tableName = "";
			if ( schemaEntity != null )
			{
				tableName = schemaEntity.getName() + ".";
			}
			if ( tableEntity != null )
			{
				tableName = tableName + tableEntity.getName();
			}
			
			this.mapObj.put( ImportData.SRC_SCHEMA_ENTITY.val(), tableEntity );
			this.mapObj.put( ImportData.SRC_DB.val()   , this.myDBAbs );
			this.mapObj.put( ImportData.SRC_TABLE.val(), tableName );
			this.wizardInf.next( this, this.mapObj );
		});
		
		Platform.runLater(()->{
			this.getScene().getWindow().setOnCloseRequest((event)->{
				MyServiceTool.shutdownService(this.service);
			});
		});
	}
	
	// AfterDBConnectedInterface
	@Override
	public void afterConnection( MyDBAbstract myDBAbs )
	{
		if ( this.myDBAbs != null )
		{
			try
			{
				this.myDBAbs.close();
			}
			catch ( SQLException sqlEx )
			{
			}
			finally
			{
				this.myDBAbs = null;
			}
		}
		this.myDBAbs = myDBAbs;
		this.txtUrl.setText(this.myDBAbs.getUrl());
		
		MainController mainCtrl = this.dbView.getMainController();
		final Task<Exception> collectTask = CollectTaskFactory.getInstanceForTableLst( mainCtrl, this.myDBAbs );
		if ( collectTask == null )
		{
			return;
		}
		// execute task
		this.service.submit( collectTask );
		
		collectTask.progressProperty().addListener((obs,oldVal,newVal)->{
			if ( newVal.doubleValue() == 1.0 )
			{
				this.cmbSchema.getItems().removeAll(this.cmbSchema.getItems());
				this.cmbTable.getItems().removeAll(this.cmbTable.getItems());
				
				// ------------------------------------------------------
				// set "Schema List" on ComboBox
				// ------------------------------------------------------
				SchemaEntity rootEntity = this.myDBAbs.getSchemaRoot();
				List<SchemaEntity> entityLst = rootEntity.getEntityLst();
				List<SchemaEntity> schemaEntityLst = entityLst.stream()
					.filter((entity)->{ 
						if ( SchemaEntity.SCHEMA_TYPE.SCHEMA.equals(entity.getType()) )
						{
							return true;
						}
						else
						{
							return false;
						}
					})
					.collect(Collectors.toList());
				this.cmbSchema.getItems().addAll(schemaEntityLst);
				if ( schemaEntityLst.size() == 0 )
				{
					SearchSchemaEntityInterface sseVisitorT = new SearchSchemaEntityVisitorFactory().createInstance( SchemaEntity.SCHEMA_TYPE.ROOT_TABLE );
					this.myDBAbs.getSchemaRoot().accept(sseVisitorT);
					SchemaEntity tableRootEntity = sseVisitorT.getHitSchemaEntity();
					if ( tableRootEntity != null )
					{
						this.cmbTable.getItems().removeAll(this.cmbTable.getItems());
						this.cmbTable.getItems().addAll(tableRootEntity.getEntityLst());
					}
				}
			}
		});

		collectTask.messageProperty().addListener((obs,oldVal,msg)->{
			System.out.println( "CollectTask:Message[" + msg + "]" );
			this.wizardInf.setMsg(msg);
		});	
		
		this.cmbSchema.valueProperty().addListener((obs,oldVal,schemaEntity)->{
			SearchSchemaEntityInterface sseVisitorT = new SearchSchemaEntityVisitorFactory().createInstance( SchemaEntity.SCHEMA_TYPE.ROOT_TABLE );
			schemaEntity.accept(sseVisitorT);
			SchemaEntity tableRootEntity = sseVisitorT.getHitSchemaEntity();
			if ( tableRootEntity != null )
			{
				this.cmbTable.getItems().removeAll(this.cmbTable.getItems());
				this.cmbTable.getItems().addAll(tableRootEntity.getEntityLst());
			}
		});
		
		this.cmbTable.valueProperty().addListener((obs,oldVal,schemaEntity)->{
			if ( schemaEntity == null )
			{
				this.btnNext.setDisable(true);
			}
			else
			{
				this.btnNext.setDisable(false);
			}
		});
	}
	
	// WatchInterface
	@Override
	public void notify( Event event )
	{
		System.out.println( "ImportDataPaneDB:notify" );
		MyServiceTool.shutdownService(this.service);
	}
	
	// ChangeLangInterface
	@Override
	public void changeLang() 
	{
		MainController mainCtrl = this.dbView.getMainController();
		ResourceBundle langRB = mainCtrl.getLangResource("conf.lang.gui.ctrl.imp.ImportDataTab");
		ResourceBundle extLangRB = mainCtrl.getLangResource("conf.lang.gui.common.NodeName");
				
		// "next" button
		this.btnNext.setGraphic( MyGUITool.createImageView( 16, 16, mainCtrl.getImage("file:resources/images/next.png") ));
		Tooltip tipNext = new Tooltip(extLangRB.getString( "TOOLTIP_NEXT" ));
		tipNext.getStyleClass().add("Common_MyToolTip");
		this.btnNext.setTooltip(tipNext);
	}
}
