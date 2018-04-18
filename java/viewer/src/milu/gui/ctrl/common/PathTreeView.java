package milu.gui.ctrl.common;

import javafx.scene.control.TreeView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.util.StringConverter;
import javafx.scene.control.TreeItem;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.io.File;
import java.io.IOException;

import milu.gui.dlg.MyAlertDialog;
import milu.main.MainController;
import milu.tool.MyTool;

// https://stackoverflow.com/questions/34534775/configuring-a-treeview-which-scans-local-fie-system-to-only-include-folders-whic?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
public class PathTreeView extends TreeView<Path> 
{
	private MainController mainCtrl = null;
	
	private String  rootDir = null;
	
	// File Extension
	private String  fileExt = "";
	
	public void setMainController( MainController mainCtrl )
	{
		this.mainCtrl = mainCtrl;
	}
	
	public void setRootDir( String rootDir )
	{
		this.rootDir = rootDir;
	}
	
	public void setFileExt( String fileExt )
	{
		this.fileExt = fileExt;
	}
	
	public void init() throws IOException
	{
		// create root
		TreeItem<Path> itemRoot = new TreeItem<Path>(Paths.get(this.rootDir));
		itemRoot.setExpanded(true);
		itemRoot.setGraphic( MyTool.createImageView( 16, 16, this.mainCtrl.getImage("file:resources/images/folder.png") ) );
		this.setRoot(itemRoot);
		this.setShowRoot(false);
		
		// create tree structure
		this.createTree(itemRoot);
		
		// configure this TreeView
		this.setEditable(true);
		this.setCellFactory
		(
			(treeView)->
			{
				return new TextFieldTreeCell<Path>
				(
					new StringConverter<Path>()
					{
						@Override
						public String toString(Path path)
						{
							if ( Files.isDirectory(path) )
							{
								return path.getFileName().toString();
							}
							else if ( Files.isRegularFile(path) )
							{
								String fileName = path.getFileName().toString();
								int pos = fileName.lastIndexOf(".");
								// No file extension 
								if ( pos <= 0 )
								{
									return fileName;
								}
								else
								{
									return fileName.substring( 0, pos );
								}
							}
							else
							{
								return "";
							}
						}
						
						@Override
						public Path fromString(String strPath)
						{
							Path selectedPath = treeView.getSelectionModel().getSelectedItem().getValue();
							Path parentPath = selectedPath.getParent();
							
							if ( Files.isDirectory(selectedPath) )
							{
								return Paths.get( parentPath.toString() + File.separator + strPath );
							}
							else if ( Files.isRegularFile(selectedPath) )
							{
								return Paths.get( parentPath.toString() + File.separator + strPath + "." + fileExt );
							}
							else
							{
								return null;
							}
						}
					}
				);
			}
		);
		
		this.setOnEditStart
		(
			(event)->
			{
				
			}
		);
		
		// rename or move
		this.setOnEditCommit
		(
			(event)->
			{
				//TreeItem<Path> itemTarget = event.getTreeItem();
				Path pathOld = event.getOldValue();
				Path pathNew = event.getNewValue();
				try
				{
					Files.move( pathOld, pathNew );
				}
				catch ( IOException ioEx )
				{
					this.showException(ioEx);
				}
			}
		);
		
		this.setOnEditCancel
		(
			(event)->
			{
				
			}
		);
		
	}
	
	public void createTree( TreeItem<Path> itemParent ) throws IOException
	{
	    try 
	    ( 
    		DirectoryStream<Path> directoryStream = 
    			Files.newDirectoryStream(itemParent.getValue());
	    ) 
	    {
	        for ( Path path : directoryStream ) 
	        {
	        	if ( Files.isRegularFile(path) )
	        	{
		        	if ( path.toString().endsWith( this.fileExt ) )
		        	{
		        		System.out.println( "file:pathOK(" + this.fileExt + "):" + path.toString() );
			            TreeItem<Path> newItem = new TreeItem<Path>(path);
			            newItem.setGraphic( MyTool.createImageView( 16, 16, this.mainCtrl.getImage("file:resources/images/file.png") ) );
			            newItem.setExpanded(true);
		
			            itemParent.getChildren().add(newItem);
		        	}
		        	else
		        	{
		        		System.out.println( "file:pathNG(" + this.fileExt + "):" + path.toString() );
		        	}
	        	}
	        	else if (Files.isDirectory(path)) 
	            {
		            TreeItem<Path> newItem = new TreeItem<Path>(path);
		            newItem.setGraphic( MyTool.createImageView( 16, 16, this.mainCtrl.getImage("file:resources/images/folder.png") ) );
		            newItem.setExpanded(true);
	
		            itemParent.getChildren().add(newItem);
		            
	            	this.createTree(newItem);
	            }
	        }
	    }
	}
	
	public void addNewFolder() throws IOException
	{
		TreeItem<Path> selectedItem = this.getSelectionModel().getSelectedItem();
		if ( selectedItem == null )
		{
			selectedItem = this.getRoot();
		}
		
        TreeItem<Path> newItem = new TreeItem<Path>();
        Path parentPath = this.getPathFolder(selectedItem);
        Path newPath = Paths.get( parentPath.toString() + File.separator + "New" );
        Files.createDirectory( newPath );
        newItem.setValue( newPath );
        newItem.setGraphic( MyTool.createImageView( 16, 16, this.mainCtrl.getImage("file:resources/images/folder.png") ) );
        newItem.setExpanded(true);
        
        selectedItem.getChildren().add(newItem);
        this.getSelectionModel().select(newItem);
	}
	
	public void addNewFile() throws IOException
	{
		TreeItem<Path> selectedItem = this.getSelectionModel().getSelectedItem();
		if ( selectedItem == null )
		{
			selectedItem = this.getRoot();
		}
		
        TreeItem<Path> newItem = new TreeItem<Path>();
        Path parentPath = this.getPathFolder(selectedItem);
        Path newPath = Paths.get( parentPath.toString() + File.separator + "New" + "." + this.fileExt );
        Files.createFile( newPath );
        newItem.setValue( newPath );
        newItem.setGraphic( MyTool.createImageView( 16, 16, this.mainCtrl.getImage("file:resources/images/file.png") ) );
        
        selectedItem.getChildren().add(newItem);
        this.getSelectionModel().select(newItem);
	}
	
	private Path getPathFolder( TreeItem<Path> item )
	{
		Path path = item.getValue();
		if ( Files.isDirectory(path) )
		{
			return path;
		}
		else if ( Files.isRegularFile(path) )
		{
			return path.getParent();
		}
		else
		{
			return null;
		}
	}
	
	private void showException( Exception ex )
	{
		MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING, this.mainCtrl );
		ResourceBundle langRB = this.mainCtrl.getLangResource("conf.lang.gui.common.MyAlert");
		alertDlg.setHeaderText( langRB.getString( "TITLE_MISC_ERROR" ) );
		alertDlg.setTxtExp( ex );
		alertDlg.showAndWait();
	}	
}
