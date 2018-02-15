package swing.text;

//import java.util.stream.Collectors;
//import java.util.List;
//import java.util.Arrays;

import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.scene.control.TextArea;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Label;
//import javafx.scene.layout.BorderPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
//import javafx.stage.Window;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.collections.transformation.FilteredList;

public class ComboOnTextArea extends Application
{
	private TextArea  taSQL    = new TextArea();
	
	private ComboBox<String>  comboHint = new ComboBox<>();
	
	private StringBuffer sbOnTheWay = new StringBuffer();
	
	private AnchorPane  ancPane = new AnchorPane();
	
	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
	
	/**/
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
		/**/
	
	/*
	private final static List<String>  hints = Arrays.asList
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
	
	public static void main( String...arg )
	{
		launch( arg );
	}	

	@Override
	public void start(Stage stage) throws Exception 
	{
		System.out.println( "line.separator[" + bytesToHex( System.getProperty("line.separator").getBytes() ) + "]" );
		
		taSQL.setText( "ABCDEFGHIJ\n1234567890" );
			
		//this.comboHint.getItems().addAll( hints );
		
		// https://stackoverflow.com/questions/19010619/javafx-filtered-combobox
		this.filteredItems = new FilteredList<String>( hints, p -> true);
		this.comboHint.setItems( this.filteredItems );
		
		setAction();
		
		this.ancPane.getChildren().add( this.taSQL );
		this.ancPane.getChildren().add( this.comboHint );
		AnchorPane.setTopAnchor( this.taSQL, 0.0 );
		AnchorPane.setBottomAnchor( this.taSQL, 0.0 );
		AnchorPane.setLeftAnchor( this.taSQL, 0.0 );
		AnchorPane.setRightAnchor( this.taSQL, 0.0 );
		this.comboHint.setVisible( false );
		
		
		SplitPane splitPane = new SplitPane();
		splitPane.orientationProperty().set( Orientation.VERTICAL );
		
		splitPane.getItems().addAll( this.ancPane, new Label( "Test" ) );
		splitPane.setDividerPositions( 0.9f, 0.1f );
		
		
		//Scene scene = new Scene( ancPane, 640, 480 );
		Scene scene = new Scene( splitPane, 640, 480 );
		stage.setScene(scene);
		
		stage.show();
	}
	
	private void setAction()
	{
		this.ancPane.widthProperty().addListener
		(
			(obs,oldVal,newVal)->
			{
				System.out.println( "width:" + newVal );
				/*
				AnchorPane.setLeftAnchor( this.taSQL, 0.0 );
				AnchorPane.setRightAnchor( this.taSQL, newVal.doubleValue() );
				*/
			}
		);

		this.ancPane.heightProperty().addListener
		(
			(obs,oldVal,newVal)->
			{
				System.out.println( "height:" + newVal );
				/*
				AnchorPane.setTopAnchor( this.taSQL, 0.0 );
				AnchorPane.setBottomAnchor( this.taSQL, newVal.doubleValue() );
				*/
			}
		);

		this.taSQL.setOnKeyPressed
		(
			(event)->
			{ 
				System.out.println( "--- TextArea KeyPressed -----------" );
				Boolean isVisibleComboHint = this.comboHint.visibleProperty().getValue();
				
				System.out.println( "CaretPositon:" + this.taSQL.getCaretPosition() );
				KeyCode keyCode = event.getCode();
				System.out.println( "KeyCode:" + keyCode );
				String chr = event.getCharacter();
				System.out.println( "Character:" + chr );
				if ( ".".equals( chr ) || KeyCode.PERIOD.equals( keyCode ) )
				{
					if ( isVisibleComboHint == Boolean.FALSE )
					{
						this.sbOnTheWay = null;
						this.sbOnTheWay = new StringBuffer();
						Path caret = findCaret(taSQL);
						// list back to full.
						this.filteredItems.setPredicate
						( 
							(item)->
							{
								return true;
							}
						);
						
						Point2D screenLoc = findScreenLocation(caret);
						System.out.println( "X:" + screenLoc.getX() + "/Y:" + screenLoc.getY() );
						AnchorPane.setLeftAnchor( this.comboHint, screenLoc.getX() );
						AnchorPane.setTopAnchor( this.comboHint, screenLoc.getY() );
						this.comboHint.getSelectionModel().selectFirst();
						this.comboHint.setVisible(true);
						this.comboHint.show();
					}
				}
			}
		);
		
		this.taSQL.setOnKeyTyped
		(
			(event)->
			{ 
				System.out.println( "--- TextArea KeyTyped -----------" );
				Boolean isVisibleComboHint = this.comboHint.visibleProperty().getValue();
				KeyCode keyCode = event.getCode();
				System.out.println( "KeyCode:" + keyCode );
				String chr = event.getCharacter();
				System.out.println( "Character[" + chr + "]" );
				System.out.println( "CharacterHex[" + bytesToHex( chr.getBytes() ) + "]" );
				/**/
				if ( System.getProperty("line.separator").equals(chr) ||
					"\r".equals(chr) ||
					"\n".equals(chr) ||	
					KeyCode.ENTER.equals( keyCode ) )
				{
					if ( isVisibleComboHint == Boolean.TRUE )
					{
						String selectedItem = this.comboHint.selectionModelProperty().getValue().getSelectedItem();
						System.out.println( "selectedItem:" + selectedItem );
						int pos = this.taSQL.caretPositionProperty().getValue().intValue();
						if ( selectedItem.contains( this.sbOnTheWay ) )
						{
							this.taSQL.insertText( pos, selectedItem.substring( this.sbOnTheWay.length() ) );
						}
						this.comboHint.setVisible(false);
						event.consume();
					}
				}
				else if ( ".".equals( chr ) )
				{
				}
				else
				{
					if ( isVisibleComboHint == Boolean.TRUE )
					{
						this.sbOnTheWay.append( chr );
						System.out.println( "sbOnTheWay:" + this.sbOnTheWay );
						/*
						List<String> hints2 = 
								hints.stream()
									.filter( str->str.startsWith( this.sbOnTheWay.toString() ) )
									.collect(Collectors.toList());
									*/
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
		
		this.taSQL.addEventFilter
		(
			MouseEvent.MOUSE_PRESSED , 
			(event)->
			{
				System.out.println( "--- TextArea MousePressed -----------" );
				this.comboHint.setVisible(false);
			}
		);
		
		this.comboHint.addEventFilter
		(
			KeyEvent.KEY_TYPED,
			(event)->
			{
				System.out.println( "--- ComboBox KeyTyped -----------" );
				KeyCode keyCode = event.getCode();
				System.out.println( "KeyCode:" + keyCode );
				String chr = event.getCharacter();
				System.out.println( "Character[" + chr + "]" );
				System.out.println( "CharacterHex[" + bytesToHex( chr.getBytes() ) + "]" );
				String selectedItem = this.comboHint.selectionModelProperty().getValue().getSelectedItem();
				System.out.println( "selectedItem:" + selectedItem );
				int pos = this.taSQL.caretPositionProperty().getValue().intValue();
				this.taSQL.insertText( pos, selectedItem );				
				this.comboHint.setVisible(false);
				event.consume();
			}
		);
	}

	  private Path findCaret(Parent parent) {
		    // Warning: this is an ENORMOUS HACK
	    for (Node n : parent.getChildrenUnmodifiable()) {
	      if (n instanceof Path) {
	        return (Path) n;
	      } else if (n instanceof Parent) {
	        Path p = findCaret((Parent) n);
	        if (p != null) {
	          return p;
	        }
	      }
	    }
	    return null;
	  }

	  private Point2D findScreenLocation(Node node) {
	    double x = 0;
	    double y = 0;
	    for (Node n = node; n != null; n=n.getParent()) {
	      Bounds parentBounds = n.getBoundsInParent();
	      x += parentBounds.getMinX();
	      y += parentBounds.getMinY();
	    }
	    Scene scene = node.getScene();
	    x += scene.getX();
	    y += scene.getY();
	    /*
	    Window window = scene.getWindow();
	    x += window.getX();
	    y += window.getY();
	    */
	    Point2D screenLoc = new Point2D(x, y);
	    return screenLoc;
	  }
	
	  public static String bytesToHex(byte[] bytes) {
		    char[] hexChars = new char[bytes.length * 2];
		    for ( int j = 0; j < bytes.length; j++ ) {
		        int v = bytes[j] & 0xFF;
		        hexChars[j * 2] = hexArray[v >>> 4];
		        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		    }
		    return new String(hexChars);
		}
}
