package ia.epsi.akinator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class GameActivity extends Activity{
	//Declaration
	Button ButtonTest, ButtonYes,ButtonNo, ButtonDontNo, ButtonRather, ButtonRatherNot;
	TextView TextViewQuestion;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		
		//Assignement
		ButtonTest = (Button)findViewById(R.id.buttonTest);
		ButtonYes = (Button)findViewById(R.id.buttonYes);
		ButtonNo = (Button)findViewById(R.id.buttonNo);
		ButtonDontNo = (Button)findViewById(R.id.buttonDontNo);
		ButtonRather = (Button)findViewById(R.id.buttonRather);
		ButtonRatherNot = (Button)findViewById(R.id.buttonRatherNot);
		TextViewQuestion = (TextView)findViewById(R.id.textViewQuestionRequest);
		
		
		//Call first question
		
		TextViewQuestion.setText("Coucou chez moi");
		
        
		
		//Button click
		ButtonTest.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		Intent intent=new Intent(GameActivity.this,ResultActivity.class);
    			startActivity(intent);
        	}
        });
		
		ButtonYes.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		
        	}
        });
		
		ButtonNo.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		
        	}
        });
		
		ButtonDontNo.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		
        	}
        });
		
		ButtonRather.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		
        	}
        });
		
		ButtonRatherNot.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		
        	}
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
