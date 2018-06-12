package milu.main;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import java.sql.SQLException;
import javax.crypto.SecretKey;

import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.application.Application;
import javafx.application.Platform;
import milu.gui.ctrl.common.DraggingTabPaneSupport;
import milu.gui.dlg.db.DBSettingDialog;
import milu.gui.view.DBView;

import milu.db.MyDBAbstract;
import milu.db.driver.DriverShim;
import milu.entity.schema.SchemaEntity;
import milu.entity.schema.search.ChangeLangSchemaEntityVisitor;
import milu.task.main.InitialLoadAbstract;
import milu.task.main.InitialLoadFactory;
import milu.tool.MyTool;

public class MainController
{
	// Application
	private Application application = null;
	
	// Application Configuration
	private AppConf  appConf = new AppConf();
	
	// Key for encryption
	private SecretKey secretKey = null;
	
	// DB Connection <=> DBView List Map
	//             1 <=> N
	private Map<MyDBAbstract,List<DBView>> myDBViewMap = new HashMap<>();
	
	// DraggingTabPaneSupport Map
	private Map<MyDBAbstract,DraggingTabPaneSupport>  draggingTabPaneMap = new HashMap<>();
	
	// Image Map
	private Map<String,Image> imageMap = new HashMap<>();
	
	// Language Resource Map
	private Map<String,ResourceBundle> langMap = new HashMap<>();
	
	void init( Application application )
	{
		this.setApplication(application);
		//this.loadLangResources();
		//this.loadImages();
		//this.loadAppConf();
		//this.loadDriver();
		//this.loadKey();
		this.createNewDBConnectionAndOpenNewWindow();
	}
	
	private void setApplication( Application application )
	{
		this.application = application;
	}
	
	public Application getApplication()
	{
		return this.application;
	}
	
	public void setAppConf( AppConf appConf )
	{
		this.appConf = appConf;
	}
	
	public AppConf getAppConf()
	{
		return this.appConf;
	}
	
	public void addImage( String resourceName, Image image )
	{
		this.imageMap.put( resourceName, image );
	}
	
	public Image getImage( String resourceName ) 
	{
		return this.imageMap.get( resourceName );
	}
	
	public void addLangResource( String name, ResourceBundle langRB )
	{
		this.langMap.put(name,langRB);
	}
	
	public ResourceBundle getLangResource( String name )
	{
		return this.langMap.get(name);
	}
	
	public void switchDriver( DriverShim driverA, DriverShim driverB )
	{
		this.myDBViewMap.forEach
		(
			(myDBAbs,dbViewLst)->
			{
				if ( myDBAbs.getDriveShim() == driverA )
				{
					myDBAbs.setDriverShim(driverB);
				}
			}
		);
	}
	
	public void setSecretKey( SecretKey secretKey )
	{
		this.secretKey = secretKey;
	}
	
	public SecretKey getSecretKey()
	{
		return this.secretKey;
	}
	
	private void closeAllDB()
	{
		// close DB connection
		this.myDBViewMap.forEach
		( 
			(myDBAbs,dbViewLst)->
			{
				try
				{
					myDBAbs.close();
				}
				catch ( SQLException sqlEx )
				{
					// suppress error
				}
			}
		);
	}
	
	/**
	 * Create New DB Connection & Open New Window
	 */
	public void createNewDBConnectionAndOpenNewWindow()
	{
		// -----------------------------------------
		// How to close on pressing 'x'
		// https://stackoverflow.com/questions/32048348/javafx-scene-control-dialogr-wont-close-on-pressing-x
		// -----------------------------------------
		DBSettingDialog dlg = new DBSettingDialog( this );
		Window       window = dlg.getDialogPane().getScene().getWindow();
		//MyTool.setWindowLocation(window, window.getWidth(), window.getHeight() );
		window.setOnCloseRequest( event ->{ window.hide(); this.close(null); } );
		
		// get result when clicking something on dialog
		Optional<MyDBAbstract> result = dlg.showAndWait();
		dlg = null;
		// Click "Connect"
		if ( result.isPresent() )
		{
			MyDBAbstract myDBAbs = result.get();

			// get Active Window(JavaFX9)
			// https://stackoverflow.com/questions/32922424/how-to-get-the-current-opened-stage-in-javafx
			List<Window>  focusWinLst = Stage.getWindows().filtered( window2->window2.isFocused() );
			System.out.println( "focusWinLst.size:" + focusWinLst.size() );
			DBView dbView = null;
			if ( focusWinLst.size() > 0 )
			{
				Window focusWin = focusWinLst.get(0);
				if ( focusWin instanceof DBView )
				{
					dbView = (DBView)focusWin;
				}
			}
			
			this.createNewWindow( myDBAbs, dbView );
		}
		// Click "Cancel"
		else
		{
			System.out.println( "Cancel!!" );
			this.close(null);
		}
	}

	public void createNewWindow( MyDBAbstract myDBAbs, DBView dbViewPrev )
	{
		// Open a view for SQL result
		DBView dbView = new DBView( this, myDBAbs );
		//dbViewLst.add( dbView );
		if ( this.myDBViewMap.containsKey(myDBAbs) )
		{
			List<DBView> dbViewLst = this.myDBViewMap.get(myDBAbs);
			dbViewLst.add(dbView);
		}
		else
		{
			List<DBView> dbViewLst = new ArrayList<>();
			dbViewLst.add(dbView);
			this.myDBViewMap.put( myDBAbs , dbViewLst );
		}
		dbView.start();
		
		// Open a new window at little bit right+lower.
		if ( dbViewPrev != null )
		{
			dbView.setX( dbViewPrev.getX() + 20 );
			dbView.setY( dbViewPrev.getY() + 20 );
		}
		else
		{
			/*
			Point2D pd = MyTool.getMousePosOnScreen();
			Point2D pd2 = MyTool.getRightWindowPos( pd, dbView.getWidth(), dbView.getHeight() );
			dbView.setX( pd2.getX() );
			dbView.setY( pd2.getY() );
			//System.out.println( "Window width:" + dbView.getWidth() + ":height:" + dbView.getHeight() );
			//System.out.println( "pd     x:"     + pd.getX()  + ":y:" + pd.getY() );
			//System.out.println( "pd2    x:"     + pd2.getX() + ":y:" + pd2.getY() );
			 * 
			 */
			MyTool.setWindowLocation( dbView, dbView.getWidth(), dbView.getHeight() );
		}
	}
	
	/**
	 * Close DBView
	 * 
	 * @param dbView
	 */
	public void close( DBView dbView )
	{
		if ( dbView != null )
		{
			MyDBAbstract myDBAbs = dbView.getMyDBAbstract();
			List<DBView> dbViewLst = this.myDBViewMap.get(myDBAbs);
			dbViewLst.remove(dbView);
			
			// close myDBAbs & remove from map, 
			// if there is no active DBView for myDBAbs
			if ( dbViewLst.size() == 0 )
			{
				try
				{
					myDBAbs.close();
				}
				catch ( SQLException sqlEx )
				{
					// suppress error
				}
				this.myDBViewMap.remove(myDBAbs);
				myDBAbs = null;
			}
			else
			{
				System.out.println( "Active DBView still exists." );
			}
		}
		
		if ( this.myDBViewMap.size() == 0 )
		{
			this.quit();
		}
	}
	
	/*********************************
	 * Quit Application
	 *********************************
	 */
	public void quit()
	{
		this.closeAllDB();
		System.out.println( "MiluDBViewer Exit." );
		// "hs_err_pid*.log" is created, when no DBView.
		//System.exit( 0 );
		Platform.exit();
	}
	
	public void addDraggingTabPaneMap( MyDBAbstract myDBAbs, TabPane tabPane )
	{
		if ( this.draggingTabPaneMap.containsKey(myDBAbs) )
		{
			DraggingTabPaneSupport dtps = this.draggingTabPaneMap.get( myDBAbs );
			dtps.addSupport( tabPane );
		}
		else
		{
			DraggingTabPaneSupport dtps = new DraggingTabPaneSupport();
			dtps.addSupport( tabPane );
			this.draggingTabPaneMap.put( myDBAbs, dtps );
		}
	}
	
	/*********************************
	 * Change Language
	 * http://www.oracle.com/technetwork/java/javase/java8locales-2095355.html
	 * 
	 * @langCode
	 *    ex. ja => Japanese
	 *        en => English
	 *********************************
	 */
	public void changeLang( String langCode )
	{
		System.out.println( "changeLang start:" + langCode );
		Locale nextLocale = new Locale( langCode );
		Locale.setDefault( nextLocale );
		
		this.langMap.clear();
		InitialLoadAbstract ilAbs = 
				InitialLoadFactory.getInstance( InitialLoadFactory.FACTORY_TYPE.LANG, this, null, null, 0.0 );
		ilAbs.load();
		
		this.myDBViewMap.forEach
		( 
			(myDBAbs, dbViewLst)->
			{
				SchemaEntity schemaRoot = myDBAbs.getSchemaRoot();
				schemaRoot.accept(new ChangeLangSchemaEntityVisitor());
				dbViewLst.forEach( (dbView)->dbView.changeLang() );
			}
		);
		
		System.out.println( "changeLang done." );
	}
}
