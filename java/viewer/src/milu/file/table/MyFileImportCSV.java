package milu.file.table;

import java.util.List;

import milu.tool.MyStringTool;

import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

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
			 throws IOException, ParseException
	{
		this.headLst.clear();
		this.dataLst.clear();
		
		if ( this.progressInf != null )
		{
			this.progressInf.setMsg("...");
		}
		String line = null;
		while ( (line = this.br.readLine()) != null )
		{
			if ( this.isCancel == true )
			{
				break;
			}
			String[] tokens = line.split(",");
			List<Object> dataRow = new ArrayList<>();
			for ( int i = 0; i < columnCnt; i++ )
			{
				if ( i >= tokens.length )
				{
					//dataRow.add("");
					dataRow.add(null);
				}
				else
				{
					String cellVal = tokens[i];
					if ( MyStringTool.isNumberNoDecimal(cellVal) )
					{
						dataRow.add(Long.parseLong(cellVal));
					}
					else if ( MyStringTool.isNumberWithDecimal(cellVal) )
					{
						dataRow.add(Double.parseDouble(cellVal));
					}
					else if ( MyStringTool.isDateTime(cellVal) )
					{
						this.addRowDateTime(cellVal, dataRow);
					}
					else if ( MyStringTool.isDate(cellVal) )
					{
						this.addRowDateTime(cellVal, dataRow);
					}
					else if ( cellVal.length() == 0 )
					{
						dataRow.add(null);
					}
					else
					{
						dataRow.add(cellVal);
					}
				}
			}
			this.dataLst.add(dataRow);
		}
		
	}

}
