package milu.gui.dlg;

import java.io.StringWriter;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import milu.tool.MyTool;
import milu.db.MyDBAbstract;

public class MyAlertDialog extends Alert 
{
	// Property File for this class 
	private static final String PROPERTY_FILENAME = 
		"conf.lang.gui.dlg.MyAlertDialog";
	
	// Language Resource
	private ResourceBundle langRB = ResourceBundle.getBundle( PROPERTY_FILENAME );
	
	/** TextArea for Message */
	private TextArea  txtMsg = new TextArea();
	//private TextField txtMsg = new TextField();
	
	/** TextArea for Exception */
	private TextArea  txtExp = new TextArea();
	
	
	public MyAlertDialog( Alert.AlertType alertType )
	{
		super( alertType );
		
		this.txtMsg.setEditable( false );
		this.txtExp.setEditable( false );
		this.txtExp.setWrapText( true );
		
		this.setTitle( "MiluDBViewer Alert");
		
        // Window Icon
		Stage stage = (Stage)this.getDialogPane().getScene().getWindow();
		stage.getIcons().add( new Image( "file:resources/images/winicon.gif" ) );
		/*
		try
		{
			InputStream inputStreamWinIcon = new FileInputStream( "resources" + File.separator + "images" + File.separator + "winicon.gif" );
			Image imgWinIcon = new Image( inputStreamWinIcon );
			Stage stage = (Stage)this.getDialogPane().getScene().getWindow();
			stage.getIcons().add( imgWinIcon );
		}
		catch ( FileNotFoundException fnfEx )
		{
			fnfEx.printStackTrace();
		}
		*/
	}
	
	public void setTxtMsg( String msg )
	{
		this.txtMsg.setText( msg );
		// count "\n"
		int lfCnt = MyTool.getCharCount( msg, "\n" ); 
		// int lfCnt = msg.length() - msg.replace( "\n", "" ).length();
		this.txtMsg.setPrefRowCount( lfCnt+1 );
		
		BorderPane  paneTxt = new BorderPane();
		paneTxt.setTop( this.txtMsg );
		
		this.getDialogPane().setExpandableContent( paneTxt );
		this.getDialogPane().setExpanded( true );
	}
	
	public void setTxtExp( Exception exp )
	{
		StringWriter sw = new StringWriter();
		PrintWriter  pw = new PrintWriter( sw );
		exp.printStackTrace( pw );
		
		String msg = exp.getMessage();
		this.txtMsg.setText( msg );
		// count "\n"
		int lfCnt = MyTool.getCharCount( msg, "\n" ); 
		//int lfCnt = msg.length() - msg.replace( "\n", "" ).length();
		this.txtMsg.setPrefRowCount( lfCnt+1 );

		this.txtExp.setText( sw.toString() );
		this.txtExp.setMaxWidth( Double.MAX_VALUE );
		
		BorderPane  paneTxt = new BorderPane();
		paneTxt.setTop( this.txtMsg );
		paneTxt.setCenter( this.txtExp );
		
		this.getDialogPane().setExpandableContent( paneTxt );
		this.getDialogPane().setExpanded( true );
	}
	
	public void setTxtExp( SQLException exp, MyDBAbstract myDBAbs )
	{
		StringWriter sw = new StringWriter();
		PrintWriter  pw = new PrintWriter( sw );
		exp.printStackTrace( pw );
		
		String msg = exp.getMessage();
		this.txtMsg.setText( msg );
		// count "\n"
		int lfCnt = MyTool.getCharCount( msg, "\n" ); 
		//int lfCnt = msg.length() - msg.replace( "\n", "" ).length();
		this.txtMsg.setPrefRowCount( lfCnt+1 );

		this.txtExp.setText( sw.toString() );
		this.txtExp.setMaxWidth( Double.MAX_VALUE );
		
		BorderPane  paneTxt = new BorderPane();
		paneTxt.setTop( this.txtMsg );
		paneTxt.setCenter( this.txtExp );
		
		if ( ( exp instanceof SQLException ) && ( myDBAbs != null ) )
		{
			ButtonType btnTypeReConnect = new ButtonType( langRB.getString("BTN_RECONNECT") );
			this.getButtonTypes().add( btnTypeReConnect );
			final Button btnReConnect = (Button)this.getDialogPane().lookupButton( btnTypeReConnect );
			btnReConnect.addEventFilter
			( 
				ActionEvent.ACTION, 
				event->
				{
					try
					{
						myDBAbs.reconnect();
						this.hide();
					}
					catch ( SQLException sqlEx )
					{
						this.setTxtExp( exp, myDBAbs );
					}
				} 
			);
		}
		
		this.getDialogPane().setExpandableContent( paneTxt );
		this.getDialogPane().setExpanded( true );
	}

}
