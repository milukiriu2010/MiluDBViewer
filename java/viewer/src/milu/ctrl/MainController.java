package milu.ctrl;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import java.util.Locale;

import java.sql.SQLException;

import javafx.stage.Stage;
//import javafx.application.Platform;
//import javafx.scene.control.Button;
//import javafx.scene.control.ButtonBar.ButtonData;
//import javafx.scene.control.ButtonType;
import javafx.stage.Window;

//import javafx.stage.Stage;

//import java.sql.SQLException;

import milu.gui.dlg.DBSettingDialog;
import milu.gui.view.DBView;

import milu.db.MyDBAbstract;

public class MainController
{
	// DB connection list
	// put MyDBAbstract Object after connected to DB.
	private List<MyDBAbstract> myDBAbsLst = new ArrayList<MyDBAbstract>();
	
	// DBView list
	private List<DBView> dbViewLst = new ArrayList<DBView>();
	
	public MainController()
	{
	}
	
	private void closeAllDB()
	{
		// close DB connection
		for ( int i = 0; i < this.myDBAbsLst.size(); i++ )
		{
			MyDBAbstract myDBAbs = this.myDBAbsLst.get( i );
			try
			{
				myDBAbs.close();
			}
			catch( SQLException sqlEx )
			{
				// suppress error
			}
		}
	}
	
	/**
	 * Create New DB Connection & Open New Window
	 */
	public void createNewDBConnectionAndOpenNewWindow()
	{
		// -----------------------------------------
		// How to close on pressing 'x'
		// https://stackoverflow.com/questions/32048348/javafx-scene-control-dialogr-wont-close-on-pressing-x
		// -----------------------------------------
		DBSettingDialog dlg = new DBSettingDialog();
		Window       window = dlg.getDialogPane().getScene().getWindow();
		window.setOnCloseRequest( event ->{ window.hide(); this.close(null); } );
		
		// get result when clicking something on dialog
		Optional<MyDBAbstract> result = dlg.showAndWait();
		dlg = null;
		// Click "Connect"
		if ( result.isPresent() )
		{
			
			MyDBAbstract myDBAbs = result.get();
		
			// add myDBAbs to a DB connection list.
			this.myDBAbsLst.add( myDBAbs );

			// get Active Window(JavaFX9)
			// https://stackoverflow.com/questions/32922424/how-to-get-the-current-opened-stage-in-javafx
			List<Window>  focusWinLst = Stage.getWindows().filtered( window2->window2.isFocused() );
			System.out.println( "focusWinLst.size:" + focusWinLst.size() );
			//List<Window>  showingWinLst = Stage.getWindows().filtered( window3->window3.isShowing() );
			//System.out.println( "showingWinLst.size:" + showingWinLst.size() );
			DBView dbView = null;
			if ( focusWinLst.size() > 0 )
			{
				Window focusWin = focusWinLst.get(0);
				if ( focusWin instanceof DBView )
				{
					dbView = (DBView)focusWin;
				}
			}
			
			this.createNewWindow( myDBAbs, dbView );
		}
		// Click "Cancel"
		else
		{
			System.out.println( "Cancel!!" );
		}
		
		
	}

	public void createNewWindow( MyDBAbstract myDBAbs, DBView dbViewPrev )
	{
		// Open a view for SQL result
		DBView dbView = new DBView( this, myDBAbs );
		dbViewLst.add( dbView );
		dbView.start();
		
		// Open a new window at little bit right+lower.
		if ( dbViewPrev != null )
		{
			dbView.setX( dbViewPrev.getX() + 20 );
			dbView.setY( dbViewPrev.getY() + 20 );
		}
	}
	
	/**
	 * Close DBView
	 * 
	 * @param dbView
	 */
	public void close( DBView dbView )
	{
		// Call when pushing 'x' on DBSettingDialog
		if ( dbView == null )
		{
			if ( dbViewLst.size() == 0 )
			{
				this.closeAllDB();
				System.out.println( "MiluDBViewer Quit." );
			}
		}
		// Call when closing DBView
		else
		{
			MyDBAbstract myDBAbs = dbView.getMyDBAbstract();
			
			boolean ret = this.dbViewLst.remove( dbView );
			if ( ret == true )
			{
				System.out.println( "dbView remove success." );
				boolean isConExist = false;
				for ( DBView dbVW : dbViewLst )
				{
					MyDBAbstract dbAbs = dbVW.getMyDBAbstract();
					if ( dbAbs == myDBAbs )
					{
						isConExist = true;
					}
				}
				// When this DB connection is not necessary anymore, 
				// remove from DB connection list & close DB connection; 
				if ( isConExist == false )
				{
					this.myDBAbsLst.remove( myDBAbs );
					try
					{
						myDBAbs.close();
					}
					catch ( SQLException sqlEx )
					{
						// suppress error
					}
					myDBAbs = null;
				}
				
				dbView = null;
			}
			else
			{
				System.out.println( "dbView remove fail." );
			}
			
			if ( dbViewLst.size() == 0 )
			{
				this.quit();
			}
		}
	}
	
	/*********************************
	 * Quit Application
	 *********************************
	 */
	public void quit()
	{
		this.closeAllDB();
		System.out.println( "MiluDBViewer Exit." );
		// "hs_err_pid*.log" is created, when no DBView.
		System.exit( 0 );
	}
	
	/*********************************
	 * Change Language
	 * http://www.oracle.com/technetwork/java/javase/java8locales-2095355.html
	 * 
	 * @langCode
	 *    ex. en_US
	 *        ja_JP
	 *********************************
	 */
	public void changeLang( String langCode )
	{
		System.out.println( "changeLang:" + langCode );
		Locale nextLocale = new Locale( langCode );
		Locale.setDefault( nextLocale );
		
		for ( DBView dbView : this.dbViewLst )
		{
			dbView.changeLang();
		}
	}
	
}
