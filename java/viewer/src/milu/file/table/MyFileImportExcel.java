package milu.file.table;

import java.io.File;
import java.io.InputStream;
import java.text.ParseException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import milu.tool.MyStringTool;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

public class MyFileImportExcel extends MyFileImportAbstract 
{
	// Workbook
	private Workbook book = null;
	
	// Sheet
	private Sheet    sheet = null;

	@Override
	public void open(File file)
			throws FileNotFoundException, IOException, InvalidFormatException
	{
		InputStream is = new FileInputStream(file.getAbsolutePath());
		this.book = WorkbookFactory.create(is);
		this.sheet = this.book.getSheetAt(0);
	}

	@Override
	public void close() throws IOException
	{
		if ( this.book != null )
		{
			this.book.close();
		}
	}
	
	// https://www.callicoder.com/java-read-excel-file-apache-poi/
	@Override
	public void load(int columnCnt) 
		throws ParseException
	{
		this.headLst.clear();
		this.dataLst.clear();
		
		DataFormatter dataFormatter = new DataFormatter();

		if ( this.progressInf != null )
		{
			this.progressInf.setMsg("...");
		}
		int lastRowNum = this.sheet.getLastRowNum()+1;
		double assignedSizeDiv = 0.0;
		if ( lastRowNum != 0 )
		{
			assignedSizeDiv = this.assignedSize/lastRowNum;
		}
		int i = 0;
		for ( Row row : this.sheet )
		{
			if ( this.isCancel == true )
			{
				break;
			}
			List<Object> dataRow = new ArrayList<>();
			
			for ( int j = 0; j < columnCnt; j++ )
			{
				Cell cell = row.getCell(j);
				if ( cell == null )
				{
					dataRow.add(null);
				}
				else
				{
					String cellVal = dataFormatter.formatCellValue(cell);
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
						this.addRowDateTime(cellVal,dataRow);
					}
					else if ( MyStringTool.isDate(cellVal) )
					{
						this.addRowDateTime(cellVal,dataRow);
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
				if ( i == 0 )
				{
					this.headLst.add(MyStringTool.getAplha(j, ""));
				}
			}
			this.dataLst.add(dataRow);
			i++;
			if ( this.progressInf != null )
			{
				this.progressInf.addProgress(assignedSizeDiv);
				this.progressInf.setMsg( i + "/" + lastRowNum );
			}
		}
	}

}
