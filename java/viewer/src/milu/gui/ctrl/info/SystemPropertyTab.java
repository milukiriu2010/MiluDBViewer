package milu.gui.ctrl.info;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.scene.control.Tab;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import milu.gui.ctrl.common.inf.FocusInterface;
import milu.gui.ctrl.common.inf.ChangeLangInterface;
import milu.gui.ctrl.common.table.ObjTableView;
import milu.gui.view.DBView;
import milu.main.MainController;

public class SystemPropertyTab extends Tab 
	implements 
		FocusInterface,
		ChangeLangInterface 
{
	private DBView          dbView = null;
	
	private ObjTableView    objTableView = null;
	
	private ObservableList<Object> objHeadLst = null;
	
	SystemPropertyTab( DBView dbView )
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
		
    	Properties   properties  = System.getProperties();
    	Set<String>  propNameSet = properties.stringPropertyNames();
        List<Object> propNameLst = new ArrayList<>( propNameSet );
        Collections.sort(propNameLst, (o1,o2)->o1.toString().compareTo(o2.toString()) );
        
        // Data List
        List<List<Object>>  dataLst = new ArrayList<>();
        for ( Object propName : propNameLst )
        {
        	//System.out.println( propName + "=" + System.getProperty(propName) );
        	List<Object>  prop = new ArrayList<>();
        	prop.add( propName );
        	String propVal = System.getProperty(propName.toString());
        	if ( propName.equals( "path.separator" ) == false )
        	{
        		// Nothing Change for URL format
        		if ( propVal.contains( "://" ) )
        		{
        		}
        		// Change "/" or ";" to "\n"
        		else
        		{
        			propVal = propVal.replaceAll( System.getProperty("path.separator") , "\n" );
        		}
        	}
        	prop.add( propVal );
        	dataLst.add( prop );
        }
        
        this.objTableView.setTableViewData( this.objHeadLst, dataLst );
	}
	
	// ChangeLangInterface
	@Override
	public void changeLang() 
	{
		ResourceBundle langRB = this.dbView.getMainController().getLangResource("conf.lang.gui.dlg.SystemInfoDialog");
		
		this.setText("System Property");
		
		this.objHeadLst = 
				FXCollections.observableArrayList(
					langRB.getString( "ITEM_KEY" ),
					langRB.getString( "ITEM_VAL" )
				);
	}
}
