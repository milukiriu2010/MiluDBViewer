package milu.tool;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import milu.gui.view.DBView;

public class MyStringTool 
{
	// -------------------------------------------------
	// null      => true
	// length=0  => true
	// else      => false
	// -------------------------------------------------
	public static boolean isEmpty( String str )
	{
		if ( str == null )
		{
			return true;
		}
		else if ( str.length() == 0 )
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	// -------------------------------------------------
	//  10
	//  10.0
	//   1.0
	//   0.1
	//   0.11
	// -10
	// -------------------------------------------------
	public static boolean isNumber( String strOrg )
	{
		//return strOrg.matches("^(\\-)?([1-9]?\\d+|\\d)(\\.\\d+)?$");
		return strOrg.matches("^(\\-)?([1-9]\\d+|\\d)(\\.\\d+)?$");
	}
	
	public static boolean isNumberNoDecimal( String strOrg )
	{
		//return strOrg.matches("^(\\-)?[1-9]?\\d+$");
		return strOrg.matches("^(\\-)?([1-9]\\d+|\\d)$");
	}
	
	public static boolean isNumberWithDecimal( String strOrg )
	{
		//return strOrg.matches("^(\\-)?[1-9]?\\d+\\.\\d+$");
		return strOrg.matches("^(\\-)?([1-9]\\d+|\\d)\\.\\d+$");
	}
	
	// 2018-07-17 10:10:10
	// 2018-7-7 9:9:9
	public static boolean isDateTime( String strOrg )
	{
		return strOrg.matches("^\\d{4}\\-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}(\\.\\d+)?$");
	}
	
	// 2018-07-17
	// 2018-7-7
	public static boolean isDate( String strOrg )
	{
		return strOrg.matches("^\\d{4}\\-\\d{1,2}-\\d{1,2}$");
	}
	
	public static String replaceMultiLine( String strOrg, String strSrc, String strDst )
	{
		String lineSP = System.getProperty("line.separator");
		StringBuffer sb = new StringBuffer();
		Pattern p = Pattern.compile( strSrc, Pattern.MULTILINE );
		Matcher m = p.matcher(strOrg);
		int i = 0;
		while ( m.find() )
		{
			System.out.println( "replaceMultiLine[" + m.group() + "]" );
			if ( i != 0 )
			{
				sb.append(lineSP);
			}
			sb.append( m.group().replaceAll( strSrc, strDst ) );
			i++;
		}
		return sb.toString();
	}
	
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
	
	public static int lastIndexOf(char ch, String str) 
	{
		if ( str.length() < 1 )
		{
			return -1;
		}
	    if (str.charAt(str.length() - 1) == ch)
	    { 
	    	return str.length()-1; 
	    }
	    if (str.length() <= 1)
	    { 
	    	return -1; 
	    }
	    return lastIndexOf( ch, str.substring(0,str.length()-1) );
	}
	
	// ----------------------------------------
	//  A  =>  0
	//  B  =>  1
	// ...
	//  Z  => 25 
	// AA  => 26
	// ...
	// ----------------------------------------
	public static int getNumFromAlpha( String str, int base )
	{
		if ( str == null || str.length() == 0 )
		{
			return -1;
		}
		else if ( str.length() == 1 )
		{
			char c = str.charAt(0);
			return (base+c-'A');
		}
		else
		{
			char c = str.charAt(str.length()-1);
			int num = c-'A';
			return getNumFromAlpha(str.substring(0,str.length()-1),26+num);
		}
	}

	// ----------------------------------------
	//  0 =>  A
	//  1 =>  B
	// ...
	// 25 =>  Z
	// 26 => AA
	// ...
	// ----------------------------------------
	public static String getAplha( int pos, String tail )
	{
		if ( pos < 26 )
		{
			char c = (char)('A'+(char)pos);
			char[] cc = { c };
			return new String( cc ) + tail;
		}
		else
		{
			int posA  = pos%26;
			char c = (char)('A'+(char)posA);
			char[] cc = { c };
			String str = new String( cc );
			
			int posAA = pos/26;
			return MyStringTool.getAplha( posAA, str );
		}
	}
	
	public static String getExceptionString( Exception exp )
	{
		StringWriter sw = new StringWriter();
		PrintWriter  pw = new PrintWriter( sw );
		exp.printStackTrace( pw );
		return sw.toString();
	}
	
	public static String bytesToHex(byte[] bytes) 
	{
		final char[] hexArray = "0123456789ABCDEF".toCharArray();
	    char[] hexChars = new char[bytes.length * 2];
	    
	    for ( int j = 0; j < bytes.length; j++ ) 
	    {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
	public static <T> T cast( Object obj, Class<T> clazz )
	{
		try
		{
			return clazz.cast(obj);
		}
		catch ( ClassCastException ccEx )
		{
			return null;
		}
	}
	
	public static <T> T newInstance( Class<T> castClazz, DBView dbView )
	{
		Constructor<?>[] constructors = castClazz.getDeclaredConstructors();
		
		T obj = null;
		for ( int i = 0; i < constructors.length; i++ )
		{
			try
			{
				// exit loop 
				// when match "new SelectdItemHandlerEachXX( DBView dbView )"
				obj = castClazz.cast(constructors[i].newInstance(dbView));
				break;
			}
			catch ( Exception ex )
			{
			}
		}
		
		return obj;
	}	
}
