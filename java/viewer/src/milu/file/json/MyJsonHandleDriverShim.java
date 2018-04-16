package milu.file.json;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import milu.db.driver.DriverShim;

public class MyJsonHandleDriverShim extends MyJsonHandleAbstract 
{
	private File file = null;

	@Override
	public void open(String filePath) 
	{
		this.file = new File(filePath);
	}

	@Override
	public Object load() throws FileNotFoundException, IOException 
	{
		if ( this.file == null )
		{
			return null;
		}

		String json = null;
		// https://stackoverflow.com/questions/14169661/read-complete-file-without-using-loop-in-java
		try
		(
			FileInputStream  fis = new FileInputStream(this.file);
		)
		{
			byte[] data = new byte[(int)this.file.length()];
			fis.read(data);
			
			json = new String( data );
		}
		
		Gson gson = new Gson();
		//DriverShim obj = gson.fromJson( json, DriverShim.class );
		Type type = new TypeToken<DriverShim>() {}.getType();
		DriverShim obj = gson.fromJson( json, type );
		
		return obj;
	}

	@Override
	public void save(Object obj) throws IOException 
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
		//String json = gson.toJson( (DriverShim)obj, DriverShim.class );
		Type type = new TypeToken<DriverShim>() {}.getType();
		String json = gson.toJson( (DriverShim)obj, type );
		
		System.out.println( "====== save(" + this.file.getAbsolutePath() + ") ======" );
		System.out.println( json );
		System.out.println( "=======================================================" );
		
		this.file.getParentFile().mkdirs();
		
		try
		(
		  BufferedWriter writer = new BufferedWriter( new FileWriter(this.file) );
		)
		{
			writer.write(json);
		}
	}

}
