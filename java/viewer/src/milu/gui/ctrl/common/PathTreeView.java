package milu.gui.ctrl.common;

import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.util.StringConverter;
import javafx.scene.control.TreeItem;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;

import java.io.IOException;

import milu.main.MainController;

// https://stackoverflow.com/questions/34534775/configuring-a-treeview-which-scans-local-fie-system-to-only-include-folders-whic?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
public class PathTreeView extends TreeView<Path> 
{
	private MainController mainCtrl = null;
	
	private String  rootDir = null;
	
	public void setMainController( MainController mainCtrl )
	{
		this.mainCtrl = mainCtrl;
	}
	
	public void setRootDir( String rootDir )
	{
		this.rootDir = rootDir;
	}
	
	public void init() throws IOException
	{
		// create root
		TreeItem<Path> itemRoot = new TreeItem<Path>(Paths.get(rootDir));
		itemRoot.setExpanded(true);
		this.setRoot(itemRoot);
		
		// create tree structure
		this.createTree(itemRoot);
		
		this.setCellFactory
		(
			(treeItem)->
			{
				return new TextFieldTreeCell<Path>
				(
					new StringConverter<Path>()
					{
						@Override
						public String toString(Path path)
						{
							return path.getFileName().toString();
						}
						
						@Override
						public Path fromString(String path)
						{
							return Paths.get(path);
						}
					}
				);
			}
		);
	}
	
	public void createTree( TreeItem<Path> itemParent ) throws IOException
	{
	    try 
	    ( 
	    	DirectoryStream<Path> directoryStream = Files.newDirectoryStream(itemParent.getValue())
	    ) 
	    {
	        for (Path path : directoryStream) 
	        {
	            TreeItem<Path> newItem = new TreeItem<Path>(path);
	            newItem.setExpanded(true);

	            itemParent.getChildren().add(newItem);
	            
	            if (Files.isDirectory(path)) 
	            {
	            	this.createTree(newItem);
	            }
	        }
	    }	
	}
	
}
