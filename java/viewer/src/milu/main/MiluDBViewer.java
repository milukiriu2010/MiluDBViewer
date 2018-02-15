package milu.main;

//import java.util.Enumeration;
import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.Map;


import javafx.application.Application;
import javafx.stage.Stage;

import javafx.scene.control.Alert.AlertType;

import milu.ctrl.MainController;
import milu.gui.dlg.MyAlertDialog;

public class MiluDBViewer extends Application
{
    public static void main(String[] args)
    {
    	System.out.println( "********** System Property **********" );
    	Properties  properties = System.getProperties();
    	Set<String> propSet    = properties.stringPropertyNames();
        List<String> propLst   = new ArrayList<String>( propSet );
        Collections.sort(propLst);
        
        for ( String propName : propLst )
        {
        	System.out.println( propName + "=" + System.getProperty(propName) );
        }
        System.out.println();
        System.out.println( "********** System Property **********" );
        
        Map<String, String>  envMap = System.getenv();
        List<String> envLst         = new ArrayList<String>( envMap.keySet() );
        Collections.sort(envLst);
        
        for ( String envName : envLst )
        {
        	System.out.println( envName + "=" + System.getenv(envName) );
        }
        System.out.println( "*************************************" );
        
        System.out.println();
        
    	launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception
    {
		MainController mainCtrl = new MainController();
    	try
    	{
    		mainCtrl.createNewDBConnectionAndOpenNewWindow();
    	}
    	catch ( Exception ex )
    	{
    		MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING );
    		alertDlg.setHeaderText( "Something Wrong!!" );
    		alertDlg.setTxtExp( ex );
    		alertDlg.showAndWait();
    	}
    	/*
    	finally
    	{
    		System.out.println( "DB connection is closing..." );
    		mainCtrl.close();
    	}
    	*/
    }
}
