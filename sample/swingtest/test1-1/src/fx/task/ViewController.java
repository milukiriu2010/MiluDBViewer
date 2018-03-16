package fx.task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class ViewController implements Initializable {
    @FXML
    private TextField fileName;
    @FXML
    private TextArea fileContents;
    @FXML
    private ProgressIndicator progressIndicator;

    private ExecutorService service = Executors.newSingleThreadExecutor();
    
    // データ交換用のブロッキングキュー
    private BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    
    // 定期的にキューに入っているデータを取り出すためのタイマ
    private Timeline timer;

    class FileLoadTask extends Task<Void> {
        // 読み込むファイル
        private File file;

        public FileLoadTask(File file) {
            this.file = file;
        }

        @Override
        protected Void call() throws IOException {
            long size = file.length();
            long readByte = 0;
            
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                for (;;) {
                    final String line = reader.readLine();
                    if (line == null) {
                        break;
                    }

                    // 進捗を更新
                    readByte += line.getBytes().length;
                    updateProgress(readByte, size);
                    
                    // 読み込んだ行をエンキューする
                    queue.offer(line);
                }
            }

            return null;
        }
    }

    @FXML
    private void openFile(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        
        File file = chooser.showOpenDialog(null);

        if (file != null) {
            fileName.setText(file.getAbsolutePath());
            loadFile(file);
        }
    }

    @FXML
    private void actionPerformed(ActionEvent event) {
        String filenameText = fileName.getText();
        File file = new File(filenameText);

        loadFile(file);
    }

    private void loadFile(File file) {
        // プログレスインディケータを表示する
        progressIndicator.setOpacity(1.0);

        // タスクを生成
        final FileLoadTask task = new FileLoadTask(file);

        // タスクの処理の進捗をブログレスインディケータにバインドする
        progressIndicator.progressProperty().bind(task.progressProperty());

        // タスクが終了したら、プログレスインディケータを隠す
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                progressIndicator.setOpacity(0.0);
            }
        });
        
        // タスクの実行
        service.submit(task);

        // テキストエリアの内容をクリアする
        fileContents.clear();
        
        // 100ミリ秒ごとにキューからデータを取得するためのタイマ
        timer = new Timeline(
            new KeyFrame(
                Duration.millis(100), 
                new EventHandler<ActionEvent>() {
                 @Override
                 public void handle(ActionEvent event) {
                     try {
                         // キューが空でなければ、データを取り出す
                         StringBuilder builder = new StringBuilder();
                         while(!queue.isEmpty()) {
                            String line = queue.take();
                            builder.append(line);
                            builder.append("\n");
                         }

                         // キューから取り出したデータをテキストエリアに追加
                         fileContents.appendText(builder.toString());
                    } catch (InterruptedException ex) {}
                     
                    // タスクが終了していたら、タイマを止め
                    // テキストエリアのキャレットを先頭にしておく
                    if (task.isDone() || task.isCancelled()) {
                        timer.stop();
                        fileContents.home();
                    }
                }
            })
        );
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    public void setStage(Stage stage) {
        // ステージがクローズする場合、ExecutorServiceを停止させる
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                service.shutdown();
            }
        });
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {}
}
