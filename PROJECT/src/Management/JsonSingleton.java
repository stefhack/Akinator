package Management;

/**
 * Created by Stef on 09/03/2015.
 */
import org.json.JSONException;
import org.json.JSONObject;

public class JsonSingleton {

    private static JsonSingleton ourInstance = null;
    private JSONObject jsonPersonnages;
    private JSONObject jsonQuestions;

    public static JsonSingleton getInstance() {
        if(ourInstance == null) ourInstance=new JsonSingleton();
        return ourInstance;
    }

    private JsonSingleton() {
        try {
            jsonPersonnages=new JSONObject("personnages.json");
            jsonQuestions=new JSONObject("");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getJsonPersonnages() {
        return jsonPersonnages;
    }

    public JSONObject getJsonQuestions() {
        return jsonQuestions;
    }
}
