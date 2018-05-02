package milu.gui.dlg.app;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

class BorderedTitledPane extends StackPane 
{
	BorderedTitledPane(String titleString, Node content) 
	{
		Label title = new Label("  " + titleString + "  ");
		title.getStyleClass().add("AppPane_bordered-titled-title");
		StackPane.setAlignment(title, Pos.TOP_LEFT);
		
		StackPane contentPane = new StackPane();
		content.getStyleClass().add("AppPane_bordered-titled-content");
		contentPane.getChildren().add(content);
		
		getStyleClass().add("AppPane_bordered-titled-border");
		getChildren().addAll(title, contentPane);
    }
}
