package milu.file.table;

import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class MyFileExportCSV extends MyFileExportAbstract 
{
	private FileWriter   fw = null;

	@Override
	public void open(File file) 
		throws FileNotFoundException, IOException 
	{
		this.fw = new FileWriter( file );
	}

	@Override
	public void close() 
	{
		try
		{
			if ( this.fw != null )
			{
				this.fw.flush();
				this.fw.close();
			}
		}
		catch ( IOException ioEx )
		{
			// suppress error
		}
	}

	@Override
	public void export(List<Object> headLst, List<List<Object>> dataLst) 
			throws IOException 
	{
		int rowSize = dataLst.size();
		//int colSize = headLst.size();
		String lineSP = System.getProperty("line.separator");
		
		// Output Header
		this.fw.write( headLst.stream().map(x->x.toString()).collect(Collectors.joining(",","",lineSP)) );
		
		double assignedSizeDiv = 0.0;
		if ( rowSize != 0 )
		{
			assignedSizeDiv = this.assignedSize/(double)rowSize;
		}
		// Output Data
		for ( int i = 0; i < rowSize; i++ )
		{
			List<Object> dataRow = dataLst.get(i);
			this.fw.write( dataRow.stream()
					.map((x)->{
						if ( x == null )
						{
							return "";
						}
						else
						{
							return x.toString();
						}
					})
					.collect(Collectors.joining(",","",lineSP)) );
			if ( this.progressInf != null )
			{
				this.progressInf.addProgress(assignedSizeDiv);
				this.progressInf.setMsg( String.valueOf(i) );
			}
		}
	}

}
