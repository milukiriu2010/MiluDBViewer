package file.json;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class ExampleConvertor {
    private static class MyCustomExclusionStrategy implements ExclusionStrategy {

        public boolean shouldSkipClass(Class<?> arg0) {
                return false;
        }

        public boolean shouldSkipField(FieldAttributes f) {
                return (f.getDeclaringClass() == Todo.class && f.getName().equals("changes"));
        }

    }

    private static int current;

	public static void main(String[] args) {
	        List<Todo> list = new ArrayList<Todo>();
	        list.add(createTodo("Application model", "Flexible and extensible"));
	        list.add(createTodo("DI", "@Inject as programming mode"));
	        list.add(createTodo("OSGi", "Services"));
	        list.add(createTodo("SWT", "Widgets"));
	        list.add(createTodo("JFace", "Especially Viewers!"));
	        list.add(createTodo("CSS Styling", "Style your application"));
	        list.add(createTodo("Eclipse services", "Selection, model, Part"));
	        list.add(createTodo("Renderer", "Different UI toolkit"));
	        list.add(createTodo("Compatibility Layer", "Run Eclipse 3.x"));
	
	        // Add .excludeFieldsWithoutExposeAnnotation() to exclude fields not annotated with @Expose
	        Gson gson = new GsonBuilder().setPrettyPrinting().setExclusionStrategies(new MyCustomExclusionStrategy()).create();
	        Type type = new TypeToken<List<Todo>>() {}.getType();
	        String json = gson.toJson(list, type);
	        try {
	                Files.write(Paths.get("db.txt"), json.getBytes(), StandardOpenOption.CREATE);
	        } catch (IOException e) {
	                e.printStackTrace();
	        }
	        String content ="";
	        try {
	                content = new String(Files.readAllBytes(Paths.get("db.txt")));
	        } catch (IOException e) {
	                e.printStackTrace();
	        }
	
	
	        System.out.println(content);
	}
	
	private static Todo createTodo(String summary, String description) {
	        return new Todo(current++, summary, description, false, new Date());
	}

}
