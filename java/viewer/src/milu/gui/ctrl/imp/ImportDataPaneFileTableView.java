package milu.gui.ctrl.imp;

import java.util.Map;
import java.util.ResourceBundle;
import java.util.HashMap;
import java.util.List;
import java.io.File;
import java.io.IOException;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import milu.file.table.MyFileImportFactory;
import milu.file.table.MyFileImportAbstract;
import milu.gui.ctrl.common.inf.ChangeLangInterface;
import milu.gui.ctrl.common.table.ObjTableView;
import milu.gui.view.DBView;
import milu.main.MainController;
import milu.tool.MyTool;

public class ImportDataPaneFileTableView extends Pane
	implements ChangeLangInterface 
{
	private DBView          dbView = null;
	
	private WizardInterface wizardInf = null;
	
	private Map<String,Object> mapObj = null;
	
	private Integer  skipRowCount = Integer.valueOf(0);
	
	private BorderPane      basePane = new BorderPane();
	
    // -----------------------------------------------------
	// [Top]
    // -----------------------------------------------------
	private Label      lblSkipRowCnt = new Label();
	private TextField  txtSkipRowCnt = new TextField(String.valueOf(this.skipRowCount));
	
    // -----------------------------------------------------
	// [Center]
    // -----------------------------------------------------
	private ObjTableView objTableView = null;

    // -----------------------------------------------------
	// [Bottom]
    // -----------------------------------------------------
	private Button btnImport = new Button();


	ImportDataPaneFileTableView( DBView dbView, WizardInterface wizardInf, Map<String,Object> mapObj )
	{
		this.dbView    = dbView;
		this.wizardInf = wizardInf;
		this.mapObj    = mapObj;
		
		this.objTableView = new ObjTableView( this.dbView );
		
	    // -----------------------------------------------------
		// [Top]
	    // -----------------------------------------------------
		HBox hBoxSkip = new HBox(2);
		hBoxSkip.setPadding( new Insets( 10, 10, 10, 10 ) );
		hBoxSkip.setSpacing(10);
		//hBoxSkip.setAlignment(Pos.BOTTOM_LEFT);
		hBoxSkip.getChildren().addAll(this.lblSkipRowCnt,this.txtSkipRowCnt);
		this.basePane.setTop(hBoxSkip);
		
	    // -----------------------------------------------------
		// [Center]
	    // -----------------------------------------------------
		this.basePane.setCenter(this.objTableView);
		
	    // -----------------------------------------------------
		// [Bottom]
	    // -----------------------------------------------------
		HBox hBoxNext = new HBox(2);
		hBoxNext.setPadding( new Insets( 10, 10, 10, 10 ) );
		hBoxNext.setSpacing(10);
		hBoxNext.setAlignment(Pos.BOTTOM_RIGHT);
		hBoxNext.getChildren().add(this.btnImport);
		this.basePane.setBottom(hBoxNext);
		
		this.getChildren().addAll(this.basePane);
		
		this.setAction();
		
		this.loadData();
		
		this.changeLang();
	}
	
	private void setAction()
	{
		this.txtSkipRowCnt.textProperty().addListener((obs,oldVal,newVal)->{
			if ( newVal == null )
			{
				this.skipRowCount = Integer.valueOf(0);
			}
			// "Numeric" or "No Input" are allowed.
			else if ( newVal.length() == 0 )
			{
				this.skipRowCount = Integer.valueOf(0);
				this.objTableView.setSkipRowCnt(0);
			}
			// if alphabets or marks are input, back to previous input.
			else if ( newVal.matches( "^[0-9]+$" ) == false )
			{
				((StringProperty)obs).setValue( oldVal );
			}
			else
			{
				System.out.println( "change skipRowCnt" );
				this.skipRowCount = Integer.valueOf(this.txtSkipRowCnt.getText());
				this.objTableView.setSkipRowCnt(this.skipRowCount);
			}
		});
		
		this.btnImport.setOnAction((event)->{
			Map<String,Object> mapObj = new HashMap<>();
			mapObj.put( "importHeadLst", this.objTableView.getHeadList() );
			mapObj.put( "importDataLst", this.objTableView.getDataList() );
			mapObj.put( "skipRowCount" , this.skipRowCount );
			this.wizardInf.next( this, mapObj );
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
	
	// ChangeLangInterface
	@Override
	public void changeLang() 
	{
		MainController mainCtrl = this.dbView.getMainController();
		ResourceBundle langRB = mainCtrl.getLangResource("conf.lang.gui.ctrl.imp.ImportDataTab");
		ResourceBundle extLangRB = mainCtrl.getLangResource("conf.lang.gui.common.NodeName");
		
		this.lblSkipRowCnt.setText(langRB.getString("LABEL_SKIP_ROW_CNT"));
		
		this.btnImport.setText(langRB.getString("BTN_IMPORT"));
	}

}
