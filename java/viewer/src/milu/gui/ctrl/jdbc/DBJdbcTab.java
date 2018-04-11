package milu.gui.ctrl.jdbc;

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import milu.gui.ctrl.common.ChangeLangInterface;
import milu.gui.ctrl.common.CopyInterface;
import milu.gui.ctrl.common.FocusInterface;
import milu.gui.ctrl.query.SqlTableView;
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
	
	private ListView<Driver>   driverListView = new ListView<>();
	
	private SqlTableView driverTableView = null;
	
	public DBJdbcTab( DBView dbView )
	{
		super();
		
		this.dbView = dbView;
		
		this.driverTableView = new SqlTableView(this.dbView);
		
		// SplitPane
		this.splitPane.setOrientation(Orientation.HORIZONTAL);
		
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
	}
	
	private void setData()
	{
		List<Driver> driverLst = new ArrayList<>();
		Enumeration<Driver> drivers = DriverManager.getDrivers();
		while (drivers.hasMoreElements())
		{
			driverLst.add(drivers.nextElement());
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
	}
	
}
