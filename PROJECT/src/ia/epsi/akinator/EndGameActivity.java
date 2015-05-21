package ia.epsi.akinator;

import Management.Algorithm;
import Management.JsonSingleton;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class EndGameActivity extends Activity{
	//Declaration
	Button buttonYes, buttonNo, buttonStats;
	TextView textViewPlayAgain;
	private Algorithm algo;
	private JsonSingleton jsonSingleton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_end_game);

		jsonSingleton = JsonSingleton.getInstance(getApplicationContext());
		algo = new Algorithm(getApplicationContext());
		algo.resetAllData();

		// Reset the JSON's with values from start
		jsonSingleton.initializeJSONs();
		
		//Assignement
		buttonYes = (Button)findViewById(R.id.buttonYes);
		buttonNo = (Button)findViewById(R.id.buttonNo);
		buttonStats = (Button)findViewById(R.id.buttonStats);
		textViewPlayAgain = (TextView)findViewById(R.id.textViewPlayAgain);
		
		//mise en place du font
		Typeface typeFace=Typeface.createFromAsset(getAssets(),"brush.ttf");
		this.buttonYes.setTypeface(typeFace);
		this.buttonNo.setTypeface(typeFace);
		this.buttonStats.setTypeface(typeFace);
		this.textViewPlayAgain.setTypeface(typeFace);
		
		//Button click
		buttonYes.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		Intent intent=new Intent(EndGameActivity.this,GameActivity.class);
        		intent.putExtra("ActivityName", "EndGameActivity");
        		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    			startActivity(intent);
        	}
        });
		buttonNo.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		Intent intent=new Intent(EndGameActivity.this,MainActivity.class);
        		//Added
        		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    			startActivity(intent);
        	}
        });
		buttonStats.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(EndGameActivity.this,StatsActivity.class);
                    startActivity(intent);
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