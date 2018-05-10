package milu.gui.ctrl.schema;

import java.util.List;

import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import milu.gui.ctrl.common.inf.SetTableViewSQLInterface;
import milu.gui.ctrl.common.inf.ToggleHorizontalVerticalInterface;
import milu.gui.ctrl.query.SqlTableView;
import milu.gui.view.DBView;

/**
 * Show Table/View Definition
 *************************************************
 * @author milu
 *
 */
public class SchemaTableViewTab extends Tab
	implements
		SetTableViewSQLInterface,
		ToggleHorizontalVerticalInterface
{
	private DBView        dbView       = null;
	private SqlTableView  sqlTableView = null;
	
	public SchemaTableViewTab( DBView dbView )
	{
		super();
		
		this.dbView = dbView;
		
		this.sqlTableView = new SqlTableView(this.dbView);
		
		BorderPane brdPane = new BorderPane();
		brdPane.setCenter( this.sqlTableView );
		
		this.setContent( brdPane );
	}
	
	@Override
	public void setTableViewSQL( List<String> headLst, List<List<String>> dataLst )
	{
		this.sqlTableView.setTableViewSQL( headLst, dataLst );
	}
	
	/**************************************************
	 * Override from ToggleHorizontalVerticalInterface
	 ************************************************** 
	 */
	@Override
	public void switchDirection()
	{
		this.sqlTableView.switchDirection();
	}
	
}
