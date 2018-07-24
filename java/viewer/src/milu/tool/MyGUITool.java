package milu.tool;

import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.io.File;
import java.sql.SQLException;

import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.shape.Line;
import javafx.scene.shape.Path;
import javafx.stage.Screen;
import javafx.stage.Window;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.stage.FileChooser;
import java.awt.MouseInfo;
import java.awt.Point;

import milu.db.MyDBAbstract;
import milu.entity.schema.SchemaEntity;
import milu.main.MainController;
import milu.gui.dlg.MyAlertDialog;

public class MyGUITool
{
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
			System.out.println( "searchParentNode:" + n );
			if ( searchNodeClass.isInstance(n) )
			{
				return n;
			}
		}
		return null;
	}
	
	public static Node searchParentNode( Node node, final Class<?> searchNodeClass, int cnt )
	{
		for ( Node n = node; n != null ; n=n.getParent() ) 
		{
			System.out.println( "searchParentNode:" + n );
			if ( searchNodeClass.isInstance(n) )
			{
				if ( cnt <= 1 )
				{
					return n;
				}
				cnt = cnt - 1;
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

	
	public static void setWindowLocation( Window window, double w, double h )
	{
		double x = 0.0;
		double y = 0.0;
		
		Point p = MouseInfo.getPointerInfo().getLocation();
		Point2D pd = new Point2D( p.x, p.y );
		List<Screen> screens = Screen.getScreens();
		for ( Screen screen : screens )
		{
			Rectangle2D screenBounds = screen.getBounds();
			if ( screenBounds.contains(pd) )
			{
				x = screenBounds.getWidth();
				y = screenBounds.getHeight();
				x = (x - w)/2 + screenBounds.getMinX();
				y = (y - h)/2 + screenBounds.getMinY();
				System.out.println( "screenBx:"     + screenBounds.getWidth()  + ":y:" + screenBounds.getHeight() );
				System.out.println( "x:" + x + ":y:" + y );
				System.out.println( "w:" + w + ":h:" + h );
				break;
			}
		}
		window.setX(x);
		window.setY(y);
	}
	
	public static File fileOpenDialog( String appConfDir, TextField txtField, List<FileChooser.ExtensionFilter> filterLst, Node node )
	{
		FileChooser fc = new FileChooser();
		File initDirFile = null;
		// Initial Directory
		if ( appConfDir != null && appConfDir.isEmpty() != true )
		{
			initDirFile = new File(appConfDir);
		}
		if ( (txtField != null) && 
			 (txtField.getText() != null) && 
			 (txtField.getText().isEmpty() != true) 
		)
		{
			initDirFile = new File(txtField.getText());
		}
		if ( initDirFile != null && initDirFile.exists() )
		{
			if ( initDirFile.isDirectory() )
			{
				fc.setInitialDirectory(initDirFile);
			}
			else
			{
				fc.setInitialDirectory(initDirFile.getParentFile());
			}
		}
		// Extension Filter
		if ( filterLst != null && filterLst.size() > 0 )
		{
			fc.getExtensionFilters().addAll(filterLst);
		}
		File file = fc.showOpenDialog(node.getScene().getWindow());
		return file;
	}
	
	public static List<File> fileOpenMultiDialog( String appConfDir, List<FileChooser.ExtensionFilter> filterLst, Node node )
	{
		FileChooser fc = new FileChooser();
		File initDirFile = null;
		// Initial Directory
		if ( appConfDir != null && appConfDir.isEmpty() != true )
		{
			initDirFile = new File(appConfDir);
		}
		if ( initDirFile != null && initDirFile.exists() )
		{
			fc.setInitialDirectory(initDirFile);
		}
		// Extension Filter
		if ( filterLst != null && filterLst.size() > 0 )
		{
			fc.getExtensionFilters().addAll(filterLst);
		}
		List<File> fileLst = fc.showOpenMultipleDialog(node.getScene().getWindow());
		return fileLst;
	}
	
	public static File fileSaveDialog( String appConfDir, List<FileChooser.ExtensionFilter> filterLst, Node node )
	{
		FileChooser fc = new FileChooser();
		File initDirFile = null;
		// Initial Directory
		if ( appConfDir != null && appConfDir.isEmpty() != true )
		{
			initDirFile = new File(appConfDir);
		}
		if ( initDirFile != null && initDirFile.exists() )
		{
			fc.setInitialDirectory(initDirFile);
		}
		// Extension Filter
		if ( filterLst != null && filterLst.size() > 0 )
		{
			fc.getExtensionFilters().addAll(filterLst);
		}
		File file = fc.showSaveDialog(node.getScene().getWindow());
		return file;
	}

	public static void showException( MainController mainCtrl, String resourceName, String headID, Exception ex )
	{
		MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING, mainCtrl );
		ResourceBundle langRB = mainCtrl.getLangResource(resourceName);
		alertDlg.setHeaderText( langRB.getString(headID) );
		alertDlg.setTxtExp( ex );
		alertDlg.showAndWait();
		alertDlg = null;
	}
	
	public static void showException( MainController mainCtrl, String resourceName, String headID, SQLException sqlEx, MyDBAbstract myDBAbs )
	{
		MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING, mainCtrl );
		ResourceBundle langRB = mainCtrl.getLangResource(resourceName);
		alertDlg.setHeaderText( langRB.getString(headID) );
		alertDlg.setTxtExp( sqlEx, myDBAbs );
		alertDlg.showAndWait();
		alertDlg = null;
	}
	
	public static void showException( MainController mainCtrl, String resourceName, String headID, Exception ex, String msgID )
	{
		MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING, mainCtrl );
		ResourceBundle langRB = mainCtrl.getLangResource(resourceName);
		alertDlg.setHeaderText( langRB.getString(headID) );
		alertDlg.setTxtExp( ex, langRB.getString(msgID) );
		alertDlg.showAndWait();
		alertDlg = null;
	}
	
	public static void showMsg( MainController mainCtrl, String resourceName, String headID )
	{
		MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING, mainCtrl );
		ResourceBundle langRB = mainCtrl.getLangResource(resourceName);
		alertDlg.setHeaderText( langRB.getString(headID) );
		alertDlg.showAndWait();
		alertDlg = null;
	}
	
	public static void showMsg( MainController mainCtrl, String resourceName, String headID, String msgID )
	{
		MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING, mainCtrl );
		ResourceBundle langRB = mainCtrl.getLangResource(resourceName);
		alertDlg.setHeaderText( langRB.getString(headID) );
		alertDlg.setTxtMsg( langRB.getString(msgID) );
		alertDlg.showAndWait();
		alertDlg = null;
	}
}
