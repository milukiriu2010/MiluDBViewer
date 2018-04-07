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
	
	
	public MyAlertDialog( Alert.AlertType alertType, MainController mainCtrl )
	{
		super( alertType );
		
		this.mainCtrl = mainCtrl;
		
		this.txtMsg.setEditable( false );
		this.txtExp.setEditable( false );
		this.txtExp.setWrapText( true );
		
		this.setTitle( "MiluDBViewer Alert");
		
        // Window Icon
		Stage stage = (Stage)this.getDialogPane().getScene().getWindow();
		stage.getIcons().add( this.mainCtrl.getImage( "file:resources/images/winicon.gif" ) );
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
