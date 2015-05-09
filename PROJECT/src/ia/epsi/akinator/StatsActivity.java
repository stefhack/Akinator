package ia.epsi.akinator;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import Management.GameStatsManager;

/**
 * Created by Stef on 07/05/2015.
 */
public class StatsActivity extends Activity {

    private TextView listStats;
    private TextView textLastGame;
    private GameStatsManager statsManager;
private TextView textMostPlayed;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_stats);
        statsManager= new GameStatsManager(getApplicationContext());


        listStats=(TextView)findViewById(R.id.textPercentWon);
        textLastGame=(TextView)findViewById(R.id.textLastGame);
        textMostPlayed=(TextView)findViewById(R.id.textMostPlayed);

        double successPercent = Math.round((double)statsManager.getNbGamesWon()/(double)statsManager.getNbGamesPlayed()*100);
        JSONObject lastGame = statsManager.getTheLastGame();
        JSONObject mostPlayed = statsManager.getTheMostPlayedCharacter();

        statsManager.sortListStats();
        Log.i("STATS ACTIVITY :", statsManager.getListStatsCharacters().toString());
        listStats.setText("Pourcentage de résussite de Akinator : "+Double.toString(successPercent)+"%  \n Nombre de parties jouées: "+statsManager.getNbGamesPlayed());
        try {
            textMostPlayed.setText("Personnage le plus joué : "+mostPlayed.getString("character").toUpperCase()+" avec "+mostPlayed.getInt("nbGamePlayed")+" fois");
            textLastGame.setText("Dernier jeu joué le "+lastGame.get("date").toString()+" avec le personnage : "+lastGame.getString("character"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}