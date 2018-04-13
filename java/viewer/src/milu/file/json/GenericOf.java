package milu.file.json;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

// https://osa030.hatenablog.com/entry/2015/10/20/182439
public class GenericOf<X, Y> implements ParameterizedType
{
    private final Class<X> container;
    private final Class<Y> wrapped;
    
    public GenericOf(Class<X> container, Class<Y> wrapped) 
    {
        this.container = container;
        this.wrapped = wrapped;
    }    
    
	@Override
	public Type[] getActualTypeArguments() 
	{
		return new Type[]{wrapped};
	}

	@Override
	public Type getOwnerType() 
	{
		return null;
	}

	@Override
	public Type getRawType() 
	{
		return container;
	}

}
