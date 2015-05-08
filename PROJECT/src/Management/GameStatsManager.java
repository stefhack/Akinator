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
import java.util.Iterator;

/**
 * Created by Stef on 07/05/2015.
 */
public class GameStatsManager {


    private  JsonWriter jsonWriter;
    private JsonSingleton jsonSingleton;
    private ArrayList<JSONObject> listStatsCharacters = new ArrayList<JSONObject>();


    public GameStatsManager(Context context){
        jsonWriter = new JsonWriter(context);
        jsonSingleton = JsonSingleton.getInstance(context);
        fillListStatsCharacters();
    }

    public ArrayList<JSONObject> getListStatsCharacters(){
        return listStatsCharacters;
    }

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

    public int getNbGamesPlayed(){;
        JSONArray jsonStats = jsonSingleton.getJsonStatistiques();
        if(jsonStats == null) return 0;
        return jsonStats.length();
    }

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

    public JSONObject getTheMostPlayedCharacter(){

        int nbGames=0;
        JSONObject mostPlayedCharacter = new JSONObject();
        String characterName="";



        for(int i=0;i<listStatsCharacters.size();i++){
            JSONObject characterStat = listStatsCharacters.get(i);

            Iterator keys = characterStat.keys();
            while (keys.hasNext()){

                String characterKey = (String) keys.next();
                try {
                    if(characterStat.getInt(characterKey) > nbGames) {
                        nbGames = characterStat.getInt(characterKey);
                        characterName=characterKey;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        try {
            mostPlayedCharacter.put("character",characterName);
            mostPlayedCharacter.put("nbGames",nbGames);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mostPlayedCharacter;
    }

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
                    listStatsCharacters.add(characterStat);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }


    }

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
