package milu.ctrl;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Locale;

import java.sql.SQLException;

import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.application.Application;

import milu.gui.dlg.db.DBSettingDialog;
import milu.gui.view.DBView;
import milu.gui.ctrl.common.DraggingTabPaneSupport;

import milu.db.MyDBAbstract;
import milu.entity.schema.SchemaEntity;
import milu.entity.schema.search.ChangeLangSchemaEntityVisitor;
import milu.conf.AppConf;

public class MainController
{
	// Application
	private Application application = null;
	
	// Application Configuration
	private AppConf  appConf = new AppConf();
	
	// DB connection list
	// put MyDBAbstract Object after connected to DB.
	private List<MyDBAbstract> myDBAbsLst = new ArrayList<MyDBAbstract>();
	
	// DBView list
	private List<DBView> dbViewLst = new ArrayList<DBView>();
	
	// DraggingTabPaneSupport Map
	private Map<MyDBAbstract,DraggingTabPaneSupport>  draggingTabPaneMap = new HashMap<>();
	
	// Image Map
	private Map<String,Image> imageMap = new HashMap<>();
	
	public MainController()
	{
	}
	
	public void setApplication( Application application )
	{
		this.application = application;
	}
	
	public Application getApplication()
	{
		return this.application;
	}
	
	public AppConf getAppConf()
	{
		return this.appConf;
	}
	
	public void loadImages()
	{
		String[] images =
			{
				// SchemaEntity
				"file:resources/images/index_p.png",
				"file:resources/images/index_u.png",
				"file:resources/images/index_f.png",
				"file:resources/images/index_i.png",
				"file:resources/images/order_a.png",
				"file:resources/images/order_d.png",
				"file:resources/images/column.png",
				"file:resources/images/aggregate.png",
				"file:resources/images/func.png",
				"file:resources/images/materialized_view.png",
				"file:resources/images/package_body.png",
				"file:resources/images/package_def.png",
				"file:resources/images/proc.png",
				"file:resources/images/schema.png",			// MainToolBar,DBSchemaTab
				"file:resources/images/seq.png",
				"file:resources/images/systemview.png",
				"file:resources/images/table.png",
				"file:resources/images/trigger.png",
				"file:resources/images/type.png",
				"file:resources/images/view.png",
				"file:resources/images/url.png",
				"file:resources/images/aggregate_root.png",
				"file:resources/images/func_root.png",
				"file:resources/images/index_root.png",
				"file:resources/images/materialized_view_root.png",
				"file:resources/images/package_body_root.png",
				"file:resources/images/package_def_root.png",
				"file:resources/images/proc_root.png",
				"file:resources/images/seq_root.png",
				"file:resources/images/systemview_root.png",
				"file:resources/images/table_root.png",
				"file:resources/images/trigger_root.png",
				"file:resources/images/type_root.png",
				"file:resources/images/view_root.png",
				"file:resources/images/ER_root.png",
				// MainMenuBar
				"file:resources/images/config.png",
				"file:resources/images/quit.png",
				"file:resources/images/sysinfo.png",
				// MainToolBar
				"file:resources/images/execsql.png",
				"file:resources/images/explain.png",
				"file:resources/images/direction.png",
				"file:resources/images/newtab.png",
				"file:resources/images/newwin.png",
				"file:resources/images/connect.png",
				"file:resources/images/copy.png",
				"file:resources/images/copy2.png",
				// Dialog
				"file:resources/images/winicon.gif",
				// DBSqlTab
				"file:resources/images/sql.png",
				// DBSettingDialog
				"file:resources/images/folder.png",
			};
		
		for ( String image : images )
		{
			System.out.println( "image loading..." + image );
			this.imageMap.put( image, new Image( image ) );
		}
	}
	
	public Image getImage( String resourceName ) 
	{
		return this.imageMap.get( resourceName );
	}
	
	private void closeAllDB()
	{
		// close DB connection
		for ( int i = 0; i < this.myDBAbsLst.size(); i++ )
		{
			MyDBAbstract myDBAbs = this.myDBAbsLst.get( i );
			try
			{
				myDBAbs.close();
			}
			catch( SQLException sqlEx )
			{
				// suppress error
			}
		}
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
		window.setOnCloseRequest( event ->{ window.hide(); this.close(null); } );
		
		// get result when clicking something on dialog
		Optional<MyDBAbstract> result = dlg.showAndWait();
		dlg = null;
		// Click "Connect"
		if ( result.isPresent() )
		{
			
			MyDBAbstract myDBAbs = result.get();
		
			// add myDBAbs to a DB connection list.
			this.myDBAbsLst.add( myDBAbs );

			// get Active Window(JavaFX9)
			// https://stackoverflow.com/questions/32922424/how-to-get-the-current-opened-stage-in-javafx
			List<Window>  focusWinLst = Stage.getWindows().filtered( window2->window2.isFocused() );
			System.out.println( "focusWinLst.size:" + focusWinLst.size() );
			//List<Window>  showingWinLst = Stage.getWindows().filtered( window3->window3.isShowing() );
			//System.out.println( "showingWinLst.size:" + showingWinLst.size() );
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
		}
		
		
	}

	public void createNewWindow( MyDBAbstract myDBAbs, DBView dbViewPrev )
	{
		// Open a view for SQL result
		DBView dbView = new DBView( this, myDBAbs );
		dbViewLst.add( dbView );
		dbView.start();
		
		// Open a new window at little bit right+lower.
		if ( dbViewPrev != null )
		{
			dbView.setX( dbViewPrev.getX() + 20 );
			dbView.setY( dbViewPrev.getY() + 20 );
		}
	}
	
	/**
	 * Close DBView
	 * 
	 * @param dbView
	 */
	public void close( DBView dbView )
	{
		// Call when pushing 'x' on DBSettingDialog
		if ( dbView == null )
		{
			if ( dbViewLst.size() == 0 )
			{
				this.closeAllDB();
				System.out.println( "MiluDBViewer Quit." );
			}
		}
		// Call when closing DBView
		else
		{
			MyDBAbstract myDBAbs = dbView.getMyDBAbstract();
			
			boolean ret = this.dbViewLst.remove( dbView );
			if ( ret == true )
			{
				System.out.println( "dbView remove success." );
				boolean isConExist = false;
				for ( DBView dbVW : dbViewLst )
				{
					MyDBAbstract dbAbs = dbVW.getMyDBAbstract();
					if ( dbAbs == myDBAbs )
					{
						isConExist = true;
					}
				}
				// When this DB connection is not necessary anymore, 
				// remove from DB connection list & close DB connection; 
				if ( isConExist == false )
				{
					this.myDBAbsLst.remove( myDBAbs );
					try
					{
						myDBAbs.close();
					}
					catch ( SQLException sqlEx )
					{
						// suppress error
					}
					myDBAbs = null;
				}
				
				dbView = null;
			}
			else
			{
				System.out.println( "dbView remove fail." );
			}
			
			if ( dbViewLst.size() == 0 )
			{
				this.quit();
			}
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
		System.exit( 0 );
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
	 *    ex. en_US
	 *        ja_JP
	 *********************************
	 */
	public void changeLang( String langCode )
	{
		System.out.println( "changeLang start:" + langCode );
		Locale nextLocale = new Locale( langCode );
		Locale.setDefault( nextLocale );
		
		SchemaEntity.loadResourceBundle();
		for ( MyDBAbstract myDBAbs : this.myDBAbsLst )
		{
			SchemaEntity schemaRoot = myDBAbs.getSchemaRoot();
			schemaRoot.accept(new ChangeLangSchemaEntityVisitor());
		}
		
		for ( DBView dbView : this.dbViewLst )
		{
			dbView.changeLang();
		}
		
		System.out.println( "changeLang done." );
	}
	
}
