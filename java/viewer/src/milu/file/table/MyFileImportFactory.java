package milu.file.table;

import java.io.File;

import milu.tool.MyFileTool;

public class MyFileImportFactory
{
	public static MyFileImportAbstract getInstance( File file )
	{
		String ext = MyFileTool.getFileExtension( file );
		if ( "xlsx".equals( ext ) )
		{
			return new MyFileImportExcel();
		}
		else if ( "csv".equals( ext ) )
		{
			return new MyFileImportCSV();
		}
		else
		{
			return null;
		}
	}
}
