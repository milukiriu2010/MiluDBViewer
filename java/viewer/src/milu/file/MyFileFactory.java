package milu.file;

import java.io.File;

import milu.tool.MyTool;

public class MyFileFactory
{
	public static MyFileAbstract getInstance( File file )
	{
		String ext = MyTool.getFileExtension( file );
		if ( "xlsx".equals( ext ) )
		{
			return new MyFileExcel();
		}
		else
		{
			return null;
		}
	}
}
