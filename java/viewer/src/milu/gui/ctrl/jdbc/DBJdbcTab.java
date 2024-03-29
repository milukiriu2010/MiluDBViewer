package milu.gui.ctrl.jdbc;

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
import java.io.File;

import javafx.geometry.Orientation;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.event.Event;
import milu.db.driver.DriverShim;
import milu.gui.ctrl.common.DriverControlPane;
import milu.gui.ctrl.common.inf.ChangeLangInterface;
import milu.gui.ctrl.common.inf.FocusInterface;
import milu.gui.ctrl.common.inf.PaneSwitchDriverInterface;
import milu.gui.ctrl.common.table.CopyTableInterface;
import milu.gui.ctrl.common.table.ObjTableView;
import milu.gui.view.DBView;
import milu.main.AppConst;
import milu.main.MainController;
import milu.tool.MyGUITool;

public class DBJdbcTab extends Tab 
	implements
		PaneSwitchDriverInterface,
		CopyTableInterface,
		FocusInterface,
		ChangeLangInterface
{
	private DBView          dbView = null;
	
    // Head List
    private List<Object> headLst = new ArrayList<>(Arrays.asList("KEY","VALUE"));
	
	// ----------------------------------------
	// [Pane(1) on Tab]
	// ----------------------------------------
	// Top Pane
	private BorderPane topPane = new BorderPane();
	
	// ----------------------------------------
	// [Pane(2) on Tab]
	// ----------------------------------------
	// Pane for Driver
	private Pane  driverCtrlPane = null; 
	
    // -----------------------------------------------------
	// [Pane(1) on Tab]-[Center]
    // -----------------------------------------------------
	// Left  => Driver List
	// Right => Driver Info
	private SplitPane  showPane = new SplitPane();
	
    // -----------------------------------------------------
	// [Pane(1) on Tab]-[Center]-[Left]
    // -----------------------------------------------------
	private ListView<DriverShim>   driverListView = new ListView<>();
	
	private Button  btnAdd  = new Button();
	
	private Button  btnEdit = new Button();
	
	private Button  btnDel  = new Button();
	
	// -----------------------------------------------------
	// [Pane(1) on Tab]-[Center]-[Right]
	// -----------------------------------------------------
	private TextField  majorVerTxt = new TextField();
	
	private TextField  minorVerTxt = new TextField();
	
	private ObjTableView driverTableView = null;
	
	public DBJdbcTab( DBView dbView )
	{
		super();
		
		this.dbView = dbView;
		
		// ------------------------------------------------
		// Show Pane
		// ------------------------------------------------
		this.setShowPane();
		this.setContent( this.topPane );
		
		MainController mainCtrl = this.dbView.getMainController();
		
		// ------------------------------------------------
		// Edit Pane
		// ------------------------------------------------
		// Create Pane for DriverControl
		this.driverCtrlPane = new DriverControlPane( mainCtrl, this );
		
		// set icon on Tab
		this.setGraphic( MyGUITool.createImageView( 16, 16, mainCtrl.getImage("file:resources/images/jdbc.png") ) );
		
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
		this.majorVerTxt.setPrefWidth(30);
		this.minorVerTxt.setEditable(false);
		this.minorVerTxt.setPrefWidth(30);
		this.driverTableView = new ObjTableView(this.dbView);

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
				DriverShim selectedDriver = this.driverListView.getSelectionModel().getSelectedItem();
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
					MyGUITool.showException( this.dbView.getMainController(), "conf.lang.gui.common.MyAlert", "TITLE_MISC_ERROR", sqlEx );
				}
				finally
				{
					this.driverListView.getItems().remove(selectedDriver);
				}
				String className = selectedDriver.getDriverClassName();
				File file = new File(AppConst.DRIVER_DIR.val()+className+".json");
				if ( file.exists() )
				{
					file.delete();
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
	}
	
	private void changeSelectedDriver( DriverShim driver )
	{
		try
		{
			this.majorVerTxt.setText( String.valueOf(driver.getMajorVersion()) );
			this.minorVerTxt.setText( String.valueOf(driver.getMinorVersion()) );
			
			String driverClassName = driver.getDriverClassName();
			String[] classDomainArray = driverClassName.split("\\.");
			
			List<String> classDomainLst = new ArrayList<>();
			classDomainLst.add("");
			classDomainLst.addAll(Arrays.asList(classDomainArray));
			
			Map<String,String> driverMap = new TreeMap<>(); 
			int classDomainLstSize = classDomainLst.size();
			for ( int i = 0; i < classDomainLstSize; i++ )
			{
				String url = "";
				try
				{
					String className = classDomainLst.get(i);
					if ( "".equals(className) == false )
					{
						url = "jdbc:" + className + "://";
					}
					DriverPropertyInfo[] driverPropInfoLst = driver.getPropertyInfo( url, null );
					for ( DriverPropertyInfo  driverPropInfo : driverPropInfoLst )
					{
						driverMap.put( driverPropInfo.name, driverPropInfo.value );
					}
					// exit loop, if success.
					break;
				}
				catch ( SQLException sqlEx2 )
				{
					System.out.println( "Error:driverClassName:url[" + url + "]" );
					if ( i == (classDomainLstSize-1) )
					{
						throw sqlEx2;
					}
				}
			}
			
			List<List<Object>> dataLst = new ArrayList<>();
			driverMap.forEach
			(
				(k,v)->
				{
					List<Object> data = new ArrayList<>();
					data.add(k);
					data.add(v);
					dataLst.add(data);
				}
			);
			this.driverTableView.setTableViewData( this.headLst, dataLst );
		}
		catch ( SQLException sqlEx )
		{
			MyGUITool.showException( this.dbView.getMainController(), "conf.lang.gui.common.MyAlert", "TITLE_MISC_ERROR", sqlEx );
		}
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
	
	// CopyTableInterface
	@Override
	public void copyTableNoHead( Event event )
	{
		this.driverTableView.copyTableNoHead(event);
	}
	
	// CopyTableInterface
	@Override
	public void copyTableWithHead( Event event )
	{
		this.driverTableView.copyTableWithHead(event);
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
			textField.setText(getItem());
			setText(null);
				
			setGraphic(textField);
			textField.selectAll();
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
