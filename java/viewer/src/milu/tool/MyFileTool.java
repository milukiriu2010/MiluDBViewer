package milu.tool;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.CopyOption;
import java.nio.file.StandardCopyOption;

import java.io.File;
import java.io.IOException;

import milu.file.json.MyJsonHandleAbstract;
import milu.file.json.MyJsonHandleFactory;
import milu.main.AppConf;
import milu.main.AppConst;
import milu.main.MainController;

public class MyFileTool
{
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
    
    public static void save( MainController mainCtrl, AppConf appConf )
    {
		try
		{
			MyJsonHandleAbstract myJsonAbs =
					new MyJsonHandleFactory().createInstance(AppConf.class);
					
			myJsonAbs.open(AppConst.APP_CONF.val());
			myJsonAbs.save( appConf );
		}
		catch ( IOException ioEx )
		{
			MyTool.showException( mainCtrl, "conf.lang.gui.common.MyAlert", "TITLE_MISC_ERROR", ioEx );
		}
    }
	
	// https://stackoverflow.com/questions/6214703/copy-entire-directory-contents-to-another-directory?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
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
}
