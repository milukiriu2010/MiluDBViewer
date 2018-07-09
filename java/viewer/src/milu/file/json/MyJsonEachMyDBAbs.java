package milu.file.json;

import java.lang.reflect.Type;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import milu.db.MyDBAbstract;

public class MyJsonEachMyDBAbs<T> extends MyJsonEachAbstract<T> {

	@Override
	protected Type getType()
	{
		// TODO Auto-generated method stub
		return new TypeToken<MyDBAbstract>() {}.getType();
	}

	@Override
	protected Class<?> getClazz() 
	{
		return MyDBAbstract.class;
	}
	
	@Override
	protected void decorate( GsonBuilder gsonBuilder )
	{
		gsonBuilder.registerTypeAdapter( MyDBAbstract.class, new JsonElementAdapter() );
	}
}
