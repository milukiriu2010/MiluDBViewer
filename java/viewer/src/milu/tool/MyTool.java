package milu.tool;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

public class MyTool
{
	public static int getCharCount( String strSrc, String strChk )
	{
		if ( strSrc == null || strChk == null )
		{
			return 0;
		}
		else
		{
			return strSrc.length() - strSrc.replace( strChk, "" ).length();
		}
	}
	
	public static String getFileExtension( File file )
	{
		String name = file.getName();
		return name.substring( name.lastIndexOf(".")+1 );
	}
	
	public static String getExceptionString( Exception exp )
	{
		StringWriter sw = new StringWriter();
		PrintWriter  pw = new PrintWriter( sw );
		exp.printStackTrace( pw );
		return sw.toString();
	}
}
