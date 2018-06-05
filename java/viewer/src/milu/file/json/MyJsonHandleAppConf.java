package milu.file.json;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.google.gson.Gson;

import com.google.gson.GsonBuilder;

import milu.main.AppConf;


public class MyJsonHandleAppConf extends MyJsonHandleAbstract 
{
	private File file = null;
	
	@Override
	public void open( String filePath )
	{
		//System.out.println("MyJsonHandleSingle:" + filePath );
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
		
		/*
		Gson gson = new Gson();
		//Type type = new TypeToken<AppConf>() {}.getType();
		//Object obj = gson.fromJson( json, type );
		AppConf obj = gson.fromJson( json, AppConf.class );
		*/
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();
		AppConf obj = gson.fromJson( json, AppConf.class );
		
		return obj;
	}
	
	@Override
	public void save( Object obj ) throws IOException
	{
		/*
		//Type type = new TypeToken<AppConf>() {}.getType();
		GsonBuilder gsonBuilder = new GsonBuilder();
		//gsonBuilder.registerTypeAdapter( type, new JsonElementAdapter() );
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		String json = gson.toJson( (AppConf)obj, AppConf.class );
		*/

		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
		String json = gson.toJson( (AppConf)obj, AppConf.class );
		
		
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
