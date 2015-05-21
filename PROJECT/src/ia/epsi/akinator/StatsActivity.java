package ia.epsi.akinator;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import Management.GameStatsManager;

/**
 * Created by Stef on 07/05/2015.
 */
public class StatsActivity extends Activity {

    private TextView textViewPercentWon,textViewLastGame,textViewMostPlayed, textViewTitle;
    private GameStatsManager statsManager;
    private Button buttonBack;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_stats);
        statsManager= new GameStatsManager(getApplicationContext());


        textViewPercentWon=(TextView)findViewById(R.id.textViewPercentWon);
        textViewLastGame=(TextView)findViewById(R.id.textViewLastGame);
        textViewMostPlayed=(TextView)findViewById(R.id.textViewMostPlayed);
        textViewTitle =(TextView)findViewById(R.id.textViewTitle);
        buttonBack = (Button)findViewById(R.id.buttonBack);
        
      //mise en place du font
  		Typeface typeFace=Typeface.createFromAsset(getAssets(),"brush.ttf");
  		this.textViewPercentWon.setTypeface(typeFace);
  		this.textViewLastGame.setTypeface(typeFace);
  		this.textViewMostPlayed.setTypeface(typeFace);
  		this.textViewTitle.setTypeface(typeFace);
  		this.buttonBack.setTypeface(typeFace);

        double successPercent = Math.round((double)statsManager.getNbGamesWon()/(double)statsManager.getNbGamesPlayed()*100);
        JSONObject lastGame = statsManager.getTheLastGame();
        JSONObject mostPlayed = statsManager.getTheMostPlayedCharacter();

        statsManager.sortListStats();
        Log.i("STATS ACTIVITY :", statsManager.getListStatsCharacters().toString());
        textViewPercentWon.setText("Pourcentage de résussite de Akinator : "+Double.toString(successPercent)+"% \nNombre de parties jouées: "+statsManager.getNbGamesPlayed());
        try {
            int nbGame = mostPlayed.getInt("nbGamePlayed");
            String mostPlayedMessage;
            if(nbGame != 0){
                mostPlayedMessage = "Personnage le plus joué : "+mostPlayed.getString("character").toUpperCase()+" avec "+nbGame+" fois";
            }
            else mostPlayedMessage = "Personnage le plus joué : "+lastGame.getString("character")+" avec 1 fois";
        	textViewMostPlayed.setText(mostPlayedMessage);
        	textViewLastGame.setText("Dernier jeu joué le "+lastGame.get("date").toString()+" avec le personnage : "+lastGame.getString("character"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
     // Button click
     		this.buttonBack.setOnClickListener(new OnClickListener() {
     			@Override
     			public void onClick(View v) {
     				Intent intent = new Intent(StatsActivity.this,
     						EndGameActivity.class);
     				startActivity(intent);
     			}
     		});
    }
}