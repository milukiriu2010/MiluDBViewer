package milu.file.table;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import milu.task.ProgressInterface;
import milu.task.ProgressReportInterface;

public abstract class MyFileImportAbstract
	implements
		ProgressReportInterface
{
	static private DateFormat dateFmtYYYYMMDDhhmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static private DateFormat dateFmtYYYYMMDDhmmss = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");	
	
	protected List<Object>       headLst = new ArrayList<>();
	protected List<List<Object>> dataLst = new ArrayList<>();
	
	protected ProgressInterface  progressInf = null;
	protected double  assignedSize = 0.0;
	
	public List<Object> getHeadLst()
	{
		return this.headLst;
	}
	
	public List<List<Object>> getDataLst()
	{
		return this.dataLst;
	}

	// ProgressReportInterface
	@Override
	public void setProgressInterface( ProgressInterface progressInf )
	{
		this.progressInf = progressInf;
	}
	
	// ProgressReportInterface
	@Override
	public void setAssignedSize( double assignedSize )
	{
		this.assignedSize = assignedSize;
	}
	
	protected void addRowDateTime( String cellVal, List<Object> dataRow ) 
		throws ParseException
	{
		// 2018-07-17 10:10:10
		if ( cellVal.matches("^\\d{4}\\-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$") )
		{
			Date date = dateFmtYYYYMMDDhhmmss.parse(cellVal);
			dataRow.add(new Timestamp(date.getTime()));
		}
		// 2018-07-17  9:10:10
		else if ( cellVal.matches("^\\d{4}\\-\\d{2}-\\d{2} \\d{1}:\\d{2}:\\d{2}$") )
		{
			Date date = dateFmtYYYYMMDDhmmss.parse(cellVal);
			dataRow.add(new Timestamp(date.getTime()));
		}						
		else
		{
			dataRow.add(cellVal);
		}
	}
	
	abstract public void open( File file )  throws FileNotFoundException, IOException, InvalidFormatException; 
	abstract public void close() throws IOException;
	abstract public void load(int columnCnt) throws IOException, ParseException;
}
