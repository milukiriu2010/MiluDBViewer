package milu.gui.ctrl.jdbc;

import java.io.File;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import milu.db.MyDBAbstract;
import milu.db.MyDBFactory;
import milu.db.driver.DriverShim;
import milu.db.driver.LoadDriver;
import milu.gui.ctrl.common.DriverControlPane;
import milu.gui.ctrl.common.inf.ChangeLangInterface;
import milu.gui.ctrl.common.inf.CopyInterface;
import milu.gui.ctrl.common.inf.FocusInterface;
import milu.gui.ctrl.common.inf.PaneSwitchDriverInterface;
import milu.gui.ctrl.query.SqlTableView;
import milu.gui.dlg.MyAlertDialog;
import milu.gui.view.DBView;
import milu.main.MainController;
import milu.tool.MyTool;

public class DBJdbcTab extends Tab 
	implements
		PaneSwitchDriverInterface,
		CopyInterface,
		FocusInterface,
		ChangeLangInterface
{
	private DBView          dbView = null;
	
    // Head List
    private List<String> headLst = new ArrayList<>(Arrays.asList("KEY","VALUE"));
	
	// ----------------------------------------
	// [Pane on Dialog(1)]
	// ----------------------------------------
	// Top Pane
	private BorderPane topPane = new BorderPane();
	
	// ----------------------------------------
	// [Pane on Dialog(2)]
	// ----------------------------------------
	// Pane for Driver
	private Pane  driverCtrlPane = null; 
	
    // -----------------------------------------------------
	// [Pane on Dialog(1)]-[Center]
    // -----------------------------------------------------
	// Left  => Driver List
	// Right => Driver Info
	private SplitPane  showPane = new SplitPane();
	
	//private BorderPane editPane = new BorderPane();
	
    // -----------------------------------------------------
	// [Pane on Dialog(1)]-[Center]-[Left]
    // -----------------------------------------------------
	private ListView<DriverShim>   driverListView = new ListView<>();
	
	private Button  btnAdd  = new Button();
	
	private Button  btnEdit = new Button();
	
	private Button  btnDel  = new Button();
	
	// -----------------------------------------------------
	// [Pane on Dialog(1)]-[Center]-[Right]
	// -----------------------------------------------------
	private TextField  majorVerTxt = new TextField();
	
	private TextField  minorVerTxt = new TextField();
	
	private SqlTableView driverTableView = null;

	/*
	// -----------------------------------------------------
	// [Another Pane]
	// -----------------------------------------------------
	private ListView<String>  driverPathListView = new ListView<>();
	
	private Button  btnAddJar = new Button("Add Jars");
	
	private Button  btnDelJar = new Button("Remove Jar");
	
	private TextField driverClassNameTxt = new TextField();
	
	private Button  btnLoad   = new Button("Load");
	
	private Button  btnCancel = new Button("Cancel");
	*/
	
	public DBJdbcTab( DBView dbView )
	{
		super();
		
		this.dbView = dbView;
		
		// ------------------------------------------------
		// Show Pane
		// ------------------------------------------------
		this.setShowPane();
		
		// ------------------------------------------------
		// Edit Pane
		// ------------------------------------------------
		//this.setEditPane();
		
		this.setContent( this.topPane );
		
		MainController mainCtrl = this.dbView.getMainController();
		
		// Create Pane for DriverControl
		this.driverCtrlPane = new DriverControlPane( mainCtrl, this );
		
		// set icon on Tab
		this.setGraphic( MyTool.createImageView( 16, 16, mainCtrl.getImage("file:resources/images/jdbc.png") ) );
		
		// set Title on Tab
		this.setText("JDBC");
		
		// set data
		this.setData();
		
		this.setAction();
		
		this.changeLang();
	}
	
	private void setShowPane()
	{
		HBox hBoxBtn = new HBox(2);
		hBoxBtn.setSpacing(2);
		hBoxBtn.getChildren().addAll( this.btnAdd, this.btnEdit, this.btnDel );
		
		VBox vBoxDriverCtrl = new VBox(2);
		vBoxDriverCtrl.getChildren().addAll( this.driverListView, hBoxBtn );
		
		Label      majorVerLbl = new Label("Major Version:");
		Label      minorVerLbl = new Label("Minor Version:");
		this.majorVerTxt.setEditable(false);
		this.minorVerTxt.setEditable(false);
		this.driverTableView = new SqlTableView(this.dbView);

		HBox hBoxVer = new HBox(2);
		hBoxVer.setSpacing(2);
		hBoxVer.getChildren().addAll( majorVerLbl, this.majorVerTxt, minorVerLbl, this.minorVerTxt );
		
		VBox vBoxDriverInfo = new VBox(2);
		vBoxDriverInfo.getChildren().addAll( hBoxVer, this.driverTableView );
		
		// Show Pane
		this.showPane.setOrientation(Orientation.HORIZONTAL);
		this.showPane.getItems().addAll( vBoxDriverCtrl, vBoxDriverInfo );
		this.showPane.setDividerPositions( 0.5f, 0.5f );
		
		this.topPane.setCenter( this.showPane );
	}
	
	/*
	private void setEditPane()
	{
		Label lblDriverPath = new Label("JDBC Driver Path");
		VBox vBoxDriverPathBtn = new VBox(2);
		vBoxDriverPathBtn.getChildren().addAll( this.btnAddJar, this.btnDelJar );
		
		this.driverPathListView.setEditable(true);
		
		HBox  hBoxDriverPath = new HBox(2);
		hBoxDriverPath.getChildren().addAll( this.driverPathListView, vBoxDriverPathBtn );
		
		Label lblDriverClassName = new Label("JDBC Driver Class Name");
		
		HBox  hBoxNextBtn = new HBox(2);
		hBoxNextBtn.getChildren().addAll( this.btnLoad, this.btnCancel );
		
		VBox vBox = new VBox(2);
		vBox.getChildren().addAll
		( 
			lblDriverPath,
			hBoxDriverPath,
			lblDriverClassName,
			this.driverClassNameTxt,
			hBoxNextBtn
		);
		this.editPane.setCenter( vBox );
	}
	*/
	
	private void setData()
	{
		List<DriverShim> driverLst = new ArrayList<>();
		Enumeration<Driver> drivers = DriverManager.getDrivers();
		while (drivers.hasMoreElements())
		{
			Driver driver = drivers.nextElement();
			if ( driver instanceof DriverShim )
			{
				driverLst.add((DriverShim)driver);
			}
		}
		
		final List<DriverShim> sortedDriverLst = driverLst.stream()
			.sorted( (o1,o2) -> o1.getDriverClazzName().compareTo(o2.getDriverClazzName()) )
			.collect(Collectors.toList());
		this.driverListView.getItems().addAll(sortedDriverLst);
		
		this.driverListView.getSelectionModel().selectFirst();
		this.changeSelectedDriver( this.driverListView.getSelectionModel().getSelectedItem() );
	}
	
	private void setAction()
	{
		this.driverListView.getSelectionModel().selectedItemProperty().addListener( (obs,oldVal,driver)->this.changeSelectedDriver(driver) );
		this.driverListView.setCellFactory
		(
			value -> new ListCell<DriverShim>()
			{
				@Override
				protected void updateItem( DriverShim driver, boolean empty )
				{
					super.updateItem( driver, empty );
					if ( empty || driver == null || driver.toString() == null )
					{
						setText(null);
					}
					else
					{
						/*
						String driverClazzName = null;
						if ( driver instanceof DriverShim )
						{
							driverClazzName = ((DriverShim)driver).getDriverClazzName();
						}
						else
						{
							driverClazzName = driver.toString();
							driverClazzName = driverClazzName.substring(0,driverClazzName.lastIndexOf("@"));
						}
						setText( driverClazzName );
						*/
						setText( driver.getDBName() ); 
					}
				}
			}
		);
		
		// ------------------------------
		// Delete JDBC Driver 
		// ------------------------------
		this.btnDel.setOnAction
		(
			(event)->
			{
				Driver selectedDriver = this.driverListView.getSelectionModel().getSelectedItem();
				if ( selectedDriver == null )
				{
					return;
				}
				try
				{
					DriverManager.deregisterDriver(selectedDriver);
				}
				catch ( SQLException sqlEx )
				{
					this.showException( sqlEx );
				}
				finally
				{
					this.driverListView.getItems().remove(selectedDriver);
				}
			}
		);
		
		// ------------------------------
		// Add JDBC Driver 
		// ------------------------------
		this.btnAdd.setOnAction
		( 
			(event)->
			{
				
				//this.topPane.setCenter( this.editPane );
				this.setContent( this.driverCtrlPane );
				((DriverControlPane)this.driverCtrlPane).setAddDriver();
			}
		);
		
		// ------------------------------
		// Edit JDBC Driver 
		// ------------------------------
		this.btnEdit.setOnAction
		( 
			(event)->
			{ 
				// set pane on dialog
				this.setContent( this.driverCtrlPane );
				DriverShim driverEdit = this.driverListView.getSelectionModel().getSelectedItem();
				((DriverControlPane)this.driverCtrlPane).setEditDriver( driverEdit );
			} 
		);
		
		
		/*
		this.driverPathListView.setCellFactory(	(callback)->new EditListCell() );
		
		
		this.btnAddJar.setOnAction
		(
			(event)->
			{
				FileChooser fc = new FileChooser();
				List<File> fileLst = fc.showOpenMultipleDialog(this.dbView);
				if ( fileLst == null )
				{
					return;
				}
				fileLst.forEach( (file)->this.driverPathListView.getItems().add(file.getAbsolutePath()) );
			}
		);
		
		this.btnDelJar.setOnAction
		(
			(event)->
			{
				ObservableList<String>  selectedItems = this.driverPathListView.getSelectionModel().getSelectedItems();
				this.driverPathListView.getItems().removeAll( selectedItems );
			}
		);
		
		this.btnLoad.setOnAction
		(
			(event)->
			{
				try
				{
					Driver driver =
							LoadDriver.loadDriver( this.driverClassNameTxt.getText(), this.driverPathListView.getItems() );
					this.driverListView.getItems().add(driver);
					this.driverListView.getSelectionModel().select(driver);
				}
				catch ( Exception ex )
				{
					this.showException(ex);
				}
				finally
				{
					this.driverPathListView.getItems().removeAll( this.driverPathListView.getItems() );
					this.driverClassNameTxt.setText("");
					this.topPane.setCenter(this.showPane);
				}
			}
		);
		
		this.btnCancel.setOnAction( (event)->this.topPane.setCenter( this.showPane ) );
		*/
	}
	
	private void changeSelectedDriver( Driver driver )
	{
		try
		{
			this.majorVerTxt.setText( String.valueOf(driver.getMajorVersion()) );
			this.minorVerTxt.setText( String.valueOf(driver.getMinorVersion()) );
			
			DriverPropertyInfo[] driverPropInfoLst = driver.getPropertyInfo( "", null );
			Map<String,String> driverMap = new TreeMap<>(); 
			for ( DriverPropertyInfo  driverPropInfo : driverPropInfoLst )
			{
				driverMap.put( driverPropInfo.name, driverPropInfo.value );
			}
			//System.out.println( "driverMap.size:" + driverMap.size() );
			List<List<String>> dataLst = new ArrayList<>();
			driverMap.forEach
			(
				(k,v)->
				{
					List<String> data = new ArrayList<>();
					data.add(k);
					data.add(v);
					dataLst.add(data);
				}
			);
			//System.out.println( "dataLst.size:" + dataLst.size() );
			this.driverTableView.setTableViewSQL( this.headLst, dataLst );
		}
		catch ( SQLException sqlEx )
		{
			this.showException( sqlEx );
		}
	}
	
	private void showException( Exception ex )
	{
		MainController mainCtrl = this.dbView.getMainController();
		MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING, mainCtrl );
		ResourceBundle langRB = mainCtrl.getLangResource("conf.lang.gui.common.MyAlert");
		alertDlg.setHeaderText( langRB.getString("TITLE_MISC_ERROR") );
		alertDlg.setTxtExp( ex );
		alertDlg.showAndWait();
		alertDlg = null;
	}
	
	@Override
	public void driverAdd( DriverShim driver )
	{
		// set pane on tab
		this.setContent( this.topPane );
		this.driverListView.getItems().add(driver);
		this.driverListView.getSelectionModel().select(driver);
	}
	
	@Override
	public void driverEdit( DriverShim driver )
	{
		// set pane on tab
		this.setContent( this.topPane );
		DriverShim driverEdit = this.driverListView.getSelectionModel().getSelectedItem();
		this.driverListView.getItems().remove(driverEdit);
		this.driverListView.getItems().add(driver);
		this.driverListView.getSelectionModel().select(driver);
		
		MainController mainCtrl = this.dbView.getMainController();
		mainCtrl.switchDriver( driverEdit, driver );
	}
	
	@Override
	public void driverCancel()
	{
		// set pane on tab
		this.setContent( this.topPane );
	}
	
	
	/**
	 * set Focus on TextArea
	 */
	@Override
	public void setFocus()
	{
		// https://sites.google.com/site/63rabbits3/javafx2/jfx2coding/dialogbox
		// https://stackoverflow.com/questions/20049452/javafx-focusing-textfield-programmatically
		// call after "new Scene"
		//Platform.runLater( ()->{ this.textAreaSQL.requestFocus(); System.out.println( "textAreaSQL focused."); } );
	}
	
	/**************************************************
	 * Override from CopyInterface
	 ************************************************** 
	 */
	@Override
	public void copyTableNoHead()
	{
		
	}
	
	/**
	 * Override from CopyInterface
	 */
	@Override
	public void copyTableWithHead()
	{
	}
	
	/**************************************************
	 * Override from ChangeLangInterface
	 ************************************************** 
	 */
	@Override	
	public void changeLang()
	{
		MainController mainCtrl = this.dbView.getMainController();
		ResourceBundle langRB = mainCtrl.getLangResource("conf.lang.gui.common.NodeName");
		
		this.btnAdd.setText(langRB.getString("BTN_ADD"));
		this.btnEdit.setText(langRB.getString("BTN_EDIT"));
		this.btnDel.setText(langRB.getString("BTN_DEL"));
	}
	
	// https://gist.github.com/skrb/6389257
	class EditListCell extends ListCell<String>
	{
		private TextField textField;
		
		@Override
		public void startEdit()
		{
			if (!isEditable() || !getListView().isEditable() )
			{
				return;
			}
			super.startEdit();
			
			if (isEditing())
			{
				if (textField == null)
				{
					textField = new TextField(getItem());
					textField.setOnAction(event->commitEdit(textField.getText()));
				}
			}
			// no else?
			//else
			//{
				textField.setText(getItem());
				setText(null);
				
				setGraphic(textField);
				textField.selectAll();
			//}
		}
		
		@Override
		public void updateItem( String item, boolean empty )
		{
			super.updateItem(item, empty);
			
			if (isEmpty())
			{
				setText(null);
				setGraphic(null);
			}
			else
			{
				if(!isEditing())
				{
					if ( textField != null )
					{
						setText(textField.getText());
					}
					else
					{
						setText( item );
					}
				}
				else
				{
					setText(item);
				}
				setGraphic(null);
			}
		}
	}
}
