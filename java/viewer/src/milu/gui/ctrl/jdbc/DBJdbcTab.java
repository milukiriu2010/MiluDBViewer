package milu.gui.ctrl.jdbc;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import milu.gui.ctrl.common.ChangeLangInterface;
import milu.gui.ctrl.common.CopyInterface;
import milu.gui.ctrl.common.FocusInterface;
import milu.gui.ctrl.query.SqlTableView;
import milu.gui.dlg.MyAlertDialog;
import milu.gui.view.DBView;
import milu.main.MainController;
import milu.tool.MyTool;

public class DBJdbcTab extends Tab 
	implements
		CopyInterface,
		FocusInterface,
		ChangeLangInterface
{
	private DBView          dbView = null;

	// Left  => Driver List
	// Right => Driver Info
	private SplitPane splitPane = new SplitPane();
	
    // Head List
    private List<String> headLst = new ArrayList<>(Arrays.asList("KEY","VALUE"));
	
	private ListView<Driver>   driverListView = new ListView<>();
	
	private Button  btnAdd = new Button();
	
	private Button  btnDel = new Button();
	
	private TextField  majorVerTxt = new TextField();
	
	private TextField  minorVerTxt = new TextField();
	
	private SqlTableView driverTableView = null;
	
	public DBJdbcTab( DBView dbView )
	{
		super();
		
		this.dbView = dbView;
		
		HBox hBoxBtn = new HBox(2);
		hBoxBtn.setSpacing(2);
		hBoxBtn.getChildren().addAll( this.btnAdd, this.btnDel );
		
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
		
		// SplitPane
		this.splitPane.setOrientation(Orientation.HORIZONTAL);
		this.splitPane.getItems().addAll( vBoxDriverCtrl, vBoxDriverInfo );
		this.splitPane.setDividerPositions( 0.5f, 0.5f );
		
		BorderPane brdPane = new BorderPane();
		brdPane.setCenter( this.splitPane );
		
		this.setContent( brdPane );
		
		MainController mainCtrl = this.dbView.getMainController();
		
		// set icon on Tab
		this.setGraphic( MyTool.createImageView( 16, 16, mainCtrl.getImage("file:resources/images/jdbc.png") ) );
		
		// set Title on Tab
		this.setText("JDBC");
		
		// set data
		this.setData();
		
		this.setAction();
		
		this.changeLang();
	}
	
	private void setData()
	{
		List<Driver> driverLst = new ArrayList<>();
		Enumeration<Driver> drivers = DriverManager.getDrivers();
		while (drivers.hasMoreElements())
		{
			driverLst.add(drivers.nextElement());
		}
		
		final List<Driver> sortedDriverLst = driverLst.stream()
			.sorted( (o1,o2) -> o1.toString().compareTo(o2.toString()) )
			.collect(Collectors.toList());
		this.driverListView.getItems().addAll(sortedDriverLst);
		
		this.driverListView.getSelectionModel().selectFirst();
		this.changeSelectedDriver( this.driverListView.getSelectionModel().getSelectedItem() );
	}
	
	private void setAction()
	{
		this.driverListView.getSelectionModel().selectedItemProperty().addListener( (obs,oldVal,driver)->this.changeSelectedDriver(driver) );
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
			MainController mainCtrl = this.dbView.getMainController();
    		MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING, mainCtrl );
    		ResourceBundle langRB = mainCtrl.getLangResource("conf.lang.gui.common.MyAlert");
			alertDlg.setHeaderText( langRB.getString("TITLE_MISC_ERROR") );
    		alertDlg.setTxtExp( sqlEx );
    		alertDlg.showAndWait();
    		alertDlg = null;
		}		
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
		this.btnDel.setText(langRB.getString("BTN_DEL"));
	}
	
}
