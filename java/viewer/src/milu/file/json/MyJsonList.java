package milu.file.json;

import java.util.List;
import java.util.Collection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import milu.db.MyDBAbstract;
import milu.db.driver.DriverInfo;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;

public class MyJsonList<T> 
{
	public List<T> load(URL url,Type type) throws FileNotFoundException, IOException
	{
		StringBuffer sb = new StringBuffer();
		try
		(
			InputStream      is  = url.openStream();
			DataInputStream  dis = new DataInputStream(is);
		)
		{
			int readSize = 0;
			int pos = 0;
			while ( ( readSize = dis.available() ) > 0 )
			{
				byte[] b = new byte [readSize];
				dis.read( b, pos, readSize );
				pos += readSize;
				sb.append( new String( b ) );
			}
		}
		
		//GsonBuilder gsonBuilder = new GsonBuilder();
		//gsonBuilder.registerTypeAdapter( clazz, new JsonElementAdapter() );
		//Gson gson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();
		Gson gson = new Gson();
		
		//Type type = new TypeToken<List<DriverInfo>>() {}.getType();
		//Type type = new TypeToken<List<T>>() {}.getType();
		
		return gson.fromJson( sb.toString(), type );
	}
	
}
