package milu.gui.ctrl.info;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Tab;
import javafx.stage.Screen;
import milu.gui.ctrl.common.inf.ChangeLangInterface;
import milu.gui.ctrl.common.inf.FocusInterface;
import milu.gui.ctrl.common.table.ObjTableView;
import milu.gui.view.DBView;
import milu.main.MainController;

public class SystemDisplayTab extends Tab 
	implements 
		FocusInterface, 
		ChangeLangInterface 
{
	private DBView          dbView = null;
	
	private ObjTableView    objTableView = null;
	
	private ObservableList<Object> objHeadLst = FXCollections.observableArrayList();
	
	SystemDisplayTab( DBView dbView )
	{
		super();
		
		this.dbView = dbView;
		
		this.objTableView = new ObjTableView(this.dbView);
		//this.objTableView.prefHeightProperty().bind(this.getTabPane().heightProperty());
		//this.objTableView.prefWidthProperty().bind(this.getTabPane().widthProperty());
		
		this.changeLang();
		
		this.setContent(this.objTableView);		
	}

	// FocusInterface
	@Override
	public void setFocus() 
	{
		if ( this.objTableView.getDataList().size() > 0 )
		{
			return;
		}
		
		// http://krr.blog.shinobi.jp/javafx/javafx%20scene%E3%83%BBscenegraph%E3%82%AF%E3%83%A9%E3%82%B9
		MainController mainCtrl = this.dbView.getMainController();
		ResourceBundle langRB = mainCtrl.getLangResource("conf.lang.gui.ctrl.info.SystemTab");
        
        // Data List
        List<List<Object>>  dataLst = new ArrayList<>();
        
        int dispID = 1;
        for ( Screen  screen : Screen.getScreens() )
        {
            // Display Size
            List<Object> dispSizeLst = new ArrayList<>();
            dispSizeLst.add( langRB.getString("ITEM_DISP_SIZE") + "(" + dispID + ")" );
    		Rectangle2D  rec = screen.getBounds();
            dispSizeLst.add( String.format( "%d x %d",(int)rec.getWidth(),(int)rec.getHeight()) );
            dataLst.add(dispSizeLst);
            // Display Size(Visual)
            List<Object> dispSizeVisualLst = new ArrayList<>();
            dispSizeVisualLst.add( langRB.getString("ITEM_DISP_SIZE_VISUAL") + "(" + dispID + ")" );
    		Rectangle2D  recVisual = screen.getVisualBounds();
            dispSizeVisualLst.add( String.format( "%d x %d",(int)recVisual.getWidth(),(int)recVisual.getHeight()) );
            dataLst.add(dispSizeVisualLst);
            // Display DPI
            List<Object> dispDPILst = new ArrayList<>();
            dispDPILst.add( langRB.getString("ITEM_DISP_DPI") + "(" + dispID + ")" );
            dispDPILst.add( String.format( "%d",(int)screen.getDpi()) );
            dataLst.add(dispDPILst);
            
            dispID++;
        }
        
        this.objTableView.setTableViewData( this.objHeadLst, dataLst );

	}

	// ChangeLangInterface
	@Override
	public void changeLang() 
	{
		MainController mainCtrl = this.dbView.getMainController();
		ResourceBundle langRB = mainCtrl.getLangResource("conf.lang.gui.ctrl.info.SystemTab");
		ResourceBundle extLangRB = mainCtrl.getLangResource("conf.lang.gui.common.NodeName");
		
		this.setText(langRB.getString("TITLE_SYSDISP"));
		
		this.objHeadLst.clear();
		this.objHeadLst.addAll(
					extLangRB.getString( "ITEM_KEY" ),
					extLangRB.getString( "ITEM_VAL" )
				);

	}

}
