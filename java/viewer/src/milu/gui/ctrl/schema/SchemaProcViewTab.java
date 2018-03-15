package milu.gui.ctrl.schema;

import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

import milu.gui.ctrl.query.SqlTextArea;
import milu.gui.view.DBView;

public class SchemaProcViewTab extends Tab
{
	private DBView     dbView      = null;
	
	private TextArea   srcTextArea = null;
	
	public SchemaProcViewTab( DBView dbView )
	{
		super();
		
		this.dbView = dbView;
		
		this.srcTextArea = new SqlTextArea( this.dbView );
		this.srcTextArea.setEditable( false );
		
		BorderPane brdPane = new BorderPane();
		brdPane.setCenter( this.srcTextArea );
		
		this.setContent( brdPane );
	}
	
	public void setSrcText( String strSrc )
	{
		this.srcTextArea.setText( strSrc );
	}
}
