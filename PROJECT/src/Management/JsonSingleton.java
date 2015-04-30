package Management;

/**
 * Created by Stef on 09/03/2015.
 */
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

/**
 * JsonSingleton used to get Json for questions and personnages .json
 * 
 * @author JOHN
 */
public class JsonSingleton {

	private static JsonSingleton ourInstance = null;
	private JSONArray jsonPersonnages;
	private JSONArray jsonQuestions;
	private Context context;
    private JsonReader jsonReader;
	/**
	 * Méthode statique permettant d'obtenir une instance unique de la Classe
	 * 
	 * @return ourInstance L'unique instance de la classe
	 */
	public static JsonSingleton getInstance(Context context) {
		if (ourInstance == null)
			ourInstance = new JsonSingleton(context);
		return ourInstance;
	}

	/**
	 * Constructeur de la classe : il charge les 2 JSON en mémoire (celui des
	 * Personnages et celui des Questions)
	 */
	private JsonSingleton(Context context) {

        this.context = context;
        this.jsonReader = new JsonReader(context);
        String jsonStringPersos = getJsonFromStorage("personnages.json");
        String jsonStringQuestions = getJsonFromStorage("questions.json");
        try {
			this.jsonPersonnages = new JSONArray(jsonStringPersos);
			this.jsonQuestions = new JSONArray(jsonStringQuestions);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    /*
    * Récupère le contenu du fichier dans le Storage
    * @param String filename Le nom du fichier JSON
    * */
    private String getJsonFromStorage(String fileName)  {
            String jsonString="";
        try {
            jsonString= jsonReader.readJSONfromInternalStorage(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    public void initializeJSONs(){
        try {
            this.setJsonQuestion(new JSONArray(jsonReader.readJSONfromInternalStorage("questions.json")));
            this.setJsonPeronnages(new JSONArray(jsonReader.readJSONfromInternalStorage("personnages.json")));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

	/**
	 * Permet d'obtenir les personnages du JSON
	 * 
	 * @return JSONArray un tableau JSON des personnages
	 */
	public JSONArray getJsonPersonnages() {
		return jsonPersonnages;
	}

	/**
	 * Permet d'obtenir les questions du JSON
	 * 
	 * @return JSONArray un tableau JSON des questions
	 */
	public JSONArray getJsonQuestions() {
		return jsonQuestions;
	}

	public void setJsonPeronnages(JSONArray newJson) {
		this.jsonPersonnages = newJson;
	}

	public void setJsonQuestion(JSONArray newJson) {
		this.jsonQuestions = newJson;
	}

    public int getQuestionsLeft() throws JSONException {
        return jsonQuestions.getJSONObject(0).length();
    }
}
