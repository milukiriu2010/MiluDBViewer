package milu.main;

import javafx.application.Application;
import javafx.stage.Stage;

import javafx.scene.control.Alert.AlertType;

import milu.ctrl.MainController;
import milu.gui.dlg.MyAlertDialog;

public class MiluDBViewer extends Application
{
    public static void main(String[] args)
    {
    	launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception
    {
		MainController mainCtrl = new MainController();
    	try
    	{
    		mainCtrl.setApplication( this );
    		mainCtrl.loadImages();
    		mainCtrl.createNewDBConnectionAndOpenNewWindow();
    	}
    	catch ( Exception ex )
    	{
    		MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING );
    		alertDlg.setHeaderText( "Something Wrong!!" );
    		alertDlg.setTxtExp( ex );
    		alertDlg.showAndWait();
    	}
    }
}
