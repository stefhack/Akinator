package Management;

/**
 * Created by Stef on 09/03/2015.
 */
import org.json.JSONArray;
import org.json.JSONException;
/**
 * JsonSingleton used to get Json for questions and personnages .json
 * @author JOHN
 */
public class JsonSingleton {

    private static JsonSingleton ourInstance = null;
    private JSONArray jsonPersonnages;
    private JSONArray jsonQuestions;
    
    /**
     * 
     * @return
     */
    public static JsonSingleton getInstance() {
        if(ourInstance == null) ourInstance=new JsonSingleton();
        return ourInstance;
    }
    
    /**
     * 
     */
    private JsonSingleton() {
        try {
            jsonPersonnages=new JSONArray("personnages.json");
            jsonQuestions=new JSONArray("questions.json");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * @return
     */
    public JSONArray getJsonPersonnages() {
        return jsonPersonnages;
    }
    
    /**
     * 
     * @return
     */
    public JSONArray getJsonQuestions() {
        return jsonQuestions;
    }
}
