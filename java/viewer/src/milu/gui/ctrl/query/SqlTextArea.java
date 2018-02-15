package milu.gui.ctrl.query;

import javafx.scene.control.TextArea;

import javafx.scene.text.Font;

public class SqlTextArea extends TextArea
{
	public SqlTextArea()
	{
		super();
		
		this.setAction();
	}
	
	private void setAction()
	{
		// Change Font Size, when MouseWheel Up/Down + Ctrl Key
		this.setOnScroll
		(
			(event)->
			{
				if ( event.isControlDown() )
				{
					double deltaY = event.getDeltaY();
					final Font font = this.getFont();
					double fontSize = font.getSize();
					System.out.println( "MouseWheel+Ctrl[deltaY]" + deltaY + "[fontSize]"+ font.getSize() );
					fontSize = ( deltaY >= 0 ) ? fontSize+1.0:fontSize-1.0;
					if ( fontSize <= 0.0 )
					{
						fontSize = 1.0;
					}
					this.setStyle( "-fx-font-size: " + fontSize + ";" );
					event.consume();
				}
			}
		);
	}
	
	public String getSQL()
	{
		String strSQL = this.getText();
		strSQL = strSQL.trim();
		while ( strSQL.charAt(strSQL.length()-1) == ';' )
		{
			strSQL = strSQL.substring( 0, strSQL.length()-1 );
		}
		return strSQL;
	}
}
