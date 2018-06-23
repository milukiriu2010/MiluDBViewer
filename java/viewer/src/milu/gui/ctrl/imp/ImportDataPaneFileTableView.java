package milu.gui.ctrl.imp;

import java.util.Map;
import java.util.List;
import java.io.File;
import java.io.IOException;

import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;
import milu.file.table.MyFileImportFactory;
import milu.file.table.MyFileImportAbstract;
import milu.gui.ctrl.common.table.ObjTableView;
import milu.gui.view.DBView;
import milu.main.MainController;
import milu.tool.MyTool;

public class ImportDataPaneFileTableView extends Pane 
{
	private DBView          dbView = null;
	
	private WizardInterface wizardInf = null;
	
	private Map<String,Object> mapObj = null;
	
	private BorderPane      basePane = new BorderPane();
	
    // -----------------------------------------------------
	// [Top]
    // -----------------------------------------------------
	private TextField  txtSkipRowCnt = new TextField("0");
	
    // -----------------------------------------------------
	// [Center]
    // -----------------------------------------------------
	private ObjTableView objTableView = null;
	

	ImportDataPaneFileTableView( DBView dbView, WizardInterface wizardInf, Map<String,Object> mapObj )
	{
		this.dbView    = dbView;
		this.wizardInf = wizardInf;
		this.mapObj    = mapObj;
		
		this.objTableView = new ObjTableView( this.dbView );
		
		this.basePane.setTop(this.txtSkipRowCnt);
		
		this.basePane.setCenter(this.objTableView);
		
		this.getChildren().addAll(this.basePane);
		
		this.setAction();
		
		this.loadData();
	}
	
	private void setAction()
	{
		this.txtSkipRowCnt.textProperty().addListener((obs,oldVal,newVal)->{
			if ( newVal == null )
			{
			}
			// "Numeric" or "No Input" are allowed.
			else if ( newVal.length() == 0 )
			{
			}
			// if alphabets or marks are input, back to previous input.
			else if ( newVal.matches( "^[0-9]+$" ) == false )
			{
				((StringProperty)obs).setValue( oldVal );
			}
			else
			{
				System.out.println( "change skipRowCnt" );
				this.objTableView.setSkipRowCnt(Integer.valueOf(this.txtSkipRowCnt.getText()));
			}
		});
	}
	
	private void loadData()
	{
		MainController mainCtrl = this.dbView.getMainController();
		String strFile = (String)this.mapObj.get("File");
		File file = new File(strFile);
		MyFileImportAbstract myFileAbs = MyFileImportFactory.getInstance(file);
		try
		{
			myFileAbs.open(file);
			myFileAbs.load();
			List<Object>       headLst = myFileAbs.getHeadLst();  
			List<List<Object>> dataLst = myFileAbs.getDataLst();
			System.out.println( "headLst.size:" + headLst.size() );
			System.out.println( "dataLst.size:" + dataLst.size() );
			
			this.objTableView.setTableViewData(headLst, dataLst);
			/*
			int no = 0;
			for ( List<Object> dataRow : dataLst )
			{
				System.out.print( no + ":" );
				for ( Object obj : dataRow )
				{
					System.out.print( obj.toString() + "\t" );
				}
				System.out.println();
				no++;
			}
			*/
		}
		catch ( Exception ex )
		{
			MyTool.showException( mainCtrl, "conf.lang.gui.common.MyAlert", "TITLE_MISC_ERROR", ex );
		}
		finally
		{
			try
			{
				myFileAbs.close();
			}
			catch ( IOException ioEx )
			{
				
			}
		}
	}
}
