package milu.gui.ctrl.info;

import java.util.ResourceBundle;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import milu.gui.ctrl.common.inf.ChangeLangInterface;
import milu.gui.ctrl.common.inf.FocusInterface;
import milu.gui.ctrl.common.table.ObjTableView;
import milu.gui.view.DBView;
import milu.main.MainController;

public class SystemSQLTypesTab extends Tab 
	implements 
		FocusInterface,
		ChangeLangInterface 
{
	private DBView          dbView = null;
	
	private ObjTableView    objTableView = null;
	
	private ObservableList<Object> objHeadLst = FXCollections.observableArrayList();
	
	SystemSQLTypesTab( DBView dbView )
	{
		super();
		
		this.dbView = dbView;
		
		this.objTableView = new ObjTableView(this.dbView);
		
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
		
		// Data List
		List<List<Object>>  dataLst = new ArrayList<>();
		dataLst.add(Arrays.asList(java.sql.Types.ARRAY        ,"java.sql.Types.ARRAY"));
		dataLst.add(Arrays.asList(java.sql.Types.BIGINT       ,"java.sql.Types.BIGINT"));
		dataLst.add(Arrays.asList(java.sql.Types.BINARY       ,"java.sql.Types.BINARY"));
		dataLst.add(Arrays.asList(java.sql.Types.BIT          ,"java.sql.Types.BIT"));
		dataLst.add(Arrays.asList(java.sql.Types.BLOB         ,"java.sql.Types.BLOB"));
		dataLst.add(Arrays.asList(java.sql.Types.BOOLEAN      ,"java.sql.Types.BOOLEAN"));
		dataLst.add(Arrays.asList(java.sql.Types.CHAR         ,"java.sql.Types.CHAR"));
		dataLst.add(Arrays.asList(java.sql.Types.CLOB         ,"java.sql.Types.CLOB"));
		dataLst.add(Arrays.asList(java.sql.Types.DATALINK     ,"java.sql.Types.DATALINK"));
		dataLst.add(Arrays.asList(java.sql.Types.DATE         ,"java.sql.Types.DATE"));
		dataLst.add(Arrays.asList(java.sql.Types.DECIMAL      ,"java.sql.Types.DECIMAL"));
		dataLst.add(Arrays.asList(java.sql.Types.DISTINCT     ,"java.sql.Types.DISTINCT"));
		dataLst.add(Arrays.asList(java.sql.Types.DOUBLE       ,"java.sql.Types.DOUBLE"));
		dataLst.add(Arrays.asList(java.sql.Types.FLOAT        ,"java.sql.Types.FLOAT"));
		dataLst.add(Arrays.asList(java.sql.Types.INTEGER      ,"java.sql.Types.INTEGER"));
		dataLst.add(Arrays.asList(java.sql.Types.JAVA_OBJECT  ,"java.sql.Types.JAVA_OBJECT"));
		dataLst.add(Arrays.asList(java.sql.Types.LONGNVARCHAR ,"java.sql.Types.LONGNVARCHAR"));
		dataLst.add(Arrays.asList(java.sql.Types.LONGVARBINARY,"java.sql.Types.LONGVARBINARY"));
		dataLst.add(Arrays.asList(java.sql.Types.LONGVARCHAR  ,"java.sql.Types.LONGVARCHAR"));
		dataLst.add(Arrays.asList(java.sql.Types.NCHAR        ,"java.sql.Types.NCHAR"));
		dataLst.add(Arrays.asList(java.sql.Types.NCLOB        ,"java.sql.Types.NCLOB"));
		dataLst.add(Arrays.asList(java.sql.Types.NULL         ,"java.sql.Types.NULL"));
		dataLst.add(Arrays.asList(java.sql.Types.NUMERIC      ,"java.sql.Types.NUMERIC"));
		dataLst.add(Arrays.asList(java.sql.Types.NVARCHAR     ,"java.sql.Types.NVARCHAR"));
		dataLst.add(Arrays.asList(java.sql.Types.OTHER        ,"java.sql.Types.OTHER"));
		dataLst.add(Arrays.asList(java.sql.Types.REAL         ,"java.sql.Types.REAL"));
		dataLst.add(Arrays.asList(java.sql.Types.REF          ,"java.sql.Types.REF"));
		dataLst.add(Arrays.asList(java.sql.Types.REF_CURSOR   ,"java.sql.Types.REF_CURSOR"));
		dataLst.add(Arrays.asList(java.sql.Types.ROWID        ,"java.sql.Types.ROWID"));
		dataLst.add(Arrays.asList(java.sql.Types.SMALLINT     ,"java.sql.Types.SMALLINT"));
		dataLst.add(Arrays.asList(java.sql.Types.SQLXML       ,"java.sql.Types.SQLXML"));
		dataLst.add(Arrays.asList(java.sql.Types.STRUCT       ,"java.sql.Types.STRUCT"));
		dataLst.add(Arrays.asList(java.sql.Types.TIME         ,"java.sql.Types.TIME"));
		dataLst.add(Arrays.asList(java.sql.Types.TIME_WITH_TIMEZONE,"java.sql.Types.TIME_WITH_TIMEZONE"));
		dataLst.add(Arrays.asList(java.sql.Types.TINYINT      ,"java.sql.Types.TINYINT"));
		dataLst.add(Arrays.asList(java.sql.Types.VARBINARY    ,"java.sql.Types.VARBINARY"));
		dataLst.add(Arrays.asList(java.sql.Types.VARCHAR      ,"java.sql.Types.VARCHAR"));
		
		this.objTableView.setTableViewData( this.objHeadLst, dataLst );
	}
	
	// ChangeLangInterface
	@Override
	public void changeLang() 
	{
		MainController mainCtrl = this.dbView.getMainController();
		ResourceBundle extLangRB = mainCtrl.getLangResource("conf.lang.gui.common.NodeName");
		
		this.setText("SQLTypes");
		
		this.objHeadLst.clear();
		this.objHeadLst.addAll(
					extLangRB.getString( "ITEM_KEY" ),
					extLangRB.getString( "ITEM_VAL" )
				);
	}	
}
