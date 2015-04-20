package Management;

import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * JsonOdm, used to manage the different .json for the application
 * 
 * @author JOHN
 * 
 */
public class JsonOdm {
	// Get jsonSingleton
	private static JsonSingleton jsonSingleton = JsonSingleton.getInstance();

	/**
	 * findCharactersByQuestionKey return a collection of character according to
	 * the question key passed by parameter
	 * 
	 * @param question
	 *            a question
	 * @return
	 * @throws JSONException
	 */
	public JSONArray findCharactersByQuestionKey(String questionKey)
			throws JSONException {
		JSONArray charactersArray = new JSONArray();
		// Load question json Array
		JSONArray characters = jsonSingleton.getJsonPersonnages();

		// Get key for the question
		for (int i = 0; i < characters.length(); i++) {
			JSONObject character = characters.getJSONObject(i);
			String response = character.getString(questionKey);
			if (response.equals("oui")) {
				charactersArray.put(character);
			}
		}
		return charactersArray;
	}

	/**
	 * insertCharacter, insert a character and its questions
	 * @param characters a jsonObject character
	 * @throws IOException
	 */
	public void insertCharacter(JSONObject caracter) throws IOException {
		// Add character to Character JSON
		// Load question json Array
		JSONArray characters = jsonSingleton.getJsonPersonnages();
		// Add character to json array
		characters.put(caracter);
		// Re-write json file
		FileWriter file = new FileWriter("/data/personnages.json");
		file.write(characters.toString());
		file.flush();
		file.close();
	}

	/**
	 * getQuestion get a question by key from questions.json
	 * @param key a JSON key
	 * @return a toString question
	 */
	public String getQuestion(String key) {
		
		return null;
	}

	/**
	 * getCharacterByIndex get a character from personnages.json by index
	 * 
	 * @param index
	 *            a JSON index
	 * @return a character jsonObject
	 */
	public JSONObject getCharacterByIndex(int index) {
		return null;
	}

}
