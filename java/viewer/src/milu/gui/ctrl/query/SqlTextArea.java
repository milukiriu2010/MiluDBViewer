package milu.gui.ctrl.query;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Path;

import net.sf.jsqlparser.JSQLParserException;

import milu.ctrl.sqlparse.SQLParse;
import milu.ctrl.visitor.SearchSchemaEntityInterface;
import milu.ctrl.visitor.SearchSchemaEntityVisitorFactory;
import milu.tool.MyTool;
import milu.gui.view.DBView;
import milu.entity.schema.SchemaEntity;
import milu.db.MyDBAbstract;

public class SqlTextArea extends TextArea
{
	private DBView  dbView = null;
	
	private ComboBox<String>  comboHint = new ComboBox<>();
	
	private final ObservableList<String>  hints = FXCollections.observableArrayList();
	/*
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
				*/
	private FilteredList<String>  filteredItems = null;
	
	private StringBuffer sbOnTheWay = new StringBuffer();
	
	private AnchorPane        parentPane = null;
	
	private List<String>  tableLst = new ArrayList<>();
	
	// Alias <=> Table
	private Map<String,String>  aliasMap = new HashMap<>();
	
	private String  strLastWord = "";
	
	public SqlTextArea( DBView dbView )
	{
		super();
		
		this.dbView = dbView;
	}
	
	/**
	 *  Call after this class added on AnchorPane
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
					//event.consume();
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
				// ComboBox is invisible
				if ( isVisibleComboHint == false )
				{
					/*
					if ( KeyCode.PERIOD.equals(keyCode) )
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
					*/
				}
				// ComboBox is visible
				else
				{
					if ( KeyCode.ENTER.equals( keyCode ) )
					{
						String selectedItem = this.comboHint.selectionModelProperty().getValue().getSelectedItem();
						System.out.println( "selectedItem[" + selectedItem + "]sbOnTheWay[" + this.sbOnTheWay + "]" );
						int pos = this.caretPositionProperty().getValue().intValue();
						// -----------------------------------------------------------------
						// On Ubuntu, "Return Code" is added after ComboBox is closed.
						// I know this is not a good solution. How should I ?
						// -----------------------------------------------------------------
						if ( pos > 0 )
						{
							String strCur = this.getText( pos-1, pos );
							if ( "\r".equals(strCur) || "\n".equals(strCur) )
							{
								pos--;
							}
						}
						// -----------------------------------------------------------------
						if ( selectedItem.contains( this.sbOnTheWay ) )
						{
							this.insertText( pos, selectedItem.substring( this.sbOnTheWay.length() ) );
						}
						else if ( selectedItem.contains( this.sbOnTheWay.toString().toUpperCase() ) )
						{
							this.insertText( pos, selectedItem.substring( this.sbOnTheWay.length() ) );
						}
						else if ( selectedItem.contains( this.sbOnTheWay.toString().toLowerCase() ) )
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
				
				try
				{
					SQLParse  sqlParse = new SQLParse();
					sqlParse.setStrSQL( this.getSQL() );
					sqlParse.parse();
					
					this.tableLst = sqlParse.getTableLst();
					this.aliasMap = sqlParse.getAliasMap();
				}
				catch ( JSQLParserException parseExp )
				{
					//parseExp.printStackTrace();
				}
				
				this.extractLastWord();
				
				Boolean isVisibleComboHint = this.comboHint.visibleProperty().getValue();
				// ComboBox is invisible
				if ( isVisibleComboHint == false )
				{
					if ( ".".equals(chr) )
					{
						this.resetComboBox();
						
						if ( this.hints.size() > 0 )
						{
							Path caret = MyTool.findCaret(this);
							Point2D screenLoc = MyTool.findScreenLocation(caret,this);
							//System.out.println( "X:" + screenLoc.getX() + "/Y:" + screenLoc.getY() );
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
				}
				// ComboBox is visible
				else
				{
					if ( "\r".equals(chr) || "\n".equals(chr) )
					{
						String selectedItem = this.comboHint.selectionModelProperty().getValue().getSelectedItem();
						System.out.println( "selectedItem[" + selectedItem + "]sbOnTheWay[" + this.sbOnTheWay + "]" );						int pos = this.caretPositionProperty().getValue().intValue();
						if ( selectedItem.contains( this.sbOnTheWay ) )
						{
							this.insertText( pos, selectedItem.substring( this.sbOnTheWay.length() ) );
						}
						else if ( selectedItem.contains( this.sbOnTheWay.toString().toUpperCase() ) )
						{
							this.insertText( pos, selectedItem.substring( this.sbOnTheWay.length() ) );
						}
						else if ( selectedItem.contains( this.sbOnTheWay.toString().toLowerCase() ) )
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
								else if ( item.startsWith( this.sbOnTheWay.toString().toUpperCase() ) )
								{
									System.out.println( "startsWith:uppercase:true" );
									return true;
								}
								else if ( item.startsWith( this.sbOnTheWay.toString().toLowerCase() ) )
								{
									System.out.println( "startsWith:lowercase:true" );
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
		// On Linux, these don't work
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
				//event.consume();
			}
		);
		this.comboHint.addEventFilter
		(
			KeyEvent.KEY_PRESSED,
			(event)->
			{
				System.out.println( "--- ComboBox KeyPressed(EventFilter) -----------" );
				String selectedItem = this.comboHint.selectionModelProperty().getValue().getSelectedItem();
				System.out.println( "selectedItem:" + selectedItem );
				int pos = this.caretPositionProperty().getValue().intValue();
				this.insertText( pos, selectedItem );				
				this.comboHint.setVisible(false);
				this.comboHint.hide();
				//event.consume();
			}
		);
		this.comboHint.addEventHandler
		(
			KeyEvent.KEY_PRESSED,
			(event)->
			{
				System.out.println( "--- ComboBox KeyPressed(EventHandler) -----------" );
				String selectedItem = this.comboHint.selectionModelProperty().getValue().getSelectedItem();
				System.out.println( "selectedItem:" + selectedItem );
				int pos = this.caretPositionProperty().getValue().intValue();
				this.insertText( pos, selectedItem );				
				this.comboHint.setVisible(false);
				this.comboHint.hide();
				//event.consume();
			}
		);
	}
	
	public String getSQL()
	{
		String strSQL = this.getText();
		strSQL = strSQL.trim();
		if ( strSQL.length() < 1 )
		{
			return strSQL;
		}
		
		while ( strSQL.charAt(strSQL.length()-1) == ';' )
		{
			strSQL = strSQL.substring( 0, strSQL.length()-1 );
		}
		return strSQL;
	}

	private void extractLastWord()
	{
		String strBeforeCaret = this.getText().substring(0, this.getCaretPosition() );
		int lastIndex = MyTool.lastIndexOf( '\n', strBeforeCaret );
		String strThisLine = strBeforeCaret;
		if ( lastIndex != -1 )
		{
			strThisLine = this.getText().substring( lastIndex+1, this.getCaretPosition() );
		}
		// "select * from dual" => dual  
		//String strAfterSpace = strBeforeCaret.replaceAll( ".*\\s+(\\S+)", "$1" );
		String strAfterSpace = strThisLine.replaceAll( ".*\\s+(\\S+)", "$1" );
		System.out.println( "strAfterSpace[" + strAfterSpace + "]" );
		// "user_data.a" => "user_data"
		// "user_data "  => "user_data" 
		this.strLastWord = strAfterSpace.replaceAll("(\\s|\\..*$)", "" );
		System.out.println( "extractLastWord[" + this.strLastWord + "]" );
	}
	
	private boolean resetComboBox()
	{
		//this.comboHint.getItems().removeAll( this.hints );
		
		
		System.out.println( "resetComoboBox start." );
		String tableName = null;
		if ( this.aliasMap.containsKey(this.strLastWord) )
		{
			tableName = this.aliasMap.get(this.strLastWord);
		}
		else if ( this.tableLst.contains(this.strLastWord) )
		{
			tableName = this.strLastWord;
		}
		else
		{
			System.out.println( "no table/alias" );
			this.hints.removeAll( this.hints );
			return false;
		}
		System.out.println( "Search Table=>" + tableName );
		
		MyDBAbstract myDBAbs = this.dbView.getMyDBAbstract();
		SearchSchemaEntityInterface sseVisitorTN = new SearchSchemaEntityVisitorFactory().createInstance(SchemaEntity.SCHEMA_TYPE.TABLE, tableName);
		myDBAbs.getSchemaRoot().accept(sseVisitorTN);
		SchemaEntity hitEntity = sseVisitorTN.getHitSchemaEntity();
		if ( hitEntity == null )
		{
			System.out.println( "No hit." );
			this.hints.removeAll( this.hints );
			return false;
		}
		
		List<String> columnLst = hitEntity.getDefinitionLst("column_name");
		columnLst.forEach( item->System.out.println("column_name:" + item) );
		this.hints.removeAll( this.hints );
		this.hints.addAll( columnLst );
		
		return true;
	}
}
