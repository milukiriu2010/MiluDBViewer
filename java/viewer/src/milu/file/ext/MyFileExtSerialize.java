package milu.file.ext;

import java.io.File;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class MyFileExtSerialize<T> extends MyFileExtAbstract<T> 
{
	@Override
	public T load(File file, Class<T> clazz) throws IOException, ClassCastException, ClassNotFoundException
	{
		if ( file.exists() == false )
		{
			return null;
		}
		
		ObjectInputStream objInStream =
			new ObjectInputStream
			(
				new FileInputStream(file)
			);
		T obj = clazz.cast(objInStream.readObject());
		objInStream.close();
		return obj;
	}

	@Override
	public void save(File file, T obj) throws IOException 
	{
		file.getParentFile().mkdirs();
		
		ObjectOutputStream objOutStream =
			new ObjectOutputStream
			(
				new FileOutputStream(file)
			);
			
		objOutStream.writeObject(obj);
		objOutStream.close();
	}

}
