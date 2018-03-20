package milu.gui.ctrl.schema;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

import milu.db.MyDBAbstract;
import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityEachFK;
import milu.entity.schema.search.SearchSchemaEntityInterface;
import milu.entity.schema.search.SearchSchemaEntityVisitorFactory;
import milu.gui.ctrl.common.LabelTable;

public class SchemaERView extends ScrollPane 
{
	private AnchorPane root = new AnchorPane();
	
	private MyDBAbstract myDBAbs = null;
	
	// [ROOT_ER]
	private SchemaEntity erEntity = null;
	
	// SchemaEntity <=> LabelTable
	private Map<SchemaEntity,LabelTable>  slMap = new HashMap<>();
	
	// Mouse Point
	private Point2D  srcP = null;
	
	public SchemaERView()
	{
		super();
		
		this.setPrefSize( 640, 480 );
		this.setContent( root );
		
	}
	
	public void setMyDBAbstract( MyDBAbstract myDBAbs )
	{
		this.myDBAbs = myDBAbs;
	}	
	
	public void setSchemaEntityRootER( SchemaEntity erEntity )
	{
		if ( this.myDBAbs == null )
		{
			return;
		}
		this.erEntity = erEntity;
		
		SchemaEntity rootEntity = this.myDBAbs.getSchemaRoot();
		
		int i = 0;
		List<SchemaEntity> fkEntityLst = this.erEntity.getEntityLst();
		for ( SchemaEntity seEntity : fkEntityLst )
		{
			System.out.println( "===================================================" );

			SchemaEntityEachFK fkEntity = (SchemaEntityEachFK)seEntity;
			
			// --- SRC ----------------------------------------------			
			
			String srcTableSchema = fkEntity.getSrcTableSchema();
			String srcTableName   = fkEntity.getSrcTableName();
			
			System.out.println( "i[" + i + "]srcTableSchema[" + srcTableSchema + "]srcTableName[" + srcTableName + "]" );
			
			// Search SRC [SCHEMA]
			SearchSchemaEntityInterface searchSrcSchemaVisitor = new SearchSchemaEntityVisitorFactory().createInstance(SchemaEntity.SCHEMA_TYPE.SCHEMA, srcTableSchema );
			rootEntity.accept(searchSrcSchemaVisitor);
			SchemaEntity srcSchemaEntity = searchSrcSchemaVisitor.getHitSchemaEntity();
			
			// Search SRC [TABLE]
			SearchSchemaEntityInterface searchSrcTableVisitor = new SearchSchemaEntityVisitorFactory().createInstance(SchemaEntity.SCHEMA_TYPE.TABLE, srcTableName );
			srcSchemaEntity.accept(searchSrcTableVisitor);
			SchemaEntity srcTableEntity = searchSrcTableVisitor.getHitSchemaEntity();
			
			// SRC LabelTable
			LabelTable  srcLabelTable = null;
			if ( srcTableEntity != null )
			{
				if ( this.slMap.containsKey(srcTableEntity) == true )
				{
					srcLabelTable = this.slMap.get(srcTableEntity);
				}
				else
				{
					srcLabelTable = new LabelTable(srcTableEntity);
					this.root.getChildren().add( srcLabelTable );
					this.slMap.put(srcTableEntity, srcLabelTable );
					
					srcLabelTable.setLayoutX( 20+(i%6)*140 );
					srcLabelTable.setLayoutY( 100+i*100 );
					this.setAction(srcLabelTable);
				}
			}
			else
			{
				System.out.println( "Not Found:i[" + i + "]srcTableSchema[" + srcTableSchema + "]srcTableName[" + srcTableName + "]" );
			}
			
			// --- DST ----------------------------------------------
			
			String dstTableSchema = fkEntity.getDstTableSchema();
			String dstTableName   = fkEntity.getDstTableName();
			
			System.out.println( "i[" + i + "]dstTableSchema[" + dstTableSchema + "]dstTableName[" + dstTableName + "]" );
			
			// Search DST [SCHEMA]
			SearchSchemaEntityInterface searchDstSchemaVisitor = new SearchSchemaEntityVisitorFactory().createInstance(SchemaEntity.SCHEMA_TYPE.SCHEMA, dstTableSchema );
			rootEntity.accept(searchDstSchemaVisitor);
			SchemaEntity dstSchemaEntity = searchDstSchemaVisitor.getHitSchemaEntity();
			
			// Search DST [TABLE]
			SearchSchemaEntityInterface searchDstTableVisitor = new SearchSchemaEntityVisitorFactory().createInstance(SchemaEntity.SCHEMA_TYPE.TABLE, dstTableName );
			dstSchemaEntity.accept(searchDstTableVisitor);
			SchemaEntity dstTableEntity = searchDstTableVisitor.getHitSchemaEntity();
			
			// DST LabelTable
			LabelTable  dstLabelTable = null;
			if ( dstTableEntity != null )
			{
				if ( this.slMap.containsKey(dstTableEntity) == true )
				{
					dstLabelTable = this.slMap.get(dstTableEntity);
				}
				else
				{
					dstLabelTable = new LabelTable(dstTableEntity);
					this.root.getChildren().add( dstLabelTable );
					this.slMap.put(dstTableEntity, dstLabelTable );
					
					dstLabelTable.setLayoutX( 180+(i%6)*140 );
					dstLabelTable.setLayoutY( 100+i*100 );
					this.setAction(dstLabelTable);
				}
			}
			else
			{
				System.out.println( "Not Found:i[" + i + "]dstTableSchema[" + dstTableSchema + "]dstTableName[" + dstTableName + "]" );			
			}
			
			// -- SRC/DST Column Map ----------------------------
			Map<String,String>  srcColumnMap = fkEntity.getSrcColumnMap();
			Map<String,String>  dstColumnMap = fkEntity.getDstColumnMap();
			
			List<String> srcColumnLst = new ArrayList<>();
			List<String> dstColumnLst = new ArrayList<>();
			
			srcColumnMap.forEach( (k,v)->srcColumnLst.add(k) );
			dstColumnMap.forEach( (k,v)->dstColumnLst.add(k) );
			
			if ( srcColumnLst.size() == dstColumnLst.size() )
			{
				for ( int j = 0; j < srcColumnLst.size(); j++ )
				{
					String srcColumn = srcColumnLst.get(j);
					String dstColumn = dstColumnLst.get(j);
					
					final LabelTable srcLabelTableF = srcLabelTable;
					final LabelTable dstLabelTableF = dstLabelTable;
					Platform.runLater
					( 
						()->
						{
							try
							{
								if ( System.getProperty("os.name").contains("Windows") == true )
								{
								}
								else
								{
									Thread.sleep(100);
								}
							}
							catch ( InterruptedException intEx )
							{
							}
							this.connect( srcLabelTableF, srcColumn, dstLabelTableF, dstColumn );
						}
					);
				}
			}
			else
			{
				System.out.println( "Column size doesn't match!! src size=[" + srcColumnLst.size() + "]dst size=[" + dstColumnLst.size() + "]" );
			}
			
			i++;
		}
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
	
	private void connect( LabelTable t1, String tlink1, LabelTable t2, String tlink2 )
	{
		if ( t1 == null || t2 == null )
		{
			return;
		}
		
		// t1
    	DoubleProperty x1 = t1.layoutXProperty();
    	DoubleProperty y1 = t1.layoutYProperty();
    	Bounds         b1 = t1.getBoundsInLocal();
    	DoubleProperty w1 = new SimpleDoubleProperty( b1.getWidth() );
    	//DoubleProperty h1 = new SimpleDoubleProperty( b1.getHeight() );
    	
    	// tlink1
    	Label          lbl1 = t1.getColumnLabel( tlink1 );
    	DoubleProperty xl1  = lbl1.layoutXProperty();
    	DoubleProperty yl1  = lbl1.layoutYProperty();
    	Bounds         bl1  = lbl1.getBoundsInParent();
    	//DoubleProperty wl1  = new SimpleDoubleProperty( bl1.getWidth() );
    	DoubleProperty hl1  = new SimpleDoubleProperty( bl1.getHeight() );
    	
    	//System.out.println( String.format( "    t1:x=%3.3f/y=%3.3f",  x1.getValue(),  y1.getValue() ) );
    	//System.out.println( String.format( "tlink1:x=%3.3f/y=%3.3f", xl1.getValue(), yl1.getValue() ) );
    	
    	// t2
    	DoubleProperty x2 = t2.layoutXProperty();
    	DoubleProperty y2 = t2.layoutYProperty();
    	Bounds         b2 = t2.getBoundsInLocal();
    	DoubleProperty w2 = new SimpleDoubleProperty( b2.getWidth() );
    	//DoubleProperty h2 = new SimpleDoubleProperty( b2.getHeight() );
    	
    	// tlink2
    	Label          lbl2 = t2.getColumnLabel( tlink2 );
    	DoubleProperty xl2  = lbl2.layoutXProperty();
    	DoubleProperty yl2  = lbl2.layoutYProperty();
    	Bounds         bl2  = lbl2.getBoundsInParent();
    	//DoubleProperty wl2  = new SimpleDoubleProperty( bl2.getWidth() );
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
    	this.root.getChildren().add( path );
    	path.getStrokeDashArray().addAll( 2.0, 4.0 );
    	path.toBack();
    	
    	// src mouse enter
    	lbl1.setOnMouseEntered(	(event)->this.addStyleClass(lbl1, lbl2, path) );
    	// src mouse exit
    	lbl1.setOnMouseExited( (event)->this.removeStyleClass(lbl1, lbl2, path)	);
    	// dst mouse enter
    	lbl2.setOnMouseEntered(	(event)->this.addStyleClass(lbl1, lbl2, path) ); 
    	// dst mouse exit
    	lbl2.setOnMouseExited( (event)->this.removeStyleClass(lbl1, lbl2, path)	);
    	// path mouse enter
    	path.setOnMouseEntered( (event)->this.addStyleClass(lbl1, lbl2, path) );
    	// path mouse exit
    	path.setOnMouseExited( (event)->this.removeStyleClass(lbl1, lbl2, path) );
	}
	
	private void addStyleClass( Label lbl1, Label lbl2, Path path )
	{
		//System.out.println( "addStyle["+lbl1.getText()+"]["+lbl2.getText()+"]" );
		lbl1.getStyleClass().add("column_red");
		lbl2.getStyleClass().add("column_cyan");
		path.getStyleClass().add("path_green");
	}

	private void removeStyleClass( Label lbl1, Label lbl2, Path path )
	{
		//System.out.println( "delStyle["+lbl1.getText()+"]["+lbl2.getText()+"]" );
		lbl1.getStyleClass().remove("column_red"); 
		lbl2.getStyleClass().remove("column_cyan"); 
		path.getStyleClass().remove("path_green");
	}
}
