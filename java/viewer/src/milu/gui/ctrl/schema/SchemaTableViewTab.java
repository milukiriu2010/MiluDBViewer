package milu.gui.ctrl.schema;

import java.util.List;

import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;

import milu.ctrl.ToggleHorizontalVerticalInterface;
import milu.gui.ctrl.query.SqlTableView;

/**
 * Show Table/View Definition
 *************************************************
 * @author milu
 *
 */
public class SchemaTableViewTab extends Tab
	implements
		ToggleHorizontalVerticalInterface
{
	private SqlTableView  sqlTableView = null;
	
	public SchemaTableViewTab()
	{
		super();
		
		this.sqlTableView = new SqlTableView();
		
		BorderPane brdPane = new BorderPane();
		brdPane.setCenter( this.sqlTableView );
		
		this.setContent( brdPane );
	}
	
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
