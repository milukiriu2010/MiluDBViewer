package milu.file.table;

import java.io.File;

import milu.tool.MyTool;

public class MyFileInFactory
{
	public static MyFileInAbstract getInstance( File file )
	{
		String ext = MyTool.getFileExtension( file );
		if ( "xlsx".equals( ext ) )
		{
			return new MyFileInExcel();
		}
		else
		{
			return null;
		}
	}
}
