package Management;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
/**
 * Created by Stef on 23/03/2015.
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
	private static ArrayList<String> listPersosSuppressed = new ArrayList<String>();

    /*
    Correspondance entre les codes et les valeurs des réponses
    * */
    private static  HashMap<String,String> responseByResponseCode =  new HashMap<String, String>();
    static{
      responseByResponseCode.put("0","inconnu");
      responseByResponseCode.put("1","oui");
      responseByResponseCode.put("2","non");
      responseByResponseCode.put("3","plutot");
      responseByResponseCode.put("4","plutotPas");

  }

    public String getResponseByCode(String code){
    	return responseByResponseCode.get(code);
    }
    
	/*
	 * Liste des scores pour les personnages
	 * String La clé du personnage dans
	 * le JSON 
	 * Integer le score de ce personnage
	 */
	private static HashMap<String, Double> scoresByPerso=  new HashMap<String, Double>();
	/*
	 * Instance de la classe gérant les JSON
	 */
	private static JsonOdm jsonOdm;

    //Minimum de questions à poser avant de proposer un résultat
    public final int QUESTIONS_THRESOLD =  10;

	/*
	 * Seuil minimum à atteindre pour proposer une réponse au joueur
	 */
	private final int PROPOSAL_THRESOLD = 80;

	/*
	 * Permet d'obtenir la question la plus pertinente, la prochaine à poser
	 * 
	 * @return question String La question sous forme de chaine
	 */
	public String getTheMostPertinenteQuestion() throws JSONException {

		String question = "";
		JSONArray jsonQuestions = jsonOdm.getSingleton().getJsonQuestions();
		JSONObject questions = jsonQuestions.getJSONObject(0);
		//Log.i("QUESTIONS ", questions.toString());

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
		return keyForQuestionToRetreive+";"+question;
	}

	/**
	 * 
	 * @param questionKey
	 * @param responseGiven
	 * @throws JSONException 
	 */
	public void calculateScoreForCharacters(String questionKey,String responseGiven) throws JSONException
	{
		JSONArray characters = jsonOdm.getSingleton().getJsonPersonnages();
		JSONArray jsonQuestions = jsonOdm.getSingleton().getJsonQuestions();
        responseGiven=responseByResponseCode.get(responseGiven);

		for(int i=0;i<characters.length();++i){

			int scorePerso=0;
			JSONObject perso= characters.getJSONObject(i);
		    String response = perso.getString(questionKey);
            String nomPerso = perso.getString("Personnage");

            double score = getScore(responseGiven,response);

                //On vérifie si le perso a déjà un score
                if(scoresByPerso.containsKey(nomPerso)){
                    score += scoresByPerso.get(nomPerso);
                }
                scoresByPerso.put(nomPerso,score);
                Log.i("ALGO SCORE "+nomPerso,Double.toString(score));
        }

	}

    private Double getScore(String responseGiven,String responsePerso){
        double score=0;
        if(responseGiven.equals(responsePerso)) {
            score=3;
        }
        else {
        	if(responsePerso.equals("oui")){
        		if(responseGiven.equals("non")){
        			score = -3;
        		}
        		else if(responseGiven.equals("plutotPas")){
        			score = -1.5;
        		}
        		else if(responseGiven.equals("plutot")){
        			score = 1.5;
        		}
        	}
        	else if(responsePerso.equals("non")){
        		if(responsePerso.equals("oui")){
        			score = -3;
        		}
        		else if(responseGiven.equals("plutotPas")){
        			score = 1.5;
        		}
        		else if(responseGiven.equals("plutot")){
        			score = -1.5;
        		}
        	}
        	else if(responsePerso.equals("inconnu")){
        		if(responsePerso.equals("oui")){
        			score = -3;
        		}
        		else if(responseGiven.equals("non")){
        			score = -3;
        		}
        		else if(responseGiven.equals("plutotPas")){
        			score = 1.5;
        		}
        		else if(responseGiven.equals("plutot")){
        			score = 1.5;
        		}
        	}
            /*if(responseGiven == "oui"){

                score = (responsePerso.equals("non")) ? -3 : 0;
            }
            else if(responseGiven == "non"){

                score = (responsePerso.equals("oui")) ? -3 : 0;
            }*/
        }
        return score;
    }

    public void deleteQuestion(String questionKey) throws JSONException {
            jsonOdm.deleteQuestionByKey(questionKey);
    }

    /*
    * Supprime un personnage de la liste des scores */
    public void deletePersoFromScore(String characterKey) {

        scoresByPerso.remove(characterKey);
    }

	/*
	 * Permet de rajouter un personnage dans le JSON des personnages
	 * 
	 * @return void
	 */
	public  void addNewPerso(HashMap<String, String> caracteristiques)
			throws IOException {

		JSONObject newCharacter = new JSONObject(caracteristiques);
		jsonOdm.insertCharacter(newCharacter);
	}

	/*
	 * Permet de rajouter un personnage dans la liste des persos supprimés
	 * 
	 * @parama index int L'index du personnage dans le JSON
	 */
	public  void addPersosSuppressed(String nomperso) {
        //S'il ne le contient pas déjà on le rajoute
        if(!listPersosSuppressed.contains(nomperso))
		listPersosSuppressed.add(nomperso);
	}



	/*
	 * Permet de récupérer la liste des personnages supprimés
	 * 
	 * @return ArrayList<String>
	 */
	public  ArrayList<String> getPersosSuppressed() {
		return listPersosSuppressed;
	}


	/*
	 * Permet d'obtnenir le nombre de personnages écartés
	 * 
	 * @return int Le nombre de personnages supprimés
	 */
	public  int countPersosSuppressed() {
		return listPersosSuppressed.size();
	}

	/*
	 * Permet de réinitialiser la liste des personnages écartés
	 */
	public  void clearPersosSuppressed() {
		listPersosSuppressed.clear();
	}



	/*
	 * Permet de connaitre le nombre de personnages restant,
	 * 
	 * qui ne sont pas encore écartés
	 */
	public  int getNbCharactersLeft() {
		return jsonOdm.getSingleton().getJsonPersonnages().length()
				- listPersosSuppressed.size();

	}

    public String getMaxScore(double nbQuestions){
        double scorePerso=0;
        for (Map.Entry<String, Double> entry : scoresByPerso.entrySet()) {
            if(scorePerso < entry.getValue()) {
                scorePerso = entry.getValue();
            }
        }
        scorePerso = (scorePerso/(double)(nbQuestions*3));
        scorePerso *=100;
       return (Double.toString( scorePerso));
    }

	/*
	 * Permet d'obtenir le personnage ayant obtenu le score le plus élevé
	 * 
	 * @return String Le nom du personnage
	 */
	public  String getPersoByMaxScore() {

		String nomPerso = "";
        double scorePerso=0;

			for (Map.Entry<String, Double> entry : scoresByPerso.entrySet()) {
                    if(scorePerso < entry.getValue()) {
                        scorePerso = entry.getValue();
                        nomPerso = entry.getKey();
                    }
			}

		return nomPerso;

	}

    /*
    * Permet de savoir si il y a AU MOINS un personnage
      * ayant un score > minimum donné par la constante PROPOSAL_THRESOLD
    * */
    public boolean hasMorePersoToPropose(){

        boolean hasPerso = false;

        for (Map.Entry<String, Double> entry : scoresByPerso.entrySet()) {

            double percent = ((double)entry.getValue()/(double)(QUESTIONS_THRESOLD*3))*(double)100;//Pourcentage = score du perso / score Total théorique

           if(percent >= (double)PROPOSAL_THRESOLD){// 3 => score max pour une question, QUESTIONS_THRESOLD le nb de questions posées
               hasPerso = true ;
               break;

           }

        }

        return  hasPerso;
    }

}
