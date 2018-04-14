package milu.file.json;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.GsonBuilder;

public class MyJsonHandleXSingle<T> extends MyJsonHandleXAbstract<T> 
{
	private File file = null;
	
	public void open( String filePath )
	{
		//System.out.println("MyJsonHandleSingle:" + filePath );
		this.file = new File(filePath);
	}
	
	public T load( Class<T> clazz ) throws FileNotFoundException, IOException
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
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		//	https://stackoverflow.com/questions/29860545/how-do-i-use-custom-deserialization-on-gson-for-generic-type?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
		gsonBuilder.registerTypeAdapter( new TypeToken<T>() {}.getType(), new JsonElementAdapter() );
		Gson gson = gsonBuilder.create();
		
		System.out.println( "gson.create" );
		
		Type type = new TypeToken<T>() {}.getType();
		T obj = gson.fromJson( json, type );
		
		System.out.println( "gson.fromJson" );
		
		return obj;
	}
	
	public void save( T t ) throws IOException
	{
		Type type = new TypeToken<T>() {}.getType();
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter( new TypeToken<T>() {}.getType(), new JsonElementAdapter() );
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		String json = gson.toJson( t, type );
		
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
