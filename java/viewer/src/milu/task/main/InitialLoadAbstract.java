package milu.task.main;

import java.util.ResourceBundle;

import javafx.scene.control.Alert.AlertType;
import milu.gui.dlg.MyAlertDialog;
import milu.main.MainController;
import milu.task.ProgressInterface;

abstract public class InitialLoadAbstract 
{
	protected MainController  mainCtrl = null;
	
	protected ProgressInterface  progressInf = null;
	
	protected double          assignedSize = 0.0;
	
	public void setMainController( MainController mainCtrl )
	{
		this.mainCtrl = mainCtrl;
	}
	
	void setProgressInterface( ProgressInterface progressInf )
	{
		this.progressInf = progressInf;
	}
	
	void setAssignedSize( double assignedSize )
	{
		this.assignedSize = assignedSize;
	}
	/*
	protected void showException( Exception ex )
	{
		MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING, this.mainCtrl );
		ResourceBundle langRB = this.mainCtrl.getLangResource("conf.lang.gui.common.MyAlert");
		alertDlg.setHeaderText( langRB.getString("TITLE_MISC_ERROR") );
		alertDlg.setTxtExp( ex );
		alertDlg.showAndWait();
		alertDlg = null;
	}
	*/	

	abstract public void load();
}
