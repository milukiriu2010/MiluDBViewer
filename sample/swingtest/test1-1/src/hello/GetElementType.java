package hello;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

// https://stackoverflow.com/questions/1942644/get-generic-type-of-java-util-list
public class GetElementType
{
    List<String> stringList = new ArrayList<String>();
    List<Integer> integerList = new ArrayList<Integer>();

    public static void main(String... args) throws Exception {
        Field stringListField = GetElementType.class.getDeclaredField("stringList");
        ParameterizedType stringListType = (ParameterizedType) stringListField.getGenericType();
        Class<?> stringListClass = (Class<?>) stringListType.getActualTypeArguments()[0];
        System.out.println(stringListClass); // class java.lang.String.

        Field integerListField = GetElementType.class.getDeclaredField("integerList");
        ParameterizedType integerListType = (ParameterizedType) integerListField.getGenericType();
        Class<?> integerListClass = (Class<?>) integerListType.getActualTypeArguments()[0];
        System.out.println(integerListClass); // class java.lang.Integer.
    }

}
