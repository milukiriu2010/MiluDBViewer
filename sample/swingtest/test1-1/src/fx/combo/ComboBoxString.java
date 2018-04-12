package fx.combo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;

import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.List;

import abc.Team;
import abc.TeamBaseBall;
import abc.TeamSoccer;
import abc.TeamBaseBall.LEAGUE;

// https://stackoverflow.com/questions/19242747/javafx-editable-combobox-showing-tostring-on-item-selection?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
public class ComboBoxString extends Application 
{
	private ComboBox<Team>  teamComboBox = new ComboBox<>();
	
	List<Team>  teamList = new ArrayList<Team>();

	@Override
	public void start(Stage stage) throws Exception 
	{
		this.createTeamList();
		
		this.teamComboBox.setConverter
		(
			new StringConverter<Team>()
			{
				@Override
				public String toString(Team team)
				{
					if ( team == null )
					{
						return "";
					}
					else
					{
						return team.getName();
					}
				}
				
				@Override
				public Team fromString(String str)
				{
					//throw new UnsupportedOperationException();
					return null;
				}
			}
		);
		
		this.teamComboBox.getItems().addAll(this.teamList);
		
		BorderPane  brdPane = new BorderPane();
		brdPane.setTop(this.teamComboBox);
		
		Scene scene = new Scene( brdPane, 640, 480 );
		stage.setScene(scene);
		stage.show();
	}
	
	private void createTeamList()
	{
		TeamBaseBall  tigers = new TeamBaseBall();
		tigers.setType("baseball");
		tigers.setName("tigers");
		tigers.setLeague( LEAGUE.CENTRAL );
		tigers.addPlayer("nomi");
		tigers.addPlayer("fujinami");
		tigers.putYearPosMap( 2016, 4 );
		tigers.putYearPosMap( 2017, 2 );
		teamList.add(tigers);

		TeamBaseBall  giants = new TeamBaseBall();
		giants.setType("baseball");
		giants.setName("giants");
		giants.setLeague( LEAGUE.CENTRAL );
		giants.addPlayer("sugano");
		giants.addPlayer("sawamura");
		giants.putYearPosMap( 2016, 2 );
		giants.putYearPosMap( 2017, 4 );
		teamList.add(giants);

		TeamSoccer  urawa = new TeamSoccer();
		urawa.setType("soccer");
		urawa.setName("urawa");
		urawa.setLevel( 1 );
		urawa.addPlayer("makino");
		urawa.addPlayer("kouroki");
		urawa.putYearPosMap( 2016, 1 );
		urawa.putYearPosMap( 2017, 7 );
		teamList.add(urawa);

		TeamSoccer  kashima = new TeamSoccer();
		kashima.setType("soccer");
		kashima.setName("kashima");
		kashima.setLevel( 1 );
		kashima.addPlayer("ogasawara");
		kashima.addPlayer("sogahata");
		kashima.putYearPosMap( 2016, 11 );
		kashima.putYearPosMap( 2017, 2 );
		teamList.add(kashima);
	}
	
    public static void main(String[] args) 
    {
        launch(args);
    }
}
