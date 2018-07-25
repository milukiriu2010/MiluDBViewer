package milu.gui.ctrl.query;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.ContextMenu;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.Event;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Path;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.beans.property.ObjectProperty;


import net.sf.jsqlparser.JSQLParserException;

import org.hibernate.engine.jdbc.internal.BasicFormatterImpl;

import milu.tool.MyGUITool;
import milu.tool.MyStringTool;
import milu.entity.schema.SchemaEntity;
import milu.entity.schema.search.SearchSchemaEntityInterface;
import milu.entity.schema.search.SearchSchemaEntityVisitorFactory;
import milu.ctrl.sql.parse.SQLBag;
import milu.ctrl.sql.parse.SQLParse;
import milu.db.MyDBAbstract;
import milu.gui.ctrl.common.inf.ChangeLangInterface;
import milu.gui.view.DBView;

public class SQLTextArea extends TextArea
	implements 
		ChangeLangInterface,
		SQLFormatInterface
{
	private DBView  dbView = null;
	
	private ComboBox<String>  comboHint = new ComboBox<>();
	
	private final ObservableList<String>  hints = FXCollections.observableArrayList();
	
	private FilteredList<String>  filteredItems = null;
	
	private StringBuffer  sbOnTheWay = new StringBuffer();
	
	// Parent pane of this class
	private AnchorPane    parentPane = null;
	
	// Table List, analyzed by jsqlparser
	private List<String>  tableLst = new ArrayList<>();
	
	// Alias <=> Table, analyzed by jsqlparser
	private Map<String,String>  aliasMap = new HashMap<>();
	
	// caret position
	private Label lblCaretPos = new Label("*");
	
	// word after last " (space)"
	// -----------------------------------------------------------
	// "select * from information_schema"          => "information_schema"
	// "select * from information_schema.schemata" => "information_schema.schemata"
	private String  strLastWord = "";
	
	public SQLTextArea( DBView dbView )
	{
		super();
		
		this.dbView = dbView;
	}
	
	/**
	 *  Call after this class added on AnchorPane
	 */
	public void init()
	{
		this.parentPane = (AnchorPane)MyGUITool.searchParentNode( this, AnchorPane.class );
		if ( this.parentPane != null )
		{
			// add "Hint ComboBox" on TextArea
			this.parentPane.getChildren().add( this.comboHint );
			this.comboHint.setVisible( false );
			
			// https://stackoverflow.com/questions/19010619/javafx-filtered-combobox
			this.filteredItems = new FilteredList<String>( this.hints, p -> true);
			this.comboHint.setItems( this.filteredItems );
		}
		
		if ( this.parentPane != null )
		{
			this.lblCaretPos.getStyleClass().add("SqlTextArea_LabelCaretPos");
			
			// add "Caret Position Label" on TextArea
			this.parentPane.getChildren().add( this.lblCaretPos );
			AnchorPane.setTopAnchor( this.lblCaretPos, 0.0 );
			AnchorPane.setRightAnchor( this.lblCaretPos, 0.0 );
			
			// label for caret position
			this.caretPositionProperty().addListener
			(
				(obs,oldVal,newVal)->
				{
					if ( newVal == null )
					{
						return;
					}
					this.lblCaretPos.textProperty().bind
					(
						Bindings.convert
						(
							new SimpleIntegerProperty( newVal.intValue() )
						)
					);
					// shift label position
					// -------------------------------------------------------
					// vertical scrollbar invisible => margin 0
					// vertical scrollbar visible   => margin scrollbar width
					this.shiftLabelCaretPosition();
				}
			);
		}
		
		this.setMouseAction();
		
		if ( this.parentPane != null )
		{
			this.setKeyAction();
			
			this.setMenu();
		}
		
		this.setAction();
	}
	
	// https://stackoverflow.com/questions/32980159/javafx-append-to-right-click-menu-for-textfield?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
	private void setMenu()
	{
		/*
		TextAreaSkin textAreaSkin = new TextAreaSkin(this)
		{
			@Override
			public void populateContextMenu(ContextMenu contextMenu) {
			}
		};
		*/
		/*
		ContextMenu contextMenu = new ContextMenu();
		this.menuItemFmtSQL.setOnAction
		(
			(event)->
			{
				String formatted = new BasicFormatterImpl().format( this.getText() );
				this.setText(formatted);
			}
		);
		
		contextMenu.getItems().addAll( this.menuItemFmtSQL );
		this.setContextMenu(contextMenu);
		*/
		System.out.println( "TextArea ContextMenu1:" + this.getContextMenu() );
		ObjectProperty<ContextMenu> contextMenProperty = this.contextMenuProperty();
		System.out.println( "TextArea ContextMenu2:" + contextMenProperty );
	}
	
	// shift label position
	// -------------------------------------------------------
	// vertical scrollbar invisible => margin 0
	// vertical scrollbar visible   => margin scrollbar width
	private void shiftLabelCaretPosition()
	{
		ScrollBar scrollBarVertical = MyGUITool.getScrollBarVertical(this);
		if ( scrollBarVertical != null )
		{
			System.out.println( "SqlTextArea:ScrollBar Found." );
			AnchorPane.setRightAnchor( this.lblCaretPos, scrollBarVertical.getWidth() );
			// "visibleProperty" doesn't work on some PC.
			scrollBarVertical.visibleProperty().addListener
			(
				(obs2,oldVal2,newVal2)->
				{
					if ( newVal2 == true )
					{
						System.out.println( "SqlTextArea:ScrollBar Found - visible." );
						AnchorPane.setRightAnchor( this.lblCaretPos, scrollBarVertical.getWidth() );
					}
					else
					{
						System.out.println( "SqlTextArea:ScrollBar Found - invisible." );
						AnchorPane.setRightAnchor( this.lblCaretPos, 0.0 );
					}
				}
			);
		}
		else
		{
			System.out.println( "SqlTextArea:ScrollBar not Found." );
		}
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
		
		// close ComboBox, when mouse clicked
		this.addEventFilter
		(
			MouseEvent.MOUSE_PRESSED , 
			(event)->
			{
				System.out.println( "--- TextArea MousePressed -----------" );
				this.comboHint.setVisible(false);
				this.comboHint.hide();
				this.sbOnTheWay.setLength(0);;				
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
						this.sbOnTheWay.setLength(0);			
					}
					else if ( KeyCode.ESCAPE.equals( keyCode ) ) 
					{
						this.comboHint.setVisible(false);
						this.comboHint.hide();
						this.sbOnTheWay.setLength(0);			
					}
				}
			}
		);
		
		this.setOnKeyTyped
		(
			(event)->
			{
				System.out.println( "--- TextArea KeyTyped -------------" );
				
				String chr = event.getCharacter();
				System.out.println( "Character:" + chr );
				System.out.println( "CharacterHex[" + MyStringTool.bytesToHex( chr.getBytes() ) + "]" );
				
				try
				{
					SQLParse  sqlParse = new SQLParse();
					sqlParse.setStrSQL( this.getSQL() );
					sqlParse.parse();
					
					this.tableLst  = sqlParse.getTableLst();
					this.aliasMap  = sqlParse.getAliasMap();
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
					// Key Typed PERIOD
					if ( ".".equals(chr) )
					{
						this.resetComboBox();
						
						if ( this.hints.size() > 0 )
						{
							Path caret = MyGUITool.findCaret(this);
							Point2D screenLoc = MyGUITool.findScreenLocation(caret,this);
							//System.out.println( "X:" + screenLoc.getX() + "/Y:" + screenLoc.getY() );
							AnchorPane.setLeftAnchor( this.comboHint, screenLoc.getX() );
							AnchorPane.setTopAnchor( this.comboHint, screenLoc.getY() );
							
							// list back to full.
							this.filteredItems.setPredicate( (item)->{ return true;	} );
							
							this.comboHint.getSelectionModel().selectFirst();
							this.comboHint.setVisible(true);
							this.comboHint.show();
							
							this.sbOnTheWay.setLength(0);				
						}
					}
				}
				// ComboBox is visible
				else
				{
					// Key Typed ENTER
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
						this.comboHint.hide();
						this.sbOnTheWay.setLength(0);			
					}
					// Key Typed except "ENTER"
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
							this.comboHint.setVisible(false);
							this.comboHint.hide();
							this.sbOnTheWay.setLength(0);
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
		String strSQL = this.getSelectedText();
		if ( strSQL.length() == 0 )
		{
			strSQL = this.getText();
		}
		strSQL = strSQL.trim();
		if ( strSQL.length() < 1 )
		{
			return strSQL;
		}
		
		// cut the last ";"
		while ( strSQL.charAt(strSQL.length()-1) == ';' )
		{
			strSQL = strSQL.substring( 0, strSQL.length()-1 );
		}
		return strSQL;
	}

	private void extractLastWord()
	{
		String strBeforeCaret = this.getText().substring( 0, this.getCaretPosition() );
		int lastIndex = MyStringTool.lastIndexOf( '\n', strBeforeCaret );
		String strThisLine = strBeforeCaret;
		// Cut string before "return code(\n)"
		if ( lastIndex != -1 )
		{
			strThisLine = this.getText().substring( lastIndex+1, this.getCaretPosition() );
		}
		// "select * from dual" => dual  
		String strAfterSpace = strThisLine.replaceAll( ".*\\s+(\\S+)", "$1" );
		System.out.println( "strAfterSpace[" + strAfterSpace + "]" );
		// "user_data.a" => "user_data"
		// "user_data "  => "user_data"
		//this.strLastWord = strAfterSpace.replaceAll("(\\s|\\..*$)", "" );
		
		// "user_data "  => "user_data"
		this.strLastWord = strAfterSpace.replaceAll("\\s+$", "" );
		System.out.println( "extractLastWord[" + this.strLastWord + "]" );
	}
	
	private boolean resetComboBox()
	{
		System.out.println( "resetComboBox start." );
		
		// "information_schema.tables" 
		//   0 => "information_schema"
		//   1 => "tables"
		String[] splitLastWord = this.strLastWord.split("\\.");
		String tableNameComp = null;
		if ( splitLastWord.length == 0 )
		{
			System.out.println( "no word" );
			this.hints.removeAll( this.hints );
			return false;
		}
		else if ( splitLastWord.length >= 1 )
		{
			tableNameComp = splitLastWord[splitLastWord.length-1];
		}
		
		String tableName = null;
		if ( this.aliasMap.containsKey(tableNameComp) )
		{
			tableName = this.aliasMap.get(tableNameComp);
		}
		else if ( this.tableLst.contains(tableNameComp) )
		{
			tableName = tableNameComp;
		}
		else
		{
			System.out.println( "no table/alias" );
			this.hints.removeAll( this.hints );
			return false;
		}
		System.out.println( "Search Table=>" + tableName );
		
		MyDBAbstract myDBAbs = this.dbView.getMyDBAbstract();
		//SearchSchemaEntityInterface sseVisitorTN = new SearchSchemaEntityVisitorFactory().createInstance(SchemaEntity.SCHEMA_TYPE.TABLE, tableName);
		List<SchemaEntity.SCHEMA_TYPE> schemaTypeLst = 
			Arrays.asList
			( 
				SchemaEntity.SCHEMA_TYPE.TABLE,
				SchemaEntity.SCHEMA_TYPE.VIEW,
				SchemaEntity.SCHEMA_TYPE.SYSTEM_VIEW,
				SchemaEntity.SCHEMA_TYPE.MATERIALIZED_VIEW
			);
		SearchSchemaEntityInterface sseVisitorTN = new SearchSchemaEntityVisitorFactory().createInstance( schemaTypeLst, tableName );
		myDBAbs.getSchemaRoot().accept(sseVisitorTN);
		SchemaEntity hitEntity = sseVisitorTN.getHitSchemaEntity();
		if ( hitEntity == null )
		{
			System.out.println( "No hit." );
			// search by schema
			if ( this.searchSchema(tableName) == false )
			{
				this.hints.removeAll( this.hints );
				return false;
			}
			else
			{
				return true;
			}
		}
		
		List<String> columnLst = hitEntity.getDefinitionLst("column_name");
		columnLst.forEach( item->System.out.println("column_name:" + item) );
		this.hints.removeAll( this.hints );
		this.hints.addAll( columnLst );
		
		return true;
	}
	
	private boolean searchSchema( String schemaName )
	{
		System.out.println( "searchSchema." );
		MyDBAbstract myDBAbs = this.dbView.getMyDBAbstract();
		SearchSchemaEntityInterface sseVisitorTN1 = new SearchSchemaEntityVisitorFactory().createInstance( SchemaEntity.SCHEMA_TYPE.SCHEMA, schemaName );
		myDBAbs.getSchemaRoot().accept(sseVisitorTN1);
		SchemaEntity schemaEntity = sseVisitorTN1.getHitSchemaEntity();
		if ( schemaEntity == null )
		{
			return false;
		}
		
		SearchSchemaEntityInterface sseVisitorTN2 = new SearchSchemaEntityVisitorFactory().createInstance( SchemaEntity.SCHEMA_TYPE.ROOT_TABLE );
		schemaEntity.accept(sseVisitorTN2);
		SchemaEntity rootTableEntity = sseVisitorTN2.getHitSchemaEntity();
		if ( rootTableEntity == null )
		{
			return false;
		}
		
		List<String> tableLst = 
			rootTableEntity.getEntityLst().stream()
				.map( x -> x.getName() )
				.collect(Collectors.toList());
		if ( tableLst.size() == 0 )
		{
			return false;
		}
		this.hints.removeAll( this.hints );
		this.hints.addAll( tableLst );
		
		return true;
		
	}
	
	public List<SQLBag> getSQLBagLst()
	{
		SQLParse sqlParse = new SQLParse();
		try
		{
			//String sqlStr = this.getText();
			String sqlStr = this.getSelectedText();
			if ( sqlStr.length() == 0 )
			{
				sqlStr = this.getText();
			}
			sqlParse.setStrSQL(sqlStr);
			sqlParse.parseStatements();
		}
		catch ( JSQLParserException jsqlEx )
		{
		}
		List<SQLBag> sqlBagLst = sqlParse.getSQLBagLst();
		if ( sqlBagLst.size() == 0 )
		{
			String strSQL = this.getSQL();
			SQLBag sqlBag = new SQLBag();
			sqlBag.setSQL(strSQL);
			sqlBag.setCommand(SQLBag.COMMAND.QUERY);
			sqlBag.setType(SQLBag.TYPE.SELECT);
			sqlBagLst.add(sqlBag);
		}
		
		return sqlBagLst;
	}
	
	public List<SQLBag> getSQLBagLst( SQLBag.COMMAND cmdType )
	{
		List<SQLBag> sqlBagLst = new ArrayList<>();
		String strSQL = this.getSQL();
		SQLBag sqlBag = new SQLBag();
		sqlBag.setSQL(strSQL);
		if ( cmdType == SQLBag.COMMAND.QUERY )
		{
			sqlBag.setCommand(SQLBag.COMMAND.QUERY);
			sqlBag.setType(SQLBag.TYPE.SELECT);
		}
		else if ( cmdType == SQLBag.COMMAND.TRANSACTION )
		{
			sqlBag.setCommand(SQLBag.COMMAND.TRANSACTION);
			sqlBag.setType(SQLBag.TYPE.UNKNOWN_TRANSACTION);
		}
		sqlBagLst.add(sqlBag);
		return sqlBagLst;
	}
	
	/**************************************************
	 * Override from ChangeLangInterface
	 ************************************************** 
	 */
	@Override	
	public void changeLang()
	{
	}
	
	
	// SQLFormatInterface
	@Override
	public void formatSQL( Event event )
	{
		if (event instanceof KeyEvent)
		{
			KeyEvent keyEvent = (KeyEvent)event;
			System.out.println( "formatSQL KeyEvent:" + keyEvent.getCode() );
			if ( keyEvent.isAltDown() )
			{
				System.out.println( "formatSQL KeyEvent => Alt is down." );
				String formatted = new BasicFormatterImpl().format( this.getText() );
				this.setText(formatted);
			}
			return;
		}
		else
		{
			System.out.println( "formatSQL event:" + event.getEventType() );
		}
		
		String lineSP = System.getProperty("line.separator");
		String strSelected = this.getSelectedText();
		
		// ------------------------------------------------
		// when some words are selected. 
		// ------------------------------------------------
		if ( strSelected.length() > 0 )
		{
			int caretPos  = this.getCaretPosition();
			int anchorPos = this.getAnchor();
			
			String strFormatted = new BasicFormatterImpl().format( strSelected );
			String strFirst  = null;
			String strSecond = null;
			if ( caretPos <= anchorPos )
			{
				strFirst  = this.getText().substring( 0, caretPos );
				strSecond = this.getText().substring( anchorPos, this.getText().length() );
			}
			else
			{
				strFirst  = this.getText().substring( 0, anchorPos );
				strSecond = this.getText().substring( caretPos, this.getText().length() );
			}
			
			String strCutFirst4Space = MyStringTool.replaceMultiLine(strFormatted, "^\\s{4}(.*)", "$1");
			String strCutSpace = strCutFirst4Space.replaceAll( "^\\s+", "" );
			
			this.setText( strFirst + strCutSpace + strSecond );
		}
		// ------------------------------------------------
		// when any words are not selected. 
		// ------------------------------------------------
		else
		{
			// -----------------------------------------------
			// select * from A;
			// select * from B;
			// -----------------------------------------------
			// =>
			// -----------------------------------------------
			// 
			//     select 
			//         * 
			//     from 
			//         A; select 
			//             * 
			//         from 
			//             B;
			// -----------------------------------------------
			String strFormatted = new BasicFormatterImpl().format( this.getText() );
			String strCutFirst4Space = MyStringTool.replaceMultiLine(strFormatted, "^\\s{4}(.*)", "$1").trim();
			String strSemiColon2LineSP = strCutFirst4Space.replaceAll(";",";"+lineSP);
			this.setText( strSemiColon2LineSP );
		}
	}

	// SQLFormatInterface
	@Override
	public void oneLineSQL( Event event )
	{
		String strSelected = this.getSelectedText();
		
		// ------------------------------------------------
		// when some words are selected. 
		// ------------------------------------------------
		if ( strSelected.length() > 0 )
		{
			int caretPos  = this.getCaretPosition();
			int anchorPos = this.getAnchor();
			
			String strFirst  = null;
			String strSecond = null;
			if ( caretPos <= anchorPos )
			{
				strFirst  = this.getText().substring( 0, caretPos );
				strSecond = this.getText().substring( anchorPos, this.getText().length() );
			}
			else
			{
				strFirst  = this.getText().substring( 0, anchorPos );
				strSecond = this.getText().substring( caretPos, this.getText().length() );
			}
			
			String strCutSpace = strSelected.replaceAll( "\\s+", " " );
			
			this.setText( strFirst + strCutSpace + strSecond );
		}
		// ------------------------------------------------
		// when any words are not selected. 
		// ------------------------------------------------
		else
		{
			// -----------------------------------------------
			// 
			//     select 
			//         * 
			//     from 
			//         A; 
			//     select 
			//         * 
			//     from 
			//         B;
			// -----------------------------------------------
			// =>
			// -----------------------------------------------
			// select * from A;
			// select * from B;
			// -----------------------------------------------
			this.setText( this.getText().replaceAll( "\\s+", " " ) );
		}
	}
}
