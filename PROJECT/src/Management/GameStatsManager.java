package Management;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Stef on 07/05/2015.
 */
public class GameStatsManager {


    private  JsonWriter jsonWriter;
    private JsonSingleton jsonSingleton;
    /**
    List containing all the stats for all the characters
    * */
    private ArrayList<JSONObject> listStatsCharacters = new ArrayList<JSONObject>();


    public GameStatsManager(Context context){
        jsonWriter = new JsonWriter(context);
        jsonSingleton = JsonSingleton.getInstance(context);
        fillListStatsCharacters();
    }

    public ArrayList<JSONObject> getListStatsCharacters(){
        return listStatsCharacters;
    }

    /**
    * Method which sort the list descending of all stats according to the number
    * of games played
    * */
    public void sortListStats(){
        Collections.sort(listStatsCharacters,new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject first, JSONObject second) {
                int returnValue = 0;
                try {
                    returnValue= second.getInt("nbGamePlayed") - first.getInt("nbGamePlayed")  ;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return returnValue;
            }
        });
    }
    /**
    * Insert a new Game played into the Stats list with all games played
    * @param String character The character name
    * @param Date date The date when the game has been played
    * @param boolean characterIsFound True if Akinator found the character (he won) false otherwise
    * */
    public void insertGame(String character,Date date,boolean characterIsFound){
        JSONArray stats = jsonSingleton.getJsonStatistiques();
        JSONObject gameStat = new JSONObject();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy 'à' hh:mm:ss");

        try {
            gameStat.put("character",character);
            gameStat.put("date",sdf.format(date));
            gameStat.put("found",characterIsFound);
            stats.put(gameStat);

            jsonWriter.writeJsonIntoInternalStorage(stats.toString(), jsonSingleton.jsonStatsFile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /**
    * To get the number of games played and recorded
    * @return int
    * */
    public int getNbGamesPlayed(){;
        JSONArray jsonStats = jsonSingleton.getJsonStatistiques();
        if(jsonStats == null) return 0;
        return jsonStats.length();
    }

    /**
    *To get the number of games won in all the gamed recorded
    * @return int
    * */
    public int getNbGamesWon(){
        JSONArray jsonStats = jsonSingleton.getJsonStatistiques();
        int nbGamesWon=0;
        for(int i=0;i<jsonStats.length();i++){

            JSONObject game;
            try {
                 game = (JSONObject) jsonStats.get(i);
                if(game.getBoolean("found") == true) nbGamesWon++;
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        return nbGamesWon;
    }

    /**
    * To get the most played character in all the gamed recorded
    * @return JSONObject a object containing the name of character and the number of games played
    * */
    public JSONObject getTheMostPlayedCharacter(){
        sortListStats();
        return listStatsCharacters.get(0);
    }
    /**
    * Method which fill in memory the list of stats for every character
    * */
    private void fillListStatsCharacters(){

        JSONArray jsonStats = jsonSingleton.getJsonStatistiques();

        JSONArray jsonCharacters = jsonSingleton.getJsonPersonnages();

        if(jsonStats.length()!= 0){
            for(int j=0;j<jsonCharacters.length();j++){

                int nbGamePlayed=0;
                JSONObject character=null;

                for(int i=0;i<jsonStats.length();i++){

                    JSONObject game;

                    try {
                        game = (JSONObject) jsonStats.get(i);
                        character = (JSONObject) jsonCharacters.get(j);
                        if(game.getString("character").equals(character.getString("Personnage"))) nbGamePlayed++;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    JSONObject characterStat = new JSONObject();
                    characterStat.put("character",character.getString("Personnage"));
                    characterStat.put("nbGamePlayed",nbGamePlayed);
                    this.listStatsCharacters.add(characterStat);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }


    }

    /**
    * Method to get the last game played in all gamed recorded
    * @return JSONObject an object representing the last game with its details
    * */
    public JSONObject getTheLastGame(){
        JSONArray jsonStats = jsonSingleton.getJsonStatistiques();
        JSONObject lastGame=null;
        try {
            lastGame= (JSONObject) jsonStats.get(jsonStats.length()-1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lastGame;
    }



}
