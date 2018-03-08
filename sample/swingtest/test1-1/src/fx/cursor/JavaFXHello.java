package fx.cursor;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
 
public class JavaFXHello extends Application {
 
    Scene scene;
 
    Button createButton(Cursor cursor) {
        Button btn = new Button();
        btn.setMnemonicParsing(false);
        //Sets the value of the property mnemonicParsing false to display '_'.
        /*
         MnemonicParsing property to enable/disable text parsing. 
         If this is set to true, then the Label text will be parsed 
         to see if it contains the mnemonic parsing character '_'. 
         When a mnemonic is detected the key combination will be 
         determined based on the succeeding character, 
         and the mnemonic added.
         */
        btn.setCursor(cursor);
        btn.setText(btn.getCursor().toString());
         
        btn.setOnAction((ActionEvent event) -> {
            Node src = (Node) event.getSource();
            scene.setCursor(src.getCursor());
        });
        return btn;
    }
 
    @Override
    public void start(Stage primaryStage) {
 
        VBox vBox = new VBox();
        scene = new Scene(vBox, 300, 250);
 
        Button btnCLOSED_HAND = createButton(Cursor.CLOSED_HAND);
        Button btnCROSSHAIR = createButton(Cursor.CROSSHAIR);
        Button btnDEFAULT = createButton(Cursor.DEFAULT);
        Button btnDISAPPEAR = createButton(Cursor.DISAPPEAR);
        Button btnE_RESIZE = createButton(Cursor.E_RESIZE);
        Button btnH_RESIZE = createButton(Cursor.H_RESIZE);
        Button btnHAND = createButton(Cursor.HAND);
        Button btnMOVE = createButton(Cursor.MOVE);
        Button btnN_RESIZE = createButton(Cursor.N_RESIZE);
        Button btnNE_RESIZE = createButton(Cursor.NE_RESIZE);
        Button btnNONE = createButton(Cursor.NONE);
        Button btnNW_RESIZE = createButton(Cursor.NW_RESIZE);
        Button btnOPEN_HAND = createButton(Cursor.OPEN_HAND);
        Button btnS_RESIZE = createButton(Cursor.S_RESIZE);
        Button btnSE_RESIZE = createButton(Cursor.SE_RESIZE);
        Button btnSW_RESIZE = createButton(Cursor.SW_RESIZE);
        Button btnTEXT = createButton(Cursor.TEXT);
        Button btnV_RESIZE = createButton(Cursor.V_RESIZE);
        Button btnW_RESIZE = createButton(Cursor.W_RESIZE);
        Button btnWAIT = createButton(Cursor.WAIT);
 
        vBox.getChildren().addAll(
                btnCLOSED_HAND, btnCROSSHAIR, btnDEFAULT,
                btnDISAPPEAR, btnE_RESIZE, btnH_RESIZE,
                btnHAND, btnMOVE, btnN_RESIZE, btnNE_RESIZE,
                btnNONE, btnNW_RESIZE, btnOPEN_HAND, btnS_RESIZE,
                btnSE_RESIZE, btnSW_RESIZE, btnTEXT, btnV_RESIZE,
                btnW_RESIZE, btnWAIT);
 
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
 
    public static void main(String[] args) {
        launch(args);
    }
 
}
