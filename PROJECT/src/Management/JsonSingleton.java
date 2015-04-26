package Management;

/**
 * Created by Stef on 09/03/2015.
 */
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

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
        String jsonStringPersos = getJsonFromFile("personnages.json");
        String jsonStringQuestions = getJsonFromFile("questions.json");
        try {
			this.jsonPersonnages = new JSONArray(jsonStringPersos);
			this.jsonQuestions = new JSONArray(jsonStringQuestions);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    private String getJsonFromFile(String fileName)
    {
    	String json = null;
		try {

		    InputStream is = this.context.getAssets().open(fileName);
		    int size = is.available();
		    byte[] buffer = new byte[size];
		    is.read(buffer);
		    is.close();
		    json = new String(buffer, "ISO-8859-1");
		    return json;
		    
		} catch (IOException ex) {
		    ex.printStackTrace();
		    return null;
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
}
