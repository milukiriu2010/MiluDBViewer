package milu.gui.ctrl.schema;

import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

import milu.gui.ctrl.query.SqlTextArea;

public class DBSchemaProcViewTab extends Tab
{
	private TextArea   srcTextArea = new SqlTextArea();
	
	public DBSchemaProcViewTab()
	{
		super();
		
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
