package milu.gui.dlg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.geometry.Rectangle2D;

import milu.gui.ctrl.query.SqlTableView;

public class SystemInfoDialog extends Dialog<Boolean>
{
	// Property File for this class 
	private static final String PROPERTY_FILENAME = 
			"conf.lang.gui.dlg.SystemInfoDialog";

	// Language Resource
	private ResourceBundle langRB = ResourceBundle.getBundle( PROPERTY_FILENAME );
	
	// TabPane
	TabPane  tabPane = new TabPane();
	
	// Tab for System Infomation
	Tab      sysInfoTab  = new Tab();
	
	// Tab for Environment Variable
	Tab      envTab  = new Tab();
	
	// Tab for Memory Information
	Tab      memTab  = new Tab();
	
	// Interval to get Memory Information
	Timeline  memTimeLine = new Timeline();

	public SystemInfoDialog()
	{
		super();
		
		// Set Content on System Information Tab
		this.setContentOnSysInfoTab();
		
		// Set Content on Environment Variable Tab
		this.setContentOnEnvTab();
		
		// Set Content on Memory Information Tab
		this.setContentOnMemTab();
		
		this.tabPane.getTabs().addAll( this.sysInfoTab, this.envTab, this.memTab );
		this.tabPane.setTabClosingPolicy( TabClosingPolicy.UNAVAILABLE );
		
		BorderPane pane = new BorderPane();
		pane.setCenter( this.tabPane );
		
		// set pane on dialog
		this.getDialogPane().setContent( pane );
		
		this.getDialogPane().getButtonTypes().add( ButtonType.CLOSE );
		
		// Window Icon
		Stage     stage   = (Stage)this.getDialogPane().getScene().getWindow();
		stage.getIcons().add( new Image( "file:resources/images/winicon.gif" ) );
		
		// set css for this dialog
		Scene scene = this.getDialogPane().getScene();
		scene.getStylesheets().add
		(
			getClass().getResource("/conf/css/dlg/SystemInfoDialog.css").toExternalForm()
		);		
		
		// set size
		this.setResizable( true );
		this.getDialogPane().setPrefSize( 640, 320 );
		
		// set Dialog Title
		this.setTitle( langRB.getString( "TITLE_SYSINFO" ) );
		
		// set Modality
		this.initModality( Modality.NONE );
		
		this.setAction();
	}
	
	private void setAction()
	{
		// result when clicking on "Close".
		this.setResultConverter
		( 
			(dialogButton)->
			{ 
				this.memTimeLine.stop(); 
				return Boolean.TRUE; 
			} 
		);
		
		this.memTimeLine.getKeyFrames().add
		( 
			new KeyFrame
			(
				Duration.millis(10000),
				new EventHandler<ActionEvent>()
				{
					@Override
					public void handle( ActionEvent event )
					{
						System.out.println( "every 10sec memInfo update." );
						updateContentOnMemTab();
					}
				}
			) 
		);
		this.memTimeLine.setCycleCount( Timeline.INDEFINITE );
		
		this.memTab.selectedProperty().addListener
		(
			(obs,oldVal,newVal)->
			{
				System.out.println( "memTab:" + newVal );
				if ( newVal == true )
				{
					this.memTimeLine.play();
				}
				else
				{
					this.memTimeLine.pause();
				}
			}
		);
	}
	
	// Set Content on System Information Tab
	private void setContentOnSysInfoTab()
	{
    	Properties   properties  = System.getProperties();
    	Set<String>  propNameSet = properties.stringPropertyNames();
        List<String> propNameLst = new ArrayList<String>( propNameSet );
        Collections.sort(propNameLst);
        
        // Head List
        List<String> headLst = new ArrayList<String>();
        headLst.add( langRB.getString( "ITEM_KEY" ) );
        headLst.add( langRB.getString( "ITEM_VAL" ) );
        
        // Data List
        List<List<String>>  dataLst = new ArrayList<List<String>>();
        for ( String propName : propNameLst )
        {
        	//System.out.println( propName + "=" + System.getProperty(propName) );
        	List<String>  prop = new ArrayList<String>();
        	prop.add( propName );
        	String propVal = System.getProperty(propName);
        	if ( propName.equals( "path.separator" ) == false )
        	{
        		// Nothing Change for URL format
        		if ( propVal.contains( "://" ) )
        		{
        		}
        		// Change "/" or ";" to "\n"
        		else
        		{
        			propVal = propVal.replaceAll( System.getProperty("path.separator") , "\n" );
        		}
        	}
        	prop.add( propVal );
        	dataLst.add( prop );
        }
        SqlTableView propTableView = new SqlTableView();
        propTableView.setTableViewSQL( headLst, dataLst );
        
        this.sysInfoTab.setContent( propTableView );
        this.sysInfoTab.setText( langRB.getString( "TITLE_SYSINFO" ) );
	}
	
	// Set Content on Environment Variable Tab
	private void setContentOnEnvTab()
	{
        Map<String, String>  envMap     = System.getenv();
        List<String>         envNameLst = new ArrayList<String>( envMap.keySet() );
        Collections.sort(envNameLst);
        
        // Head List
        List<String> headLst = new ArrayList<String>();
        headLst.add( langRB.getString( "ITEM_KEY" ) );
        headLst.add( langRB.getString( "ITEM_VAL" ) );
        
        // Data List
        List<List<String>>  dataLst = new ArrayList<List<String>>();        
        for ( String envName : envNameLst )
        {
        	//System.out.println( envName + "=" + System.getenv(envName) );
        	List<String>  env = new ArrayList<String>();
        	env.add( envName );
        	String envVal = System.getenv(envName);
    		// Nothing Change for URL format
        	if ( envVal.contains( "://") )
        	{
        	}
    		// Change "/" or ";" to "\n"
        	else
        	{
        		envVal = envVal.replaceAll( System.getProperty( "path.separator"), "\n" );
        	}
        	env.add( envVal );
        	dataLst.add( env );
        }
        
        SqlTableView envTableView = new SqlTableView();
        envTableView.setTableViewSQL( headLst, dataLst );
        
        this.envTab.setContent( envTableView );        
		this.envTab.setText( langRB.getString( "TITLE_ENV_VARIABLE" ) );
	}
	
	// Set Content on Memory Information Tab
	private void setContentOnMemTab()
	{
		// Nodes for Memory Information
		Label        memLabel = new Label(langRB.getString( "LABEL_MEMINFO" ));
		memLabel.getStyleClass().add("SystemInfoDialog_Label_Title");
		Button       btnGC = new Button(langRB.getString("BTN_GC"));
		btnGC.setOnAction( event->System.gc() );
		HBox hbox = new HBox(2);
		hbox.getChildren().addAll( memLabel, btnGC );
        SqlTableView memTableView = new SqlTableView();
        memTableView.setId("memTableView");
        
        // Nodes for Display Information
		Label        dispLabel = new Label(langRB.getString( "LABEL_DISPINFO" ));
		dispLabel.getStyleClass().add("SystemInfoDialog_Label_Title");
        SqlTableView dispTableView = new SqlTableView();
        this.setContentForDispInfo( dispTableView );
        
        VBox vbox = new VBox( 2 );
        
        vbox.getChildren().addAll( hbox , memTableView, dispLabel, dispTableView );
        
        this.memTab.setContent( vbox );        
		this.memTab.setText( langRB.getString( "TITLE_MISCINFO" ) );
		// update contents on this tab, when activated.
		this.memTab.setOnSelectionChanged
		(
			(event)->
			{
				updateContentOnMemTab();
			}
		);
		
		
	}
	
	private void updateContentOnMemTab()
	{
		// https://stackoverflow.com/questions/74674/how-to-do-i-check-cpu-and-memory-usage-in-java
		Runtime runtime   = Runtime.getRuntime();
		long maxMemory    = runtime.maxMemory();
		long totalMemory  = runtime.totalMemory();
		long freeMemory   = runtime.freeMemory();
		//long totalFreeMemory = freeMemory + ( maxMemory - allocatedMemory );
		
        // Head List
        List<String> headLst = new ArrayList<String>();
        headLst.add( langRB.getString( "ITEM_KEY" ) );
        headLst.add( langRB.getString( "ITEM_VAL" ) );
        
        // Data List
        List<List<String>>  dataLst = new ArrayList<List<String>>();
        // Max Memory
        List<String> maxMemoryLst = new ArrayList<String>();
        maxMemoryLst.add( langRB.getString("ITEM_MEM_MAX_MEMORY") );
        maxMemoryLst.add( String.format( "%,d",maxMemory) );
        dataLst.add(maxMemoryLst);
        // Total Memory
        List<String> totalMemoryLst = new ArrayList<String>();
        totalMemoryLst.add( langRB.getString("ITEM_MEM_TOTAL_MEMORY") );
        totalMemoryLst.add( String.format( "%,d",totalMemory) );
        dataLst.add(totalMemoryLst);
        // Free Memory
        List<String> freeMemoryLst = new ArrayList<String>();
        freeMemoryLst.add( langRB.getString("ITEM_MEM_FREE_MEMORY") );
        freeMemoryLst.add( String.format( "%,d",freeMemory) );
        dataLst.add(freeMemoryLst);
        
        Node nodeP = this.memTab.getContent();
        Scene scene = nodeP.getScene();
        if ( scene != null )
        {
        	Node node  = scene.lookup("#memTableView");
			if ( node instanceof SqlTableView )
			{
				((SqlTableView)node).setTableViewSQL( headLst, dataLst );
			}
        }
	}
	
	private void setContentForDispInfo( SqlTableView dispTableView )
	{
		// http://krr.blog.shinobi.jp/javafx/javafx%20scene%E3%83%BBscenegraph%E3%82%AF%E3%83%A9%E3%82%B9
		
        // Head List
        List<String> headLst = new ArrayList<String>();
        headLst.add( langRB.getString( "ITEM_KEY" ) );
        headLst.add( langRB.getString( "ITEM_VAL" ) );
        
        // Data List
        List<List<String>>  dataLst = new ArrayList<List<String>>();
        
        /*
        // Display Size
        List<String> dispSizeLst = new ArrayList<String>();
        dispSizeLst.add( langRB.getString("ITEM_DISP_SIZE") );
		Rectangle2D  rec = Screen.getPrimary().getBounds();
        dispSizeLst.add( String.format( "%d x %d",(int)rec.getWidth(),(int)rec.getHeight()) );
        dataLst.add(dispSizeLst);
        // Display Size(Visual)
        List<String> dispSizeVisualLst = new ArrayList<String>();
        dispSizeVisualLst.add( langRB.getString("ITEM_DISP_SIZE_VISUAL") );
		Rectangle2D  recVisual = Screen.getPrimary().getVisualBounds();
        dispSizeVisualLst.add( String.format( "%d x %d",(int)recVisual.getWidth(),(int)recVisual.getHeight()) );
        dataLst.add(dispSizeVisualLst);
        // Display DPI
        List<String> dispDPILst = new ArrayList<String>();
        dispDPILst.add( langRB.getString("ITEM_DISP_DPI") );
        dispDPILst.add( String.format( "%d",(int)Screen.getPrimary().getDpi()) );
        dataLst.add(dispDPILst);
        */
        
        int dispID = 1;
        for ( Screen  screen : Screen.getScreens() )
        {
            // Display Size
            List<String> dispSizeLst = new ArrayList<String>();
            dispSizeLst.add( langRB.getString("ITEM_DISP_SIZE") + "(" + dispID + ")" );
    		Rectangle2D  rec = screen.getBounds();
            dispSizeLst.add( String.format( "%d x %d",(int)rec.getWidth(),(int)rec.getHeight()) );
            dataLst.add(dispSizeLst);
            // Display Size(Visual)
            List<String> dispSizeVisualLst = new ArrayList<String>();
            dispSizeVisualLst.add( langRB.getString("ITEM_DISP_SIZE_VISUAL") + "(" + dispID + ")" );
    		Rectangle2D  recVisual = screen.getVisualBounds();
            dispSizeVisualLst.add( String.format( "%d x %d",(int)recVisual.getWidth(),(int)recVisual.getHeight()) );
            dataLst.add(dispSizeVisualLst);
            // Display DPI
            List<String> dispDPILst = new ArrayList<String>();
            dispDPILst.add( langRB.getString("ITEM_DISP_DPI") + "(" + dispID + ")" );
            dispDPILst.add( String.format( "%d",(int)screen.getDpi()) );
            dataLst.add(dispDPILst);
            
            dispID++;
        }
        
        dispTableView.setTableViewSQL( headLst, dataLst );
	}
	
}
