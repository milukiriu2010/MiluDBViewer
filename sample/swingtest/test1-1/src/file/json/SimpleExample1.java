package file.json;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

// http://www.vogella.com/tutorials/JavaLibrary-Gson/article.html
public class SimpleExample1 {
    public static void main(String[] args) {
        List<Task> list = new ArrayList<Task>();
        for (int i = 0; i < 20; i++) {
                list.add(new Task(i, "Test1", "Test2", Task.Status.ASSIGNED, 10));
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Task>>() {}.getType();
        String json = gson.toJson(list, type);
        System.out.println(json);
        List<Task> fromJson = gson.fromJson(json, type);

        for (Task task : fromJson) {
                System.out.println(task);
        }
    }
}
