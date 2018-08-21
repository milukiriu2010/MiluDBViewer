package milu.tool;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

import milu.file.json.MyJsonEachAbstract;
import milu.file.json.MyJsonEachFactory;
import milu.gui.ctrl.menu.AfterDBConnectedInterface;
import milu.gui.ctrl.menu.DBMenuItem;
import milu.gui.view.DBView;
import milu.main.AppConf;
import milu.main.AppConst;
import milu.main.MainController;

public class MyFileTool
{
	public static String getBaseName( String fileName )
	{
		int pos = fileName.lastIndexOf(".");
		if ( pos == -1 )
		{
			return fileName;
		}
		else
		{
			return fileName.substring( 0, pos );
		}
	}
	
	public static String getFileExtension( File file )
	{
		String name = file.getName();
		int pos = name.lastIndexOf(".");
		if ( pos == -1 )
		{
			return "";
		}
		else
		{
			return name.substring( pos+1 );
		}
	}
	
	// delete all folders & files under "dir"
	public static void purgeDirectory( File dir )
	{
		File[] files = dir.listFiles();
		if ( files == null )
		{
			return;
		}
		
		for ( File file: files )
		{
			file.delete();
		}
	}
	
	// https://howtodoinjava.com/core-java/io/how-to-copy-directories-in-java/
    public static void copyFolder(File sourceFolder, File destinationFolder) throws IOException
    {
        //Check if sourceFolder is a directory or file
        //If sourceFolder is file; then copy the file directly to new location
        if (sourceFolder.isDirectory())
        {
            //Verify if destinationFolder is already present; If not then create it
            if (!destinationFolder.exists())
            {
                destinationFolder.mkdir();
                System.out.println("Directory created :: " + destinationFolder);
            }
             
            //Get all files from source directory
            String files[] = sourceFolder.list();
             
            //Iterate over all files and copy them to destinationFolder one by one
            for (String file : files)
            {
                File srcFile = new File(sourceFolder, file);
                File destFile = new File(destinationFolder, file);
                 
                //Recursive function call
                copyFolder(srcFile, destFile);
            }
        }
        else
        {
            //Copy the file content from one place to another
            Files.copy(sourceFolder.toPath(), destinationFolder.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File copied :: " + destinationFolder);
        }
    }
    
    public static String loadFile( File file, MainController mainCtrl, AppConf appConf )
    {
    	String lineSP = System.getProperty("line.separator");
    	StringBuffer sb = new StringBuffer();
    	try ( BufferedReader br = new BufferedReader(new FileReader(file)) )
    	{
    		br.lines().forEach(line->sb.append(line+lineSP));
    	}
    	catch ( IOException ioEx )
    	{
    		MyGUITool.showException( mainCtrl, "conf.lang.gui.common.MyAlert", "TITLE_MISC_ERROR", ioEx );
    	}
    	
    	return sb.toString();
    }
    
    public static void saveFile( File file, String str, MainController mainCtrl, AppConf appConf )
    {
    	try ( BufferedWriter wr = new BufferedWriter(new FileWriter(file)) )
    	{
    		wr.write(str);
    	}
    	catch ( IOException ioEx )
    	{
    		MyGUITool.showException( mainCtrl, "conf.lang.gui.common.MyAlert", "TITLE_MISC_ERROR", ioEx );
    	}
    }
    
    public static void save( MainController mainCtrl, AppConf appConf )
    {
		try
		{
			MyJsonEachAbstract<AppConf> myJsonAbs =
					MyJsonEachFactory.<AppConf>getInstance(MyJsonEachFactory.factoryType.APP_CONF);
					
			myJsonAbs.save( new File(AppConst.APP_CONF.val()), appConf );
		}
		catch ( IOException ioEx )
		{
			MyGUITool.showException( mainCtrl, "conf.lang.gui.common.MyAlert", "TITLE_MISC_ERROR", ioEx );
		}
    }
	
	public static void createMenuBookMark( Path pathParent, Menu menuParent, DBView dbView, AfterDBConnectedInterface adbcInf ) throws IOException
	{
		if ( Files.isDirectory(pathParent) == false )
		{
			return;
		}
		
		try
		(
	    	DirectoryStream<Path> directoryStream = 
    			Files.newDirectoryStream(pathParent);
		)
		{
			for ( Path path : directoryStream )
			{
				if ( Files.isRegularFile(path) )
				{
					if ( path.toString().endsWith("json") )
					{
						MenuItem menuItem = new DBMenuItem(path,dbView.getMainController(), adbcInf);
						menuParent.getItems().add(menuItem);
					}
				}
				else if ( Files.isDirectory(path) )
				{
					Menu menuSub = new Menu(path.toFile().getName());
					menuParent.getItems().add(menuSub);
					
					createMenuBookMark( path, menuSub, dbView, adbcInf );
				}
			}
		}
	}
		
    // It doesn't work well.
	// https://stackoverflow.com/questions/6214703/copy-entire-directory-contents-to-another-directory?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
    /*
	public static void copyDir(String src, String dest, boolean overwrite) throws IOException
	{
        Files.walk(Paths.get(src)).forEach(a ->{
        	try
        	{
	            Path b = Paths.get(dest, a.toString().substring(src.length()));
	            if (!a.toString().equals(src))
	            {
	            	Files.copy(a, b, overwrite ? new CopyOption[]{StandardCopyOption.REPLACE_EXISTING} : new CopyOption[]{});
	            }
        	}
        	catch ( IOException ioEx )
        	{
        		throw new RuntimeException( ioEx );
        	}
        });		
	}
	*/
}
