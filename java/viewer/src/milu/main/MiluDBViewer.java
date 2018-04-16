package milu.main;

import javafx.application.Application;
import javafx.stage.Stage;

import javafx.scene.control.Alert.AlertType;
import milu.gui.dlg.MyAlertDialog;

public class MiluDBViewer extends Application
{
    public static void main(String[] args)
    {
    	launch(args);
    	// https://stackoverflow.com/questions/46053974/using-platform-exit-and-system-exitint-together?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
    	System.exit(0);
    }
    
    @Override
    public void start(Stage stage) throws Exception
    {
		MainController mainCtrl = new MainController();
    	try
    	{
    		mainCtrl.init(this);
    	}
    	catch ( Exception ex )
    	{
    		MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING, mainCtrl );
    		alertDlg.setHeaderText( "Something Wrong!!" );
    		alertDlg.setTxtExp( ex );
    		alertDlg.showAndWait();
    	}
    }
}
