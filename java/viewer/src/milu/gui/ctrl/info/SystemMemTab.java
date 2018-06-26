package milu.gui.ctrl.info;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.util.Duration;
import milu.gui.ctrl.common.inf.ChangeLangInterface;
import milu.gui.ctrl.common.inf.FocusInterface;
import milu.gui.ctrl.common.table.ObjTableView;
import milu.gui.view.DBView;
import milu.main.MainController;

public class SystemMemTab extends Tab 
	implements 
		FocusInterface,
		ChangeLangInterface 
{
	private DBView          dbView = null;
	
	private ObjTableView    objTableView = null;
	
	private ObservableList<Object> objHeadLst = FXCollections.observableArrayList();
	
	private Button    btnGC = new Button();
	
	private BorderPane      basePane = new BorderPane();
	
	// Interval to get Memory Information
	private Timeline  memTimeLine = new Timeline();
	
	SystemMemTab( DBView dbView )
	{
		super();
		
		this.dbView = dbView;
		
		this.objTableView = new ObjTableView(this.dbView);
		//this.objTableView.prefHeightProperty().bind(this.getTabPane().heightProperty());
		//this.objTableView.prefWidthProperty().bind(this.getTabPane().widthProperty());
		
		this.changeLang();
		
	    // -----------------------------------------------------
		// [Center]
	    // ----------------------------------------------------- 
		this.basePane.setCenter(this.objTableView);
		
	    // -----------------------------------------------------
		// [Bottom]
	    // ----------------------------------------------------- 
		HBox hBoxBtm = new HBox(2);
		hBoxBtm.setPadding( new Insets( 10, 10, 10, 10 ) );
		hBoxBtm.setSpacing(10);
		Pane spacer = new Pane();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		spacer.setMinSize(10,1);
		hBoxBtm.getChildren().addAll( spacer, this.btnGC );
		
		this.basePane.setBottom(hBoxBtm);
		
		this.setContent(this.basePane);
		
		this.setAction();
	}
	
	private void setAction()
	{
		// refresh, every 10 sec
		this.memTimeLine.getKeyFrames().add(
			new KeyFrame(
				Duration.millis(10000),
				new EventHandler<ActionEvent>() {
					@Override
					public void handle( ActionEvent event )
					{
						//System.out.println( "every 10sec memInfo update." );
						SystemMemTab.this.setFocus();
					}
				}
			)	
		);
		this.memTimeLine.setCycleCount( Timeline.INDEFINITE );
		this.memTimeLine.play();
		
		this.setOnCloseRequest((event)->{
			this.memTimeLine.stop();
		});
		
		this.btnGC.setOnAction( event->System.gc() );
	}
	
	// FocusInterface
	@Override
	public void setFocus()
	{
		MainController mainCtrl = this.dbView.getMainController();
		ResourceBundle langRB = mainCtrl.getLangResource("conf.lang.gui.ctrl.info.SystemTab");
		//ResourceBundle extLangRB = mainCtrl.getLangResource("conf.lang.gui.common.NodeName");
		
		// https://stackoverflow.com/questions/74674/how-to-do-i-check-cpu-and-memory-usage-in-java
		Runtime runtime   = Runtime.getRuntime();
		long maxMemory    = runtime.maxMemory();
		long totalMemory  = runtime.totalMemory();
		long freeMemory   = runtime.freeMemory();
		//long totalFreeMemory = freeMemory + ( maxMemory - allocatedMemory );
		
        // Data List
        List<List<Object>>  dataLst = new ArrayList<>();
        // Max Memory
        List<Object> maxMemoryLst = new ArrayList<>();
        maxMemoryLst.add( langRB.getString("ITEM_MEM_MAX_MEMORY") );
        maxMemoryLst.add( String.format( "%,d",maxMemory) );
        dataLst.add(maxMemoryLst);
        // Total Memory
        List<Object> totalMemoryLst = new ArrayList<>();
        totalMemoryLst.add( langRB.getString("ITEM_MEM_TOTAL_MEMORY") );
        totalMemoryLst.add( String.format( "%,d",totalMemory) );
        dataLst.add(totalMemoryLst);
        // Free Memory
        List<Object> freeMemoryLst = new ArrayList<>();
        freeMemoryLst.add( langRB.getString("ITEM_MEM_FREE_MEMORY") );
        freeMemoryLst.add( String.format( "%,d",freeMemory) );
        dataLst.add(freeMemoryLst);
        
        this.objTableView.setTableViewData( this.objHeadLst, dataLst );
	}
	
	// ChangeLangInterface
	@Override
	public void changeLang() 
	{
		MainController mainCtrl = this.dbView.getMainController();
		ResourceBundle langRB = mainCtrl.getLangResource("conf.lang.gui.ctrl.info.SystemTab");
		ResourceBundle extLangRB = mainCtrl.getLangResource("conf.lang.gui.common.NodeName");
		
		this.setText(langRB.getString("TITLE_SYSMEM"));
		
		this.objHeadLst.clear();
		this.objHeadLst.addAll(
					extLangRB.getString( "ITEM_KEY" ),
					extLangRB.getString( "ITEM_VAL" )
				);
		
		this.btnGC.setText(langRB.getString("BTN_GC"));
	}	
}
