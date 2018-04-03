package milu.gui.ctrl.schema;

import java.util.ResourceBundle;
import java.sql.SQLException;

import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.AnchorPane;
import javafx.geometry.Orientation;

import milu.db.MyDBAbstract;

import milu.gui.view.DBView;
import milu.gui.ctrl.common.ChangeLangInterface;
import milu.gui.ctrl.common.ExecQueryDBInterface;
import milu.gui.ctrl.common.RefreshInterface;
import milu.gui.ctrl.common.ToggleHorizontalVerticalInterface;
import milu.gui.ctrl.schema.SchemaTreeView;
import milu.gui.ctrl.schema.handle.SelectedItemHandlerAbstract;
import milu.gui.ctrl.schema.handle.SelectedItemHandlerChooser;
import milu.gui.dlg.MyAlertDialog;
import milu.ctrl.MainController;

public class DBSchemaTab extends Tab
	implements 
		ExecQueryDBInterface,
		RefreshInterface,
		ToggleHorizontalVerticalInterface,
		ChangeLangInterface
{
	// Property File for this class 
	private static final String PROPERTY_FILENAME = 
			"conf.lang.gui.ctrl.schema.DBSchemaTab";

	// Language Resource
	private ResourceBundle langRB = ResourceBundle.getBundle( PROPERTY_FILENAME );
	
	private DBView          dbView = null;
	
	// upper pane on SplitPane
	private AnchorPane      upperPane = new AnchorPane();
	
	private SchemaTreeView  schemaTreeView = null;
	private TabPane         tabPane = null;
	
	public DBSchemaTab( DBView dbView )
	{
		super();
		
		this.dbView = dbView;
		
		this.schemaTreeView = new SchemaTreeView( this.dbView );
		this.tabPane        = new TabPane();
		// no tab text & dragging doesn't work well.
		// -----------------------------------------------------
		// Enable tag dragging
		//DraggingTabPaneSupport  dragSupport = new DraggingTabPaneSupport();
		//dragSupport.addSupport( this.tabPane );
		
		this.upperPane.getChildren().add( this.schemaTreeView );
		this.schemaTreeView.init();
		AnchorPane.setTopAnchor( this.schemaTreeView, 0.0 );
		AnchorPane.setBottomAnchor( this.schemaTreeView, 0.0 );
		AnchorPane.setLeftAnchor( this.schemaTreeView, 0.0 );
		AnchorPane.setRightAnchor( this.schemaTreeView, 0.0 );
		
		SplitPane splitPane = new SplitPane();
		splitPane.setOrientation(Orientation.VERTICAL);
		//splitPane.getItems().addAll( this.schemaTreeView, this.tabPane );
		splitPane.getItems().addAll( this.upperPane, this.tabPane );
		splitPane.setDividerPositions( 0.5f, 0.5f );
		
		BorderPane brdPane = new BorderPane();
		brdPane.setCenter( splitPane );
		
		this.setContent( brdPane );
		
		MainController mainController = this.dbView.getMainController();
		
		// set icon on Tab
		ImageView iv = new ImageView(  mainController.getImage("file:resources/images/schema.png") );
		iv.setFitHeight( 16 );
		iv.setFitWidth( 16 );
		this.setGraphic( iv );
		
		this.changeLang();
	}
	
	/**************************************************
	 * Override from ExecQueryDBInterface
	 ************************************************** 
	 */
	@Override
	public void Go( MyDBAbstract myDBAbs )
	{
		try
		{
			SelectedItemHandlerChooser.exec
			( 
				this.schemaTreeView, 
				this.tabPane, 
				this.dbView,
				myDBAbs, 
				SelectedItemHandlerAbstract.REFRESH_TYPE.NO_REFRESH 
			);
		}
		catch ( UnsupportedOperationException uoEx )
		{
			MyAlertDialog alertDlg = new MyAlertDialog(AlertType.WARNING);
			alertDlg.setHeaderText( langRB.getString("TITLE_UNSUPPORT_ERROR") );
    		alertDlg.showAndWait();
    		alertDlg = null;
		}
		catch ( SQLException sqlEx )
		{
			MyAlertDialog alertDlg = new MyAlertDialog(AlertType.WARNING);
			alertDlg.setHeaderText( langRB.getString("TITLE_EXEC_QUERY_ERROR") );
    		alertDlg.setTxtExp( sqlEx, myDBAbs );
    		alertDlg.showAndWait();
    		alertDlg = null;
		}
		catch ( Exception ex )
		{
			MyAlertDialog alertDlg = new MyAlertDialog(AlertType.WARNING);
			alertDlg.setHeaderText( langRB.getString("TITLE_MISC_ERROR") );
    		alertDlg.setTxtExp( ex );
    		alertDlg.showAndWait();
    		alertDlg = null;
		}
	}
	
	/**************************************************
	 * Override from RefreshInterface
	 ************************************************** 
	 */
	@Override
	public void Refresh( MyDBAbstract myDBAbs )
	{
		try
		{
			SelectedItemHandlerChooser.exec
			( 
				this.schemaTreeView, 
				this.tabPane,
				this.dbView,
				myDBAbs, 
				SelectedItemHandlerAbstract.REFRESH_TYPE.WITH_REFRESH 
			);
		}
		catch ( UnsupportedOperationException uoEx )
		{
			MyAlertDialog alertDlg = new MyAlertDialog(AlertType.WARNING);
			alertDlg.setHeaderText( langRB.getString("TITLE_UNSUPPORT_ERROR") );
    		alertDlg.showAndWait();
    		alertDlg = null;
		}
		catch ( SQLException sqlEx )
		{
			MyAlertDialog alertDlg = new MyAlertDialog(AlertType.WARNING);
			alertDlg.setHeaderText( langRB.getString("TITLE_EXEC_QUERY_ERROR") );
    		alertDlg.setTxtExp( sqlEx, myDBAbs );
    		alertDlg.showAndWait();
    		alertDlg = null;
		}
		catch ( Exception ex )
		{
			MyAlertDialog alertDlg = new MyAlertDialog(AlertType.WARNING);
			alertDlg.setHeaderText( langRB.getString("TITLE_MISC_ERROR") );
    		alertDlg.setTxtExp( ex );
    		alertDlg.showAndWait();
    		alertDlg = null;
		}
	}
	
	/**************************************************
	 * Override from ToggleHorizontalVerticalInterface
	 ************************************************** 
	 */
	@Override
	public void switchDirection()
	{
		final Tab selectedTab = this.tabPane.getSelectionModel().getSelectedItem();
		if ( 
			( selectedTab != null ) && 
			( selectedTab instanceof ToggleHorizontalVerticalInterface )
		)
		{
			((ToggleHorizontalVerticalInterface)selectedTab).switchDirection();
		}
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
		// Tab Title
		this.setText( langRB.getString("TITLE_TAB") );
		
		this.schemaTreeView.changeLang();
	}
}
