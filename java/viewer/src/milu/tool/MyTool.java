package milu.tool;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;

import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.shape.Path;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.skin.TabPaneSkin;
import javafx.scene.control.Label;

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
		return name.substring( name.lastIndexOf(".")+1 );
	}
	
	public static String getExceptionString( Exception exp )
	{
		StringWriter sw = new StringWriter();
		PrintWriter  pw = new PrintWriter( sw );
		exp.printStackTrace( pw );
		return sw.toString();
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

	
}
