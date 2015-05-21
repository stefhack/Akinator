package ia.epsi.akinator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;

import Management.Algorithm;
import Management.JsonReader;
import Management.JsonSingleton;

public class GameActivity extends Activity {
	private static Context gameContext;
	// Declaration
	private Button buttonYes, buttonNo, buttonDontNo, buttonRather,
			buttonRatherNot;
	private TextView textViewQuestionRequest;
	private String actualQuestion;
	private String actualKey;
	private HashMap<String, String> hashMapQuestionResponse = new HashMap<String, String>();
	private Algorithm algo;
	private JsonSingleton jsonSingleton;
	private int nb_questions_asked = 0;
	private JsonReader jsonReader;
	ImageView imageViewGenie;
	boolean isAlreadyIDontKnow = true;
	Integer countForGeniePicture = 0;
	private int iDontknowCounter = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		GameActivity.gameContext = getApplicationContext();
		jsonSingleton = JsonSingleton.getInstance(gameContext);
		jsonReader = new JsonReader(gameContext);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_game);

		algo = new Algorithm(gameContext);
		if(getIntent().getStringExtra("ActivityName").equals("ResultActivity")){
			nb_questions_asked = getIntent().getIntArrayExtra("nbQuestions")[0];
			hashMapQuestionResponse = (HashMap<String, String>) getIntent().getSerializableExtra("responses");
		}
		
		// Assignement
		this.buttonYes = (Button) findViewById(R.id.buttonYes);
		this.buttonNo = (Button) findViewById(R.id.buttonNo);
		this.buttonDontNo = (Button) findViewById(R.id.buttonDontNo);
		this.buttonRather = (Button) findViewById(R.id.buttonRather);
		this.buttonRatherNot = (Button) findViewById(R.id.buttonRatherNot);
		this.textViewQuestionRequest = (TextView) findViewById(R.id.textViewQuestionRequest);
		this.imageViewGenie = (ImageView) findViewById(R.id.imageViewGenie);
		

		//mise en place du font
		Typeface typeFace=Typeface.createFromAsset(getAssets(),"brush.ttf");
		this.textViewQuestionRequest.setTypeface(typeFace);
		this.buttonYes.setTypeface(typeFace);
		this.buttonNo.setTypeface(typeFace);
		this.buttonDontNo.setTypeface(typeFace);
		this.buttonRather.setTypeface(typeFace);
		this.buttonRatherNot.setTypeface(typeFace);
		
		

		// TESTS
		{
			try {
				Log.i("GAME ACTIVITY READ JSON persos FROM INTERNAL STORAGE :",
						jsonReader
								.readJSONfromInternalStorage("personnages.json"));
				Log.i("GAME ACTIVITY READ JSON questions FROM INTERNAL STORAGE :",
						jsonReader
								.readJSONfromInternalStorage("questions.json"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		displayQuestion();

		this.buttonDontNo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				takeResponseAndGoOn("0");
				if (!isAlreadyIDontKnow) {
					isAlreadyIDontKnow = true;
				} else {
					iDontknowCounter++;
				}
			}
		});

		this.buttonYes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				takeResponseAndGoOn("1");
				isAlreadyIDontKnow = false;
				iDontknowCounter = 0;
			}
		});

		this.buttonNo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				takeResponseAndGoOn("2");
				isAlreadyIDontKnow = false;
				iDontknowCounter = 0;
			}
		});

		this.buttonRather.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				takeResponseAndGoOn("3");
				isAlreadyIDontKnow = false;
				iDontknowCounter = 0;
			}
		});

		this.buttonRatherNot.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				takeResponseAndGoOn("4");
				isAlreadyIDontKnow = false;
				iDontknowCounter = 0;
			}
		});
	}

	private void takeResponseAndGoOn(String responseCode) {
		// On incrémente le nb de questions posées
        try {
            Log.e("GAME ACTIVITY QUESTIONS LEFT: ",Integer.toString(jsonSingleton.getQuestionsLeft()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ++nb_questions_asked;

		// Remplit la liste des questions avec les réponses données
		fillHashMapQuestionResponse(responseCode);

		// Calcule du score pour chaque perso pour la question courante
		// avec la réponse donnée par le USer

		try {
			algo.calculateScoreForCharacters(this.actualKey, responseCode);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// Suppression de la dernière question posée
		try {

			algo.deleteQuestion(this.actualKey);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// On a pas atteint le minimum de questions à poser
		// OU il n'y a pas encore de persos à proposer
		// ET s'il y a encore des questions
		try {
			if (iDontknowCounter < 5) {
				if(jsonSingleton.getQuestionsLeft() > 0){
					if ((nb_questions_asked < algo.QUESTIONS_THRESOLD)
							|| (!algo.hasMorePersoToPropose(nb_questions_asked))) {
						// On pose à nouveau une question
						displayQuestion();
					}
					// On commence à proposer un personnage
					else {
						goToResultActivity();
					}
				}
				else{
					goToResultActivity();
				}
			} else {
				goToResultActivity();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private void goToResultActivity(){
		Intent intent = new Intent(GameActivity.this,
				ResultActivity.class);
		intent.putExtra("iDontKnowCounter", iDontknowCounter);
		intent.putExtra("responses", this.hashMapQuestionResponse);
		int[] nbQuestionArray = new int[1];
		nbQuestionArray[0] = nb_questions_asked;
		intent.putExtra("nbQuestionsAsked", nbQuestionArray);
		startActivity(intent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public static Context getGameContext() {
		return gameContext;
	}

	private void fillHashMapQuestionResponse(String response) {
		this.hashMapQuestionResponse.put(this.actualKey, response);
	}

	private void displayQuestion() {
		showGeniePicture();
		String requestAlgorithm = "";
		try {

			requestAlgorithm = algo.getTheMostPertinenteQuestion();
		} catch (JSONException e) {

			e.printStackTrace();
		}

		String[] partsRequestAlgorithm = requestAlgorithm.split(";");
		this.actualKey = partsRequestAlgorithm[0];
		this.actualQuestion = partsRequestAlgorithm[1];
		this.textViewQuestionRequest.setText(actualQuestion);
	}

	private void showGeniePicture() {
		if (countForGeniePicture == 0) {
			countForGeniePicture = 3;
		}
		if (countForGeniePicture == 1) {
			imageViewGenie.setImageResource(R.drawable.genie1);
		} else if (countForGeniePicture == 2) {
			imageViewGenie.setImageResource(R.drawable.genie2);
		} else if (countForGeniePicture == 3) {
			imageViewGenie.setImageResource(R.drawable.genie3);
		}
		countForGeniePicture--;
	}
}
