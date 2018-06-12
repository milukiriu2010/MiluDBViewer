package milu.gui.ctrl.schema;

import java.util.ResourceBundle;
import java.sql.SQLException;

import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Node;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.geometry.Orientation;

import milu.db.MyDBAbstract;

import milu.gui.view.DBView;
import milu.main.MainController;
import milu.tool.MyTool;
import milu.gui.ctrl.common.inf.ChangeLangInterface;
import milu.gui.ctrl.common.inf.ExecQueryDBInterface;
import milu.gui.ctrl.common.inf.FocusInterface;
import milu.gui.ctrl.common.inf.RefreshInterface;
import milu.gui.ctrl.schema.SchemaTreeView;
import milu.gui.ctrl.schema.handle.SelectedItemHandlerAbstract;
import milu.gui.ctrl.schema.handle.SelectedItemHandlerFactory;
import milu.gui.dlg.MyAlertDialog;

public class DBSchemaTab extends Tab
	implements 
		FocusInterface,
		GetDataInterface,
		ChangeLangInterface
{
	private DBView          dbView = null;
	
	// upper pane on SplitPane
	private AnchorPane      upperPane = new AnchorPane();
	
	private SchemaTreeView  schemaTreeView = null;
	private TabPane         tabPane = null;
	
	public DBSchemaTab( DBView dbView )
	{
		super();
		
		this.dbView = dbView;
		
		this.schemaTreeView = new SchemaTreeView( this.dbView, this );
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
		splitPane.getItems().addAll( this.upperPane, this.tabPane );
		splitPane.setDividerPositions( 0.5f, 0.5f );
		
		BorderPane brdPane = new BorderPane();
		brdPane.setCenter( splitPane );
		
		this.setContent( brdPane );

		// set icon on Tab
		this.setGraphic( MyTool.createImageView( 16, 16, this.dbView.getMainController().getImage("file:resources/images/schema.png") ) );
		
		this.setAction();
		
		this.changeLang();
		
		this.getDataNoRefresh( null );
	}
	
	private void setAction()
	{
		this.selectedProperty().addListener
		(
			(obs,oldval,newVal)->
			{
				if ( newVal == false )
				{
					return;
				}
				
				this.setFocus();
			}
		);
	}
	
	/**
	 * set Focus on TreeView
	 */
	@Override
	public void setFocus()
	{
		// https://sites.google.com/site/63rabbits3/javafx2/jfx2coding/dialogbox
		// https://stackoverflow.com/questions/20049452/javafx-focusing-textfield-programmatically
		// call after "new Scene"
		Platform.runLater( ()->{ this.schemaTreeView.requestFocus(); System.out.println( "schemaTreeView focused."); } );
	}
	
	/**************************************************
	 * Override from ExecQueryDBInterface
	 ************************************************** 
	 */
	/*
	@Override
	public void Go()
	{
		MyDBAbstract myDBAbs = this.dbView.getMyDBAbstract();
		try
		{
			SelectedItemHandlerAbstract handleAbs = 
				SelectedItemHandlerFactory.getInstance
				( 
					this.schemaTreeView, 
					this.tabPane, 
					this.dbView,
					myDBAbs, 
					SelectedItemHandlerAbstract.REFRESH_TYPE.NO_REFRESH 
				);
			if ( handleAbs != null )
			{
				handleAbs.exec();
			}
		}
		catch ( UnsupportedOperationException uoEx )
		{
			ResourceBundle langRB = this.dbView.getMainController().getLangResource("conf.lang.gui.common.MyAlert");
			MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING, this.dbView.getMainController() );
			alertDlg.setHeaderText( langRB.getString("TITLE_UNSUPPORT_ERROR") );
    		alertDlg.showAndWait();
    		alertDlg = null;
		}
		catch ( SQLException sqlEx )
		{
			ResourceBundle langRB = this.dbView.getMainController().getLangResource("conf.lang.gui.common.MyAlert");
			MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING, this.dbView.getMainController() );
			alertDlg.setHeaderText( langRB.getString("TITLE_EXEC_QUERY_ERROR") );
    		alertDlg.setTxtExp( sqlEx, myDBAbs );
    		alertDlg.showAndWait();
    		alertDlg = null;
		}
		catch ( Exception ex )
		{
			ResourceBundle langRB = this.dbView.getMainController().getLangResource("conf.lang.gui.common.MyAlert");
			MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING, this.dbView.getMainController() );
			alertDlg.setHeaderText( langRB.getString("TITLE_MISC_ERROR") );
    		alertDlg.setTxtExp( ex );
    		alertDlg.showAndWait();
    		alertDlg = null;
		}
	}
	*/
	
	/**************************************************
	 * Override from RefreshInterface
	 ************************************************** 
	 */
	/*
	@Override
	public void Refresh()
	{
		MyDBAbstract myDBAbs = this.dbView.getMyDBAbstract();
		try
		{
			SelectedItemHandlerAbstract handleAbs = 
				SelectedItemHandlerFactory.getInstance
				( 
					this.schemaTreeView, 
					this.tabPane, 
					this.dbView,
					myDBAbs, 
					SelectedItemHandlerAbstract.REFRESH_TYPE.WITH_REFRESH 
				);
			if ( handleAbs != null )
			{
				handleAbs.exec();
			}			
		}
		catch ( UnsupportedOperationException uoEx )
		{
			ResourceBundle langRB = this.dbView.getMainController().getLangResource("conf.lang.gui.common.MyAlert");
			MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING, this.dbView.getMainController() );
			alertDlg.setHeaderText( langRB.getString("TITLE_UNSUPPORT_ERROR") );
    		alertDlg.showAndWait();
    		alertDlg = null;
		}
		catch ( SQLException sqlEx )
		{
			ResourceBundle langRB = this.dbView.getMainController().getLangResource("conf.lang.gui.common.MyAlert");
			MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING, this.dbView.getMainController() );
			alertDlg.setHeaderText( langRB.getString("TITLE_EXEC_QUERY_ERROR") );
    		alertDlg.setTxtExp( sqlEx, myDBAbs );
    		alertDlg.showAndWait();
    		alertDlg = null;
		}
		catch ( Exception ex )
		{
			ResourceBundle langRB = this.dbView.getMainController().getLangResource("conf.lang.gui.common.MyAlert");
			MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING, this.dbView.getMainController() );
			alertDlg.setHeaderText( langRB.getString("TITLE_MISC_ERROR") );
    		alertDlg.setTxtExp( ex );
    		alertDlg.showAndWait();
    		alertDlg = null;
		}
	}
	*/
	
	// GetDataInterface
	@Override
	public void getDataNoRefresh( Event event )
	{
		this.getSchemaData( event, SelectedItemHandlerAbstract.REFRESH_TYPE.NO_REFRESH );
	}
	
	// GetDataInterface
	@Override
	public void getDataWithRefresh( Event event )
	{
		this.getSchemaData( event, SelectedItemHandlerAbstract.REFRESH_TYPE.WITH_REFRESH );
	}
	
	private void getSchemaData( Event event, SelectedItemHandlerAbstract.REFRESH_TYPE refreshType )
	{
		MyDBAbstract myDBAbs = this.dbView.getMyDBAbstract();
		try
		{
			SelectedItemHandlerAbstract handleAbs = 
				SelectedItemHandlerFactory.getInstance
				( 
					this.schemaTreeView, 
					this.tabPane, 
					this.dbView,
					myDBAbs, 
					refreshType 
				);
			if ( handleAbs != null )
			{
				handleAbs.exec();
			}
		}
		catch ( UnsupportedOperationException uoEx )
		{
			ResourceBundle langRB = this.dbView.getMainController().getLangResource("conf.lang.gui.common.MyAlert");
			MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING, this.dbView.getMainController() );
			alertDlg.setHeaderText( langRB.getString("TITLE_UNSUPPORT_ERROR") );
    		alertDlg.showAndWait();
    		alertDlg = null;
		}
		catch ( SQLException sqlEx )
		{
			ResourceBundle langRB = this.dbView.getMainController().getLangResource("conf.lang.gui.common.MyAlert");
			MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING, this.dbView.getMainController() );
			alertDlg.setHeaderText( langRB.getString("TITLE_EXEC_QUERY_ERROR") );
    		alertDlg.setTxtExp( sqlEx, myDBAbs );
    		alertDlg.showAndWait();
    		alertDlg = null;
		}
		catch ( Exception ex )
		{
			ResourceBundle langRB = this.dbView.getMainController().getLangResource("conf.lang.gui.common.MyAlert");
			MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING, this.dbView.getMainController() );
			alertDlg.setHeaderText( langRB.getString("TITLE_MISC_ERROR") );
    		alertDlg.setTxtExp( ex );
    		alertDlg.showAndWait();
    		alertDlg = null;
		}		
	}
	
	/**************************************************
	 * Override from ChangeLangInterface
	 ************************************************** 
	 */
	@Override	
	public void changeLang()
	{
		MainController mainCtrl = this.dbView.getMainController();
		ResourceBundle langRB = mainCtrl.getLangResource("conf.lang.gui.ctrl.schema.DBSchemaTab");
		
		
		// Tab Title
		Node tabGraphic = this.getGraphic();
		if ( tabGraphic instanceof Label )
		{
			((Label)tabGraphic).setText( langRB.getString("TITLE_TAB") );
		}
		else
		{
			this.setText( langRB.getString("TITLE_TAB") );
		}
		
		this.schemaTreeView.changeLang();
		this.schemaTreeView.refresh();
	}
}
