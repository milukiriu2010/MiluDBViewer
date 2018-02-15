package hello;

import java.util.Locale;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

//http://bitwalk.blogspot.jp/2017/08/javafx-gui-netbeans-ide.html
//https://stackoverflow.com/questions/3776687/how-to-add-a-java-properties-file-to-my-java-project-in-eclipse
//http://blog.k11i.biz/2014/09/java-utf-8.html
//http://d.hatena.ne.jp/torazuka/20130225/eclipse
public class HelloWorld extends Application {
	private static String FILENAME = "resources/hello/HelloWorld";
     
    @Override
    public void start(Stage primaryStage) {
    	Locale nowLocale = Locale.getDefault();
    	
    	System.out.println( "Locale:" + nowLocale.toString() );
    	Label label = new Label( nowLocale.toString() );
    	
        Button btn = new Button();
        /*
        btn.setText("Say 'Hello World'");
        btn.setOnAction((ActionEvent event) -> {
            System.out.println("Hello World!");
        });
        */
        btn.setText(java.util.ResourceBundle.getBundle(FILENAME).getString("SAY 'HELLO WORLD'"));
        btn.setOnAction((ActionEvent event) -> {
            System.out.println(java.util.ResourceBundle.getBundle(FILENAME).getString("HELLO WORLD!"));
        });
        
        VBox vbox = new VBox();
        vbox.getChildren().add( btn );
        vbox.getChildren().add( label );
         
        StackPane root = new StackPane();
        //root.getChildren().add(btn);
        root.getChildren().add( vbox );
         
        Scene scene = new Scene(root, 300, 250);
         
        //primaryStage.setTitle("Hello World!");
        primaryStage.setTitle(java.util.ResourceBundle.getBundle(FILENAME).getString("HELLO WORLD!"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }
 
    public static void main(String[] args) {
        launch(args);
    }  
}
