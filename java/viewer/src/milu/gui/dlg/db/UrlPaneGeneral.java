package milu.gui.dlg.db;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Dialog;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.collections.ObservableList;

import milu.db.MyDBAbstract;
import milu.gui.ctrl.common.PersistentButtonToggleGroup;
import milu.main.MainController;

public class UrlPaneGeneral extends UrlPaneAbstract
{	
	private Dialog<?>      dlg          = null;
	
	private MainController mainCtrl     = null;
	
	private MyDBAbstract   myDBAbs      = null;
	
	private HBox           hBoxToggle     = new HBox(2);
	
	private ToggleGroup    tglGroup       = new PersistentButtonToggleGroup();
	
	// ToggleButton for Free Hand
	private ToggleButton   tglBtnFreeHand = new ToggleButton();
	
	// ----------------------------------------------------
	// Items for "All"
	// ----------------------------------------------------
	// field for URL
	private TextArea   urlTextArea = new TextArea();
	
	/*
	// Driver File Path
	private TextField  txtDriverPath  = new TextField();
	
	// Button to input "Driver File Path"
	private Button     btnDriverPath = new Button();
	*/
	
	private ListView<String>  lvDriverPath = new ListView<>();
	
	private Button     btnAdd = new Button();
	
	private Button     btnDel = new Button();
	
	// Driver Class Name
	private TextField  txtDriverClassName = new TextField();
	
	@Override
	public void createPane( Dialog<?> dlg, MainController mainCtrl, MyDBAbstract myDBAbs, Map<String,String> mapProp )
	{
		this.dlg       = dlg;
		this.mainCtrl  = mainCtrl;
		this.myDBAbs   = myDBAbs;
		
		ResourceBundle extLangRB = this.mainCtrl.getLangResource("conf.lang.gui.dlg.db.DBSettingDialog");
		
		// ToggleButton for Free Hand
		this.tglBtnFreeHand.setText(extLangRB.getString("TOGGLE_FREE"));
		this.tglBtnFreeHand.setToggleGroup( this.tglGroup );
		
		this.hBoxToggle.getChildren().addAll( this.tglBtnFreeHand );
		
		/*
		// "select driver path" button
		this.btnDriverPath.setGraphic( MyTool.createImageView( 16, 16, this.mainCtrl.getImage("file:resources/images/folder.png") ));
		*/
		
		this.btnAdd.setText( extLangRB.getString("BTN_ADD") );
		this.btnDel.setText( extLangRB.getString("BTN_DEL") );
		
		this.tglBtnFreeHand.setSelected(true);
		this.setPaneFreeHand();
		
		this.setAction();
		
		this.setUrlTextArea();
	}
	
	private void setAction()
	{
		// --------------------------------------------
		// Selected Toggle Button is changed
		// --------------------------------------------
		this.tglGroup.selectedToggleProperty().addListener
		(
			(obs,oldVal,newVal)->
			{
				if ( newVal == this.tglBtnFreeHand )
				{
					this.setPaneFreeHand();
				}
				Stage stage = (Stage)this.dlg.getDialogPane().getScene().getWindow();
				stage.sizeToScene();
			}
		);
		/*
		this.btnDriverPath.setOnAction
		(
			(event)->
			{
				FileChooser fc = new FileChooser();
				File file = fc.showOpenDialog(null);
				if ( file != null )
				{
					this.txtDriverPath.setText( file.getAbsolutePath() );
				}
			}
		);
		*/
		
		this.btnAdd.setOnAction
		(
			(event)->
			{
				FileChooser fc = new FileChooser();
				List<File> fileLst = fc.showOpenMultipleDialog(this.dlg.getOwner());
				if ( fileLst == null )
				{
					return;
				}
				fileLst.forEach( (file)->this.lvDriverPath.getItems().add(file.getAbsolutePath()) );
			}
		);
		
		this.btnDel.setOnAction
		(
			(event)->
			{
				ObservableList<String>  selectedItems = this.lvDriverPath.getSelectionModel().getSelectedItems();
				this.lvDriverPath.getItems().removeAll( selectedItems );
			}
		);
	}
	
	private void setPaneFreeHand()
	{
		this.getChildren().removeAll( this.getChildren() );
		
		this.urlTextArea.setEditable( true );
		this.urlTextArea.setWrapText(true);
		
		// Fit width
		// ------------------------------------------
		// |  TextField                    | Button |
		// ------------------------------------------
		// |  TextArea                              |
		// ------------------------------------------
		/*
		this.txtDriverPath.setPrefWidth( this.urlTextArea.getWidth() - this.btnDriverPath.getWidth() );
		//this.txtDriverPath.setPrefWidth( -1 );
		HBox.setHgrow( this.txtDriverPath, Priority.ALWAYS );
		HBox.setHgrow( this.btnDriverPath, Priority.ALWAYS );
		*/
		Label lblDriverPath = new Label("JDBC Driver Path");
		/*
		HBox hBoxDriverPath = new HBox(2);
		hBoxDriverPath.getChildren().addAll( this.txtDriverPath, this.btnDriverPath );
		*/
		VBox vBoxDriverPathBtn = new VBox(2);
		vBoxDriverPathBtn.getChildren().addAll( this.btnAdd, this.btnDel );
		
		/*
		HBox hBoxDriverPath2 = new HBox(2);
		this.lvDriverPath.setPrefHeight( (this.btnAdd.getHeight() + this.btnDel.getHeight())*2 );
		hBoxDriverPath2.getChildren().addAll( this.lvDriverPath, vBoxDriverPathBtn );
		HBox.setHgrow( this.lvDriverPath, Priority.ALWAYS );
		HBox.setHgrow( vBoxDriverPathBtn, Priority.ALWAYS );
		*/
		
		BorderPane brdPaneDriverPath = new BorderPane();
		this.lvDriverPath.setPrefHeight( this.btnAdd.getHeight() + this.btnDel.getHeight() );
		brdPaneDriverPath.setCenter( this.lvDriverPath );
		brdPaneDriverPath.setRight( vBoxDriverPathBtn );
		
		Label lblDriverClassName = new Label("JDBC Driver Class Name");
		
		Label lblUrl = new Label("JDBC URL");
		
		VBox vBox = new VBox(2);
		vBox.getChildren().addAll
		( 
			this.hBoxToggle,
			lblDriverPath,
			//hBoxDriverPath,
			//hBoxDriverPath2,
			brdPaneDriverPath,
			lblDriverClassName,
			this.txtDriverClassName,
			lblUrl,
			this.urlTextArea 
		);
		
		this.getChildren().addAll( vBox );
	}
	
	@Override
	public Map<String,String> getProp()
	{
		Map<String,String> dbOptMap = new HashMap<String,String>();
		
		return dbOptMap;
	}

	@Override
	public void setUrl() 
	{
		if ( this.tglBtnFreeHand.isSelected() )
		{
			//this.myDBAbs.addDriverPath( this.txtDriverPath.getText() );
			this.lvDriverPath.getItems().forEach( this.myDBAbs::addDriverPath );
			this.myDBAbs.setDriverClassName( this.txtDriverClassName.getText() );
			this.myDBAbs.setUrl( this.urlTextArea.getText() );
		}
	}
	
	private void setUrlTextArea()
	{
		this.setUrl();
		this.urlTextArea.setText( this.myDBAbs.getUrl() );
	}
	
}
