package ia.epsi.akinator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LearnCharacterActivity extends Activity{
	//Declaration
	Button ButtonSave, ButtonAddQuestion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_learn_character);
		
		//Assignement
		ButtonSave = (Button)findViewById(R.id.buttonSaveCharacter);
		ButtonAddQuestion = (Button)findViewById(R.id.buttonAddQuestion);
		
		
		//Button click
		ButtonSave.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		Intent intent=new Intent(LearnCharacterActivity.this,EndGameActivity.class);
    			startActivity(intent);
        	}
        });
		ButtonAddQuestion.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		addNewQuestion();
        	}
        });
	}
	public void addNewQuestion(){
		EditText editTextNewQuestion = new EditText(this);
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
		LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayoutQuestionScroll);
        layout.addView(editTextNewQuestion, p);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
