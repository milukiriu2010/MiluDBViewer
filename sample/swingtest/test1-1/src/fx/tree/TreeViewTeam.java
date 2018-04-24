package fx.tree;

import javafx.application.Application;
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

import abc.*;

public class TreeViewTeam extends Application 
{
	TreeView<Team> treeView = new TreeView<>();

	@Override
	public void start(Stage stage) throws Exception 
	{
		Team  teamRoot = new TeamBaseBall();
		teamRoot.setName("プロ野球");
		TreeItem<Team>  itemRoot = new TreeItem<>();
		itemRoot.setValue(teamRoot);
		
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
				return new TextFieldTreeCell<Team>
				(
					new StringConverter<Team>()
					{
						@Override
						public String toString(Team obj)
						{
							return obj.getName();
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
	}
	
    public static void main(String[] args) 
    {
        launch(args);
    }
    /*
    private final class TeamTreeCell extends TreeCell<Team>
    {
    	private TextField textField;
    	
        @Override
        public void startEdit() 
        {
            super.startEdit();
 
            if (textField == null) 
            {
                createTextField();
            }
            setText(null);
            setGraphic(textField);
            textField.selectAll();
        }
        
        @Override
        public void cancelEdit() 
        {
            super.cancelEdit();
            setText( (String)getItem().getName() );
            setGraphic(getTreeItem().getGraphic());
        }
        
        @Override
        public void updateItem(Team item, boolean empty) 
        {
            super.updateItem(item, empty);
 
            if (empty) 
            {
                setText(null);
                setGraphic(null);
            }
            else 
            {
                if (isEditing()) 
                {
                    if (textField != null) 
                    {
                        textField.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField);
                } 
                else 
                {
                    setText(getString());
                    setGraphic(getTreeItem().getGraphic());
                }
            }
        }
        
        private void createTextField() 
        {
            textField = new TextField(getString());
            textField.setOnKeyReleased(new EventHandler<KeyEvent>() 
            {
 
                @Override
                public void handle(KeyEvent t) 
                {
                    if (t.getCode() == KeyCode.ENTER) 
                    {
                    	//commitEdit(textField.getText());
                        //commitEdit
                        //(
                        //	new StringConverter<Team>()
                        //	{
        				//		@Override
        				//		public String toString(Team obj)
        				//		{
        				//			return obj.getName();
        				//		}
        				//		
        				//		@Override
        				//		public Team fromString(String str)
        				//		{
        				//			Team team = new TeamBaseBall();
        				//			team.setName(str);
        				//			return team;
        				//		}
						//
                        //	}
                        //);
                    } 
                    else if (t.getCode() == KeyCode.ESCAPE) 
                    {
                        cancelEdit();
                    }
                }
            });
        }
 
        private String getString() 
        {
            //return getItem() == null ? "" : getItem().toString();
            return getItem() == null ? "" : getItem().getName();
        }        
    }
    */
}
