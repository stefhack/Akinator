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
     * Méthode statique permettant d'obtenir une instance unique
     * de la Classe
     * @return ourInstance L'unique instance de la classe
     */
    public static JsonSingleton getInstance() {
        if(ourInstance == null) ourInstance=new JsonSingleton();
        return ourInstance;
    }
    
    /**
     * Constructeur de la classe : il charge les 2 JSON en mémoire
     * (celui des Personnages et celui des Questions)
     */
    private JsonSingleton() {
        try {
            jsonPersonnages=new JSONArray("Personnages.json");
            jsonQuestions=new JSONArray("questions.json");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Permet d'obtenir les personnages du JSON
     * @return JSONArray un tableau JSON des personnages
     */
    public JSONArray getJsonPersonnages() {
        return jsonPersonnages;
    }
    
    /**
     * Permet d'obtenir les questions du JSON
     * @return JSONArray un tableau JSON des questions
     */
    public JSONArray getJsonQuestions() {
        return jsonQuestions;
    }
}
