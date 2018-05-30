package milu.gui.dlg;

import java.io.StringWriter;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import milu.tool.MyTool;
import milu.db.MyDBAbstract;
import milu.main.MainController;

public class MyAlertDialog extends Alert 
{
	private MainController mainCtrl = null;
	
	/** TextArea for Message */
	private TextArea  txtMsg = new TextArea();
	
	/** TextArea for Exception */
	private TextArea  txtExp = new TextArea();
	
	
	// 
	private TextArea  txtMsgExtra = new TextArea();
	
	public MyAlertDialog( Alert.AlertType alertType, MainController mainCtrl )
	{
		super( alertType );
		
		this.mainCtrl = mainCtrl;
		
		this.setPane();
	}
	
	// https://stackoverflow.com/questions/36309385/how-to-change-the-text-of-yes-no-buttons-in-javafx-8-alert-dialogs?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
	public MyAlertDialog( Alert.AlertType alertType, MainController mainCtrl, ButtonType...buttonTypes )
	{
		super( alertType, "", buttonTypes );
		
		this.mainCtrl = mainCtrl;
		
		this.setPane();
	}
	
	private void setPane()
	{
		this.txtMsg.setEditable( false );
		this.txtExp.setEditable( false );
		this.txtExp.setWrapText( true );
		
		this.setTitle( "MiluDBViewer Alert");
		
        // Window Icon
		Stage stage = (Stage)this.getDialogPane().getScene().getWindow();
		stage.getIcons().add( this.mainCtrl.getImage( "file:resources/images/winicon.gif" ) );
		
		// set location
		Platform.runLater( ()->MyTool.setWindowLocation( stage, stage.getWidth(), stage.getHeight() ) );
	}
	
	public void setTxtMsg( String msg )
	{
		this.txtMsg.setText( msg );
		// count "\n"
		int lfCnt = MyTool.getCharCount( msg, "\n" ); 
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
		this.txtMsg.setPrefRowCount( lfCnt+1 );

		this.txtExp.setText( sw.toString() );
		this.txtExp.setMaxWidth( Double.MAX_VALUE );
		
		BorderPane  paneTxt = new BorderPane();
		paneTxt.setTop( this.txtMsg );
		paneTxt.setCenter( this.txtExp );
		
		this.getDialogPane().setExpandableContent( paneTxt );
		this.getDialogPane().setExpanded( true );
	}
	
	public void setTxtExp( Exception exp, String msgExtra )
	{
		StringWriter sw = new StringWriter();
		PrintWriter  pw = new PrintWriter( sw );
		exp.printStackTrace( pw );
		
		String msg = exp.getMessage();
		this.txtMsg.setText( msg );
		// count "\n"
		int lfCnt = MyTool.getCharCount( msg, "\n" ); 
		this.txtMsg.setPrefRowCount( lfCnt+1 );

		this.txtExp.setText( sw.toString() );
		this.txtExp.setMaxWidth( Double.MAX_VALUE );
		
		this.txtMsgExtra.setText( msgExtra );
		this.txtMsgExtra.setPrefColumnCount( MyTool.getCharCount( msgExtra, "\n" ) ); 
		
		BorderPane  paneTxt = new BorderPane();
		paneTxt.setTop( this.txtMsgExtra );
		paneTxt.setCenter( this.txtMsg );
		paneTxt.setBottom( this.txtExp );
		
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
		this.txtMsg.setPrefRowCount( lfCnt+1 );

		this.txtExp.setText( sw.toString() );
		this.txtExp.setMaxWidth( Double.MAX_VALUE );
		
		BorderPane  paneTxt = new BorderPane();
		paneTxt.setTop( this.txtMsg );
		paneTxt.setCenter( this.txtExp );
		
		if ( ( exp instanceof SQLException ) && ( myDBAbs != null ) )
		{
			ResourceBundle langRB = this.mainCtrl.getLangResource("conf.lang.gui.common.MyAlert");
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
