package milu.file.table;

import java.io.File;

import milu.tool.MyFileTool;

public class MyFileExportFactory
{
	public static MyFileExportAbstract getInstance( File file )
	{
		String ext = MyFileTool.getFileExtension( file );
		if ( "xlsx".equals( ext ) )
		{
			return new MyFileExportExcel();
		}
		else if ( "csv".equals( ext ) )
		{
			return new MyFileExportCSV();
		}
		else
		{
			return null;
		}
	}
}
