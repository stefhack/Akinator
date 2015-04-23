package Management;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Stef on 23/03/2015.
 */

/*
 * Classe principale gérant l'algorithme du Jeu Akinator
 */
public class Algorithm {

	private Context context;

	public Algorithm(Context context) {
		this.context = context;
		this.jsonOdm = new JsonOdm(this.context);
	}

	/*
	 * Liste des personnages écartés , qui ne correspondent pas à la réponse
	 * à une question posée
	 */
	private static ArrayList<Integer> listPersosSuppressed;
	/*
	 * Liste des questions déjà posées dans une partie
	 */
	private static ArrayList<String> listQuestionsSuppressed;

	/*
	 * Liste des scores pour les personnages 
	 * String La cl� du personnage dans
	 * le JSON 
	 * Integer le score de ce personnage
	 */
	private static HashMap<String, Integer> scoresByPerso;
	/*
	 * Instance de la classe gérant les JSON
	 */
	private static JsonOdm jsonOdm;

	/*
	 * Seuil minimum à atteindre pour proposer une réponse au joueur
	 */
	private final int MINIMUM_THRESOLD = 80;

	/*
	 * Permet d'obtenir la question la plus pertinente, la prochaine à poser
	 * 
	 * @return question String La question sous forme de chaine
	 */
	public String getTheMostPertinenteQuestion() throws JSONException {

		String question = "";
		JSONArray characters = jsonOdm.getSingleton().getJsonPersonnages();
		JSONArray jsonQuestions = jsonOdm.getSingleton().getJsonQuestions();
		JSONObject questions = jsonQuestions.getJSONObject(0);
		Log.i("QUESTIONS ", questions.toString());
		// ArrayList<HashMap<String,Integer>> listResponsesByQuestion =new
		// ArrayList<HashMap<String, Integer>>();
		// HashMap<String,Integer> nbPersoByQuestion = new HashMap<String,
		// Integer>();

		Iterator keys = questions.keys();
		int curentScore = 0;
		String keyForQuestionToRetreive = "";
		while (keys.hasNext()) {
			String questionKey = (String) keys.next();
			JSONArray persosWhereOui = jsonOdm.findCharactersByQuestionKey(
					questionKey, "oui");
			JSONArray persosWhereNon = jsonOdm.findCharactersByQuestionKey(
					questionKey, "non");
			JSONArray persosWhereInconnu = jsonOdm.findCharactersByQuestionKey(
					questionKey, "inconnu");

			int nbOui = persosWhereOui.length();
			int nbNon = persosWhereNon.length();
			int nbInconnu = persosWhereInconnu.length();
			
			int scoreCalculated	= (nbOui + 1) * (nbNon + 1) * (nbInconnu + 1);
			if(curentScore < scoreCalculated){
				curentScore = scoreCalculated;
				keyForQuestionToRetreive = questionKey;
			}
					
		}
		//Get question string by question key
		question= jsonOdm.getQuestion(keyForQuestionToRetreive);
		Log.i("MOST PERTINENT QUESTION SCORE : ",String.valueOf(curentScore));
		Log.i("MOST PERTINENT QUESTION : ",question);
		return keyForQuestionToRetreive+";"+question;
	}
	/**
	 * 
	 * @param questionKey
	 * @param response
	 * @throws JSONException 
	 */
	public void calculateScoreForCharacters(String questionKey,String response) throws JSONException
	{
		JSONArray characters = jsonOdm.getSingleton().getJsonPersonnages();
		JSONArray jsonQuestions = jsonOdm.getSingleton().getJsonQuestions();
		
		for(int i=0;i<characters.length();++i){
			int scorePerso=0;
			JSONObject perso= characters.getJSONObject(i);
			
			for(int j=0;j<jsonQuestions.length();++j){
				
			}
		}
	}
	/*
	 * Permet de rajouter un personnage dans le JSON des personnages
	 * 
	 * @return void
	 */
	public static void addNewPerso(HashMap<String, String> caracteristiques)
			throws IOException {

		JSONObject newCharacter = new JSONObject(caracteristiques);
		jsonOdm.insertCharacter(newCharacter);
	}

	/*
	 * Permet de rajouter un personnage dans la liste des persos supprimés
	 * 
	 * @parama index int L'index du personnage dans le JSON
	 */
	public static void addPersosSuppressed(int index) {
		listPersosSuppressed.add(index);
	}

	/*
	 * Permet de rajouter une question dans la liste des questions supprimées
	 * 
	 * @param key String La clé correspondant à la question dans le JSON
	 */
	public static void addQuestionSuppressed(String key) {
		listQuestionsSuppressed.add(key);
	}

	/*
	 * Permet de récupérer la liste des personnages supprimés
	 * 
	 * @return ArrayList<Integer>
	 */
	public static ArrayList<Integer> getPersosSuppressed() {
		return listPersosSuppressed;
	}

	/*
	 * Permet de récupérer la liste des questions déjà posées
	 * 
	 * @return ArrayList<String>
	 */
	public static ArrayList<String> getQuestionsSuppressed() {
		return listQuestionsSuppressed;
	}

	/*
	 * Permet d'obtnenir le nombre de personnages écartés
	 * 
	 * @return int Le nombre de personnages supprimés
	 */
	public static int countPersosSuppressed() {
		return listPersosSuppressed.size();
	}

	/*
	 * Permet de réinitialiser la liste des personnages écartés
	 */
	public static void clearPersosSuppressed() {
		listPersosSuppressed.clear();
	}

	/*
	 * Permet de réinitialiser la liste des questions déjà posées
	 */
	public static void clearListQuestions() {
		listQuestionsSuppressed.clear();
	}

	/*
	 * Permet de connaitre le nombre de personnages restant,
	 * 
	 * qui ne sont pas encore écartés
	 */
	public static int getNbCharactersLeft() {
		return jsonOdm.getSingleton().getJsonPersonnages().length()
				- listPersosSuppressed.size();

	}

	/*
	 * Permet d'obtenir le score maximum obtenu par une question
	 * 
	 * @return int Le score maximum
	 */
	public static int getMaxScore() {

		ArrayList<Integer> scores = new ArrayList<Integer>();

		/*for (HashMap<String, Integer> hashMap : scoresByPerso) {

			for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
				scores.add(entry.getValue());
			}

		}*/

		return Collections.max(scores);

	}
}
