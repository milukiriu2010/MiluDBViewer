package milu.gui.dlg.app;

import java.util.ResourceBundle;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TreeView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TreeItem;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.event.ActionEvent;
import javafx.application.Platform;
import milu.gui.ctrl.common.ButtonOrderNoneDialogPane;
import milu.gui.dlg.MyAlertDialog;
import milu.main.MainController;
import milu.tool.MyTool;

public class AppSettingDialog extends Dialog<Boolean>
{
	// Configuration List
	private TreeView<AppSettingMenu> treeViewConf = new TreeView<AppSettingMenu>();
	
	// Apply Button
	private ButtonType applyButtonType = null;
	
	// Apply&Close Button
	private ButtonType okButtonType = null;
	
	// Main Controller
	private MainController mainCtrl = null;
	
	public AppSettingDialog( MainController mainCtrl )
	{
		super();
		
		this.mainCtrl = mainCtrl;
		ResourceBundle langRB = this.mainCtrl.getLangResource("conf.lang.gui.dlg.app.AppSettingDialog");
		
		// set DialogPane which has no button order.
		this.setDialogPane( new ButtonOrderNoneDialogPane() );
		
		// Set dialog title.
		this.setTitle( langRB.getString( "TITLE_APP_SETTING" ) );
		
		// set items on TreeView
		TreeItem<AppSettingMenu>  treeItemRoot = 
			new TreeItem<>( new AppSettingMenu( "" , AppSettingMenu.APPSET_TYPE.TYPE_ROOT ) );
		TreeItem<AppSettingMenu>  treeItemDB = 
			new TreeItem<>( new AppSettingMenu( langRB.getString( "ITEM_DB" ), AppSettingMenu.APPSET_TYPE.TYPE_DB ) );
		TreeItem<AppSettingMenu>  treeItemDBMySQL = 
			new TreeItem<>( new AppSettingMenu( langRB.getString( "ITEM_DB_MYSQL" ), AppSettingMenu.APPSET_TYPE.TYPE_DB_MYSQL ) );
		TreeItem<AppSettingMenu>  treeItemDBOracle = 
			new TreeItem<>( new AppSettingMenu( langRB.getString( "ITEM_DB_ORACLE" ), AppSettingMenu.APPSET_TYPE.TYPE_DB_ORACLE ) );
		TreeItem<AppSettingMenu>  treeItemDBPostgreSQL = 
			new TreeItem<>( new AppSettingMenu( langRB.getString( "ITEM_DB_POSTGRESQL" ), AppSettingMenu.APPSET_TYPE.TYPE_DB_POSTGRESQL ) );
		TreeItem<AppSettingMenu>  treeItemGeneral = 
			new TreeItem<>( new AppSettingMenu( langRB.getString( "ITEM_GENERAL" ), AppSettingMenu.APPSET_TYPE.TYPE_GENERAL ) );

		treeItemRoot.getChildren().add( treeItemDB );
		treeItemDB.getChildren().add( treeItemDBMySQL );
		treeItemDB.getChildren().add( treeItemDBOracle );
		treeItemDB.getChildren().add( treeItemDBPostgreSQL );
		treeItemRoot.getChildren().add( treeItemGeneral );
		treeItemRoot.setExpanded(true);
		
		this.treeViewConf.setRoot( treeItemRoot );
		// do not show root item
		this.treeViewConf.setShowRoot(false);
		
		// pane for Dialog
        BorderPane paneDlg = new BorderPane();
        BorderPane.setMargin( this.treeViewConf, new Insets( 5, 5, 5, 5 ) );
        paneDlg.setLeft( this.treeViewConf );
        
        PaneFactory paneFactory = new AppPaneFactory();
        AppPaneAbstract appPaneAbs = paneFactory.createPane( AppSettingMenu.APPSET_TYPE.TYPE_DB, this, this.mainCtrl, langRB );
        paneDlg.setCenter( appPaneAbs );
		
		// set pane on dialog
		this.getDialogPane().setContent( paneDlg );
		
		// add button "Apply&Close" and "Cancel"
		this.okButtonType           = new ButtonType( langRB.getString( "BTN_OK" )    , ButtonData.OK_DONE );
		ButtonType cancelButtonType = new ButtonType( langRB.getString( "BTN_CANCEL" ), ButtonData.CANCEL_CLOSE );
		this.applyButtonType        = new ButtonType( langRB.getString( "BTN_APPLY" ) , ButtonData.APPLY );
		this.getDialogPane().getButtonTypes().addAll( applyButtonType, okButtonType, cancelButtonType );
	
		// set css for this dialog
		Scene scene = this.getDialogPane().getScene();
		scene.getStylesheets().add
		(
			getClass().getResource("/conf/css/dlg/AppSettingDialog.css").toExternalForm()
		);
		
        // Window Icon
		Stage stage = (Stage)this.getDialogPane().getScene().getWindow();
		stage.getIcons().add( this.mainCtrl.getImage( "file:resources/images/winicon.gif" ) );

		// set location
		Platform.runLater( ()->MyTool.setWindowLocation( stage, stage.getWidth(), stage.getHeight() ) );
		
		// set Action
		this.setAction();
	
		// set Focus on listView 
		Platform.runLater( ()->{ this.treeViewConf.requestFocus(); } );
	}
	
	private void setAction()
	{
		// result when clicking on "Apply&Close"
		this.setResultConverter
		( 
			dialogButton ->
			{
				if ( dialogButton == this.okButtonType )
				{
					return Boolean.TRUE;
				}
				return Boolean.FALSE;
			}
		);
		
		// Prevent from closing this dialog,
		// when clicking "Apply&Close" button, then something wrong happen 
		final Button btnOK = (Button)this.getDialogPane().lookupButton( okButtonType );
		btnOK.addEventFilter
		(
		    ActionEvent.ACTION, 
		    (event)-> 
		    {
		    	Node paneOnDlg = getDialogPane().getContent();
		    	Node node = ((BorderPane)paneOnDlg).getCenter();
		    	boolean ret = ((ApplyInterface)node).apply();
		    	try
		    	{
		    		((AppPaneAbstract)node).save();
		    	}
		    	catch ( Exception ex )
		    	{
		    		this.showException(ex);
		    	}
		    	// if false, do not close this dialog.
		    	if ( ret == false )
		    	{
		    		event.consume();
		    	}
		    }
		);
		
		// Prevent from closing this dialog,
		// when clicking "Apply" Button
		final Button btnApply = (Button)this.getDialogPane().lookupButton( this.applyButtonType );
		btnApply.addEventFilter
		(
		    ActionEvent.ACTION, 
		    (event)-> 
		    {
		    	Node paneOnDlg = getDialogPane().getContent();
		    	Node node = ((BorderPane)paneOnDlg).getCenter();
		    	((ApplyInterface)node).apply();
		    	try
		    	{
		    		((AppPaneAbstract)node).save();
		    	}
		    	catch ( Exception ex )
		    	{
		    		this.showException(ex);
		    	}
		    	// always consume
	    		event.consume();
		    }
		);
		
		// Switch a pane when selecting a different item.
		// -------------------------------------------------------------
		// new ChangeListener<String>()
		// {
		//  	@Override
		//  	public void changed
		// 		(
		// 		ObservableValue<? extends String>obs,
		// 		String oldVal,
		// 		String newVal
		// 		)
		// }
		// -------------------------------------------------------------
		this.treeViewConf.getSelectionModel().selectedItemProperty().addListener
		(
			( obs, oldVal, newVal ) ->
			{
				//System.out.println( "obs[" + obs.getValue() + "]newVal[" + newVal + "]" );
				
		        PaneFactory paneFactory = new AppPaneFactory();
		        ResourceBundle langRB = this.mainCtrl.getLangResource("conf.lang.gui.dlg.app.AppSettingDialog");
		        AppPaneAbstract appPaneAbs = paneFactory.createPane( newVal.getValue().getType(), this, this.mainCtrl, langRB );
		    	Node paneOnDlg = getDialogPane().getContent();
		    	((BorderPane)paneOnDlg).setCenter( appPaneAbs );
		    	
				Stage stage = (Stage)this.getDialogPane().getScene().getWindow();
				stage.sizeToScene();
			}
		);
	}
	
	private void showException( Exception ex )
	{
		MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING, this.mainCtrl );
		ResourceBundle langRB = this.mainCtrl.getLangResource("conf.lang.gui.common.MyAlert");
		alertDlg.setHeaderText( langRB.getString("TITLE_MISC_ERROR") );
		alertDlg.setTxtExp( ex );
		alertDlg.showAndWait();
		alertDlg = null;
	}	
}
