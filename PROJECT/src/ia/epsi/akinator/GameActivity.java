package ia.epsi.akinator;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;


public class GameActivity extends Activity{
	static Context gameContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gameContext = getApplicationContext();
		setContentView(R.layout.activity_game);
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
}
