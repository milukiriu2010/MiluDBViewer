package milu.file.table;

import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MyFileImportCSV extends MyFileImportAbstract 
{
	private BufferedReader br = null;
	@Override
	public void open(File file) throws FileNotFoundException, IOException
	{
		this.br = new BufferedReader(new FileReader(file));
	}

	@Override
	public void close() throws IOException 
	{
		if ( this.br != null )
		{
			this.br.close();
		}
	}

	@Override
	public void load(int columnCnt) 
			 throws IOException
	{
		this.headLst.clear();
		this.dataLst.clear();

		String line = null;
		while ( (line = this.br.readLine()) != null )
		{
			String[] tokens = line.split(",");
			List<Object> dataRow = new ArrayList<>();
			for ( int i = 0; i < columnCnt; i++ )
			{
				if ( i >= tokens.length )
				{
					dataRow.add("");
				}
				else
				{
					dataRow.add(tokens[i]);
				}
			}
			this.dataLst.add(dataRow);
		}
		
	}

}
