package milu.file.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

//https://www.javacodegeeks.com/2012/04/json-with-gson-and-abstract-classes.html
public class JsonElementAdapter implements JsonSerializer<Object>, JsonDeserializer<Object> 
{
	private static final String CLASS_META_KEY = "CLASS_META_KEY";
	
	// https://stackoverflow.com/questions/3629596/deserializing-an-abstract-class-in-gson
	@Override
	public Object deserialize(JsonElement json, Type tpeOfT, JsonDeserializationContext context) throws JsonParseException 
	{
        JsonObject jsonObject = json.getAsJsonObject();
        String className = jsonObject.get(CLASS_META_KEY).getAsString();
        
        try 
        {
        	Class<?> clz = Class.forName(className);
        	return context.deserialize( json, clz );
        } 
        catch (ClassNotFoundException cnfe) 
        {
        	throw new JsonParseException( "Unknown element: " + className, cnfe );
        }
	}

	@Override
	public JsonElement serialize(Object src, Type typeOfSrc, JsonSerializationContext context)
	{
        JsonElement jsonEle = context.serialize( src, src.getClass() );
        jsonEle.getAsJsonObject().addProperty( CLASS_META_KEY, src.getClass().getCanonicalName() );
        
        return jsonEle;
	}

}
