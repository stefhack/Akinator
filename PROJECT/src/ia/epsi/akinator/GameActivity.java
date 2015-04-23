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
import android.widget.TextView;

import org.json.JSONException;

import java.util.HashMap;

import Management.Algorithm;


public class GameActivity extends Activity{
	static Context gameContext;
	//Declaration
	Button buttonTest, buttonYes,buttonNo, buttonDontNo, buttonRather, buttonRatherNot;
	TextView textViewQuestion;
	String actualQuestion;
	String actualKey;
	HashMap<String,String> hashMapQuestionResponse = new HashMap<String, String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gameContext = getApplicationContext();
		setContentView(R.layout.activity_game);
		
		//Assignement
		buttonTest = (Button)findViewById(R.id.buttonTest);
		buttonYes = (Button)findViewById(R.id.buttonYes);
		buttonNo = (Button)findViewById(R.id.buttonNo);
		buttonDontNo = (Button)findViewById(R.id.buttonDontNo);
		buttonRather = (Button)findViewById(R.id.buttonRather);
		buttonRatherNot = (Button)findViewById(R.id.buttonRatherNot);
		textViewQuestion = (TextView)findViewById(R.id.textViewQuestionRequest);
		
		displayQuestion();
		
		
		//Button click
		buttonTest.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		Intent intent=new Intent(GameActivity.this,ResultActivity.class);
    			startActivity(intent);
        	}
        });
		
		buttonDontNo.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		fillHashMapQuestionResponse("0");
        		displayQuestion();
        	}
        });
		
		buttonYes.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		fillHashMapQuestionResponse("1");
        		displayQuestion();
        	}
        });
		
		buttonNo.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		fillHashMapQuestionResponse("2");
        		displayQuestion();
        	}
        });
		
		buttonRather.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		fillHashMapQuestionResponse("3");
        		displayQuestion();
        	}
        });
		
		buttonRatherNot.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		fillHashMapQuestionResponse("4");
        		displayQuestion();
        	}
        });
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
		hashMapQuestionResponse.put(actualKey, response);
	}
	
	private void displayQuestion(){
        try {
            String requestAlgorithm = Algorithm.getTheMostPertinenteQuestion();
            Log.e("requestAlgorithm++++++++++++++",requestAlgorithm);
            String[] partsRequestAlgorithm = requestAlgorithm.split(";");
    		actualKey = partsRequestAlgorithm[0];
    		actualQuestion = partsRequestAlgorithm[1];
    		textViewQuestion.setText(actualQuestion);
        } catch (JSONException e) {
            e.printStackTrace();
        }
	}
}
