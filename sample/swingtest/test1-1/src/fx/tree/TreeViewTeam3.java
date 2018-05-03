package fx.tree;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.TreeView;
import javafx.scene.control.TreeItem;
//import javafx.scene.control.TreeCell;
//import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTreeCell;
//import javafx.scene.input.KeyEvent;
//import javafx.scene.input.KeyCode;
//import javafx.event.EventHandler;
import javafx.util.StringConverter;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.util.List;
import java.util.ArrayList;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import javafx.concurrent.Task;

import abc.*;

public class TreeViewTeam3 extends Application 
{
	TreeView<Team> treeView = new TreeView<>();
	
	//private ExecutorService service = Executors.newSingleThreadExecutor();
	private ExecutorService service = Executors.newFixedThreadPool(5);
	
	private boolean loading = false;

	@Override
	public void start(Stage stage) throws Exception 
	{
		Team  teamRoot = new TeamBaseBall();
		teamRoot.setName("プロ野球");
		TreeItem<Team>  itemRoot = new TreeItem<>();
		itemRoot.setValue(teamRoot);
		itemRoot.setExpanded(true);
		
		Team teamCentral = new TeamBaseBall();
		teamCentral.setName("セントラル・リーグ");
		TreeItem<Team>  itemCentral = new TreeItem<>();
		itemCentral.setValue(teamCentral);
		itemRoot.getChildren().add(itemCentral);
		
		Team teamTigers = new TeamBaseBall();
		teamTigers.setName("阪神");
		TreeItem<Team>  itemTigers = new TreeItem<>();
		itemTigers.setValue(teamTigers);
		itemCentral.getChildren().add(itemTigers);
		
		Team teamGiants = new TeamBaseBall();
		teamGiants.setName("巨人");
		TreeItem<Team>  itemGiants = new TreeItem<>();
		itemGiants.setValue(teamGiants);
		itemCentral.getChildren().add(itemGiants);
		
		this.treeView.setRoot(itemRoot);
		this.treeView.setEditable(true);
		
		// https://stackoverflow.com/questions/39465985/javafx-treeview-edit-item?rq=1&utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
		this.treeView.setCellFactory
		(
			(treeItem)->
			{
				
				TextFieldTreeCell<Team> textCell = new TextFieldTreeCell<Team>();
				textCell.setConverter
				(
					new StringConverter<Team>()
					{
						@Override
						public String toString(Team obj)
						{
							if ( loading == false )
							{
								return obj.getName();
							}
							else
							{
								TreeItem<Team> selectedItem = treeView.getSelectionModel().getSelectedItem();
								TreeItem<Team> textCellItem = textCell.getTreeItem();
								if ( selectedItem == textCellItem )
								{
									return obj.getName() + "...loading...";
								}
								else
								{
									return obj.getName();
								}
							}
						}
						
						@Override
						public Team fromString(String str)
						{
							Team team = new TeamBaseBall();
							team.setName(str);
							return team;
						}
					}
				);
				
				return textCell;
			}
		);
		this.treeView.setOnEditCommit
		(
			(event)->
			{
				System.out.println("edit commit.");
			}
		);
		/* */
		/*
		this.treeView.setCellFactory
		(
			(treeItem)->
			{
				return new TeamTreeCell();
			}
		);
		*/
		
		Scene scene = new Scene(treeView, 640, 480 );
		stage.setScene(scene);
		stage.show();
		
		this.treeView.getSelectionModel().selectedItemProperty().addListener
		(
			(obs,oldVal,newVal)->
			{
				Callable<Integer> task = new Callable<Integer>()
				{
					@Override
					public Integer call()
					{
						try
						{
							System.out.println( "loading..." );
							loading = true;
							Platform.runLater( ()->treeView.refresh() );
							Thread.sleep(1000);
							List<Callable<Integer>> taskLst = new ArrayList<>();
							for ( int i = 2; i <= 5; i++ )
							{
								Callable<Integer> taskX = new SleepTask(i);
								taskLst.add(taskX);
							}
							List<Future<Integer>> futureLst = service.invokeAll(taskLst);
							futureLst.stream().forEach
							( 
								(future)->
								{
									try
									{
										System.out.println(future.get());
									}
									catch ( Exception ex3 )
									{
										
									}
								}
							);
							loading = false;
							Thread.sleep(100);
							Platform.runLater( ()->treeView.refresh() );
							System.out.println( "loaded." );
							return Integer.valueOf(1);
						}
						catch ( Exception ex )
						{
							ex.printStackTrace();
							return Integer.valueOf(-1);
						}
						finally
						{
							
						}
					}
				};
				service.submit(task);
				
				/*
				List<Callable<Integer>> taskLst = new ArrayList<>();
				taskLst.add(task);
				for ( int i = 2; i <= 5; i++ )
				{
					final int ii = i;
					Callable<Integer> taskX = new Callable<Integer>()
					{
						@Override
						public Integer call()
						{
							try
							{
								Thread.sleep(1000);
								System.out.println(ii);
								return Integer.valueOf(ii);
							}
							catch ( Exception ex2 )
							{
								ex2.printStackTrace();
								return Integer.valueOf(-1*ii);
							}
						}
					};
					taskLst.add(taskX);
				}
				
				//service.submit(task);
				try
				{
					List<Future<Integer>> futureLst = service.invokeAll(taskLst);
					futureLst.stream().forEach
					( 
						(future)->
						{
							try
							{
								System.out.println(future.get());
							}
							catch ( Exception ex3 )
							{
								
							}
						}
					);
					loading = false;
					Thread.sleep(100);
					Platform.runLater( ()->treeView.refresh() );
					System.out.println( "loaded." );
				}
				catch ( InterruptedException intEx )
				{
					intEx.printStackTrace();
				}
				*/
			}
		);
	}
	
    public static void main(String[] args) 
    {
        launch(args);
    }
    
    class SleepTask implements Callable<Integer>
    {
    	private int num = 0;
    	
    	SleepTask( int num )
    	{
    		this.num = num;
    	}
    	
    	@Override
    	public Integer call()
    	{
			try
			{
				Thread.sleep(1000);
				System.out.println(num);
				return Integer.valueOf(num);
			}
			catch ( Exception ex2 )
			{
				ex2.printStackTrace();
				return Integer.valueOf(-1*num);
			}
    	}
    }
}
