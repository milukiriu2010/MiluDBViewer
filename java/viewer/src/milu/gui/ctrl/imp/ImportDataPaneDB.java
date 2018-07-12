package milu.gui.ctrl.imp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;

import milu.db.MyDBAbstract;
import milu.gui.ctrl.common.PathTreeView;
import milu.gui.ctrl.common.inf.ChangePathInterface;
import milu.gui.ctrl.menu.AfterDBConnectedInterface;
import milu.main.AppConst;
import milu.main.MainController;
import milu.tool.MyFileTool;
import milu.gui.view.DBView;

public class ImportDataPaneDB extends Pane 
	implements
		AfterDBConnectedInterface,
		ChangePathInterface
{
	private DBView          dbView = null;
	
	private WizardInterface wizardInf = null;
	
	private MenuBar       menuBar = new MenuBar();
	
	private Menu          menuBookMark = new Menu("Select DB");
	
	private PathTreeView  pathTreeView = new PathTreeView();
	
	private MyDBAbstract  myDBAbs = null;
	
	ImportDataPaneDB( DBView dbView, WizardInterface wizardInf, Map<String,Object> mapObj )
	{
		this.dbView = dbView;
		this.wizardInf = wizardInf;
		MainController mainCtrl = this.dbView.getMainController();
		
		/*
		this.pathTreeView.setMainController(mainCtrl);
		this.pathTreeView.setChangePathInterface(this);
		this.pathTreeView.setRootDir(AppConst.DB_DIR.val());
		this.pathTreeView.setFileExt("json");
		try
		{
			this.pathTreeView.init();
		}
		catch( IOException ioEx )
		{
			ioEx.printStackTrace();
		}

		this.pathTreeView.setEditable(false);
		
		this.getChildren().add(this.pathTreeView);
		*/
		
		this.menuBar.getMenus().add(this.menuBookMark);
		this.menuBookMark.getItems().addAll( new MenuItem("dummy") );
		
		this.setAction();
		
		this.getChildren().add(this.menuBar);
	}
	
	private void setAction()
	{
	    this.menuBookMark.setOnShowing((event)->{
	    	this.menuBookMark.getItems().removeAll(this.menuBookMark.getItems());
	    	try
	    	{
	    		//this.createMenuBookMark( Paths.get(AppConst.DB_DIR.val()), this.menuBookMark );
	    		MyFileTool.createMenuBookMark( Paths.get(AppConst.DB_DIR.val()), this.menuBookMark, this.dbView, this );
	    	}
	    	catch ( IOException ioEx )
	    	{
	    		throw new RuntimeException(ioEx);
	    	}
	    });
	}
	
	// AfterDBConnectedInterface
	@Override
	public void afterConnection( MyDBAbstract myDBAbs )
	{
		this.myDBAbs = myDBAbs;
	}	
	
	// ChangePathInterface
	@Override
	public void changePath( Path path )
	{
		System.out.println( "changePath[" + path.toString() + "]" );
		if ( Files.isRegularFile(path) == false )
		{
			return;
		}
		
	}
}
