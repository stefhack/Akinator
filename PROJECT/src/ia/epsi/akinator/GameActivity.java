package ia.epsi.akinator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;

import Management.Algorithm;
import Management.JsonReader;
import Management.JsonSingleton;

public class GameActivity extends Activity{
	private static Context gameContext;
	//Declaration
	private Button buttonYes,buttonNo, buttonDontNo, buttonRather, buttonRatherNot;
	private TextView textViewQuestion;
	private String actualQuestion;
	private String actualKey;
	private HashMap<String,String> hashMapQuestionResponse = new HashMap<String, String>();
	private Algorithm algo ;
    private JsonSingleton jsonSingleton;
    private int nb_questions_asked =0;
    private JsonReader jsonReader;
	ImageView imageViewGenie;
	Integer countForGeniePicture = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.gameContext = getApplicationContext();
        jsonSingleton=JsonSingleton.getInstance(gameContext);
        jsonReader=new JsonReader(gameContext);
		setContentView(R.layout.activity_game);
		

        algo = new Algorithm(gameContext);
		algo.resetAllData();
		
		// Reset the JSON's with values from start
		jsonSingleton.initializeJSONs();
		//Assignement
		this.buttonYes = (Button)findViewById(R.id.buttonYes);
		this.buttonNo = (Button)findViewById(R.id.buttonNo);
		this.buttonDontNo = (Button)findViewById(R.id.buttonDontNo);
		this.buttonRather = (Button)findViewById(R.id.buttonRather);
		this.buttonRatherNot = (Button)findViewById(R.id.buttonRatherNot);
		this.textViewQuestion = (TextView)findViewById(R.id.textViewQuestionRequest);
		this.imageViewGenie = (ImageView)findViewById(R.id.imageViewGenie);

        //TESTS
        {
            try {
                Log.i("GAME ACTIVITY READ JSON FROM INTERNAL STORAGE :", jsonReader.readJSONfromInternalStorage("questions.json"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
		displayQuestion();




		
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
        //OU il n'y a pas encore de persos à proposer
        //ET  s'il y a encore des questions
        try {
            if(nb_questions_asked < algo.QUESTIONS_THRESOLD || !algo.hasMorePersoToPropose() && jsonSingleton.getQuestionsLeft() > 0){
                //On pose à nouveau une question
                displayQuestion();
            }
            //On commence à proposer un personnage
            else{
                Intent intent=new Intent(GameActivity.this,ResultActivity.class);
                intent.putExtra("responses",this.hashMapQuestionResponse);//on fait passer les réponses à la prochaine activité
                int[] nbQuestionArray = new int[1];
                nbQuestionArray[0] = nb_questions_asked;
                intent.putExtra("nbQuestionsAsked",nbQuestionArray);//on fait passer le nombre de questions pour calculer le score
                startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
		if(countForGeniePicture == 0){
			countForGeniePicture = 3;
		}
		if(countForGeniePicture == 1){
			imageViewGenie.setImageResource(R.drawable.genie1);
		}
		else if(countForGeniePicture == 2){
			imageViewGenie.setImageResource(R.drawable.genie2);
		}
		else if(countForGeniePicture == 3) {
			imageViewGenie.setImageResource(R.drawable.genie3);
		}
		countForGeniePicture--;
		
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
