package fx.tree;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.*;
import javafx.geometry.*;


public class TreeViewSample2 extends Application {

    // Members to store the horizontal and vertical positions
    private ScrollBarState hScrollBarState = null;
    private ScrollBarState vScrollBarState = null;

    TreeView<String> tree;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Tree View Sample with scrollbars");

        TreeItem<String> rootItem = new TreeItem<String>("Items");
        rootItem.setExpanded(true);
        for (int i = 1; i < 30; i++) {
            TreeItem<String> item = new TreeItem<String>("Long long long long long long long Item" + i);
            rootItem.getChildren().add(item);
        }

        tree = new TreeView<String>(rootItem);
        VBox root = new VBox();


        Button buttonSave = new Button("Save ScrollBars!");
        buttonSave.setOnAction((event) -> {
            // On save get the scrollbars and update the members (if the scrollbar is present)
            ScrollBar bar = getScrollBar(tree, Orientation.HORIZONTAL);
            if (bar != null)
                hScrollBarState = new ScrollBarState(bar.getMin(), bar.getMax(), bar.getValue(), bar.getBlockIncrement(), bar.getUnitIncrement());

            bar = getScrollBar(tree, Orientation.VERTICAL);

            if (bar != null)
                vScrollBarState = new ScrollBarState(bar.getMin(), bar.getMax(), bar.getValue(), bar.getBlockIncrement(), bar.getUnitIncrement());

        });

        Button buttonRestore = new Button("Restore ScrollBars!");
        buttonRestore.setOnAction((event) -> {

            restoreScrollBarPositions(getScrollBar(tree, Orientation.HORIZONTAL), hScrollBarState);
            restoreScrollBarPositions(getScrollBar(tree, Orientation.VERTICAL), vScrollBarState);
        });

        root.getChildren().addAll(tree, buttonSave, buttonRestore);

        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }

    private ScrollBar getScrollBar(TreeView<?> tree, Orientation orientation) {
        // Get the ScrollBar with the given Orientation using lookupAll
        for (Node n : tree.lookupAll(".scroll-bar")) {
            if (n instanceof ScrollBar) {
                ScrollBar bar = (ScrollBar) n;

                if (bar.getOrientation().equals(orientation))
                    return bar;
            }
        }
        return null;
    }

    private void restoreScrollBarPositions(ScrollBar bar, ScrollBarState state) {
        // Set back the position values if they present
        if (bar != null && state != null) {
            bar.setMin(state.min);
            bar.setMax(state.max);
            bar.setValue(state.value);
            bar.setUnitIncrement(state.unitIncrement);
            bar.setBlockIncrement(state.blockIncrement);
        }
    }

    // Simple class to store the position values
    class ScrollBarState {
        double min;
        double max;
        double value;
        double blockIncrement;
        double unitIncrement;

        ScrollBarState(double min, double max, double value, double blockInc, double unitInc) {
            this.min = min;
            this.max = max;
            this.value = value;
            this.blockIncrement = blockInc;
            this.unitIncrement = unitInc;
        }
    }
}
