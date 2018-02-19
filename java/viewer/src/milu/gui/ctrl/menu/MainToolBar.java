package milu.gui.ctrl.menu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ResourceBundle;

import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import milu.ctrl.ChangeLangInterface;
import milu.gui.view.DBView;

import milu.db.explain.ExplainDBFactory;

public class MainToolBar extends ToolBar
	implements ChangeLangInterface
{
	// Property File for this class 
	private static final String PROPERTY_FILENAME = 
		 	"conf.lang.ctrl.menu.MainToolBar";

	private ResourceBundle langRB = ResourceBundle.getBundle( PROPERTY_FILENAME );
	
	// DBView
	private DBView  dbView = null;
	
	// Button to get data
	private Button btnGo = null;
	// Button to get explain
	private Button btnExplain = null;
	// Button to toggle Horizontal/Vertical mode
	private Button btnToggleHV = null;
	// Button to add new tab
	private Button btnNewTab = null;
	// Button to add new window
	private Button btnNewWin = null;
	// Button to add new DB connection
	private Button btnNewCon = null;
	// Button to copy table data(no column)
	private Button btnCopyTblNoHead = null;
	// Button to copy table data(with column)
	private Button btnCopyTblWithHead = null;
	// Button  to Open Schema View
	private Button btnSchema = null;
	
	public MainToolBar( DBView dbView )
	{
		// DBView
		this.dbView = dbView;
		
		// set Menu for this ToolBar
		this.setMenu();
		
		// set Action for this ToolBar
		this.setAction();
		
		// set text on Menu
		this.changeLang();
	}
	
	private void setMenu()
	{
		// Button to get data
		try
		{
			InputStream inputStreamGo = new FileInputStream( "resources" + File.separator + "images" + File.separator + "execsql.png" );
			Image imgGo = new Image( inputStreamGo );
			ImageView ivGo = new ImageView( imgGo );
			ivGo.setFitWidth( 32 );
			ivGo.setFitHeight( 32 );
			this.btnGo = new Button( "", ivGo );
		}
		catch ( FileNotFoundException fnfEx )
		{
			fnfEx.printStackTrace();
			this.btnGo = new Button( "Go!" );
		}
		
		// Button to get explain
		ImageView   ivExplain = new ImageView( new Image("file:resources/images/explain.png") );
		ivExplain.setFitWidth( 32 );
		ivExplain.setFitHeight( 32 );
		this.btnExplain = new Button( "", ivExplain );
		
		// Button to toggle Horizontal/Vertical mode
		try
		{
			InputStream inputStreamToggleHV = new FileInputStream( "resources" + File.separator + "images" + File.separator + "direction.png" );
			Image imgToggleHV = new Image( inputStreamToggleHV );
			ImageView ivToggleHV = new ImageView( imgToggleHV );
			ivToggleHV.setFitWidth( 32 );
			ivToggleHV.setFitHeight( 32 );
			this.btnToggleHV = new Button( "", ivToggleHV );
		}
		catch ( FileNotFoundException fnfEx )
		{
			fnfEx.printStackTrace();
			this.btnToggleHV = new Button( "Toggle H/V" );
		}
		
		// Button to add a new tab
		try
		{
			InputStream inputStreamNewTab = new FileInputStream( "resources" + File.separator + "images" + File.separator + "newtab.png" );
			Image imgNewTab = new Image( inputStreamNewTab );
			ImageView ivNewTab = new ImageView( imgNewTab );
			ivNewTab.setFitWidth( 32 );
			ivNewTab.setFitHeight( 32 );
			this.btnNewTab = new Button( "", ivNewTab );
		}
		catch ( FileNotFoundException fnfEx )
		{
			fnfEx.printStackTrace();
			this.btnNewTab = new Button( "New Tab" );
		}
		
		// Button to add a new window
		try
		{
			InputStream inputStreamNewWin = new FileInputStream( "resources" + File.separator + "images" + File.separator + "newwin.png" );
			Image imgNewWin = new Image( inputStreamNewWin );
			ImageView ivNewWin = new ImageView( imgNewWin );
			ivNewWin.setFitWidth( 32 );
			ivNewWin.setFitHeight( 32 );
			this.btnNewWin = new Button( "", ivNewWin );
		}
		catch ( FileNotFoundException fnfEx )
		{
			fnfEx.printStackTrace();
			this.btnNewWin = new Button( "New Window" );
		}
		
		// Button for new db connection
		ImageView   ivNewCon = new ImageView( new Image("file:resources/images/connect.png") );
		ivNewCon.setFitWidth( 32 );
		ivNewCon.setFitHeight( 32 );
		this.btnNewCon = new Button( "", ivNewCon );
		
		// Button to copy table data(no column)
		try
		{
			InputStream inputStreamCopyTblNoHead = new FileInputStream( "resources" + File.separator + "images" + File.separator + "copy.png" );
			Image imgCopyTblNoHead = new Image( inputStreamCopyTblNoHead );
			ImageView ivCopyTblNoHead = new ImageView( imgCopyTblNoHead );
			ivCopyTblNoHead.setFitWidth( 32 );
			ivCopyTblNoHead.setFitHeight( 32 );
			this.btnCopyTblNoHead = new Button( "", ivCopyTblNoHead );
		}
		catch ( FileNotFoundException fnfEx )
		{
			fnfEx.printStackTrace();
			this.btnCopyTblNoHead = new Button( "Copy Table without Column" );
		}
		
		// Button to copy table data(with column)
		try
		{
			InputStream inputStreamCopyTblWithHead = new FileInputStream( "resources" + File.separator + "images" + File.separator + "copy2.png" );
			Image imgCopyTblWithHead = new Image( inputStreamCopyTblWithHead );
			ImageView ivCopyTblWithHead = new ImageView( imgCopyTblWithHead );
			ivCopyTblWithHead.setFitWidth( 32 );
			ivCopyTblWithHead.setFitHeight( 32 );
			this.btnCopyTblWithHead = new Button( "", ivCopyTblWithHead );
		}
		catch ( FileNotFoundException fnfEx )
		{
			fnfEx.printStackTrace();
			this.btnCopyTblWithHead = new Button( "Copy Table with Column" );
		}
		
		// Button to Open Schema View
		try
		{
			InputStream inputStreamSchema = new FileInputStream( "resources" + File.separator + "images" + File.separator + "schema.png" );
			Image imgSchema = new Image( inputStreamSchema );
			ImageView ivSchema = new ImageView( imgSchema );
			ivSchema.setFitWidth( 32 );
			ivSchema.setFitHeight( 32 );
			this.btnSchema = new Button( "", ivSchema );
		}
		catch ( FileNotFoundException fnfEx )
		{
			fnfEx.printStackTrace();
			this.btnSchema = new Button( "Open Schema View" );
		}
		
		// put buttons on this ToolBar
		this.getItems().addAll
		(
			new Separator(),
			this.btnGo
		);
		
		if ( ExplainDBFactory.getInstance( this.dbView.getMyDBAbstract() ) != null )
		{
			this.getItems().add( this.btnExplain );
		}
		
		this.getItems().addAll
		(
			this.btnToggleHV,
			this.btnCopyTblNoHead,
			this.btnCopyTblWithHead,
			new Separator(),
			this.btnNewTab,
			this.btnNewWin,
			this.btnNewCon,
			new Separator(),
			this.btnSchema,
			new Separator()
		);
	}
	
	private void setAction()
	{
		// "Go!!" button clicked
		// http://code.makery.ch/blog/javafx-8-event-handling-examples/		
		this.btnGo.setOnAction(	(event)->{ this.dbView.Go(); } );
		
		// Explain button clicked
		this.btnExplain.setOnAction( event->this.dbView.execExplain() );
		
		// "Toggle H/V" button clicked
		this.btnToggleHV.setOnAction( (event)->{ this.dbView.switchDirection();	} );
		
		// "New Tab" button clicked
		this.btnNewTab.setOnAction(	(event)->{ this.dbView.createNewTab(); } );
		
		// "New Window" button clicked
		this.btnNewWin.setOnAction(	(event)->{ this.dbView.createNewWindow(); } );
		
		// "New DB Connection" button clicked
		this.btnNewCon.setOnAction( (event)->{ this.dbView.createNewDBConnection(); } );
		
		// "Copy datas on TableView without column heads" button clicked
		this.btnCopyTblNoHead.setOnAction( (event)->{ this.dbView.copyTableNoHead(); } );
		
		// "Copy datas on TableView with column heads" button clicked
		this.btnCopyTblWithHead.setOnAction( (event)->{ this.dbView.copyTableWithHead(); } );
		
		// "Open Schema View" button clicked
		this.btnSchema.setOnAction( (event)->{ this.dbView.openSchemaView(); } );
		// disable until finishing to get schema list.
		this.btnSchema.setDisable(true);
	}
	
	public void setMnemonic()
	{
		// ---------------------------------
		// Mnemonic for "Exec SQL"
		// Ctrl+G
		// ---------------------------------
		this.btnGo.getScene().getAccelerators().put
		(
			new KeyCodeCombination( KeyCode.G, KeyCombination.CONTROL_DOWN ),	
			new Runnable()
			{
				@Override
				public void run()
				{
					dbView.Go();
				}
			}
		);
		
		// ---------------------------------
		// Mnemonic for "Toggle horizontal/vertical mode for Table"
		// Ctrl+D
		// ---------------------------------
		this.btnToggleHV.getScene().getAccelerators().put
		(
			new KeyCodeCombination( KeyCode.D, KeyCombination.CONTROL_DOWN ),	
			new Runnable()
			{
				@Override
				public void run()
				{
					dbView.switchDirection();
				}
			}
		);
		
		// ---------------------------------
		// Mnemonic for "Open new tab"
		// Ctrl+T
		// ---------------------------------
		this.btnNewTab.getScene().getAccelerators().put
		(
			new KeyCodeCombination( KeyCode.T, KeyCombination.CONTROL_DOWN ),	
			new Runnable()
			{
				@Override
				public void run()
				{
					dbView.createNewTab();
				}
			}
		);
		
		// ---------------------------------
		// Mnemonic for "Open new window"
		// Ctrl+N
		// ---------------------------------
		this.btnNewWin.getScene().getAccelerators().put
		(
			new KeyCodeCombination( KeyCode.N, KeyCombination.CONTROL_DOWN ),	
			new Runnable()
			{
				@Override
				public void run()
				{
					dbView.createNewWindow();
				}
			}
		);

		// ------------------------------------------------------------
		// Mnemonic for "Copy datas on TableView without column heads"
		// Alt+C
		// ------------------------------------------------------------
		this.btnCopyTblNoHead.getScene().getAccelerators().put
		(
			new KeyCodeCombination( KeyCode.C, KeyCombination.ALT_DOWN ),	
			new Runnable()
			{
				@Override
				public void run()
				{
					dbView.copyTableNoHead();
				}
			}
		);

		// ------------------------------------------------------------
		// Mnemonic for "Copy datas on TableView with column heads"
		// Alt+Shift+C
		// ------------------------------------------------------------
		this.btnCopyTblWithHead.getScene().getAccelerators().put
		(
			//new KeyCodeCombination( KeyCode.C, KeyCombination.ALT_DOWN | KeyCombination.SHIFT_DOWN ),	
			KeyCombination.keyCombination( "ALT+SHIFT+C" ),
			new Runnable()
			{
				@Override
				public void run()
				{
					dbView.copyTableWithHead();
				}
			}
		);
	}
	
	public void taskDone()
	{
		// disable until finishing to get schema list.
		this.btnSchema.setDisable(false);
	}

	/**
	 * Load Language Resource
	 */
	private void loadLangResource()
	{
		this.langRB = ResourceBundle.getBundle( PROPERTY_FILENAME );
	}
	
	/**************************************************
	 * Override from ChangeLangInterface
	 ************************************************** 
	 */
	@Override	
	public void changeLang()
	{
		this.loadLangResource();
		
		// ----------------------------------------------
		// ToolTip
		//   Button[Exec Query] 
		// ----------------------------------------------
		Tooltip tipGo = new Tooltip( this.langRB.getString( "TIP_EXECSQL" ) );
		tipGo.getStyleClass().add("mytooltip");
		this.btnGo.setTooltip( tipGo );
		
		// ----------------------------------------------
		// ToolTip
		//   Button[Exec Explain] 
		// ----------------------------------------------
		Tooltip tipExplain = new Tooltip( this.langRB.getString( "TIP_EXEC_EXPLAIN" ) );
		tipExplain.getStyleClass().add("mytooltip");
		this.btnExplain.setTooltip( tipExplain );
		
		// ----------------------------------------------
		// ToolTip
		//   Button[Toggle Horizontal/Vertical mode] 
		// ----------------------------------------------
		Tooltip tipToggleHV = new Tooltip(this.langRB.getString( "TIP_TOGGLE_HV" ));
		tipToggleHV.getStyleClass().add("mytooltip");
		this.btnToggleHV.setTooltip( tipToggleHV );

		// ----------------------------------------------
		// ToolTip
		//   Button[New Tab] 
		// ----------------------------------------------
		Tooltip tipNewTab = new Tooltip(this.langRB.getString( "TIP_NEW_TAB" ));
		tipNewTab.getStyleClass().add("mytooltip");
		this.btnNewTab.setTooltip( tipNewTab );

		// ----------------------------------------------
		// ToolTip
		//   Button[New Window] 
		// ----------------------------------------------
		Tooltip tipNewWin = new Tooltip(this.langRB.getString( "TIP_NEW_WIN" ));
		tipNewWin.getStyleClass().add("mytooltip");
		this.btnNewWin.setTooltip( tipNewWin );

		// ----------------------------------------------
		// ToolTip
		//   Button[New DB Connection] 
		// ----------------------------------------------
		Tooltip tipNewCon = new Tooltip(this.langRB.getString( "TIP_NEW_CON" ));
		tipNewCon.getStyleClass().add("mytooltip");
		this.btnNewCon.setTooltip( tipNewCon );

		// ----------------------------------------------
		// ToolTip
		//   Button[Copy table data without column] 
		// ----------------------------------------------
		Tooltip tipCopyTblNoHead = new Tooltip(this.langRB.getString( "TIP_COPY_TABLE_NO_HEAD" ));
		tipCopyTblNoHead.getStyleClass().add("mytooltip");
		this.btnCopyTblNoHead.setTooltip( tipCopyTblNoHead );

		// ----------------------------------------------
		// ToolTip
		//   Button[Copy table data with column] 
		// ----------------------------------------------
		Tooltip tipCopyTblWithHead = new Tooltip(this.langRB.getString( "TIP_COPY_TABLE_WITH_HEAD" ));
		tipCopyTblWithHead.getStyleClass().add("mytooltip");
		this.btnCopyTblWithHead.setTooltip( tipCopyTblWithHead );

		// ----------------------------------------------
		// ToolTip
		//   Button[Open Schema View] 
		// ----------------------------------------------
		Tooltip tipSchema = new Tooltip(this.langRB.getString( "TIP_SCHEMA" ));
		tipSchema.getStyleClass().add("mytooltip");
		this.btnSchema.setTooltip( tipSchema );
	}
	
}
