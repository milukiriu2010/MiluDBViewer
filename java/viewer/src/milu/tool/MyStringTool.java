package milu.tool;

public class MyStringTool 
{
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
}
