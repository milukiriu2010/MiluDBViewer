package file.json.my;

import java.util.Set;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Collections;
import java.lang.reflect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.GsonBuilder;

import abc.TeamBaseBall.LEAGUE;
import abc.Team;
import abc.TeamBaseBall;
import abc.TeamSoccer;

public class GsonExample extends Application
{
	private TextArea  taJSon    = new TextArea();
	private TextArea  taResult  = new TextArea();
	private Button    btnSimple = new Button("simple");
	private Button    btnEntry  = new Button("entry");
	private Button    btnObj2Json   = new Button("List<Team>2Json");
	private Button    btnJson2JsonElement = new Button("json2jsonelement");
	private Button    btnJson2ListTeam = new Button("Json2List<Team>");
	private Button    btnJson2ListTeamBaseBall = new Button("Json2List<TeamBaseBall>");
	private Button    btnClone  = new Button("clone");

	@Override
	public void start(Stage stage) throws Exception 
	{
		taJSon.setText
		(
			"{\r\n" + 
			"	\"teams\": [{\r\n" + 
			"			\"type\": \"baseball\",\r\n" + 
			"			\"team_name\": \"tigers\",\r\n" + 
			"			\"players\": [\"nomi\", \"fujinami\"]\r\n" + 
			"		},\r\n" + 
			"		{\r\n" + 
			"			\"type\": \"baseball\",\r\n" + 
			"			\"team_name\": \"giants\",\r\n" + 
			"			\"players\": [\"sugano\", \"sawamura\"]\r\n" + 
			"		},\r\n" + 
			"		{\r\n" + 
			"			\"type\": \"soccer\",\r\n" + 
			"			\"team_name\": \"urawa\",\r\n" + 
			"			\"players\": [\"makino\", \"kouroki\"]\r\n" + 
			"		},\r\n" + 
			"		{\r\n" + 
			"			\"type\": \"soccer\",\r\n" + 
			"			\"team_name\": \"kashima\",\r\n" + 
			"			\"players\": [\"ogasawara\", \"sogahata\"]\r\n" + 
			"		}\r\n" + 
			"	]\r\n" + 
			"}"	
		);
		
		btnSimple.setOnAction( e->simple() );
		btnEntry.setOnAction( e->entry() );
		btnObj2Json.setOnAction( e->obj2Json() );
		btnJson2JsonElement.setOnAction( e->json2jsonelement() );
		btnJson2ListTeam.setOnAction( e->json2ListTeam() );
		btnJson2ListTeamBaseBall.setOnAction( e->json2ListTeamBaseBall() );
		btnClone.setOnAction( e->cloneTest() );
		
		HBox hBoxParse = new HBox(2);
		hBoxParse.getChildren().addAll
		( 
			btnSimple, 
			btnEntry, 
			btnObj2Json, 
			btnJson2JsonElement,
			btnJson2ListTeam,
			btnJson2ListTeamBaseBall,
			btnClone
		);
		
		ToggleGroup tglGroup = new ToggleGroup();
		ToggleButton tb1 = new ToggleButton( "1" );
		tb1.setToggleGroup(tglGroup);
		ToggleButton tb2 = new ToggleButton( "2" );
		tb2.setToggleGroup(tglGroup);
		ToggleButton tb3 = new ToggleButton( "3" );
		tb3.setToggleGroup(tglGroup);
		
		tglGroup.selectedToggleProperty().addListener
		(
			(obs,oldVal,newVal)->
			{
				if ( newVal  == tb1 )
				{
					taJSon.setText
					(
						"{\r\n" + 
						"	\"teams\": [{\r\n" + 
						"			\"type\": \"baseball\",\r\n" + 
						"			\"team_name\": \"tigers\",\r\n" + 
						"			\"players\": [\"nomi\", \"fujinami\"]\r\n" + 
						"		},\r\n" + 
						"		{\r\n" + 
						"			\"type\": \"baseball\",\r\n" + 
						"			\"team_name\": \"giants\",\r\n" + 
						"			\"players\": [\"sugano\", \"sawamura\"]\r\n" + 
						"		},\r\n" + 
						"		{\r\n" + 
						"			\"type\": \"soccer\",\r\n" + 
						"			\"team_name\": \"urawa\",\r\n" + 
						"			\"players\": [\"makino\", \"kouroki\"]\r\n" + 
						"		},\r\n" + 
						"		{\r\n" + 
						"			\"type\": \"soccer\",\r\n" + 
						"			\"team_name\": \"kashima\",\r\n" + 
						"			\"players\": [\"ogasawara\", \"sogahata\"]\r\n" + 
						"		}\r\n" + 
						"	]\r\n" + 
						"}"
					);
				}
				else if ( newVal  == tb2 )
				{
					taJSon.setText
					(
						"[\r\n" + 
						"  {\r\n" + 
						"    \"league\": \"CENTRAL\",\r\n" + 
						"    \"type\": \"baseball\",\r\n" + 
						"    \"name\": \"tigers\",\r\n" + 
						"    \"playerLst\": [\r\n" + 
						"      \"nomi\",\r\n" + 
						"      \"fujinami\"\r\n" + 
						"    ],\r\n" + 
						"    \"yearPosMap\": {\r\n" + 
						"      \"2016\": 4,\r\n" + 
						"      \"2017\": 2\r\n" + 
						"    }\r\n" + 
						"  },\r\n" + 
						"  {\r\n" + 
						"    \"league\": \"CENTRAL\",\r\n" + 
						"    \"type\": \"baseball\",\r\n" + 
						"    \"name\": \"giants\",\r\n" + 
						"    \"playerLst\": [\r\n" + 
						"      \"sugano\",\r\n" + 
						"      \"sawamura\"\r\n" + 
						"    ],\r\n" + 
						"    \"yearPosMap\": {\r\n" + 
						"      \"2016\": 2,\r\n" + 
						"      \"2017\": 4\r\n" + 
						"    }\r\n" + 
						"  },\r\n" + 
						"  {\r\n" + 
						"    \"level\": 1,\r\n" + 
						"    \"type\": \"soccer\",\r\n" + 
						"    \"name\": \"urawa\",\r\n" + 
						"    \"playerLst\": [\r\n" + 
						"      \"makino\",\r\n" + 
						"      \"kouroki\"\r\n" + 
						"    ],\r\n" + 
						"    \"yearPosMap\": {\r\n" + 
						"      \"2016\": 1,\r\n" + 
						"      \"2017\": 7\r\n" + 
						"    }\r\n" + 
						"  },\r\n" + 
						"  {\r\n" + 
						"    \"level\": 1,\r\n" + 
						"    \"type\": \"soccer\",\r\n" + 
						"    \"name\": \"kashima\",\r\n" + 
						"    \"playerLst\": [\r\n" + 
						"      \"ogasawara\",\r\n" + 
						"      \"sogahata\"\r\n" + 
						"    ],\r\n" + 
						"    \"yearPosMap\": {\r\n" + 
						"      \"2016\": 11,\r\n" + 
						"      \"2017\": 2\r\n" + 
						"    }\r\n" + 
						"  }\r\n" + 
						"]"
					);					
				}
				else if ( newVal == tb3 )
				{
					taJSon.setText
					(
						"[\r\n" + 
						"  {\r\n" + 
						"    \"league\": \"CENTRAL\",\r\n" + 
						"    \"type\": \"baseball\",\r\n" + 
						"    \"name\": \"tigers\",\r\n" + 
						"    \"playerLst\": [\r\n" + 
						"      \"nomi\",\r\n" + 
						"      \"fujinami\"\r\n" + 
						"    ],\r\n" + 
						"    \"yearPosMap\": {\r\n" + 
						"      \"2016\": 4,\r\n" + 
						"      \"2017\": 2\r\n" + 
						"    },\r\n" + 
						"    \"CLASS_META_KEY\": \"file.json.my.TeamBaseBall\"\r\n" + 
						"  },\r\n" + 
						"  {\r\n" + 
						"    \"league\": \"CENTRAL\",\r\n" + 
						"    \"type\": \"baseball\",\r\n" + 
						"    \"name\": \"giants\",\r\n" + 
						"    \"playerLst\": [\r\n" + 
						"      \"sugano\",\r\n" + 
						"      \"sawamura\"\r\n" + 
						"    ],\r\n" + 
						"    \"yearPosMap\": {\r\n" + 
						"      \"2016\": 2,\r\n" + 
						"      \"2017\": 4\r\n" + 
						"    },\r\n" + 
						"    \"CLASS_META_KEY\": \"file.json.my.TeamBaseBall\"\r\n" + 
						"  },\r\n" + 
						"  {\r\n" + 
						"    \"level\": 1,\r\n" + 
						"    \"type\": \"soccer\",\r\n" + 
						"    \"name\": \"urawa\",\r\n" + 
						"    \"playerLst\": [\r\n" + 
						"      \"makino\",\r\n" + 
						"      \"kouroki\"\r\n" + 
						"    ],\r\n" + 
						"    \"yearPosMap\": {\r\n" + 
						"      \"2016\": 1,\r\n" + 
						"      \"2017\": 7\r\n" + 
						"    },\r\n" + 
						"    \"CLASS_META_KEY\": \"file.json.my.TeamSoccer\"\r\n" + 
						"  },\r\n" + 
						"  {\r\n" + 
						"    \"level\": 1,\r\n" + 
						"    \"type\": \"soccer\",\r\n" + 
						"    \"name\": \"kashima\",\r\n" + 
						"    \"playerLst\": [\r\n" + 
						"      \"ogasawara\",\r\n" + 
						"      \"sogahata\"\r\n" + 
						"    ],\r\n" + 
						"    \"yearPosMap\": {\r\n" + 
						"      \"2016\": 11,\r\n" + 
						"      \"2017\": 2\r\n" + 
						"    },\r\n" + 
						"    \"CLASS_META_KEY\": \"file.json.my.TeamSoccer\"\r\n" + 
						"  }\r\n" + 
						"]"
					);
				}
			}
		);
		
		HBox hBoxTG = new HBox(2);
		hBoxTG.getChildren().addAll( tb1, tb2, tb3 );		
		
		VBox vBox = new VBox(2);
		vBox.getChildren().addAll( taJSon, hBoxParse, hBoxTG );
		
		SplitPane sp = new SplitPane();
		sp.setOrientation( Orientation.VERTICAL );
		sp.getItems().addAll( vBox, taResult );
		sp.setDividerPositions( 0.5f, 0.5f );
		
		Scene scene = new Scene( sp, 640, 480 );
		stage.setScene(scene);
		stage.setResizable(true);
		stage.show();
	}
	
	private void simple()
	{
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse( taJSon.getText() );
		
		StringBuffer sb = new StringBuffer( "[simple]\n" );
		
		if ( element.isJsonObject() )
		{
			JsonObject root = element.getAsJsonObject();
			JsonArray  teams = root.getAsJsonArray("teams");
			for ( int i = 0; i < teams.size(); i++ )
			{
				JsonObject team = teams.get(i).getAsJsonObject();
				sb.append( "type=>" + team.get("type").getAsString() + "\n" );
				sb.append( "team_name=>" + team.get("team_name").getAsString() + "\n" );
				
				JsonArray players = team.getAsJsonArray("players");
				for ( int j = 0; j < players.size(); j++ )
				{
					sb.append( "  player_name=>" + players.get(j).getAsString() + "\n" );
				}
			}
		}
		
		taResult.setText(sb.toString());
	}
	
	private void entry()
	{
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse( taJSon.getText() );
		
		StringBuffer sb = new StringBuffer( "[entry]\n" );
		
		analyze( element, 0, sb );
		
		taResult.setText(sb.toString());
	}
	
	private void analyze( JsonElement element, int level, StringBuffer sb )
	{
		String tab = String.join( "",  Collections.nCopies( level, "  " ) );
		if ( element.isJsonPrimitive() )
		{
			sb.append( tab + "val[" + element.getAsString() + "]\n" );
		}
		else if ( element.isJsonObject() )
		{
			Set<Map.Entry<String,JsonElement>> entrySet = element.getAsJsonObject().entrySet();
			
			Iterator<Map.Entry<String,JsonElement>> iterator = entrySet.iterator();
			while ( iterator.hasNext() )
			{
				Map.Entry<String,JsonElement>  map = iterator.next();
				sb.append( tab + "key[" + map.getKey() + "]\n" );
				analyze( map.getValue(), level+1, sb );
			}
		}
		else if ( element.isJsonArray() )
		{
			JsonArray array = element.getAsJsonArray();
			sb.append( tab + "array=>\n" );
			array.forEach( val->analyze( val, level+1, sb ) );
		}
	}
	
	private void obj2Json()
	{
		List<Team>  teamList = new ArrayList<Team>();

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
		
		Type type = new TypeToken<List<Team>>() {}.getType();
		
		//Gson gson2 = new GsonBuilder().setPrettyPrinting().create();
		//String json2 = gson2.toJson(teamList, type );
		
		// for abstract
		// https://stackoverflow.com/questions/3629596/deserializing-an-abstract-class-in-gson
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter( Team.class, new TeamElementAdapter() );
		Gson gson2 = gsonBuilder.setPrettyPrinting().create();
		String json2 = gson2.toJson(teamList, type );
		
		taResult.setText(json2);
	}
	
	private void json2jsonelement()
	{
		Gson gson = new Gson();
		
		JsonElement element = gson.fromJson( taJSon.getText(), JsonElement.class );
		
		StringBuffer sb = new StringBuffer( "[json2jsonelement]\n" );
		
		analyze( element, 0, sb );
		
		taResult.setText(sb.toString());
	}
	
	private void json2ListTeam()
	{
		//Gson gson = new Gson();
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter( Team.class, new TeamElementAdapter() );
		Gson gson = gsonBuilder.create();
		
		Type type = new TypeToken<ArrayList<Team>>() {}.getType();
		List<Team> teamList = gson.fromJson( taJSon.getText(), type );
		
		StringBuffer sb = new StringBuffer("[json2ListTeam]\n");
		teamList.forEach
		(
			(team)->
			{
				sb.append( team.getClass() + "\n" );
				sb.append( "  " + team.getType() + "\n" );
				if ( team instanceof TeamBaseBall )
				{
					sb.append( "  " + ((TeamBaseBall)team).getLeague().toString() + "\n" );
				}
				else if ( team instanceof TeamSoccer )
				{
					sb.append( "  " + ((TeamSoccer)team).getLevel() + "\n" );
				}
				team.getPlayerLst().forEach( (player)->sb.append( "  " + player + "\n" ) );
				team.getYearPosMap().forEach( (year,pos)->sb.append( "  " + year + ":" + pos + "\n" ));
			}
		);
		taResult.setText(sb.toString());
	}
	
	private void json2ListTeamBaseBall()
	{
		Gson gson = new Gson();
		
		Type type = new TypeToken<ArrayList<TeamBaseBall>>() {}.getType();
		List<Team> teamList = gson.fromJson( taJSon.getText(), type );
		
		StringBuffer sb = new StringBuffer("[json2ListTeamBaseBall]\n");
		teamList.forEach
		(
			(team)->
			{
				sb.append( team.getClass() + "\n" );
				sb.append( "  " + team.getType() + "\n" );
				sb.append( "  " + ((TeamBaseBall)team).getLeague().toString() + "\n" );
				team.getPlayerLst().forEach( (player)->sb.append( "  " + player + "\n" ) );
				team.getYearPosMap().forEach( (year,pos)->sb.append( "  " + year + ":" + pos + "\n" ));
			}
		);
		taResult.setText(sb.toString());
	}
	
	private void cloneTest()
	{
		List<Team>  teamList = new ArrayList<Team>();
		
		TeamBaseBall  tigers = new TeamBaseBall();
		tigers.setType("baseball");
		tigers.setName("tigers");
		tigers.setLeague( LEAGUE.CENTRAL );
		tigers.addPlayer("nomi");
		tigers.addPlayer("fujinami");
		tigers.putYearPosMap( 2016, 4 );
		tigers.putYearPosMap( 2017, 2 );
		teamList.add(tigers);

		TeamSoccer  urawa = new TeamSoccer();
		urawa.setType("soccer");
		urawa.setName("urawa");
		urawa.setLevel( 1 );
		urawa.addPlayer("makino");
		urawa.addPlayer("kouroki");
		urawa.putYearPosMap( 2016, 1 );
		urawa.putYearPosMap( 2017, 7 );
		teamList.add(urawa);
		
		@SuppressWarnings("unchecked")
		List<Team> teamListClone = (List<Team>)((ArrayList<Team>)teamList).clone();
		Team tigersClone = null;
		try
		{
			tigersClone = tigers.clone();
		}
		catch ( CloneNotSupportedException cloneEx )
		{
			cloneEx.printStackTrace();
		}
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("<<original:list>>\n");
		teamList.forEach
		(
			(team)->
			{
				sb.append( team.hashCode() + "\n" );
				sb.append( team.getClass() + "\n" );
				sb.append( team.getName().hashCode() + "/" + team.getName() + "\n" );
				if ( team instanceof TeamBaseBall)
				{
					sb.append( "  " + ((TeamBaseBall)team).getLeague().toString() + "\n" );
				}
				team.getPlayerLst().forEach
				( 
					(player)->
					{
						sb.append( "  " + player.hashCode() + "/" + player + "\n" ); 
					}
				);
				//team.getPlayerLst().forEach( (player)->sb.append( "  " + player + "\n" ) );
				//team.getYearPosMap().forEach( (year,pos)->sb.append( "  " + year + ":" + pos + "\n" ));
			}
		);
		
		sb.append("<<clone:list>>\n");
		teamListClone.forEach
		(
			(team)->
			{
				sb.append( team.hashCode() + "\n" );
				sb.append( team.getClass() + "\n" );
				sb.append( team.getName().hashCode() + "/" + team.getName() + "\n" );
				if ( team instanceof TeamBaseBall)
				{
					sb.append( "  " + ((TeamBaseBall)team).getLeague().toString() + "\n" );
				}
				team.getPlayerLst().forEach
				( 
					(player)->
					{
						sb.append( "  " + player.hashCode() + "/" + player + "\n" ); 
					}
				);
				//team.getPlayerLst().forEach( (player)->sb.append( "  " + player + "\n" ) );
				//team.getYearPosMap().forEach( (year,pos)->sb.append( "  " + year + ":" + pos + "\n" ));
			}
		);
		
		
		sb.append("<<original:each>>\n");
		sb.append( tigers.hashCode() + "\n" );
		sb.append( tigers.getClass() + "\n" );
		sb.append( tigers.getName().hashCode() + "/" + tigers.getName() + "\n" );
		tigers.getPlayerLst().forEach
		( 
			(player)->
			{
				sb.append( "  " + player.hashCode() + "/" + player + "\n" ); 
			}
		);
		sb.append("<<clone:each>>\n");
		sb.append( tigersClone.hashCode() + "\n" );
		sb.append( tigersClone.getClass() + "\n" );
		sb.append( tigersClone.getName().hashCode() + "/" + tigersClone.getName() + "\n" );
		tigersClone.getPlayerLst().forEach
		( 
			(player)->
			{
				sb.append( "  " + player.hashCode() + "/" + player + "\n" );
			}
		);
		
		taResult.setText(sb.toString());
		
		String xxx1 = "xxx";
		String xxx2 = new String( xxx1 );
		System.out.println( "xxx1:"+xxx1.hashCode() );
		System.out.println( "xxx2:"+xxx2.hashCode() );
		
		Integer int1 = Integer.valueOf(111);
		Integer int2 = Integer.valueOf(111);
		System.out.println( "int1:"+int1.hashCode() );
		System.out.println( "int2:"+int2.hashCode() );
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch( args );
	}

}
