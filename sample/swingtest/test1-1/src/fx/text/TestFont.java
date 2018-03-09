package fx.text;

import java.io.File;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
 
public class TestFont extends Application {
 
    public static void main(String[] args) {
        launch(args);
    }
     
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        // シーングラフの作成
        VBox   root     = new VBox( 10.0f );
         
        // ボールド、イタリック、下線、文字寄せ
        Text    t       = new Text( "ボールド、ITALIC、下線、文字寄せ" );
        t.setFont( Font.font( "Meiryo UI" , FontWeight.BOLD  , FontPosture.ITALIC , 35 ) );
        t.setTextAlignment( TextAlignment.CENTER );
        t.setWrappingWidth( 700.0 );
        t.setUnderline( true );
        root.getChildren().add( t );
         
        // フォントの設定(インストール済みフォント)
        Text    t1      = new Text("インストール済みフォントの読込");
        t1.setFont( Font.font( "MS Mincho" , 45 ) );
        root.getChildren().add( t1 );
         
        // フォントの設定(フォントファイルの読み込み)
        Text    t2      = new Text("フォントファイルからの読込");
        t2.setFont( Font.loadFont( new File( "font/chogokubosogothic_5.ttf" ).toURI().toString() , 50 ) );
        root.getChildren().add( t2 );
         
        // 袋文字
        Text    t3      = new Text("袋文字で出力");
        t3.setFont( Font.font( null , 75 ) );
        t3.setFill( Color.ALICEBLUE );
        t3.setStroke( Color.CORNFLOWERBLUE );
        root.getChildren().add( t3 );
         
        // テキスト表示幅を指定することで
        // 自動改行（折り返し）をさせる
        Text    t4  = new Text( 300.0, 450.0, "自動折り返しです。『\\n』で明示的に改行\nもできます。" );
        t4.setFont( Font.font( "Meiryo UI" , 20 ) );
        t4.setWrappingWidth( 200.0 );
        root.getChildren().add( t4 );
         
        // シーン作成
        Scene   scene   = new Scene( root , 750 , 400 , Color.web( "9FCC7F" ) );
         
        // ステージ設定
        primaryStage.setTitle("Test Font");
        primaryStage.setScene(scene);
        primaryStage.show();
 
    }
     
 
}