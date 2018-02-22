package milu.gui.ctrl.query;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Path;

import milu.tool.MyTool;

public class SqlTextArea extends TextArea
{
	private ComboBox<String>  comboHint = new ComboBox<>();
	
	private final static ObservableList<String>  hints = FXCollections.observableArrayList
			(
					"createIndex",
					"createProcedure",
					"createPackage",
					"createTable",
					"createView",
					"deleteIndex",
					"deleteProcedure",
					"deletePackage",
					"deleteTable",
					"deleteView"
				);
	FilteredList<String>  filteredItems = null;	
	
	private AnchorPane        parentPane = null;
	
	public SqlTextArea()
	{
		super();
		
		this.setAction();
	}
	
	public SqlTextArea( AnchorPane parentPane )
	{
		super();
		
		this.parentPane = parentPane;
		this.parentPane.getChildren().add( this.comboHint );
		this.comboHint.setVisible( false );
		
		// https://stackoverflow.com/questions/19010619/javafx-filtered-combobox
		this.filteredItems = new FilteredList<String>( hints, p -> true);
		this.comboHint.setItems( this.filteredItems );		
		
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
		
		if ( this.parentPane != null )
		{
			this.setKeyAction();
		}
	}
	
	private void setKeyAction()
	{
		
		this.setOnKeyPressed
		(
			(event)->
			{
				System.out.println( "--- TextArea KeyPressed -------------" );
				System.out.println( "CaretPosition:" + this.getCaretPosition() );
				
				
				KeyCode keyCode = event.getCode();
				System.out.println( "KeyCode:" + keyCode );
				String chr = event.getCharacter();
				System.out.println( "Character:" + chr );
				
				if ( KeyCode.PERIOD.equals( keyCode ) )
				{
					Boolean isVisibleComboHint = this.comboHint.visibleProperty().getValue();
					Path caret = MyTool.findCaret(this);
					Point2D screenLoc = MyTool.findScreenLocation(caret,this);
					System.out.println( "X:" + screenLoc.getX() + "/Y:" + screenLoc.getY() );
					AnchorPane.setLeftAnchor( this.comboHint, screenLoc.getX() );
					AnchorPane.setTopAnchor( this.comboHint, screenLoc.getY() );
					this.comboHint.getSelectionModel().selectFirst();
					this.comboHint.setVisible(true);
					this.comboHint.show();
				}
					
			}
		);
		
		
		this.setOnKeyTyped
		(
			(event)->
			{
				try
				{
					System.out.println( "--- TextArea Typed -------------" );
					System.out.println( "CaretPosition:" + this.getCaretPosition() );
					System.out.println( "this:" + this.getClass() );
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
