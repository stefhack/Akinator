package ia.epsi.akinator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;

import java.util.HashMap;

import Management.Algorithm;
import Management.JsonSingleton;

public class GameActivity extends Activity{
	private static Context gameContext;
	//Declaration
	private Button buttonTest, buttonYes,buttonNo, buttonDontNo, buttonRather, buttonRatherNot;
	private TextView textViewQuestion;
	private String actualQuestion;
	private String actualKey;
	private HashMap<String,String> hashMapQuestionResponse = new HashMap<String, String>();
	private Algorithm algo ;
    private JsonSingleton jsonSingleton;
    private int nb_questions_asked =0;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.gameContext = getApplicationContext();
        jsonSingleton=JsonSingleton.getInstance(gameContext);
		setContentView(R.layout.activity_game);
		
		//Assignement
		this.buttonTest = (Button)findViewById(R.id.buttonTest);
		this.buttonYes = (Button)findViewById(R.id.buttonYes);
		this.buttonNo = (Button)findViewById(R.id.buttonNo);
		this.buttonDontNo = (Button)findViewById(R.id.buttonDontNo);
		this.buttonRather = (Button)findViewById(R.id.buttonRather);
		this.buttonRatherNot = (Button)findViewById(R.id.buttonRatherNot);
		this.textViewQuestion = (TextView)findViewById(R.id.textViewQuestionRequest);
		
		displayQuestion();
		
		
		//Button click
		this.buttonTest.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		Intent intent=new Intent(GameActivity.this,ResultActivity.class);
    			startActivity(intent);
        	}
        });
		
		this.buttonDontNo.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
                takeResponseAndGoOn("0");
        	}
        });
		
		this.buttonYes.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
                takeResponseAndGoOn("1");
        	}
        });
		
		this.buttonNo.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
                takeResponseAndGoOn("2");
        	}
        });
		
		this.buttonRather.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
                takeResponseAndGoOn("3");
        	}
        });
		
		this.buttonRatherNot.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
                takeResponseAndGoOn("4");
        	}
        });
	}


    private void takeResponseAndGoOn(String responseCode){
        //On incrémente le nb de questions posées
        ++nb_questions_asked;

        //Remplit la liste des questions avec les réponses données
        fillHashMapQuestionResponse(responseCode);

        //Calcule du score pour chaque perso pour la question courante
        // avec la réponse donnée par le USer

        try {
            algo.calculateScoreForCharacters(this.actualKey,responseCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //Suppression de la dernière question posée
        try {

            algo.deleteQuestion(this.actualKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //On a pas atteint le minimum de questions à poser
        if(nb_questions_asked < algo.QUESTIONS_THRESOLD){
            //On pose à nouveau une question
            displayQuestion();
        }
        //On commence à proposer un personnage
        else{
            Intent intent=new Intent(GameActivity.this,ResultActivity.class);
            startActivity(intent);
        }

    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public static Context getGameContext(){
		return gameContext;
	}
	
	private void fillHashMapQuestionResponse(String response){
		this.hashMapQuestionResponse.put(this.actualKey, response);
	}
	
	private void displayQuestion(){
        algo = new Algorithm(gameContext);
        String requestAlgorithm = "";
        try {
        	requestAlgorithm=algo.getTheMostPertinenteQuestion();
		} catch (JSONException e) {

			e.printStackTrace();
		}
        
        
        String[] partsRequestAlgorithm = requestAlgorithm.split(";");
        this.actualKey = partsRequestAlgorithm[0];
        this.actualQuestion = partsRequestAlgorithm[1];
        this.textViewQuestion.setText(actualQuestion);
	}
}
