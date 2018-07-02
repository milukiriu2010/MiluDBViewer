package milu.file.table;

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
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

public class MyFileImportExcel extends MyFileImportAbstract 
{
	// Workbook
	private Workbook book = null;
	
	// Sheet
	private Sheet    sheet = null;

	@Override
	public void open(File file) throws Exception
	{
		//this.book = WorkbookFactory.create(file);
		InputStream is = new FileInputStream(file.getAbsolutePath());
		this.book = WorkbookFactory.create(is);
		//InputStream is = new FileInputStream(file.getAbsolutePath());
		//this.book = new XSSFWorkbook(is);
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
	{
		this.headLst.clear();
		this.dataLst.clear();
		
		DataFormatter dataFormatter = new DataFormatter();

		/*
		int cnt = 0;
		for ( Row row : this.sheet )
		{
			List<Object> dataRow = new ArrayList<>();
			
			int j = 0;
			for ( Cell cell : row )
			{
				String cellVal = dataFormatter.formatCellValue(cell);
				dataRow.add(cellVal);
				if ( cnt == 0 )
				{
					this.headLst.add(MyStringTool.getAplha(j, ""));
				}
				j++;
			}
			this.dataLst.add(dataRow);
			cnt++;
		}
		*/
		int i = 0;
		for ( Row row : this.sheet )
		{
			List<Object> dataRow = new ArrayList<>();
			
			for ( int j = 0; j < columnCnt; j++ )
			{
				Cell cell = row.getCell(j);
				if ( cell == null )
				{
					dataRow.add("");
				}
				else
				{
					String cellVal = dataFormatter.formatCellValue(cell);
					dataRow.add(cellVal);
				}
				if ( i == 0 )
				{
					this.headLst.add(MyStringTool.getAplha(j, ""));
				}
			}
			this.dataLst.add(dataRow);
			i++;
		}
	}

}
