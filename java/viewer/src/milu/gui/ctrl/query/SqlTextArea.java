package milu.gui.ctrl.query;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Path;

import milu.tool.MyTool;

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
		
		this.setOnKeyPressed
		(
			(event)->
			{
				try
				{
					System.out.println( "--- TextArea KeyPressed -------------" );
					System.out.println( "CaretPosition:" + this.getCaretPosition() );
					KeyCode keyCode = event.getCode();
					System.out.println( "KeyCode:" + keyCode );
					String chr = event.getCharacter();
					System.out.println( "Character:" + chr );
					
					Path caret = MyTool.findCaret(this.getParent());
					if ( caret != null )
					{
						Point2D screenLoc = MyTool.findScreenLocation(caret);
						System.out.println( "X:" + screenLoc.getX() + "/Y:" + screenLoc.getY() );
					}
				}
				catch ( Exception ex )
				{
					ex.printStackTrace();
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
