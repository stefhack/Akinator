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

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;

import Management.Algorithm;
import Management.JsonOdm;
import Management.JsonSingleton;


public class GameActivity extends Activity{
	private static Context gameContext;
	//Declaration
	private Button buttonTest, buttonYes,buttonNo, buttonDontNo, buttonRather, buttonRatherNot;
	private TextView textViewQuestion;
	private String actualQuestion;
	private String actualKey;
	private HashMap<String,String> hashMapQuestionResponse = new HashMap<String, String>();
	private Algorithm algo = new Algorithm(getApplicationContext());
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.gameContext = getApplicationContext();
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
        		fillHashMapQuestionResponse("0");
        		
        		displayQuestion();
        	}
        });
		
		this.buttonYes.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		fillHashMapQuestionResponse("1");
        		displayQuestion();
        	}
        });
		
		this.buttonNo.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		fillHashMapQuestionResponse("2");
        		displayQuestion();
        	}
        });
		
		this.buttonRather.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		fillHashMapQuestionResponse("3");
        		displayQuestion();
        	}
        });
		
		this.buttonRatherNot.setOnClickListener(new OnClickListener() {
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
		this.hashMapQuestionResponse.put(this.actualKey, response);
	}
	
	private void displayQuestion(){
        String requestAlgorithm = "";
        try {
        	requestAlgorithm=algo.getTheMostPertinenteQuestion();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        String[] partsRequestAlgorithm = requestAlgorithm.split(";");
        this.actualKey = partsRequestAlgorithm[0];
        this.actualQuestion = partsRequestAlgorithm[1];
        this.textViewQuestion.setText(actualQuestion);
	}
}
