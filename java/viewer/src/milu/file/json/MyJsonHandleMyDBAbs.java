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

import milu.db.MyDBAbstract;

public class MyJsonHandleMyDBAbs extends MyJsonHandleAbstract 
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
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter( MyDBAbstract.class, new JsonElementAdapter() );
		Gson gson = gsonBuilder.create();
		
		Type type = new TypeToken<MyDBAbstract>() {}.getType();
		MyDBAbstract myDBAbs = gson.fromJson( json, type );
		
		return myDBAbs;
	}

	@Override
	public void save(Object obj) throws IOException 
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter( MyDBAbstract.class, new JsonElementAdapter() );
		Gson gson = gsonBuilder.setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
		Type type = new TypeToken<MyDBAbstract>() {}.getType();
		String json = gson.toJson( (MyDBAbstract)obj, type );
		
		System.out.println( "====== save(" + this.file.getAbsolutePath() + ") ======" );
		System.out.println( json );
		System.out.println( "=======================================================" );
		
		try
		(
		  BufferedWriter writer = new BufferedWriter( new FileWriter(this.file) );
		)
		{
			writer.write(json);
		}
	}

}
