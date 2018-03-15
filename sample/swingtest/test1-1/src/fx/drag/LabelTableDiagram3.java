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
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class LabelTableDiagram3 extends Application 
{
	private Point2D  srcP = null;

	@Override
	public void start(Stage stage) throws Exception 
	{
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setPrefSize(640, 480);
		AnchorPane root = new AnchorPane();
		//Group root = new Group();
		scrollPane.setContent( root );
		scrollPane.setPannable(true);
		scrollPane.setFitToHeight(true);
		Scene scene = new Scene( scrollPane, 640, 480 );
		//Scene scene = new Scene( root, 640, 480 );
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
		lt4.setLayoutX(300);
		lt4.setLayoutY(300);
		
		this.setAction(lt4);
		
		stage.setScene(scene);
		stage.show();
		
		this.connect( lt1, "id", lt3, "country_id", root );
		this.connect( lt2, "id", lt3, "team_id", root );
		this.connect( lt3, "id", lt4, "id", root );
		
		//this.getPosition( lt1 );
		//this.getPosition( lt2 );
		
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
	
	private void connect( LabelTable t1, String tlink1, LabelTable t2, String tlink2, Pane parent )
	{
		// t1
    	DoubleProperty x1 = t1.layoutXProperty();
    	DoubleProperty y1 = t1.layoutYProperty();
    	Bounds         b1 = t1.getBoundsInLocal();
    	DoubleProperty w1 = new SimpleDoubleProperty( b1.getWidth() );
    	DoubleProperty h1 = new SimpleDoubleProperty( b1.getHeight() );
    	
    	// tlink1
    	Label          lbl1 = t1.getColumnLabel( tlink1 );
    	DoubleProperty xl1  = lbl1.layoutXProperty();
    	DoubleProperty yl1  = lbl1.layoutYProperty();
    	Bounds         bl1  = lbl1.getBoundsInParent();
    	DoubleProperty wl1  = new SimpleDoubleProperty( bl1.getWidth() );
    	DoubleProperty hl1  = new SimpleDoubleProperty( bl1.getHeight() );
    	
    	System.out.println( String.format( "    t1:x=%3.3f/y=%3.3f",  x1.getValue(),  y1.getValue() ) );
    	System.out.println( String.format( "tlink1:x=%3.3f/y=%3.3f", xl1.getValue(), yl1.getValue() ) );
    	
    	// t2
    	DoubleProperty x2 = t2.layoutXProperty();
    	DoubleProperty y2 = t2.layoutYProperty();
    	Bounds         b2 = t2.getBoundsInLocal();
    	DoubleProperty w2 = new SimpleDoubleProperty( b2.getWidth() );
    	DoubleProperty h2 = new SimpleDoubleProperty( b2.getHeight() );
    	
    	// tlink2
    	Label          lbl2 = t2.getColumnLabel( tlink2 );
    	DoubleProperty xl2  = lbl2.layoutXProperty();
    	DoubleProperty yl2  = lbl2.layoutYProperty();
    	Bounds         bl2  = lbl2.getBoundsInParent();
    	DoubleProperty wl2  = new SimpleDoubleProperty( bl2.getWidth() );
    	DoubleProperty hl2  = new SimpleDoubleProperty( bl2.getHeight() );
    	
    	DoubleBinding  x1b = x1.add(w1.divide(2)).add(xl1.doubleValue());
    	DoubleBinding  y1b = y1.add(yl1.doubleValue()).add(hl1.divide(2));
    	
    	DoubleBinding  x2b = x2.add(w2.divide(2)).add(xl2.doubleValue());
    	DoubleBinding  y2b = y2.add(yl2.doubleValue()).add(hl2.divide(2));
    	
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
    	
    	// mouse enter/exit
    	lbl1.setOnMouseEntered
    	( 
    		(event)->
    		{ 
    			lbl1.getStyleClass().add("column_y");
    			lbl2.getStyleClass().add("column_y");
    		} 
    	);
    	lbl1.setOnMouseExited
    	(  
    		(event)->
    		{ 
    			lbl1.getStyleClass().remove("column_y"); 
    			lbl2.getStyleClass().remove("column_y"); 
    		} 
    	);

	}
	
	private void getPosition( LabelTable t1 )
	{
    	DoubleProperty x1 = t1.layoutXProperty();
    	DoubleProperty y1 = t1.layoutYProperty();
    	Bounds         b1 = t1.getBoundsInLocal();
    	DoubleProperty w1 = new SimpleDoubleProperty( b1.getWidth() );
    	DoubleProperty h1 = new SimpleDoubleProperty( b1.getHeight() );
    	
    	System.out.println( String.format( "    t1:x=%3.3f/y=%3.3f/w=%3.3f/h=%3.3f",  x1.getValue(),  y1.getValue(), w1.getValue(), h1.getValue() ) );

    	for ( Label lbl : t1.getColumnLabelLst() )
    	{
        	DoubleProperty xl  = lbl.layoutXProperty();
        	DoubleProperty yl  = lbl.layoutYProperty();
        	Bounds         bl  = lbl.getBoundsInParent();
        	DoubleProperty wl = new SimpleDoubleProperty( bl.getWidth() );
        	DoubleProperty hl = new SimpleDoubleProperty( bl.getHeight() );
        	
        	System.out.println( String.format( "%-10s:x=%3.3f/y=%3.3f/w=%3.3f/h=%3.3f", lbl.getText(), xl.getValue(), yl.getValue(), wl.getValue(), hl.getValue() ) );
    	}
    	
	}

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		launch( args );
	}

}
