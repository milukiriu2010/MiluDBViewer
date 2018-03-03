package fx.drag;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Path;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.LineTo;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.geometry.Bounds;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.binding.DoubleBinding;

public class TextOnRectangle2  extends Application 
{
	
	double orgSceneX, orgSceneY;

    public static void main(String[] args) 
    {
        Application.launch(args);
    }
    
	public static int getCharCount( String strSrc, String strChk )
	{
		if ( strSrc == null || strChk == null )
		{
			return 0;
		}
		else
		{
			return strSrc.length() - strSrc.replace( strChk, "" ).length();
		}
	}
	
	private Rectangle createRectangle( Text text )
    {
    	Rectangle rect = new Rectangle();
    	DoubleProperty x = text.xProperty();
    	DoubleProperty y = text.yProperty();
    	Bounds textBounds = text.getBoundsInLocal();
    	double wd = textBounds.getWidth();
    	double hd = textBounds.getHeight();
    	DoubleProperty w = new SimpleDoubleProperty( wd );
    	DoubleProperty h = new SimpleDoubleProperty( hd );
    	
    	int rtnCnt = getCharCount( text.getText(), "\n" );
    	
    	double margin = 5.0;
    	rect.xProperty().bind( x.subtract( margin ) );
		rect.yProperty().bind( y.subtract( h.doubleValue()/(1+rtnCnt) ) );
    	rect.widthProperty().bind( w.add( margin*2 ) );
    	rect.heightProperty().bind( h.add( margin*2 ) );
    	
    	rect.setFill( Color.WHITE );
    	rect.setStroke( Color.BLACK );
    	
    	return rect;
    }
    
    private Text createText( double x, double y, String str )
    {
    	Text text = new Text( x, y, str );
    	text.setCursor(Cursor.HAND);
    	
    	text.setOnMousePressed
    	( 
    		(event)->
    		{
    			orgSceneX = event.getSceneX();
    			orgSceneY = event.getSceneY();
    			
    			Text t = (Text)(event.getSource());
    			t.toFront();
    		}
    	);
    	
    	text.setOnMouseDragged
    	(
    		(event)->
    		{
    			double deltaX = event.getSceneX() - orgSceneX;
    			double deltaY = event.getSceneY() - orgSceneY;
    			
    			Text t = (Text)(event.getSource());
    			t.setX( t.getX() + deltaX );
    			t.setY( t.getY() + deltaY );
    	    	
    	    	orgSceneX = event.getSceneX();
    	    	orgSceneY = event.getSceneY();
    		}
    	);
    	
    	return text;
    }
    
    void connect( Text t1, Text t2, Group parent )
    {
    	DoubleProperty x1 = t1.xProperty();
    	DoubleProperty y1 = t1.yProperty();
    	Bounds         b1 = t1.getBoundsInLocal();
    	DoubleProperty w1 = new SimpleDoubleProperty( b1.getWidth() );
    	DoubleProperty h1 = new SimpleDoubleProperty( b1.getHeight() );
    	
    	DoubleProperty x2 = t2.xProperty();
    	DoubleProperty y2 = t2.yProperty();
    	Bounds         b2 = t2.getBoundsInLocal();
    	DoubleProperty w2 = new SimpleDoubleProperty( b2.getWidth() );
    	DoubleProperty h2 = new SimpleDoubleProperty( b2.getHeight() );
    	
    	DoubleBinding  x1b = x1.add(w1.divide(2));
    	DoubleBinding  y1b = y1.subtract(h1.divide(2));
    	
    	DoubleBinding  x2b = x2.add(w2.divide(2));
    	DoubleBinding  y2b = y2.subtract(h2.divide(2));
    	
    	MoveTo mt = new MoveTo();
    	mt.xProperty().bind( x1b );
    	mt.yProperty().bind( y1b );
    	
    	LineTo lt1 = new LineTo();
    	lt1.xProperty().bind( x2b );
    	lt1.yProperty().bind( y1b );
    
    	
    	LineTo lt2 = new LineTo();
    	lt2.xProperty().bind( x2b );
    	lt2.yProperty().bind( y2b );
    	
    	Path path = new Path();
    	path.getElements().addAll( mt, lt1, lt2 );
    	parent.getChildren().add( path );
    	path.getStrokeDashArray().addAll( 2.0, 4.0 );
    	path.toBack();
    }
    
    

	@Override
	public void start(Stage primaryStage) 
	{
		Group root = new Group();
		Scene scene = new Scene( root, 640, 480 );
		
		Text       text1 = createText( 300, 200, "CENTER" );
		Rectangle  rect1 = createRectangle( text1 );
		Text       text2 = createText( 300, 100, "NORTH" );
		Rectangle  rect2 = createRectangle( text2 );
		Text       text3 = createText( 200, 200, "WEST" );
		Rectangle  rect3 = createRectangle( text3 );
		Text       text4 = createText( 300, 300, "SOUTH" );
		Rectangle  rect4 = createRectangle( text4 );
		Text       text5 = createText( 400, 200, "EAST" );
		Rectangle  rect5 = createRectangle( text5 );
		Text       textNW = createText( 200, 100, "NORTH\nWEST" );
		Rectangle  rectNW = createRectangle( textNW );
		
		root.getChildren().add( text1 );
		root.getChildren().add( rect1 );
		root.getChildren().add( text2 );
		root.getChildren().add( rect2 );
		root.getChildren().add( text3 );
		root.getChildren().add( rect3 );
		root.getChildren().add( text4 );
		root.getChildren().add( rect4 );
		root.getChildren().add( text5 );
		root.getChildren().add( rect5 );
		root.getChildren().add( textNW );
		root.getChildren().add( rectNW );
		text1.toFront();
		text2.toFront();
		text3.toFront();
		text4.toFront();
		text5.toFront();
		textNW.toFront();
		
		connect( text1, text2, root );
		connect( text1, text3, root );
		connect( text1, text4, root );
		connect( text1, text5, root );
		connect( text4, text5, root );
		connect( text1, textNW, root );
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
