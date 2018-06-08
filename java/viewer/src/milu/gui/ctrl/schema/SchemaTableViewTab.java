package milu.gui.ctrl.schema;

import java.util.List;

import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import milu.gui.ctrl.common.inf.SetTableViewDataInterface;
import milu.gui.ctrl.common.table.ObjTableView;
import milu.gui.view.DBView;

/**
 * Show Table/View Definition
 *************************************************
 * @author milu
 *
 */
public class SchemaTableViewTab extends Tab
	implements
		SetTableViewDataInterface
{
	private DBView        dbView       = null;
	private ObjTableView  objTableView = null;
	
	public SchemaTableViewTab( DBView dbView )
	{
		super();
		
		this.dbView = dbView;
		
		this.objTableView = new ObjTableView(this.dbView);
		
		BorderPane brdPane = new BorderPane();
		brdPane.setCenter( this.objTableView );
		
		this.setContent( brdPane );
	}
	
	@Override
	public void setTableViewData( List<Object> headLst, List<List<Object>> dataLst )
	{
		this.objTableView.setTableViewData( headLst, dataLst );
	}
}
