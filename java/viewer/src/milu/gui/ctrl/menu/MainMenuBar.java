package milu.gui.ctrl.menu;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;
import java.io.IOException;
import java.nio.file.Paths;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.stage.Window;
import milu.db.MyDBAbstract;
import milu.gui.ctrl.common.inf.ChangeLangInterface;
import milu.gui.ctrl.info.SystemTab;
import milu.gui.ctrl.info.VersionTab;
import milu.gui.ctrl.jdbc.DBJdbcTab;
import milu.gui.dlg.app.AppSettingDialog;
import milu.gui.view.DBView;
import milu.main.AppConf;
import milu.main.AppConst;
import milu.main.MainController;
import milu.tool.MyFileTool;
import milu.tool.MyGUITool;

public class MainMenuBar extends MenuBar
	implements
		AfterDBConnectedInterface,
		ChangeLangInterface
{
	// Control View
	private DBView  dbView = null;
	
	// Language Map
	//   String  => Language Menu
	//   String  => Language Code
	private Map<String, String>  langMap = new TreeMap<String, String>();

	// ----------------------------------------------
	// Menu 
	// ----------------------------------------------
	// [File]
	//   - [Preferences]
	//   - [Quit]
	// ----------------------------------------------
	Menu     menuFile     = new Menu();
	MenuItem menuItemPref = new MenuItem();
    MenuItem menuItemQuit = new MenuItem();

	// ----------------------------------------------
	// Menu 
	// ----------------------------------------------
	// [Language]
	//   - [English]
    //   - [日本語]
    //     etc
	// ----------------------------------------------
    Menu     menuLang   = new Menu();
    List<CheckMenuItem> menuItemLangLst = new ArrayList<CheckMenuItem>();
    
	// ----------------------------------------------
	// Menu 
	// ----------------------------------------------
	// [Bookmark]
    //   - <separator>
	//   - [DB Alias]
    //     etc
	// ----------------------------------------------
    Menu     menuBookMark = new Menu();
    
	// ----------------------------------------------
	// Menu 
	// ----------------------------------------------
	// [Window]
    //   - <separator>
	//   - [Window Title]
    //     etc
	// ----------------------------------------------
    Menu     menuWin       = new Menu();

	// ----------------------------------------------
	// Menu 
	// ----------------------------------------------
	// [Help]
    //   - [System Info]
    //   - [JDBC Info]
    //   - [Version]
    //     etc
	// ----------------------------------------------
    Menu     menuHelp         = new Menu();
    MenuItem menuItemSysInfo  = new MenuItem();
    MenuItem menuItemJDBC     = new MenuItem("JDBC");
    MenuItem menuItemVersion  = new MenuItem();
    
	public MainMenuBar( DBView dbView )
	{
		// Control View
		this.dbView = dbView;
		
		// Create Language Map
		//   String  => Language Menu
		//   String  => Language Code
		this.langMap.put( "English" , "en" );
		this.langMap.put( "日本語"   , "ja" );
		this.langMap.put( "Français", "fr" );
		this.langMap.put( "Español" , "es" );
		this.langMap.put( "中文简体" , "zh" );
		
		// set Menu for this MenuBar
		this.setMenu();
		
		// set Action for this MenuBar
		this.setAction();
		
		// set text on Menu
		this.changeLang();
	}

	private void setMenu()
	{
		// ----------------------------------------------
		// Menu 
		// [File]
		//   - [Preferences]
		//   - [Quit]
		// ----------------------------------------------
		this.menuFile.getItems().addAll
		( 
			this.menuItemPref,
			new SeparatorMenuItem(),
			this.menuItemQuit 
		);
		
		MainController mainCtrl = this.dbView.getMainController();
		
		// set icon on menuItemPref
		this.menuItemPref.setGraphic( MyGUITool.createImageView( 16, 16, mainCtrl.getImage("file:resources/images/config.png") ) );
		// set icon on menuItemQuit
		this.menuItemQuit.setGraphic( MyGUITool.createImageView( 16, 16, mainCtrl.getImage("file:resources/images/quit.png") ) );
		// https://blog.idrsolutions.com/2014/04/tutorial-how-to-setup-key-combinations-in-javafx/
		// Close [Alt+F4]
		this.menuItemQuit.setAccelerator( new KeyCodeCombination(KeyCode.F4, KeyCombination.ALT_DOWN ));
		
		// ----------------------------------------------
		// Menu 
		// ----------------------------------------------
		// [Language]
		//   - [English]
	    //   - [日本語]
	    //     etc
		// ----------------------------------------------
		Set<String> langKeySet = this.langMap.keySet();
		Iterator<String> langKeyIterator = langKeySet.iterator();
		while ( langKeyIterator.hasNext() )
		{
			String langName = langKeyIterator.next();
			CheckMenuItem menuItemLang = new CheckMenuItem( langName );
			// deprecated version 19
			//if ( Locale.getDefault().getLanguage().equals( new Locale( this.langMap.get(langName) ).getLanguage() ) )
			if ( Locale.getDefault().getLanguage().equals( Locale.of( this.langMap.get(langName) ).getLanguage() ) )
			{
				menuItemLang.selectedProperty().set( true );
			}
			this.menuLang.getItems().addAll( menuItemLang );
			this.menuItemLangLst.add( menuItemLang );
		}
	    
		// ----------------------------------------------
		// Menu 
		// ----------------------------------------------
		// [Bookmark]
	    //   - <separator>
		//   - [DB Alias]
	    //     etc
		// ----------------------------------------------
	    this.menuBookMark.getItems().addAll( new MenuItem("dummy") );
	    
		// ----------------------------------------------
		// Menu 
		// ----------------------------------------------
		// [Window]
	    //   - <separator>
		//   - [Window Title]
	    //     etc
		// ----------------------------------------------
		this.menuWin.getItems().addAll( new MenuItem( "dummy") );
		
		// ----------------------------------------------
		// Menu 
		// ----------------------------------------------
		// [Help]
	    //   - [System Info]
	    //   - [JDBC Info]
		//   - [About]
		//   - [Version]
	    //     etc
		// ----------------------------------------------
		this.menuHelp.getItems().addAll
		( 
			this.menuItemSysInfo,
			this.menuItemJDBC,
			this.menuItemVersion
		);
		// set icon on menuItemSysInfo
		this.menuItemSysInfo.setGraphic( MyGUITool.createImageView( 16, 16, mainCtrl.getImage("file:resources/images/sysinfo.png") ) );
		// set icon on menuItemSysInfo
		this.menuItemJDBC.setGraphic( MyGUITool.createImageView( 16, 16, mainCtrl.getImage("file:resources/images/jdbc.png") ) );
		// set icon on menuItemVersion
		this.menuItemVersion.setGraphic( MyGUITool.createImageView( 16, 16, mainCtrl.getImage("file:resources/images/winicon.gif") ) );
		
		// put Menu on MenuBar
		this.getMenus().addAll( this.menuFile, this.menuBookMark, this.menuWin, this.menuHelp, this.menuLang );
	}
	
	private void setAction()
	{
		final MainController mainCtrl = this.dbView.getMainController();
		AppConf appConf = mainCtrl.getAppConf();
		
		// "Preferences" menu clicked
		this.menuItemPref.setOnAction((event)->{
			AppSettingDialog dlg = new AppSettingDialog( mainCtrl );
			dlg.showAndWait();
		});
		
		// "Quit" menu clicked
		this.menuItemQuit.setOnAction( mainCtrl::quit );
		
		// [Language]
		//   - [English] menu clicked
		//   - [日本語]   menu clicked
		//   .....
		for ( CheckMenuItem menuItemLang : this.menuItemLangLst )
		{
			menuItemLang.setOnAction((event)->{
				// check off the item of other language 
				for ( CheckMenuItem chkMenuItem : this.menuItemLangLst )
				{
					if ( chkMenuItem != menuItemLang )
					{
						chkMenuItem.selectedProperty().set( false );
					}
				}
				String langCode = this.langMap.get( menuItemLang.getText() );
				appConf.setLangCode(langCode);
				MyFileTool.save(mainCtrl, appConf);
				
				mainCtrl.changeLang();
			});
		}
		
	    
		// ----------------------------------------------
		// Menu 
		// ----------------------------------------------
		// [Bookmark]
	    //   - <separator>
		//   - [DB Alias]
	    //     etc
		// ----------------------------------------------
	    this.menuBookMark.setOnShowing((event)->{
	    	this.menuBookMark.getItems().removeAll(this.menuBookMark.getItems());
	    	try
	    	{
	    		//this.createMenuBookMark( Paths.get(AppConst.DB_DIR.val()), this.menuBookMark );
	    		MyFileTool.createMenuBookMark( Paths.get(AppConst.DB_DIR.val()), this.menuBookMark, this.dbView, this );
	    	}
	    	catch ( IOException ioEx )
	    	{
	    		throw new RuntimeException(ioEx);
	    	}
	    });
		

		// ----------------------------------------------
		// Menu 
		// ----------------------------------------------
		// [Window]
		//   - [Window Title]
	    //     etc
		// ----------------------------------------------
		this.menuWin.setOnShowing((event)->{
			this.menuWin.getItems().removeAll(this.menuWin.getItems());
			List<Window>  showingWinLst = Stage.getWindows().filtered( win->win.isShowing() );
			for ( Window win : showingWinLst )
			{
				if ( win instanceof DBView )
				{
					// Add window list on Menu.
					CheckMenuItem menuItemWin = new CheckMenuItem( ((DBView)win).getTitle() );
					if ( win == this.dbView )
					{
						menuItemWin.setSelected(true);
					}
					this.menuWin.getItems().add( menuItemWin );
					
					// Activate the selected window
					menuItemWin.setOnAction((event2)->win.requestFocus());
				}
			}
		});
		
		// ----------------------------------------------
		// [Help]
		//   - [System Information] menu clicked
		// ----------------------------------------------
		this.menuItemSysInfo.setOnAction((event)->this.dbView.openView(SystemTab.class,true));
		
		// ----------------------------------------------
		// [Help]
		//   - [JDBC Info] menu clicked
		// ----------------------------------------------
		this.menuItemJDBC.setOnAction( (event)->this.dbView.openView( DBJdbcTab.class,true ) );
		
		// ----------------------------------------------
		// [Help]
		//   - [Version] menu clicked
		// ----------------------------------------------
		this.menuItemVersion.setOnAction( (event)->this.dbView.openView( VersionTab.class,true ) );
	}
	
	/*
	private void createMenuBookMark( Path pathParent, Menu menuParent ) throws IOException
	{
		if ( Files.isDirectory(pathParent) == false )
		{
			return;
		}
		
		try
		(
	    	DirectoryStream<Path> directoryStream = 
    			Files.newDirectoryStream(pathParent);
		)
		{
			for ( Path path : directoryStream )
			{
				if ( Files.isRegularFile(path) )
				{
					if ( path.toString().endsWith("json") )
					{
						MenuItem menuItem = new DBMenuItem(path,this.dbView.getMainController(), this);
						menuParent.getItems().add(menuItem);
					}
				}
				else if ( Files.isDirectory(path) )
				{
					Menu menuSub = new Menu(path.toFile().getName());
					menuParent.getItems().add(menuSub);
					
					this.createMenuBookMark( path, menuSub );
				}
			}
		}
	}
	*/
	
	// AfterDBConnectedInterface
	@Override
	public void afterConnection( MyDBAbstract myDBAbs )
	{
		this.dbView.getMainController().createNewWindow(myDBAbs);		
	}
	
	// ChangeLangInterface
	@Override
	public void changeLang()
	{
		MainController mainCtrl = this.dbView.getMainController();
		ResourceBundle langRB = mainCtrl.getLangResource("conf.lang.gui.ctrl.menu.MainMenuBar");
		
		// ----------------------------------------------
		// Menu 
		// [File]
		//   - [Preferences]
		//   - [Quit]
		// ----------------------------------------------
	    this.menuFile.setText( langRB.getString( "MENU_FILE" ) );
	    this.menuItemPref.setText( langRB.getString( "MENU_FILE_PREFERENCES" ) );
		this.menuItemQuit.setText( langRB.getString( "MENU_FILE_QUIT" ) );
		
		// ----------------------------------------------
		// Menu 
		// ----------------------------------------------
		// [Language]
		//   - [English]
	    //   - [日本語]
	    //     etc
		// ----------------------------------------------
		this.menuLang.setText( langRB.getString( "MENU_LANG" ) );
	    
		// ----------------------------------------------
		// Menu 
		// ----------------------------------------------
		// [Bookmark]
	    //   - <separator>
		//   - [DB Alias]
	    //     etc
		// ----------------------------------------------
		this.menuBookMark.setText( langRB.getString("MENU_BOOKMARK") );
	    
		// ----------------------------------------------
		// Menu 
		// ----------------------------------------------
		// [Window]
	    //   - <separator>
		//   - [Window Title]
	    //     etc
		// ----------------------------------------------
		this.menuWin.setText( langRB.getString("MENU_WIN") );

		// ----------------------------------------------
		// Menu 
		// ----------------------------------------------
		// [Help]
		//   - [System Info]
		//   - [About]
		//   - [Version]
		// ----------------------------------------------
	    this.menuHelp.setText( langRB.getString( "MENU_HELP" ) );
	    this.menuItemSysInfo.setText( langRB.getString( "MENU_HELP_SYSINFO" ) );
	    this.menuItemVersion.setText( langRB.getString( "MENU_HELP_VERSION" ) );
	}
	
}
