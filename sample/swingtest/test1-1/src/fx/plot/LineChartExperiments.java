package fx.plot;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// http://tutorials.jenkov.com/javafx/linechart.html
public class LineChartExperiments extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("LineChart Experiments");

        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("No of employees");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Revenue per employee");

        LineChart<Number,Number> lineChart = new LineChart<>(xAxis, yAxis);

        XYChart.Series<Number,Number> dataSeries1 = new XYChart.Series<>();
        dataSeries1.setName("2014");

        dataSeries1.getData().add(new XYChart.Data<Number,Number>( 1, 567));
        dataSeries1.getData().add(new XYChart.Data<Number,Number>( 5, 612));
        dataSeries1.getData().add(new XYChart.Data<Number,Number>(10, 800));
        dataSeries1.getData().add(new XYChart.Data<Number,Number>(20, 780));
        dataSeries1.getData().add(new XYChart.Data<Number,Number>(40, 810));
        dataSeries1.getData().add(new XYChart.Data<Number,Number>(80, 850));

        lineChart.getData().add(dataSeries1);

        VBox vbox = new VBox(lineChart);

        Scene scene = new Scene(vbox, 400, 200);

        primaryStage.setScene(scene);
        primaryStage.setHeight(300);
        primaryStage.setWidth(1200);

        primaryStage.show();
    }


    public static void main(String[] args) {
        Application.launch(args);
    }
}
