package fx.combo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;

import abc.Team;
import abc.TeamBaseBall;
import abc.TeamSoccer;

// https://stackoverflow.com/questions/19242747/javafx-editable-combobox-showing-tostring-on-item-selection?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
public class ComboBoxString extends Application 
{
	private ComboBox<Team>  teamComboBox = new ComboBox<>();

	@Override
	public void start(Stage stage) throws Exception 
	{
		
		BorderPane  brdPane = new BorderPane();
		brdPane.setTop(this.teamComboBox);
		
		Scene scene = new Scene( this.teamComboBox, 640, 480 );
		stage.setScene(scene);
		stage.show();
		
	}
	
    public static void main(String[] args) 
    {
        launch(args);
    }
}
