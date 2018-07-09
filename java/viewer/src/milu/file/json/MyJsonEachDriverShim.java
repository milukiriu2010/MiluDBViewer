package milu.file.json;

import java.lang.reflect.Type;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import milu.db.driver.DriverShim;

public class MyJsonEachDriverShim<T> extends MyJsonEachAbstract<T> 
{
	@Override
	protected Type getType() 
	{
		return new TypeToken<DriverShim>() {}.getType();
	}

	@Override
	protected Class<?> getClazz() 
	{
		return DriverShim.class;
	}
	
	@Override
	protected void decorate( GsonBuilder gsonBuilder )
	{
		
	}

}
