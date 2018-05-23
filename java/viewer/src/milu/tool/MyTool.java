package milu.tool;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.util.Collections;

import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.shape.Line;
import javafx.scene.shape.Path;
import javafx.scene.control.ScrollBar;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import milu.entity.schema.SchemaEntity;
import milu.main.MainController;
import milu.gui.view.DBView;

public class MyTool
{
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
	
	public static int lastIndexOf(char ch, String str) 
	{
		if ( str.length() < 1 )
		{
			return -1;
		}
	    if (str.charAt(str.length() - 1) == ch)
	    { 
	    	return str.length()-1; 
	    }
	    if (str.length() <= 1)
	    { 
	    	return -1; 
	    }
	    return lastIndexOf( ch, str.substring(0,str.length()-1) );
	}
	
	public static String getFileExtension( File file )
	{
		String name = file.getName();
		int pos = name.lastIndexOf(".");
		if ( pos == -1 )
		{
			return "";
		}
		else
		{
			return name.substring( pos+1 );
		}
	}
	
	public static String getExceptionString( Exception exp )
	{
		StringWriter sw = new StringWriter();
		PrintWriter  pw = new PrintWriter( sw );
		exp.printStackTrace( pw );
		return sw.toString();
	}
	
	public static ImageView createImageView( int width, int height, Image image )
	{
		ImageView iv = new ImageView( image );
		iv.setFitWidth( width );
		iv.setFitHeight( height );
		return iv;
	}
	
	public static Node createImageView( int width, int height, MainController mainCtrl, SchemaEntity schemaEntity )
	{
		String imageResourceName = schemaEntity.getImageResourceName();
		//System.out.println( "image:" + imageResourceName );
		ImageView iv = new ImageView( mainCtrl.getImage(imageResourceName) );
		iv.setFitHeight( 16 );
		iv.setFitWidth( 16 );
		
		SchemaEntity.STATE state = schemaEntity.getState();
		
		Group imageGroup = new Group();
		if ( state == SchemaEntity.STATE.VALID )
		{
			imageGroup.getChildren().add( iv );
		}
		else if ( state == SchemaEntity.STATE.INVALID )
		{
			Line lineLTRB = new Line( 0, 0, iv.getFitWidth(), iv.getFitHeight() );
			lineLTRB.setStyle( "-fx-stroke: red; -fx-stroke-width: 2;" );
			Line lineRTLB = new Line( iv.getFitWidth(), 0, 0, iv.getFitHeight() );
			lineRTLB.setStyle( "-fx-stroke: red; -fx-stroke-width: 2;" );
			imageGroup.getChildren().addAll( iv, lineLTRB, lineRTLB );
			imageGroup.setEffect( new Blend(BlendMode.OVERLAY) );
		}

		return imageGroup;
	}

	public static Path findCaret(Parent parent) 
	{
		if ( parent == null )
		{
			//System.out.println( "findCaret:parent is null." );
			return null;
		}
		//System.out.println( "findCaret:parent:" + parent.getClass() );
		for (Node n : parent.getChildrenUnmodifiable()) 
		{
			//System.out.println( "findCaret:child :" + n.getClass() );
			if (n instanceof Path) 
			{
			    return (Path) n;
			} 
			else if (n instanceof Parent) 
			{
			    Path p = findCaret((Parent) n);
			    if (p != null) 
			    {
			      return p;
			    }
			}
		}
		//System.out.println( "findCaret:not found." );
		return null;
	}
	
	public static Point2D findScreenLocation( Node node, Node base ) 
	{
		double x = 0;
		double y = 0;
		for ( Node n = node; n != null ; n=n.getParent() ) 
		{
			if ( n == base )
			{
				break;
			}
			System.out.println( "location :" + n.getClass() );
			Bounds parentBounds = n.getBoundsInParent();
			x += parentBounds.getMinX();
			y += parentBounds.getMinY();
		}
		/**/
		Scene scene = node.getScene();
		x += scene.getX();
		y += scene.getY();
		/**/
		/*
		Window window = scene.getWindow();
		x += window.getX();
		y += window.getY();
		*/
		Point2D screenLoc = new Point2D(x, y);
		return screenLoc;
	}
	
	/*
	public static AnchorPane findAnchorPane( Node node )
	{
		for ( Node n = node; n != null ; n=n.getParent() ) 
		{
			System.out.println( "findAnchorPane :" + n.getClass() );
			if ( n instanceof AnchorPane )
			{
				return ((AnchorPane)n);
			}
		}
		return null;
	}
	*/
	
	// https://stackoverflow.com/questions/24810197/how-to-know-if-a-scroll-bar-is-visible-on-a-javafx-tableview
	public static ScrollBar getScrollBarVertical( Node node )
	{
		ScrollBar result = null;
		
		for ( Node n: node.lookupAll(".scroll-bar") )
		{
			if ( n instanceof ScrollBar )
			{
				ScrollBar bar = (ScrollBar)n;
				if ( bar.getOrientation().equals(Orientation.VERTICAL) )
				{
					result = bar;
				}
			}
		}
		
		return result;
	}
	/*
	public static String findTabText(Node node,Node checkNode) 
	{
		if ( node == null )
		{
			return null;
		}
		
		if ( node instanceof Parent )
		{
			Parent parent = (Parent)node;
			String strLabel = null;
			for ( Node n : parent.getChildrenUnmodifiable() )
			{
				String className = n.getClass().getName();
				//System.out.println( "findTabText:className:" + className );
				if ( checkNode == null )
				{
					if ( className.contains("TabHeader") )
					//if ( n instanceof TabHeaderArea )
					{
						strLabel = findTabText( n, n );
					}
				}
				else
				{
					if ( n instanceof Label )
					{
						strLabel = ((Label)n).getText();
						//System.out.println( "findTabText:hit:" + strLabel );
					}
					if ( strLabel == null )
					{
						strLabel = findTabText( n, checkNode );
					}
				}
			}
			return strLabel;
		}
		return null;
	}
	*/
	
	public static Node searchParentNode( Node node, final Class<?> searchNodeClass )
	{
		for ( Node n = node; n != null ; n=n.getParent() ) 
		{
			if ( searchNodeClass.isInstance(n) )
			{
				return n;
			}
		}
		return null;
	}
	
	public static Node searchChildNode( Node node, final Class<?> searchNodeClass )
	{
		if ( node == null )
		{
			return null;
		}
		if ( node instanceof Parent )
		{
			Parent parent = (Parent)node;
			for ( Node n : parent.getChildrenUnmodifiable() )
			{
				//System.out.println( "SearchNode[" + searchNodeClass.getName() + "]ChildNode[" + n.getClass().getName() + "]" );
				//if ( searchNodeClass.getName().equals(n.getClass().getName()) )
				if ( searchNodeClass.isInstance(n) )
				{
					return n;
				}
				else
				{
					Node nodeResult = searchChildNode( n, searchNodeClass );
					if ( nodeResult != null )
					{
						return nodeResult;
					}
				}
			}		
		}
		return null;
	}

	public static void skimThroughParent( Node node )
	{
		if ( node == null )
		{
			return;
		}
		
		for ( Node n = node; n != null; n = n.getParent() )
		{
			System.out.println( "skimThroughParent:" + n.getClass().getName() );
		}
	}
	
	public static void skimThroughChildren( Node node, int level )
	{
		if ( node == null )
		{
			return;
		}
		if ( node instanceof Parent )
		{
			String tab = String.join("", Collections.nCopies(level, "  ") );
			Parent parent = (Parent)node;
			for ( Node n : parent.getChildrenUnmodifiable() )
			{
				System.out.println( tab + "Child Node:" + n.getClass() );
				skimThroughChildren( n, level+1 );
			}		
		}
	}
	
	public static String bytesToHex(byte[] bytes) 
	{
		final char[] hexArray = "0123456789ABCDEF".toCharArray();
	    char[] hexChars = new char[bytes.length * 2];
	    
	    for ( int j = 0; j < bytes.length; j++ ) 
	    {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}

	public static <T> T cast( Object obj, Class<T> clazz )
	{
		try
		{
			return clazz.cast(obj);
		}
		catch ( ClassCastException ccEx )
		{
			return null;
		}
	}
	
	public static <T> T newInstance( Class<T> castClazz, DBView dbView )
	{
		Constructor<?>[] constructors = castClazz.getDeclaredConstructors();
		
		T obj = null;
		for ( int i = 0; i < constructors.length; i++ )
		{
			try
			{
				// exit loop 
				// when match "new SelectdItemHandlerEachXX( DBView dbView )"
				obj = castClazz.cast(constructors[i].newInstance(dbView));
				break;
			}
			catch ( Exception ex )
			{
			}
		}
		
		return obj;
	}
}
