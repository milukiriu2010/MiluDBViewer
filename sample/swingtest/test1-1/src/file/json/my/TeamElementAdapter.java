package file.json.my;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonObject;

// https://www.javacodegeeks.com/2012/04/json-with-gson-and-abstract-classes.html
public class TeamElementAdapter implements JsonSerializer<Object>, JsonDeserializer<Object> 
{
	private static final String CLASS_META_KEY = "CLASS_META_KEY";
	
	// https://stackoverflow.com/questions/3629596/deserializing-an-abstract-class-in-gson
	@Override
	public Object deserialize(JsonElement json, Type tpeOfT, JsonDeserializationContext context) throws JsonParseException 
	{
        //JsonObject jsonObject = json.getAsJsonObject();
        //String type = jsonObject.get("type").getAsString();
        //JsonElement element = jsonObject.get("properties");
        JsonObject jsonObject = json.getAsJsonObject();
        String className = jsonObject.get(CLASS_META_KEY).getAsString();
        
        try 
        {
        	//return context.deserialize(element, Class.forName("file.json.my." + type));
        	Class<?> clz = Class.forName(className);
        	return context.deserialize( json, clz );
        } 
        catch (ClassNotFoundException cnfe) 
        {
        	throw new JsonParseException("Unknown element: " + className, cnfe );
        }
	}

	@Override
	//public JsonElement serialize(Team src, Type typeOfSrc, JsonSerializationContext context)
	public JsonElement serialize(Object src, Type typeOfSrc, JsonSerializationContext context)
	{
        //JsonObject result = new JsonObject();
        
        //result.add("type", new JsonPrimitive(src.getClass().getSimpleName()));
        //result.add("properties", context.serialize(src, src.getClass()));
        //return result;
        
        JsonElement jsonEle = context.serialize( src, src.getClass() );
        jsonEle.getAsJsonObject().addProperty( CLASS_META_KEY, src.getClass().getCanonicalName() );
        
        return jsonEle;
	}

}
