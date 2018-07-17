package milu.file.table;

import java.util.List;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.FileNotFoundException;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class MyFileExportExcel extends MyFileExportAbstract
{
	private FileOutputStream   foutStream = null;
	
	// Workbook
	private Workbook book         = new SXSSFWorkbook();
	
	// Sheet
	private Sheet sheet           = null;
	
	// CellStyle for Header
	private CellStyle styleHeader = null;
	
	// CellStyle for Body
	private CellStyle styleBody   = null;
	
	@Override
	public void open( File file )
		throws FileNotFoundException
	{
		this.foutStream = new FileOutputStream( file );
		
		// Sheet
		this.sheet = this.book.createSheet();
		if ( this.sheet instanceof SXSSFSheet )
		{
			((SXSSFSheet)this.sheet).trackAllColumnsForAutoSizing();
        }
		
		// CellStyle for Header
        this.styleHeader = this.book.createCellStyle();
        this.styleHeader.setBorderBottom(BorderStyle.THIN);
        this.styleHeader.setBorderTop(BorderStyle.THIN);
        this.styleHeader.setBorderLeft(BorderStyle.THIN);
        this.styleHeader.setBorderRight(BorderStyle.THIN);
        this.styleHeader.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LIGHT_CORNFLOWER_BLUE.getIndex());
        this.styleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        this.styleHeader.setVerticalAlignment(VerticalAlignment.TOP);
        //this.styleHeader.setFont(font);
        
        // CellStyle for Body
        this.styleBody = this.book.createCellStyle();
        this.styleBody.setBorderBottom(BorderStyle.THIN);
        this.styleBody.setBorderTop(BorderStyle.THIN);
        this.styleBody.setBorderLeft(BorderStyle.THIN);
        this.styleBody.setBorderRight(BorderStyle.THIN);
        this.styleBody.setVerticalAlignment(VerticalAlignment.TOP);
	}
	
	@Override
	public void close()
	{
		try
		{
			if ( this.foutStream != null )
			{
				this.foutStream.close();
			}
		}
		catch ( IOException ioEx )
		{
			// suppress error
		}
		
		try
		{
			((SXSSFWorkbook)book).dispose();
			this.book.close();
		}
		catch ( IOException ioEx )
		{
			// suppress error
		}
	}
	
	@Override
	public void export( List<Object> headLst, List<List<Object>> dataLst )
			 throws IOException
	{
		int rowSize = dataLst.size();
		int colSize = headLst.size();
		
		// Header for Sheet
		Row  rowHead = this.sheet.createRow( 0 );
		for ( int i = 0; i < colSize; i++ )
		{
			Cell cellHead = rowHead.createCell(i);
			cellHead.setCellStyle(this.styleHeader);
			cellHead.setCellType(CellType.STRING);
			cellHead.setCellValue(headLst.get(i).toString());
		}

		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		double assignedSizeDiv = 0.0;
		if ( rowSize != 0 )
		{
			assignedSizeDiv = this.assignedSize/(double)rowSize;
		}
		// Body for Sheet
		for ( int i = 0; i < rowSize; i++ )
		{
			Row rowBody = this.sheet.createRow(i+1);
			List<Object> dataRow = dataLst.get(i);
			for ( int j = 0; j < colSize; j++ )
			{
				Cell cellBody = rowBody.createCell(j);
				cellBody.setCellStyle(this.styleBody);
				Object obj = dataRow.get(j);
				if ( obj == null )
				{
					cellBody.setCellType(CellType.BLANK);
				}
				else
				{
					if ( obj instanceof Number )
					{
						Number num = (Number)obj;
						cellBody.setCellType(CellType.NUMERIC);
						cellBody.setCellValue(num.doubleValue());
					}
					else if ( obj instanceof java.sql.Timestamp )
					{
						java.sql.Timestamp dateTime = (java.sql.Timestamp)obj;
						cellBody.setCellType(CellType.STRING);
						cellBody.setCellValue(dateFormat.format(dateTime));
					}
					else
					{
						cellBody.setCellType(CellType.STRING);
						cellBody.setCellValue(obj.toString());
					}
				}
			}
			if ( this.progressInf != null )
			{
				this.progressInf.addProgress(assignedSizeDiv);
				this.progressInf.setMsg( String.valueOf(i) );
			}
		}
		
		// Resize column size
		for ( int i = 0; i < colSize; i++ )
		{
			this.sheet.autoSizeColumn( i, true );
		}
		
		// Output to file.
		this.book.write( this.foutStream );
	}
	/*
	@Override
	public void save( List<String> headLst, List<List<String>> dataLst )
		throws IOException
	{
		int rowSize = dataLst.size();
		int colSize = headLst.size();
		
		// Header for Sheet
		Row  rowHead = this.sheet.createRow( 0 );
		for ( int i = 0; i < colSize; i++ )
		{
			Cell cellHead = rowHead.createCell(i);
			cellHead.setCellStyle(this.styleHeader);
			cellHead.setCellType(CellType.STRING);
			cellHead.setCellValue(headLst.get(i));
		}
		
		// Body for Sheet
		for ( int i = 0; i < rowSize; i++ )
		{
			Row rowBody = this.sheet.createRow(i+1);
			List<String> dataRow = dataLst.get(i);
			for ( int j = 0; j < colSize; j++ )
			{
				Cell cellBody = rowBody.createCell(j);
				cellBody.setCellStyle(this.styleBody);
				cellBody.setCellType(CellType.STRING);
				cellBody.setCellValue(dataRow.get(j));
			}
		}
		
		// Resize column size
		for ( int i = 0; i < colSize; i++ )
		{
			this.sheet.autoSizeColumn( i, true );
		}
		
		// Output to file.
		this.book.write( this.foutStream );
	}
	*/
}
