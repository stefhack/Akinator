package Management;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Stef on 23/03/2015.
 */

/*
* Classe principale gérant l'algorithme du Jeu Akinator
* */
public  class  Algorithm {

    /*
    * Liste des personnages écartés , qui ne correspondent pas à la
    * réponse à une question posée
    * */
    private static ArrayList<Integer> listPersosSuppressed;
    /*
    * Liste des questions déjà posées dans une partie
    * */
    private static ArrayList<String> listQuestionsSuppressed;

    /*
    *Liste des scores pour les personnages
    * Integer L'index du personnage dans le JSON
    * Integer le score de ce personnage
    * */
    private static ArrayList<HashMap<Integer,Integer>> listScoresByPerso;
    /*
   Instance de la classe gérant les JSON
   * */
     private static JsonOdm jsonOdm = new JsonOdm();

    /*
    * Instance du singleton Json
    * */
    private static JsonSingleton jsonSingleton=JsonSingleton.getInstance();

    /*
    * Seuil minimum  à atteindre pour proposer une réponse au joueur
    * */
    private static final int MINIMUM_THRESOLD = 80;


    /*
    * Permet d'obtenir la  question la plus pertinente,
    * la prochaine à poser
    * @return question String La question sous forme de chaine
    * */
    public static String getTheMostPertinenteQuestion(){
        String question="";

        return question;
    }

    /*
    * Permet de rajouter un personnage dans le JSON des personnages
    * @return void
    * */
     public static void addNewPerso(HashMap<String,String> caracteristiques) throws IOException{

        JSONObject newCharacter = new JSONObject(caracteristiques);
         jsonOdm.insertCharacter(newCharacter);
     }

    /*
    * Permet de rajouter un personnage dans la liste des persos supprimés
    * @parama index int L'index du personnage dans le JSON
    * */
    public static void addPersosSuppressed(int index){
        listPersosSuppressed.add(index);
    }

    /*
    Permet de rajouter une question dans la liste des questions supprimées
    @param key String La clé correspondant à la question dans le JSON
    * */
    public static void addQuestionSuppressed(String key){
        listQuestionsSuppressed.add(key);
    }

    /*
    * Permet de récupérer la liste des personnages supprimés
    * @return ArrayList<Integer>
    * */
    public static ArrayList<Integer> getPersosSuppressed(){
        return listPersosSuppressed;
    }

    /*
    * Permet de récupérer la liste des questions déjà posées
    * @return ArrayList<String>
    * */
    public static ArrayList<String> getQuestionsSuppressed(){
        return  listQuestionsSuppressed;
    }

    /*
    Permet d'obtnenir le nombre de personnages écartés
    @return int Le nombre de personnages supprimés
    * */
    public static int countPersosSuppressed(){
        return listPersosSuppressed.size();
    }

    /*
    * Permet de réinitialiser la liste des personnages écartés
    * */
    public static void clearPersosSuppressed(){
        listPersosSuppressed.clear();
    }

    /*
    * Permet de réinitialiser la liste des questions déjà posées
    * */
    public static void clearListQuestions(){
        listQuestionsSuppressed.clear();
    }

    /*
    * Permet de connaitre le nombre de personnages  restant,
    *
    *  qui ne sont pas encore écartés
    * */
    public static int getNbCharactersLeft(){
     return jsonSingleton.getJsonPersonnages().length() - listPersosSuppressed.size()   ;

    }

    /*
    * Permet d'obtenir le score maximum obtenu par une question
    * @return int Le score maximum
    * */
    public static int getMaxScore(){

        ArrayList<Integer> scores = new ArrayList<Integer>();

        for(HashMap<Integer,Integer> hashMap: listScoresByPerso){

           for(Map.Entry<Integer,Integer> entry : hashMap.entrySet()){
               scores.add(entry.getValue());
           }

        }

       return Collections.max(scores);

    }
}

