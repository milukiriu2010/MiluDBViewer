package milu.file;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
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
		// TODO Auto-generated method stub

	}

	@Override
	public String load() throws IOException 
	{
		if ( this.file == null )
		{
			return null;
		}
		/*
		try
		(
			FileReader fr = new FileReader(this.file.getAbsolutePath());
			BufferedReader br = new BufferedReader(fr);
		)
		{
			StringBuffer sb = new StringBuffer();
			
			String line;
			while ( (line=br.readLine()) != null )
			{
				sb.append( line );
			}
			
			return sb.toString();
		}
		*/
		
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
