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
	public void load() 
	{
		this.headLst.clear();
		this.dataLst.clear();
		
		DataFormatter dataFormatter = new DataFormatter();
		
		/*
		Iterator<Row> rowIterator = this.sheet.rowIterator();
		while ( rowIterator.hasNext() )
		{
			Row row = rowIterator.next();
			List<Object> dataRow = new ArrayList<>();
			
			Iterator<Cell> cellIterator = row.cellIterator();
			while( cellIterator.hasNext() )
			{
				Cell cell = cellIterator.next();
				String cellVal = dataFormatter.formatCellValue(cell);
				dataRow.add(cellVal);
			}
			this.dataLst.add(dataRow);
		}
		*/
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
		
	}

}
