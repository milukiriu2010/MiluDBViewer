package milu.net;

import java.io.IOException;

public class ModuleUpdate 
{
	public void exec( String moduleName )
		throws IOException
	{
		// System.getProperty:os.name
		//   "Windows 10"
		String osName = System.getProperty("os.name");
		if ( osName.contains("Windows") )
		{
			Runtime.getRuntime().exec(moduleName);
			System.out.println( "exec: ModuleUpdate.");
			
			// user.dir
			// C:\myjava\MiluDBViewer.git\java\viewer
		}
		else
		{
			// http://rauschig.org/jarchivelib/download.html
		}
	}
}
