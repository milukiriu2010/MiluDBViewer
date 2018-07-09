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

abstract public class MyJsonListAbstract<T> 
{
	public List<T> load(URL url) throws FileNotFoundException, IOException
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
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();
		
		return gson.fromJson( sb.toString(), this.getType() );
	}
	
	abstract protected Type getType();
}
