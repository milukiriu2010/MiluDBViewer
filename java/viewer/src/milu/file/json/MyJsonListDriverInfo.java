package milu.file.json;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.reflect.TypeToken;

import milu.db.driver.DriverInfo;

public class MyJsonListDriverInfo<T> extends MyJsonListAbstract<T> 
{
	protected Type getType()
	{
		return new TypeToken<List<DriverInfo>>() {}.getType();
	}
}
