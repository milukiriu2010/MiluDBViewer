package encrypt;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.security.Security;
import java.security.Provider;

// http://kaworu.jpn.org/java/%E4%BD%BF%E7%94%A8%E5%8F%AF%E8%83%BD%E3%81%AA%E6%9A%97%E5%8F%B7%E3%82%A2%E3%83%AB%E3%82%B4%E3%83%AA%E3%82%BA%E3%83%A0%E3%82%92%E8%AA%BF%E3%81%B9%E3%82%8B
public class Algorithm extends Application 
{
	ComboBox<String> serviceCombo = new ComboBox<>();
	
	TextArea serviceTextArea = new TextArea();
	
	public void start(Stage stage) 
	{
		// https://docs.oracle.com/javase/9/docs/specs/security/standard-names.html#keygenerator-algorithms
		ObservableList<String> serviceList = 
			FXCollections.observableArrayList
			(
				"Signature",
				"MessageDigest",
				"Cipher",
				"Mac",
				"KeyStore",
				"KeyGenerator"
			);
		this.serviceCombo.getItems().addAll(serviceList);
		
		this.serviceCombo.getSelectionModel().selectedItemProperty().addListener
		(
			(obs,oldVal,newVal)->
			{
				StringBuffer sb = new StringBuffer();
				Security.getAlgorithms(newVal).stream()
					.sorted( (x1,x2)->x1.compareTo(x2) )
					.forEach( x->sb.append(x+"\n") );
				this.serviceTextArea.setText(sb.toString());
			}
		);

		HBox root = new HBox(2);
		root.getChildren().addAll( this.serviceCombo, this.serviceTextArea );
		
		Scene scene = new Scene( root, 450, 250 );
		stage.setScene(scene);
		stage.show();
	}
	
	public static void main( String[] args )
	{
		System.out.println( "=== Provider ==========================" );
		for ( Provider provider : Security.getProviders() )
		{
			System.out.println( "Name:" + provider.getName() );
			System.out.println( "Info:" + provider.getInfo() );
		}
		System.out.println( "=============================" );
		launch(args);
	}
}
