package fx.drag;

import java.util.List;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;

public class LabelTableDiagram1 extends Application 
{
	private Point2D  srcP = null;

	@Override
	public void start(Stage stage) throws Exception 
	{
		Group root = new Group();
		Scene scene = new Scene( root, 640, 480 );
        scene.getStylesheets().add(getClass().getResource("/resources/label/copyable-text.css").toExternalForm());
		
        //////////////////////////////////////////////////////////////////
        
		List<String>  columnLst1 = new ArrayList<>();
		columnLst1.add("id");
		columnLst1.add("english");
		columnLst1.add("japanese");
		columnLst1.add("chinese");
		
		LabelTable lt1 = new LabelTable( "m_country", columnLst1 );
		root.getChildren().add( lt1 );
		lt1.setLayoutX(100);
		lt1.setLayoutY(100);
		
		this.setAction(lt1);
		
        //////////////////////////////////////////////////////////////////
        
		List<String>  columnLst2 = new ArrayList<>();
		columnLst2.add("id");
		columnLst2.add("head_name");
		columnLst2.add("tail_name");
		columnLst2.add("league");
		
		LabelTable lt2 = new LabelTable( "m_npb_team_list", columnLst2 );
		root.getChildren().add( lt2 );
		lt2.setLayoutX(200);
		lt2.setLayoutY(200);
		
		this.setAction(lt2);
		
        //////////////////////////////////////////////////////////////////
        
		List<String>  columnLst3 = new ArrayList<>();
		columnLst3.add("id");
		columnLst3.add("team_id");
		columnLst3.add("sei");
		columnLst3.add("mei");
		columnLst3.add("country_id");
		
		LabelTable lt3 = new LabelTable( "t_npb_player_list", columnLst3 );
		root.getChildren().add( lt3 );
		lt3.setLayoutX(100);
		lt3.setLayoutY(300);
		
		this.setAction(lt3);
		
        //////////////////////////////////////////////////////////////////
        
		List<String>  columnLst4 = new ArrayList<>();
		columnLst4.add("id");
		columnLst4.add("tyear");
		columnLst4.add("game");
		columnLst4.add("run");
		columnLst4.add("hit");
		
		LabelTable lt4 = new LabelTable( "t_npb_hit_stats", columnLst4 );
		root.getChildren().add( lt4 );
		lt4.setLayoutX(200);
		lt4.setLayoutY(300);
		
		this.setAction(lt4);
		
		stage.setScene(scene);
		stage.show();
		
		this.connect( lt1, lt3, root );
		this.connect( lt2, lt3, root );
		this.connect( lt3, lt4, root );
		
	}
	
	private void setAction( LabelTable lt )
	{
		lt.setCursor( Cursor.HAND );
		
		lt.setOnMousePressed
		(
			(event)->
			{
				this.srcP = new Point2D( event.getSceneX(), event.getSceneY() );
				
				LabelTable obj = (LabelTable)event.getSource();
				obj.toFront();
			}
		);
		
		lt.setOnMouseDragged
		(
			(event)->
			{
				double deltaX = event.getSceneX() - srcP.getX();
				double deltaY = event.getSceneY() - srcP.getY();
				
				LabelTable obj = (LabelTable)event.getSource();
				obj.setLayoutX( obj.getLayoutX() + deltaX );
				obj.setLayoutY( obj.getLayoutY() + deltaY );
				
				this.srcP = this.srcP.add( deltaX, deltaY );
			}
		);
	}
	
	private void connect( LabelTable t1, LabelTable t2, Group parent )
	{
    	DoubleProperty x1 = t1.layoutXProperty();
    	DoubleProperty y1 = t1.layoutYProperty();
    	Bounds         b1l = t1.getBoundsInLocal();
    	Bounds         b1p = t1.getBoundsInParent();
    	Bounds         b1o = t1.getLayoutBounds();
    	//Bounds         b1 = t1.getTableBoundsInLocal();
    	Point2D        p1 = t1.getTablePoint2D();
    	DoubleProperty w1 = new SimpleDoubleProperty( b1l.getWidth() );
    	DoubleProperty h1 = new SimpleDoubleProperty( b1l.getHeight() );
    	System.out.println( String.format( "b1l:x=%3.3f/y=%3.3f", b1l.getWidth(), b1l.getHeight() ) );
    	System.out.println( String.format( "b1p:x=%3.3f/y=%3.3f", b1p.getWidth(), b1p.getHeight() ) );
    	System.out.println( String.format( "b1o:x=%3.3f/y=%3.3f", b1o.getWidth(), b1o.getHeight() ) );
    	System.out.println( String.format( "p1 :x=%3.3f/y=%3.3f", p1.getX()     , p1.getY() ) );
    	
    	DoubleProperty x2 = t2.layoutXProperty();
    	DoubleProperty y2 = t2.layoutYProperty();
    	//Bounds         b2 = t2.getBoundsInLocal();
    	//Bounds         b2 = t2.getBoundsInParent();
    	Bounds         b2 = t2.getLayoutBounds();
    	DoubleProperty w2 = new SimpleDoubleProperty( b2.getWidth() );
    	DoubleProperty h2 = new SimpleDoubleProperty( b2.getHeight() );
    	
    	DoubleBinding  x1b = x1.add(w1.divide(2));
    	DoubleBinding  y1b = y1.add(h1.divide(2));
    	
    	DoubleBinding  x2b = x2.add(w2.divide(2));
    	DoubleBinding  y2b = y2.add(h2.divide(2));
    	
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

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		launch( args );
	}

}
