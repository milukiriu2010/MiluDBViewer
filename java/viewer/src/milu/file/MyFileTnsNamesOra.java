package milu.file;

import java.io.File;
import java.io.FileInputStream;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class MyFileTnsNamesOra extends MyFileAbstract 
{
	private File  file = null;

	@Override
	public void open(File file) throws FileNotFoundException 
	{
		if ( file.exists() )
		{
			this.file = file;
		}
	}

	@Override
	public void close() 
	{
		if ( this.file == null )
		{
			return;
		}
	}

	@Override
	public String load() throws IOException 
	{
		if ( this.file == null )
		{
			return null;
		}
		
		// https://stackoverflow.com/questions/14169661/read-complete-file-without-using-loop-in-java
		try
		(
			FileInputStream  fis = new FileInputStream(this.file);
		)
		{
			byte[] data = new byte[(int)this.file.length()];
			fis.read(data);
			
			String str = new String( data );
			return str;
		}
		
	}

	@Override
	public void save(List<String> headLst, List<List<String>> dataLst) throws IOException 
	{
		throw new UnsupportedOperationException();
	}
}
