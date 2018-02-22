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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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
	
	private StringBuffer sbOnTheWay = new StringBuffer();
	
	private AnchorPane        parentPane = null;
	
	public SqlTextArea()
	{
		super();
	}

	/*
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
	*/
	
	public void init()
	{
		this.parentPane = MyTool.findAnchorPane( this );
		if ( this.parentPane != null )
		{
			this.parentPane.getChildren().add( this.comboHint );
			this.comboHint.setVisible( false );
			
			// https://stackoverflow.com/questions/19010619/javafx-filtered-combobox
			this.filteredItems = new FilteredList<String>( hints, p -> true);
			this.comboHint.setItems( this.filteredItems );		
		}
		
		this.setMouseAction();
		
		if ( this.parentPane != null )
		{
			this.setKeyAction();
		}
		
		this.setAction();
	}
	
	private void setMouseAction()
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
			
		this.addEventFilter
		(
			MouseEvent.MOUSE_PRESSED , 
			(event)->
			{
				System.out.println( "--- TextArea MousePressed -----------" );
				this.comboHint.setVisible(false);
			}
		);
	}
	
	private void setKeyAction()
	{
		this.setOnKeyPressed
		(
			(event)->
			{
				System.out.println( "--- TextArea KeyPressed -------------" );
				KeyCode keyCode = event.getCode();
				System.out.println( "KeyCode:" + keyCode );
				
				Boolean isVisibleComboHint = this.comboHint.visibleProperty().getValue();
				if ( isVisibleComboHint == true )
				{
					if ( KeyCode.ENTER.equals( keyCode ) )
					{
						String selectedItem = this.comboHint.selectionModelProperty().getValue().getSelectedItem();
						System.out.println( "selectedItem:" + selectedItem );
						int pos = this.caretPositionProperty().getValue().intValue();
						if ( selectedItem.contains( this.sbOnTheWay ) )
						{
							this.insertText( pos, selectedItem.substring( this.sbOnTheWay.length() ) );
						}
						
						this.comboHint.setVisible(false);
						this.comboHint.hide();
						this.sbOnTheWay = null;
					}
					else if ( KeyCode.ESCAPE.equals( keyCode ) ) 
					{
						this.comboHint.setVisible(false);
						this.comboHint.hide();
						this.sbOnTheWay = null;
					}
				}
				else
				{
				}
			}
		);
		
		this.setOnKeyTyped
		(
			(event)->
			{
				System.out.println( "--- TextArea KeyTyped -------------" );
				//System.out.println( "CaretPosition:" + this.getCaretPosition() );
				
				String chr = event.getCharacter();
				System.out.println( "Character:" + chr );
				System.out.println( "CharacterHex[" + MyTool.bytesToHex( chr.getBytes() ) + "]" );
				
				Boolean isVisibleComboHint = this.comboHint.visibleProperty().getValue();
				
				if ( isVisibleComboHint == false )
				{
					if ( ".".equals( chr ) )
					{
						Path caret = MyTool.findCaret(this);
						Point2D screenLoc = MyTool.findScreenLocation(caret,this);
						System.out.println( "X:" + screenLoc.getX() + "/Y:" + screenLoc.getY() );
						AnchorPane.setLeftAnchor( this.comboHint, screenLoc.getX() );
						AnchorPane.setTopAnchor( this.comboHint, screenLoc.getY() );
						
						// list back to full.
						this.filteredItems.setPredicate( (item)->{ return true;	} );
						
						this.comboHint.getSelectionModel().selectFirst();
						this.comboHint.setVisible(true);
						this.comboHint.show();
						
						this.sbOnTheWay = null;
						this.sbOnTheWay = new StringBuffer();
					}
				}
				else
				{
					if ( "\r".equals(chr) || "\n".equals(chr) )
					{
						String selectedItem = this.comboHint.selectionModelProperty().getValue().getSelectedItem();
						System.out.println( "selectedItem:" + selectedItem );
						int pos = this.caretPositionProperty().getValue().intValue();
						if ( selectedItem.contains( this.sbOnTheWay ) )
						{
							this.insertText( pos, selectedItem.substring( this.sbOnTheWay.length() ) );
						}
						
						this.comboHint.setVisible(false);
						//this.comboHint.hide();
						this.sbOnTheWay = null;
					}
					else
					{
						this.sbOnTheWay.append( chr );
						System.out.println( "sbOnTheWay:" + this.sbOnTheWay );
						this.comboHint.getSelectionModel().selectFirst();
						
						this.filteredItems.setPredicate
						( 
							(item)->
							{
								System.out.println( "setPredicate" );
								if ( item.startsWith( this.sbOnTheWay.toString() ) )
								{
									System.out.println( "startsWith:true" );
									return true;
								}
								else
								{
									System.out.println( "startsWith:false" );
									return false;
								}
							}
						);
						
						if ( this.filteredItems.size() == 0 )
						{
							this.comboHint.hide();
							this.comboHint.setVisible(false);
						}
					}
				}
			}
		);
		
	}
	
	private void setAction()
	{
		this.comboHint.addEventFilter
		(
			KeyEvent.KEY_TYPED,
			(event)->
			{
				System.out.println( "--- ComboBox KeyTyped -----------" );
				String selectedItem = this.comboHint.selectionModelProperty().getValue().getSelectedItem();
				System.out.println( "selectedItem:" + selectedItem );
				int pos = this.caretPositionProperty().getValue().intValue();
				this.insertText( pos, selectedItem );				
				this.comboHint.setVisible(false);
				this.comboHint.hide();
				event.consume();
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
