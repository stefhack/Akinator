package ia.epsi.akinator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import ia.epsi.akinator.R.drawable;

import java.util.HashMap;

import org.json.JSONException;

import Management.Algorithm;
import Management.JsonSingleton;

public class ResultActivity extends Activity {
	// Declaration
	Button buttonYes, buttonNo;
	TextView resultPerso, textViewResponse;
	Algorithm algo;
	ImageView imageViewResult;
	private static Context gameContext;
	private HashMap<String, String> hashMapQuestionResponse = new HashMap<String, String>();
	private double nb_questions_asked;
	private int iDontKnowCounter;
	private JsonSingleton jsonSingleton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_result);
		Intent intent = getIntent();
		hashMapQuestionResponse = (HashMap<String, String>) intent
				.getSerializableExtra("responses");
		nb_questions_asked = (double) intent
				.getIntArrayExtra("nbQuestionsAsked")[0];
		algo = new Algorithm(getApplicationContext());
		iDontKnowCounter = intent.getIntExtra("iDontKnowCounter", 0);
		jsonSingleton = JsonSingleton.getInstance(gameContext);
		// Assignement
		buttonYes = (Button) findViewById(R.id.buttonYes);
		buttonNo = (Button) findViewById(R.id.buttonNo);
		resultPerso = (TextView) findViewById(R.id.textViewResult);
		textViewResponse = (TextView) findViewById(R.id.textViewResponse);
		imageViewResult = (ImageView)findViewById(R.id.imageViewResult);
		
		// On affiche la première proposition
		showNextProposition();

		// Button click
		buttonYes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ResultActivity.this,
						EndGameActivity.class);
				startActivity(intent);
			}
		});

		buttonNo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				algo.deletePersoFromScore((String) resultPerso.getText());
				try {
					goOnProposition();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	/*
	 * Continue d'afficher des propositions pertinentes
	 */
	private void goOnProposition() throws JSONException {

		Log.i("GO ON PROPOSITION",String.valueOf(algo.hasMorePersoToPropose(nb_questions_asked)));
		if (algo.hasMorePersoToPropose(nb_questions_asked) == true){
			showNextProposition();
		}
		else if(jsonSingleton.getQuestionsLeft() > 0)
		{ 
			// Il n' y a plus de propositions à faire, on demande à
				// l'utilisateur
				// à quel personnage il pensait
			Intent intent = new Intent(ResultActivity.this,GameActivity.class);
			intent.putExtra("ActivityName", "ResultActivity");
			int[] nbQuestionArray = new int[1];
			nbQuestionArray[0] = (int) nb_questions_asked;
			intent.putExtra("nbQuestions",nbQuestionArray);
			intent.putExtra("responses", this.hashMapQuestionResponse);
			startActivity(intent);
			
			
		}
		else{
			Intent intent = new Intent(ResultActivity.this,LearnCharacterActivity.class);
			intent.putExtra("responses", this.hashMapQuestionResponse);// on
																		// fait
																		// passer
																		// les
																		// réponses
																		// à la
																		// prochaine
																		// activité
			startActivity(intent);
		}
	}

	/* Affichage de la proposition */
	private void showNextProposition() {
		if (iDontKnowCounter == 5) {
			//Display response for "je sais pas"
			textViewResponse.setText("Je suis sur � 100%");
			resultPerso.setText("Celui qui ne savait jamais rien!");
			//Set visibility to noButton TP invisible
			buttonNo.setVisibility(View.INVISIBLE);
		} else {
			textViewResponse.setText("Je suis sûr à "
					+ algo.getMaxScore(nb_questions_asked)
					+ " % que vous pensiez �:");
			resultPerso.setText(algo.getPersoByMaxScore());
			String namePerso = algo.getPersoByMaxScore().replaceAll("\\s+","").replaceAll("'", "").replace("-", "");
			showImage(namePerso);
		}
	}
	private void showImage(String name){
		int imageResource = getResources().getIdentifier(name, "drawable", getPackageName());
		if(imageResource != 0){
			Drawable res = getResources().getDrawable(imageResource);
			imageViewResult.setImageDrawable(res);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}