package milu.gui.ctrl.common;

import javafx.scene.control.TreeView;
import javafx.application.Platform;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.util.StringConverter;
import javafx.scene.control.TreeItem;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.input.DragEvent;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;

import milu.gui.ctrl.common.inf.ChangePathInterface;
import milu.gui.dlg.MyAlertDialog;
import milu.main.MainController;
import milu.tool.MyTool;

// https://stackoverflow.com/questions/34534775/configuring-a-treeview-which-scans-local-fie-system-to-only-include-folders-whic?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
public class PathTreeView extends TreeView<Path> 
{
	private MainController mainCtrl = null;
	
	private ChangePathInterface chgPathInf = null;
	
	private String  rootDir = null;
	
	// File Extension
	private String  fileExt = "";
	
	//private TextFieldTreeCell<Path> treeCell = null;
	
	private TreeItem<Path>  itemDrag = null;
	
	public void setMainController( MainController mainCtrl )
	{
		this.mainCtrl = mainCtrl;
	}
	
	public void setChangePathInterface( ChangePathInterface chgPathInf )
	{
		this.chgPathInf = chgPathInf;
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
		this.getSelectionModel().select(itemRoot);
		//this.setShowRoot(false);
		
		// create tree structure
		this.createTree(itemRoot);
		
		// configure this TreeView
		this.setEditable(true);
		this.setCellFactory
		(
			(treeView)->
			{
				TextFieldTreeCell<Path> treeCell =
					new TextFieldTreeCell<Path>
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
								Path selectedPath = null;
								try
								{
									selectedPath = treeView.getSelectionModel().getSelectedItem().getValue();
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
										return selectedPath;
									}
								}
								catch ( InvalidPathException ipEx )
								{
									//showException(ipEx,"TITLE_NOT_ALLOWED_CHARACTER");
									MyTool.showException( PathTreeView.this.mainCtrl, "conf.lang.gui.common.MyAlert", "TITLE_MISC_ERROR", ipEx, "TITLE_NOT_ALLOWED_CHARACTER" );
									Platform.runLater( ()->requestFocus() );
									return selectedPath;
								}
							}
						}
					);
				this.setDragDropEvent(treeCell);
				
				// disable editor for root item.
				// ----------------------------------------------------
				//if ( treeView.getSelectionModel().getSelectedItem() == treeView.getRoot() )
				//{
				//	treeCell.setDisable(true);
				//}
				// ----------------------------------------------------
				//if ( treeCell.getTreeItem() == treeView.getRoot() )
				//{
				//	treeCell.setDisable(true);
				//}
				// ----------------------------------------------------
				//if ( treeCell.getTreeItem() != null && treeCell.getTreeItem().getParent() == null )
				//{
				//	treeCell.setDisable(true);
				//}
				// ----------------------------------------------------
				TreeItem<Path> treeItem = treeCell.getTreeItem();
				if ( treeItem != null )
				{
					System.out.println( "TreeCell:" + treeItem.getValue().toString() );
				}
				else
				{
					System.out.println( "TreeCell:null" );
				}
				System.out.println( "TreeCell.getText:" + treeCell.getText() );
				
				
				return treeCell;
			}
		);
		
		this.setOnEditStart
		(
			(event)->
			{
				//this.treeCell.focusTraversableProperty().
				
				//if ( this.getSelectionModel().getSelectedItem() == this.getRoot() )
				//{
				//	this.getOnEditCancel();
				//}
				
			}
		);
		
		// rename or move
		this.setOnEditCommit
		(
			(event)->
			{
				TreeItem<Path> itemTarget = event.getTreeItem();
				if ( itemTarget == this.getRoot() )
				{
					event.consume();
					return;
				}
				
				Path pathOld = event.getOldValue();
				Path pathNew = event.getNewValue();
				try
				{
					if ( Files.exists(pathNew) == false )
					{
						Files.move( pathOld, pathNew );
						event.consume();
						//this.renamePath( event.getTreeItem(), pathOld, pathNew );
						
						// -------------------------------------------------
						// itemTarget is still "pathOld",
						// so call re-create tree in Platform.runLater
						// -------------------------------------------------
						Platform.runLater
						(
							()->
							{
								itemTarget.getChildren().removeAll(itemTarget.getChildren());
								try
								{
									createTree(itemTarget);
								}
								catch ( IOException ioEx )
								{
									throw new RuntimeException(ioEx);
								}
							}
						);
					}
					else
					{
						System.out.println( "PathTreeView:setOnEditCommit:already exists." );
						System.out.println( "pathOld:" + pathOld );
						System.out.println( "pathNew:" + pathNew );
						//this.getSelectionModel().getSelectedItem().setValue(pathOld);
						//this.showMsg("TITLE_ALREADY_EXIST");
						MyTool.showMsg( this.mainCtrl, "conf.lang.gui.common.MyAlert", "TITLE_MISC_ERROR", "TITLE_ALREADY_EXIST" );
						event.consume();
						// -------------------------------------------------
						// itemTarget set wrong value
						// so call re-create tree in Platform.runLater
						// -------------------------------------------------
						Platform.runLater
						(
							()->
							{
								TreeItem<Path> itemParent = itemTarget.getParent();
								itemParent.getChildren().removeAll(itemParent.getChildren());
								try
								{
									createTree(itemParent);
								}
								catch ( IOException ioEx )
								{
									throw new RuntimeException(ioEx);
								}
								finally
								{
									Platform.runLater( ()->requestFocus() );
								}
							}
						);
					}
				}
				catch ( Exception ex )
				{
					MyTool.showException( this.mainCtrl, "conf.lang.gui.common.MyAlert", "TITLE_MISC_ERROR", ex );
				}
			}
		);
		
		this.setOnEditCancel
		(
			(event)->
			{
				
			}
		);
		
		this.getSelectionModel().selectedItemProperty().addListener
		(
			(obs,oldVal,newVal)->
			{
				this.chgPathInf.changePath( newVal.getValue() );
			}
		);
		
	}
	
	public void setDragDropEvent( TextFieldTreeCell<Path> treeCell )
	{
		treeCell.setOnDragDetected
		(
			(event)->
			{
				System.out.println( "TreeCell:DragDetected." );
				TreeItem<Path> selectedItem = this.getSelectionModel().getSelectedItem();
				this.itemDrag = selectedItem;
				Dragboard dragBoard = treeCell.startDragAndDrop(TransferMode.MOVE);
				ClipboardContent content = new ClipboardContent();
				content.putString(selectedItem.getValue().toString());
				dragBoard.setContent(content);
				event.consume();
			}
		);
		
		treeCell.setOnDragOver
		(
			(event)->
			{
				if (this.check(event, treeCell.getTreeItem(), treeCell) == true )
				{
					event.acceptTransferModes(TransferMode.MOVE);
				}
			}
		);
		
		
		treeCell.setOnDragDropped
		(
			(event)->
			{
				TreeItem<Path> itemCurr = treeCell.getTreeItem();
				if (this.check( event, itemCurr, treeCell) == true )
				{
					System.out.println( "TreeCell:DragDropped." );
					String itemDragName = this.itemDrag.getValue().toFile().getName();
					String itemCurrAbsolutePathName = itemCurr.getValue().toString();
					Path pathNew = Paths.get( itemCurrAbsolutePathName + File.separator + itemDragName );
					System.out.println( pathNew.toString() );

					try
					{
						Files.move( this.itemDrag.getValue(), pathNew );
						//this.itemDrag.getChildren().removeAll(this.itemDrag.getChildren());
						TreeItem<Path> itemParent = this.itemDrag.getParent();
						itemParent.getChildren().removeAll(itemParent.getChildren());
						createTree(itemParent);
						event.consume();
						
						Platform.runLater
						(
							()->
							{
								try
								{
									itemCurr.getChildren().removeAll(itemCurr.getChildren());
									createTree(itemCurr);
								}
								catch ( IOException ioEx )
								{
									throw new RuntimeException(ioEx);
								}
							}
						);
						
					}
					catch ( Exception ex )
					{
						MyTool.showException( this.mainCtrl, "conf.lang.gui.common.MyAlert", "TITLE_MISC_ERROR", ex );
					}
					
				}
			}
		);
		
		treeCell.setOnDragDone
		(
			(event)->
			{
				System.out.println( "TreeCell:DragDone." );
				this.itemDrag = null;
			}
		);
	}
	
	private boolean check( DragEvent event, TreeItem<Path> itemCur, TextFieldTreeCell<Path> treeCell )
	{
		if ( 
			( event.getGestureSource() != treeCell ) &&
			// target should be a folder.
			( Files.isDirectory(itemCur.getValue()) ) &&
			// "parent(itemDrag) => children(itemCur)" is not allowed.
			( itemCur.getValue().toString().startsWith(this.itemDrag.getValue().toString()) == false ) &&
			//( this.itemDrag.getValue().toFile().getName().equals(itemCur.getValue().toFile().getName()) == false ) &&
			( event.getDragboard().getString().equals(this.itemDrag.getValue().toString()))
		)
		{
			// ------------------------------------------------------
			// The same file/folder name is not allowed,
			//   to put in the same folder.
			// ------------------------------------------------------
			TreeItem<Path> itemMatch = 
				itemCur.getChildren().stream()
					.filter( item -> item.getValue().toFile().getName().equals(this.itemDrag.getValue().toFile().getName()) )
					.findAny()
					.orElse(null);
			if ( itemMatch == null )
			{
				return true;
			}
		}
		
		return false;
	}
	
	public void createTree( TreeItem<Path> itemParent ) throws IOException
	{
		if ( Files.isDirectory(itemParent.getValue()) == false )
		{
			return;
		}
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
		Path selectedPath = selectedItem.getValue();
		// skip, if file is selected.
		if ( Files.isDirectory(selectedPath) == false )
		{
			return;
		}
		
        TreeItem<Path> newItem = new TreeItem<Path>();
        Path parentPath = this.getPathFolder(selectedItem);
        //Path newPath = Paths.get( parentPath.toString() + File.separator + "New" );
        Path newPath = this.getNewFolderPath( parentPath.toString() + File.separator, "New", 0, "" );
        Files.createDirectory( newPath );
        newItem.setValue( newPath );
        newItem.setGraphic( MyTool.createImageView( 16, 16, this.mainCtrl.getImage("file:resources/images/folder.png") ) );
        newItem.setExpanded(true);
        
        selectedItem.getChildren().add(newItem);
        this.getSelectionModel().select(newItem);
        Platform.runLater
        ( 
        	()->
        	{
	        	this.requestFocus();
	        	this.edit(newItem); 
	        	//this.treeCell.updateSelected(true); 
	        	//this.treeCell.requestFocus(); 
        	} 
        );
	}
	
	public void addNewFile() throws IOException
	{
		TreeItem<Path> selectedItem = this.getSelectionModel().getSelectedItem();
		if ( selectedItem == null )
		{
			selectedItem = this.getRoot();
		}
		Path selectedPath = selectedItem.getValue();
		// skip, if file is selected.
		if ( Files.isDirectory(selectedPath) == false )
		{
			return;
		}
		
        TreeItem<Path> newItem = new TreeItem<Path>();
        Path parentPath = this.getPathFolder(selectedItem);
        //Path newPath = Paths.get( parentPath.toString() + File.separator + "New" + "." + this.fileExt );
        Path newPath = this.getNewFolderPath( parentPath.toString() + File.separator, "New", 0, "." + this.fileExt );
        Files.createFile( newPath );
        newItem.setValue( newPath );
        newItem.setGraphic( MyTool.createImageView( 16, 16, this.mainCtrl.getImage("file:resources/images/file.png") ) );
        
        selectedItem.getChildren().add(newItem);
        this.getSelectionModel().select(newItem);
        Platform.runLater
        ( 
        	()->
        	{ 
        		this.requestFocus(); 
        		this.edit(newItem); 
        		//this.treeCell.updateSelected(true); 
        		//this.treeCell.requestFocus(); 
        	} 
        );
	}
	
	public void editItem()
	{
		TreeItem<Path> selectedItem = this.getSelectionModel().getSelectedItem();
		if ( selectedItem == null )
		{
			return;
		}
		if ( selectedItem == this.getRoot() )
		{
			return;
		}
		
        Platform.runLater
        ( 
        	()->
        	{ 
        		this.requestFocus(); 
        		this.edit(selectedItem); 
        	} 
        );
		
	}
	
	private Path getNewFolderPath( String strRoot, String strSelf, int cnt, String ext )
	{
		Path newPath = null;
		if ( cnt == 0 )
		{
			newPath = Paths.get( strRoot + strSelf + ext );
		}
		else
		{
			newPath = Paths.get( strRoot + strSelf + cnt + ext );
		}
		if ( Files.exists(newPath) )
		{
			return getNewFolderPath( strRoot, strSelf, cnt+1, ext );
		}
		else
		{
			return newPath;
		}
	}
	
	public void delFolder() throws IOException
	{
		TreeItem<Path> selectedItem = this.getSelectionModel().getSelectedItem();
		// skip, if "root" is selected.
		if ( selectedItem == null || selectedItem == this.getRoot() )
		{
			return;
		}
		
		Path path = selectedItem.getValue();
		if ( Files.isDirectory(path) )
		{
			this.deleteFolder(path);
		}
		else
		{
			Files.delete(path);
		}
		selectedItem.getParent().getChildren().remove(selectedItem);
	}
	
	private void deleteFolder( Path pathParent ) throws IOException
	{
	    try 
	    ( 
    		DirectoryStream<Path> directoryStream = 
    			Files.newDirectoryStream(pathParent);
	    ) 
	    {
	        for ( Path path : directoryStream ) 
	        {
	        	if (Files.isDirectory(path)) 
	            {
	            	this.deleteFolder(path);
	            }
	        	else
	        	{
	        		Files.delete(path);
	        	}
	        }
	    }
	    Files.delete( pathParent );
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
	
	/*
	private void showException( Exception ex )
	{
		MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING, this.mainCtrl );
		ResourceBundle langRB = this.mainCtrl.getLangResource("conf.lang.gui.common.MyAlert");
		alertDlg.setHeaderText( langRB.getString( "TITLE_MISC_ERROR" ) );
		alertDlg.setTxtExp( ex );
		alertDlg.showAndWait();
	}
	*/
	/*
	private void showException( Exception ex, String msgID )
	{
		MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING, this.mainCtrl );
		ResourceBundle langRB = this.mainCtrl.getLangResource("conf.lang.gui.common.MyAlert");
		alertDlg.setHeaderText( langRB.getString( "TITLE_MISC_ERROR" ) );
		alertDlg.setTxtExp( ex, langRB.getString( msgID ) );
		alertDlg.showAndWait();
	}
	*/	
	
	/*
	private void showMsg( String msgID )
	{
		MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING, this.mainCtrl );
		ResourceBundle langRB = this.mainCtrl.getLangResource("conf.lang.gui.common.MyAlert");
		alertDlg.setHeaderText( langRB.getString( "TITLE_MISC_ERROR" ) );
		alertDlg.setTxtMsg( langRB.getString( msgID ) );
		alertDlg.showAndWait();
	}	
	*/
}
