package fx.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Application;
//import static javafx.application.Application.launch;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class TaskDemo extends Application {
    // スレッドプール
    private ExecutorService service = Executors.newSingleThreadExecutor();

    // ステータスおよび結果を表示するラベル
    private Label label;
    
    @Override
    public void start(Stage stage) {
        VBox root = new VBox(12);
        root.setAlignment(Pos.CENTER);
        
        root.getChildren().add(initButton());

        label = new Label("READY");
        root.getChildren().add(label);

        Scene scene = new Scene(root, 300, 150);

        // 終了時にスレッドプールをシャットダウンさせる
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                service.shutdown();
            }
        });

        stage.setTitle("Task Demo");
        stage.setScene(scene);
        stage.show();
    }

    private Button initButton() {
        // 長い時間のかかるタスクを実行するボタン
        Button button = new Button("Long Term Task");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                label.setText("RUNNING");
                executeTaskAsync();
            }
        });

        return button;
    }

    private void executeTaskAsync() {
        // 非同期に処理するタスク
        Task<Integer> task = new Task<Integer>() {
            @Override
            public Integer call() {
                // 長い時間のかかるタスク
                int i = 0;
                try {
                    for (; i < 10; i++) {
                        // 進捗をアップデート
                        updateProgress(i, 10.0);
                        // メッセージを更新
                        updateMessage("Message: " + i);
                        Thread.sleep(1_000);
                    }
                } catch (InterruptedException ex) {}

                return i;
            }
            
            // タスクの終了時処理
            // JavaFX Application Threadで実行
            @Override
            protected void succeeded() {
                // タスクの結果を取得して、表示
                label.setText("DONE Result: " + getValue());
            }
        };

        // タスクの状態が変化したら出力する
        task.stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> value,
                                 Worker.State oldState, Worker.State newState) {
                System.out.println("New: " + newState + " Old: " + oldState);
            }
        });

        // 進捗が変化したら出力
        task.progressProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> value, 
                                 Number oldValue, Number newValue) {
                label.setText("RUNNING: " + newValue.doubleValue() * 100 + "%");
            }
        });

        // メッセージが変化したら出力
//        task.messageProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> value, 
//                                 String oldMessage, String newMessage) {
//                System.out.println(newMessage);
//            }
//        });

        // タスクの実行
        service.submit(task);
    }

    public static void main(String... args) {
        launch(args);
    }
}
