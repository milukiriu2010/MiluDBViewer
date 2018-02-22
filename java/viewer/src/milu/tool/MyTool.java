package milu.tool;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.shape.Path;
import javafx.stage.Window;

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
			System.out.println( "findCaret:parent is null." );
		}
		System.out.println( "findCaret:parent:" + parent.getClass() );
		for (Node n : parent.getChildrenUnmodifiable()) 
		{
			System.out.println( "findCaret:child :" + n.getClass() );
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
		System.out.println( "findCaret:not found." );
		return null;
	}
	
	public static Point2D findScreenLocation( Node node, Node base) 
	{
		double x = 0;
		double y = 0;
		for (Node n = node; n != null ; n=n.getParent()) 
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
		/*
		Scene scene = node.getScene();
		x += scene.getX();
		y += scene.getY();
		Window window = scene.getWindow();
		x += window.getX();
		y += window.getY();
		*/
		Point2D screenLoc = new Point2D(x, y);
		return screenLoc;
	}
	
}
