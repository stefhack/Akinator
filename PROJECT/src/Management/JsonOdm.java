package Management;

import org.json.JSONArray;
import org.json.JSONObject;
/**
 * JsonOdm, used to manage the different .json for the application
 * @author JOHN
 *
 */
public class JsonOdm {
	/**
	 * findCharactersByQuestion return a collection of character according to the question passed by parameter
	 * @param question a question
	 * @return
	 */
	public JSONObject findCharactersByQuestion(String question){
		//Get jsonSingleton
		JsonSingleton jsonSingleton = JsonSingleton.getInstance();
		//Load question json Array
		JSONArray questions = jsonSingleton.getJsonQuestions();
		JSONArray characters = jsonSingleton.getJsonPersonnages();
		
		
		return null;
	}
	/**
	 * insertCharacter, insert a character and its questions
	 * @param characters a jsonObject character
	 */
	public void insertCharacter(JSONObject caracters){
		
	}
	/**
	 * getQuestion get a question by key from questions.json
	 * @param key a JSON key
	 * @return a toString question
	 */
	public String getQuestion(String key){
		return null;
	}
	/**
	 * getCharacterByIndex get a character from personnages.json by index
	 * @param index a JSON index
	 * @return a character jsonObject
	 */
	public JSONObject getCharacterByIndex(int index){
		return null;
	}
	
	
}
