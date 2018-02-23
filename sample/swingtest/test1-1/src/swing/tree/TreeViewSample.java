package swing.tree;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class TreeViewSample extends Application {

    private Node rootIcon = null;
    
    private Node fileIcon = null;

    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Tree View Sample");
        
        ImageView iv = new ImageView( new Image("file:resources/images/folder.jpg") );
        iv.setFitHeight( 16 );
        iv.setFitWidth( 16 );
        this.rootIcon = iv;

        ImageView ivFile = new ImageView( new Image("file:resources/images/file.png") );
        ivFile.setFitHeight( 16 );
        ivFile.setFitWidth( 16 );
        this.fileIcon = ivFile;
        
        Image  imageFile2 = new Image("file:resources/images/file2.png"); 
        
        TreeItem<String> rootItem = new TreeItem<String> ("Inbox", rootIcon);
        rootItem.setExpanded(true);
        for (int i = 1; i <= 3; i++) {
            TreeItem<String> item = new TreeItem<String> ("Message" + i, this.fileIcon );            
            rootItem.getChildren().add(item);
        }
        for (int i = 1; i <= 3; i++) {
        	ImageView ivFile2 = new ImageView(imageFile2);
        	ivFile2.setFitHeight(16);
        	ivFile2.setFitWidth(16);
            TreeItem<String> item = new TreeItem<String> ("Message" + i, ivFile2 );            
            rootItem.getChildren().add(item);
        }

        
        
        
        TreeView<String> tree = new TreeView<String> (rootItem);        
        StackPane root = new StackPane();
        root.getChildren().add(tree);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }

}
