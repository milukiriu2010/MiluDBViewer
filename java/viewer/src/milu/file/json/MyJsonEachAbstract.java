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

abstract public class MyJsonEachAbstract<T> 
{
	public T load(File file) throws FileNotFoundException, IOException
	{
		System.out.println("MyJsonEachAbstract:file:"+file.getAbsolutePath());
		String json = null;
		try
		(
			FileInputStream  fis = new FileInputStream(file);
		)
		{
			byte[] data = new byte[(int)file.length()];
			fis.read(data);
			
			json = new String( data );
		}
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		this.decorate(gsonBuilder);
		//Gson gson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();
		GsonBuilder gsonBuilder2 = gsonBuilder.excludeFieldsWithoutExposeAnnotation();
		Gson gson = gsonBuilder2.create();
		
		return gson.fromJson( json, this.getType() );
	}
	
	public void save( File file, T obj ) throws IOException
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		this.decorate(gsonBuilder);
		Gson gson = gsonBuilder.setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
		String json = gson.toJson( obj, this.getClazz() );
		
		System.out.println( "====== save(" + file.getAbsolutePath() + ") ======" );
		System.out.println( json );
		System.out.println( "=======================================================" );

		file.getParentFile().mkdirs();
		
		try
		(
		  BufferedWriter writer = new BufferedWriter( new FileWriter(file) );
		)
		{
			writer.write(json);
		}
	}
	
	abstract protected Type getType();
	abstract protected Class<?> getClazz();
	abstract protected void decorate( GsonBuilder gsonBuilder );
}
