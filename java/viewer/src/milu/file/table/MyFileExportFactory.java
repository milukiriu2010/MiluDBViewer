package milu.file.table;

import java.io.File;

import milu.tool.MyTool;

public class MyFileExportFactory
{
	public static MyFileExportAbstract getInstance( File file )
	{
		String ext = MyTool.getFileExtension( file );
		if ( "xlsx".equals( ext ) )
		{
			return new MyFileExportExcel();
		}
		else
		{
			return null;
		}
	}
}
