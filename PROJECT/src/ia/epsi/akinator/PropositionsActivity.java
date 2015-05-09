package ia.epsi.akinator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

import Management.Algorithm;

/**
 * Created by Stef on 09/05/2015.
 */
public class PropositionsActivity extends Activity {
    private HashMap<String, String> hashMapQuestionResponse = new HashMap<String, String>();
    private Button dontAppearBtn ;
    private Algorithm algo;
    private TextView propositionOne;
    private TextView propositionTwo;
    private TextView propositionThird;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propositions);
        algo=new Algorithm(getApplicationContext());
        propositionOne=(TextView)findViewById(R.id.textProposition1st);
        propositionTwo=(TextView)findViewById(R.id.textProposition2nd);
        propositionThird=(TextView)findViewById(R.id.textProposition3rd);

        Intent intent = getIntent();
        hashMapQuestionResponse = (HashMap<String, String>) intent
                .getSerializableExtra("responses");

        HashMap<String,Double> scoresByPerso =algo.getScoresByPerso();

        Log.i("PROPOSITIONS ACTIVITY SCORES BEFORE SORT ",scoresByPerso.toString());
        algo.sortListScoresDesc();
        Log.i("PROPOSITIONS ACTIVITY SCORES AFETR SORT ",scoresByPerso.toString());

        Object[] keys=scoresByPerso.keySet().toArray();

        propositionOne.setText(keys[0].toString());
        propositionTwo.setText(keys[1].toString());
        propositionThird.setText(keys[2].toString());

        dontAppearBtn = (Button)findViewById(R.id.dontAppearBtn);
        dontAppearBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PropositionsActivity.this,LearnCharacterActivity.class);
                intent.putExtra("responses", PropositionsActivity.this.hashMapQuestionResponse);
                startActivity(intent);
            }


        });


    }

    public void onClickProposition(View view){
        TextView textViewProposition =(TextView)view;
        Log.i("On Click Proposition : ",textViewProposition.getText().toString());
    }
}