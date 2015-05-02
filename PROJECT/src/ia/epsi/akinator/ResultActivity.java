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

import java.util.HashMap;

import Management.Algorithm;

public class ResultActivity extends Activity{
	//Declaration
	Button buttonYes, buttonNo;
	TextView resultPerso,scorePerso;
    Algorithm algo;
    private static Context gameContext;
    private HashMap<String,String> hashMapQuestionResponse = new HashMap<String, String>();
    private double nb_questions_asked;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
        Intent intent = getIntent();
        hashMapQuestionResponse=(HashMap<String,String>)intent.getSerializableExtra("responses");
        nb_questions_asked= (double)intent.getIntArrayExtra("nbQuestionsAsked")[0];
        algo=new Algorithm(getApplicationContext());

		//Assignement
		buttonYes = (Button)findViewById(R.id.buttonYes);
		buttonNo = (Button)findViewById(R.id.buttonNo);
        resultPerso = (TextView)findViewById(R.id.textViewResult);
        scorePerso = (TextView)findViewById(R.id.score);
        //On affiche la première proposition
        showNextProposition();

		//Button click
		buttonYes.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, EndGameActivity.class);
                startActivity(intent);
            }
        });

		buttonNo.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {

                algo.deletePersoFromScore((String) resultPerso.getText());
                goOnProposition();
        	}
        });
	}


    /*
    * Continue d'afficher des propositions  pertinentes
    * */
    private void goOnProposition(){

        if(algo.hasMorePersoToPropose()) showNextProposition();

        else { //Il n' y a plus de propositions à faire, on demande à l'utilisateur
            // à quel personnage il pensait
            Intent intent=new Intent(ResultActivity.this,LearnCharacterActivity.class);
            intent.putExtra("responses",this.hashMapQuestionResponse);//on fait passer les réponses à la prochaine activité
            startActivity(intent);
        }
    }

    /*Affichage de la proposition */
    private void showNextProposition(){
        scorePerso.setText("(Je suis sûr à "+algo.getMaxScore(nb_questions_asked)+" %)");
        resultPerso.setText(algo.getPersoByMaxScore());
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}