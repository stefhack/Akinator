package ia.epsi.akinator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class EndGameActivity extends Activity{
	//Declaration
	Button ButtonYes, ButtonNo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_end_game);
		
		//Assignement
		ButtonYes = (Button)findViewById(R.id.buttonYes);
		ButtonNo = (Button)findViewById(R.id.buttonNo);
		
		//Button click
		ButtonYes.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		Intent intent=new Intent(EndGameActivity.this,GameActivity.class);
    			startActivity(intent);
        	}
        });
		ButtonNo.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		Intent intent=new Intent(EndGameActivity.this,MainActivity.class);
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