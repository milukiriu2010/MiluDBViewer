package milu.gui.dlg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ResourceBundle;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.geometry.Insets;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;

import milu.conf.AppConf;
import milu.ctrl.ApplyInterface;
import milu.entity.AppSettingEntity;

public class AppSettingDialog extends Dialog<Boolean>
{
	// Property File for this class 
	private static final String PROPERTY_FILENAME = 
		"conf.lang.dlg.AppSettingDialog";

	// Language Resource
	private ResourceBundle langRB = ResourceBundle.getBundle( PROPERTY_FILENAME );

	// Configuration List
	private ListView<AppSettingEntity> listViewConf = new ListView<AppSettingEntity>();
	
	// Apply&Close Button
	private ButtonType okButtonType = null;
	
	public AppSettingDialog()
	{
		super();
		
		// Set dialog title.
		this.setTitle( langRB.getString( "TITLE_APP_SETTING" ) );
		
		// set items on ListView
		ObservableList<AppSettingEntity> itemsConf = 
			FXCollections.observableArrayList
			(
				new AppSettingEntity( langRB.getString( "ITEM_DB" )     , AppSettingEntity.APPSET_TYPE.TYPE_DB ),
				new AppSettingEntity( langRB.getString( "ITEM_GENERAL" ), AppSettingEntity.APPSET_TYPE.TYPE_GENERAL )
			);
		this.listViewConf.setItems( itemsConf );
		this.listViewConf.setMaxWidth( Control.USE_PREF_SIZE );
		
		// pane for Dialog
        BorderPane paneDlg = new BorderPane();
        //paneDlg.setPadding( new Insets( 10, 20, 10, 20 ) );
        BorderPane.setMargin( this.listViewConf, new Insets( 5, 5, 5, 5 ) );
        paneDlg.setLeft( this.listViewConf );
        paneDlg.setCenter( new DBConfPane() );
		
		// set pane on dialog
		this.getDialogPane().setContent( paneDlg );
		
		// add button "Apply&Close" and "Cancel"
		this.okButtonType           = new ButtonType( langRB.getString( "BTN_OK" )    , ButtonData.OK_DONE );
		ButtonType cancelButtonType = new ButtonType( langRB.getString( "BTN_CANCEL" ), ButtonData.CANCEL_CLOSE );
		this.getDialogPane().getButtonTypes().addAll( okButtonType, cancelButtonType );
	
		// set css for this dialog
		Scene scene = this.getDialogPane().getScene();
		scene.getStylesheets().add
		(
			getClass().getResource("/conf/css/dlg/AppSettingDialog.css").toExternalForm()
		);
		
        // Window Icon
		try
		{
			InputStream inputStreamWinIcon = new FileInputStream( "resources" + File.separator + "images" + File.separator + "winicon.gif" );
			Image imgWinIcon = new Image( inputStreamWinIcon );
			Stage stage = (Stage)this.getDialogPane().getScene().getWindow();
			stage.getIcons().add( imgWinIcon );
		}
		catch ( FileNotFoundException fnfEx )
		{
			fnfEx.printStackTrace();
		}
		
		// set Action
		this.setAction();
	
		// set Focus on listView 
		Platform.runLater( ()->{ this.listViewConf.requestFocus(); } );
		
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
		    event -> 
		    {
		    	Node paneOnDlg = getDialogPane().getContent();
		    	Node node = ((BorderPane)paneOnDlg).getCenter();
		    	boolean ret = ((ApplyInterface)node).apply();
		    	// if false, do not close this dialog.
		    	if ( ret == false )
		    	{
		    		event.consume();
		    	}
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
		this.listViewConf.getSelectionModel().selectedItemProperty().addListener
		(
			( obs, oldVal, newVal ) ->
			{
				//System.out.println( "obs[" + obs.getValue() + "]newVal[" + newVal + "]" );
				AppSettingFactory af = new AppSettingFactory();
				Pane pane = af.getInstance( newVal.getType() );
				// Get BorderPane on DialogPane
				//BorderPane paneOnDlg = (BorderPane)getDialogPane().getChildren().get( 0 );
				//pane.setMaxWidth( Control.USE_COMPUTED_SIZE );
				//pane.setPrefWidth( Double.MAX_VALUE );
		    	Node paneOnDlg = getDialogPane().getContent();
		    	((BorderPane)paneOnDlg).setCenter( pane );
				//paneDlg.setManaged( false );
				
				af = null;
			}
		);
	}
	
	
	class AppSettingFactory
	{
		public Pane getInstance( AppSettingEntity.APPSET_TYPE key )
		{
			Pane pane = null;
			if ( AppSettingEntity.APPSET_TYPE.TYPE_DB.equals( key ) )
			{
				pane = new DBConfPane();
			}
			else if ( AppSettingEntity.APPSET_TYPE.TYPE_GENERAL.equals( key ) )
			{
				pane = new GeneralConfPane();
			}
			
			return pane;
		}
	}
	
	class DBConfPane extends BorderPane
		implements ApplyInterface
	{	
		// TextField for "Fetch Row Size"
		TextField  txtRowMax = new TextField();
		
		public DBConfPane()
		{
			Label  title  = new Label( langRB.getString( "TITLE_DB_CONF_PANE" ) );
			title.getStyleClass().add("label-title");
			
			HBox     hbxRowMax = new HBox( 2 );
			Label    lblRowMax = new Label( langRB.getString( "LABEL_ROW_MAX" ) );
			AppConf  appConf   = AppConf.getInstance();
			this.txtRowMax.setText( String.valueOf( appConf.getFetchMax() ) );
			hbxRowMax.getChildren().addAll( lblRowMax, this.txtRowMax );
			
			// put controls on pane
			this.setTop( title );
			this.setCenter( hbxRowMax );
			
			// restriction for TextField "Fetch Row Size"
			// https://stackoverflow.com/questions/15615890/recommended-way-to-restrict-input-in-javafx-textfield
			this.txtRowMax.textProperty().addListener
			(
				(obs, oldVal, newVal) ->
				{
					// "Numeric" or "No Input" are allowed.
					if ( newVal.length() == 0 )
					{
						
					}
					// if alphabets or marks are input, back to previous input.
					else if ( newVal.matches( "^[0-9]+$" ) == false )
					{
						((StringProperty)obs).setValue( oldVal );
					}
				}
			);
		}
		
		/******************************************
		 * 
		 * @see milu.ctrl.ApplyInterface#apply()
		 ******************************************
		 */
		@Override
		public boolean apply()
		{
			String strRowMax = this.txtRowMax.getText();
			if ( strRowMax.length() > 0 )
			{
				AppConf appConf = AppConf.getInstance();
				appConf.setFetchMax( Integer.valueOf( strRowMax ) );
			}
			return true;
		}
	}
	
	class GeneralConfPane extends BorderPane
		implements ApplyInterface
	{
		public GeneralConfPane()
		{
			Label  title = new Label( langRB.getString( "TITLE_GENERAL_CONF_PANE" ) );
			title.getStyleClass().add("label-title");
			
			this.setTop( title );
		}
		
		/*********************************************
		 * 
		 *********************************************
		 * @see milu.ctrl.ApplyInterface#apply()
		 *********************************************
		 */
		@Override
		public boolean apply()
		{
			return true;
		}
	}
}
