package milu.file.table;

import java.io.File;

import milu.tool.MyTool;

public class MyFileImportFactory
{
	public static MyFileImportAbstract getInstance( File file )
	{
		String ext = MyTool.getFileExtension( file );
		if ( "xlsx".equals( ext ) )
		{
			return new MyFileImportExcel();
		}
		else
		{
			return null;
		}
	}
}
