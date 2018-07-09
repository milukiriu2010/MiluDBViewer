package milu.file.json;

import java.lang.reflect.Type;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import milu.main.AppConf;

public class MyJsonEachAppConf<T> extends MyJsonEachAbstract<T> 
{
	@Override
	protected Type getType() 
	{
		return new TypeToken<AppConf>() {}.getType();
	}

	@Override
	protected Class<?> getClazz() 
	{
		return AppConf.class;
	}
	
	@Override
	protected void decorate( GsonBuilder gsonBuilder )
	{
		
	}
}
