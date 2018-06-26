package milu.gui.ctrl.info;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import milu.gui.ctrl.common.inf.ChangeLangInterface;
import milu.gui.ctrl.common.inf.FocusInterface;
import milu.gui.ctrl.common.table.ObjTableView;
import milu.gui.view.DBView;
import milu.main.MainController;

public class SystemEnvTab extends Tab 
	implements 
		FocusInterface,
		ChangeLangInterface 
{
	private DBView          dbView = null;
	
	private ObjTableView    objTableView = null;
	
	private ObservableList<Object> objHeadLst = FXCollections.observableArrayList();
	
	SystemEnvTab( DBView dbView )
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
		
        Map<String, String>  envMap     = System.getenv();
        List<Object>         envNameLst = new ArrayList<>( envMap.keySet() );
        Collections.sort( envNameLst, (o1,o2)->o1.toString().compareTo(o2.toString()) );
        
        // Data List
        List<List<Object>>  dataLst = new ArrayList<>();        
        for ( Object envName : envNameLst )
        {
        	//System.out.println( envName + "=" + System.getenv(envName) );
        	List<Object>  env = new ArrayList<>();
        	env.add( envName );
        	String envVal = System.getenv((String)envName);
    		// Nothing Change for URL format
        	if ( envVal.contains( "://") )
        	{
        	}
    		// Change "/" or ";" to "\n"
        	else
        	{
        		envVal = envVal.replaceAll( System.getProperty( "path.separator"), "\n" );
        	}
        	env.add( envVal );
        	dataLst.add( env );
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
		
		this.setText(langRB.getString("TITLE_SYSENV"));
		
		this.objHeadLst.clear();
		this.objHeadLst.addAll(
					extLangRB.getString( "ITEM_KEY" ),
					extLangRB.getString( "ITEM_VAL" )
				);
	}	
}
