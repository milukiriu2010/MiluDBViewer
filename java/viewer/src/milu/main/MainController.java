package milu.main;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.text.MessageFormat;

import javafx.scene.control.TabPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.application.Application;
import javafx.application.Platform;

import milu.file.json.MyJsonHandleAbstract;
import milu.file.json.MyJsonHandleFactory;
import milu.gui.dlg.MyAlertDialog;
import milu.gui.dlg.db.DBSettingDialog;
import milu.gui.view.DBView;
import milu.tool.MyTool;
import milu.gui.ctrl.common.DraggingTabPaneSupport;

import milu.db.MyDBAbstract;
import milu.db.driver.DriverClassConst;
import milu.db.driver.DriverShim;
import milu.db.driver.LoadDriver;
import milu.entity.schema.SchemaEntity;
import milu.entity.schema.search.ChangeLangSchemaEntityVisitor;

public class MainController
{
	// Application
	private Application application = null;
	
	// Application Configuration
	private AppConf  appConf = new AppConf();
	
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
		this.loadLangResources();
		this.loadImages();
		this.loadAppConf();
		this.loadDriver();
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
	
	public AppConf getAppConf()
	{
		return this.appConf;
	}
	
	private void loadImages()
	{
		String[] images =
			{
				// SchemaEntity
				"file:resources/images/index_p.png",
				"file:resources/images/index_u.png",
				"file:resources/images/index_f.png",
				"file:resources/images/index_fk.png",
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
				"file:resources/images/jdbc.png",
				// MainToolBar
				"file:resources/images/execsql.png",
				"file:resources/images/explain.png",
				"file:resources/images/direction.png",
				"file:resources/images/commit.png",
				"file:resources/images/rollback.png",
				"file:resources/images/newtab.png",
				"file:resources/images/newwin.png",
				"file:resources/images/connect.png",
				"file:resources/images/copy.png",
				"file:resources/images/copy2.png",
				// Dialog
				"file:resources/images/winicon.gif",
				// DBSqlTab
				"file:resources/images/sql.png",
				"file:resources/images/result.png",
				//"file:resources/images/script.png",
				// DBSettingDialog
				"file:resources/images/folder.png",
				"file:resources/images/folder_new.png",
				"file:resources/images/file.png",
				"file:resources/images/file_new.png",
				"file:resources/images/delete.png"
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
	
	private void loadLangResources()
	{
		String[] languagess =
		{
			"conf.lang.entity.schema.SchemaEntity",
			"conf.lang.gui.common.MyAlert",
			"conf.lang.gui.common.NodeName",
			"conf.lang.gui.ctrl.common.DriverControlPane",
			"conf.lang.gui.ctrl.menu.MainMenuBar",
			"conf.lang.gui.ctrl.menu.MainToolBar",
			"conf.lang.gui.ctrl.query.DBSqlTab",
			"conf.lang.gui.ctrl.query.SqlTableView",
			"conf.lang.gui.ctrl.schema.DBSchemaTab",
			"conf.lang.gui.ctrl.schema.SchemaTreeView",
			"conf.lang.gui.dlg.VersionDialog",
			"conf.lang.gui.dlg.SystemInfoDialog",
			"conf.lang.gui.dlg.app.AppSettingDialog",
			"conf.lang.gui.dlg.db.DBSettingDialog"
		};
		
		for ( String lang : languagess )
		{
			System.out.println( "language resource loading..." + lang );
			this.langMap.put( lang , ResourceBundle.getBundle( lang ) );
		}
	}
	
	public ResourceBundle getLangResource( String name )
	{
		return this.langMap.get(name);
	}
	
	private void loadAppConf()
	{
		MyJsonHandleAbstract myJsonAbs =
			new MyJsonHandleFactory().createInstance(AppConf.class);
		try
		{
			myJsonAbs.open(AppConst.APP_CONF.val());
			Object obj = myJsonAbs.load();
			if ( obj instanceof AppConf )
			{
				this.appConf = (AppConf)obj;
				System.out.println( "AppConf:" + this.appConf );
			}
		}
		catch ( FileNotFoundException nfEx )
		{
			// When this application starts at the first time,
			// "app_conf.json" doesn't exists yet.
			// So it always enters this logic.
			System.out.println( "Not Found:" + AppConst.APP_CONF.val() );
		}
		catch ( Exception ex )
		{
			// "app_conf.json" exists
			// but, cannot read.
			this.showException(ex);
		}
	}

	private void loadDriver()
	{
		this.loadDriverUser();
		this.loadDriverDefault();
	}
	
	private void loadDriverUser()
	{
		File folder = new File(AppConst.DRIVER_DIR.val());
		File[] fileArray = folder.listFiles();
		if ( fileArray == null )
		{
			return;
		}
		
		List<File> fileLst = new ArrayList<File>( Arrays.asList(fileArray) );
		
		List<File> jsonLst =
				fileLst.stream()
					.filter( file -> file.isFile() )
					.filter( file -> MyTool.getFileExtension(file).equals("json") )
					.collect(Collectors.toList());
		
		jsonLst.forEach
		(
			(json)->
			{
				MyJsonHandleAbstract myJsonAbs =
					new MyJsonHandleFactory().createInstance(DriverShim.class);
				try
				{
					myJsonAbs.open(json.getAbsolutePath());
					Object obj = myJsonAbs.load();
					if ( obj instanceof DriverShim )
					{
						DriverShim driverShim = (DriverShim)obj;
						if ( LoadDriver.isAlreadyLoadCheck( driverShim.getDriverClassName() ) == false )
						{
							DriverShim loadedDriver = LoadDriver.loadDriver( driverShim.getDriverClassName(), driverShim.getDriverPathLst() );
							loadedDriver.setTemplateUrl( driverShim.getTemplateUrl() );
							loadedDriver.setReferenceUrl( driverShim.getReferenceUrl() );
							System.out.println( driverShim.getDriverClassName() + " Driver(User) Load done." );
						}
						else
						{
							System.out.println( driverShim.getDriverClassName() + " Driver(User) Load skip." );
						}
					}
				}
				catch ( Exception ex )
				{
					// "driver/'driver class name'.json" exists
					// but, cannot read.
					//this.showException(ex, "Cannot load(" + json.getAbsolutePath() + ")");
					
					// https://stackoverflow.com/questions/36309385/how-to-change-the-text-of-yes-no-buttons-in-javafx-8-alert-dialogs?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
					// https://stackoverflow.com/questions/29535395/javafx-default-focused-button-in-alert-dialog?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
					ButtonType btnYes = ButtonType.YES; //new ButtonType( "Yes", ButtonBar.ButtonData.YES );
					ButtonType btnNo  = ButtonType.NO; //new ButtonType( "No" , ButtonBar.ButtonData.NO );
					
					MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING, this, btnYes, btnNo );
					((Button)alertDlg.getDialogPane().lookupButton(btnYes)).setDefaultButton(false);
					((Button)alertDlg.getDialogPane().lookupButton(btnNo)).setDefaultButton(true);
					ResourceBundle langRB = this.getLangResource("conf.lang.gui.common.MyAlert");
					String msgExtra =
						MessageFormat.format( langRB.getString("MSG_DB_DRIVER_ERROR"), json.getAbsolutePath() );
					alertDlg.setHeaderText( langRB.getString("TITLE_DB_DRIVER_ERROR") );
					alertDlg.setTxtExp( ex, msgExtra );
					final Optional<ButtonType> result = alertDlg.showAndWait();
					if ( result.get() == btnYes )
					{
						System.out.println( "delete:"+json.getAbsolutePath() );
						new File(json.getAbsolutePath()).delete();
					}
					alertDlg = null;					
				}
			}
		);
		
	}
	
	private void loadDriverDefault()
	{
		// DriverClassConst <=> Driver Path List
		Map<DriverClassConst,List<String>>  driverMap = new HashMap<>();
		// DriverClassConst <=> Template URL
		Map<DriverClassConst,String>  driverTemplateUrlMap = new HashMap<>();
		// DriverClassConst <=> Reference URL
		Map<DriverClassConst,String>  driverReferenceUrlMap = new HashMap<>();
		
		// Oracle
		List<String>  driverPathLstOracle = new ArrayList<>();
		driverPathLstOracle.add( "file:lib/oracle/ojdbc8.jar" );
		driverPathLstOracle.add( "file:lib/oracle/orai18n.jar" );
		driverPathLstOracle.add( "file:lib/oracle/xdb6.jar" );
		driverPathLstOracle.add( "file:lib/oracle/xmlparserv2.jar" );
		
		driverMap.put( DriverClassConst.CLASS_NAME_ORACLE, driverPathLstOracle );
		driverTemplateUrlMap.put( DriverClassConst.CLASS_NAME_ORACLE, "jdbc:oracle:thin:@//<host>[:1521]/<service_name>[?internal_logon=sysdba|sysoper]" );
		driverReferenceUrlMap.put( DriverClassConst.CLASS_NAME_ORACLE, "https://docs.oracle.com/cd/B28359_01/java.111/b31224/urls.htm" );
		
		// PostgreSQL
		List<String>  driverPathLstPostgres = new ArrayList<>();
		driverPathLstPostgres.add( "file:lib/postgresql/postgresql-42.1.4.jar" );
		
		driverMap.put( DriverClassConst.CLASS_NAME_POSTGRESQL, driverPathLstPostgres );
		driverTemplateUrlMap.put( DriverClassConst.CLASS_NAME_POSTGRESQL, "jdbc:postgresql://host1:5432,host2:port2/database[?targetServerType=master]" );
		driverReferenceUrlMap.put( DriverClassConst.CLASS_NAME_POSTGRESQL, "https://jdbc.postgresql.org/documentation/head/connect.html" );
		
		// MySQL
		List<String>  driverPathLstMySQL = new ArrayList<>();
		driverPathLstMySQL.add( "file:lib/mysql/mysql-connector-java-5.1.45-bin.jar" );
		
		driverMap.put( DriverClassConst.CLASS_NAME_MYSQL, driverPathLstMySQL );
		driverTemplateUrlMap.put( DriverClassConst.CLASS_NAME_MYSQL, "jdbc:mysql://[host1][:3306][,[host2][:port2]]...[/[database]][?autoReconnect=true][&autoClosePStmtStreams=true]" );
		driverReferenceUrlMap.put( DriverClassConst.CLASS_NAME_MYSQL, "https://dev.mysql.com/doc/connector-j/5.1/en/connector-j-reference-configuration-properties.html" );
		
		// Cassandra
		List<String>  driverPathLstCassandra = new ArrayList<>();
		driverPathLstCassandra.add( "file:lib/cassandra/cassandra-jdbc-driver-0.6.4-shaded.jar" );
		
		driverMap.put( DriverClassConst.CLASS_NAME_CASSANDRA1, driverPathLstCassandra );
		driverTemplateUrlMap.put( DriverClassConst.CLASS_NAME_CASSANDRA1, "jdbc:c*:datastax//[host][:9042]/[keyspace][?consistencyLevel=ONE|ANY|...][&compression=LZ4|SNAPPY]" );
		driverReferenceUrlMap.put( DriverClassConst.CLASS_NAME_CASSANDRA1, "https://github.com/zhicwu/cassandra-jdbc-driver" );
		
		// --------------------------------------------------
		// Load Driver
		// --------------------------------------------------
		driverMap.forEach
		(
			(driverClassType,driverPathLst)->
			{
				try
				{
					// Driver is not yet loaded
					if ( LoadDriver.isAlreadyLoadCheck( driverClassType.val() ) == false )
					{
						DriverShim driver = LoadDriver.loadDriver( driverClassType.val(), driverPathLst );
						driver.setTemplateUrl( driverTemplateUrlMap.get(driverClassType) );
						driver.setReferenceUrl( driverReferenceUrlMap.get(driverClassType) );
						System.out.println( DriverShim.driverDBMap.get(driverClassType).val() + " Driver(Default) Load done." );
					}
					// Driver is already loaded
					else
					{
						System.out.println( DriverShim.driverDBMap.get(driverClassType).val() + " Driver(Default) Load skip." );
					}
				}
				catch ( Exception ex )
				{
					ex.printStackTrace();
					System.out.println( DriverShim.driverDBMap.get(driverClassType).val() + " Driver(Default) Load failed." );
				}
			}
		);
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
	 *    ex. en_US
	 *        ja_JP
	 *********************************
	 */
	public void changeLang( String langCode )
	{
		System.out.println( "changeLang start:" + langCode );
		Locale nextLocale = new Locale( langCode );
		Locale.setDefault( nextLocale );
		
		this.loadLangResources();
		
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
	
	private void showException( Exception ex )
	{
		MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING, this );
		ResourceBundle langRB = this.getLangResource("conf.lang.gui.common.MyAlert");
		alertDlg.setHeaderText( langRB.getString("TITLE_MISC_ERROR") );
		alertDlg.setTxtExp( ex );
		alertDlg.showAndWait();
		alertDlg = null;
	}	
	/*
	private void showException( Exception ex, String msg )
	{
		MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING, this );
		alertDlg.setHeaderText( msg );
		alertDlg.setTxtExp( ex );
		alertDlg.showAndWait();
		alertDlg = null;
	}	
	*/
}
