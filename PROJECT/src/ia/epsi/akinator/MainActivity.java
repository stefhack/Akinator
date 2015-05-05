package ia.epsi.akinator;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

import Management.JsonReader;
import Management.JsonWriter;

public class MainActivity extends Activity {
	//Declaration
	ImageView buttonPlay;

    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private final String JSON_CHARACTERS_FILE = "personnages.json";
    private final String JSON_QUESTIONS_FILE = "questions.json";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

        jsonReader=new JsonReader(getApplicationContext());
        jsonWriter=new JsonWriter(getApplicationContext());

        /*
        * On copie les JSON du dossier ASSETS vers le Storage interne du contexte de l'application
        * Pour l'instant pas de vérification si le fichier existe déjà (à laisser pour les tests)
        * */

        try {
            InputStream is = getApplicationContext().openFileInput("questions.json");
             Log.i("MAIN aCTIVITY","JSON ALREADY IN STORAGE");

        } catch (IOException e) {
            try {
                Log.i("MAIN ACTIVITY","WRITING JSON IN STORAGE");
                //Write files into internal storage only for the 1st time
                jsonWriter.writeJsonIntoInternalStorage(jsonReader.readJSONfromAssets(JSON_QUESTIONS_FILE),JSON_QUESTIONS_FILE);
                jsonWriter.writeJsonIntoInternalStorage(jsonReader.readJSONfromAssets(JSON_CHARACTERS_FILE),JSON_CHARACTERS_FILE);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }

        //LOGS
        try {
            Log.i("MAIN ACTIVITY READ JSON FROM INTERNAL STORAGE :", jsonReader.readJSONfromInternalStorage("personnages.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }

		//Assignment 
		buttonPlay = (ImageView)findViewById(R.id.buttonPlay);

		//Button click
		buttonPlay.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		Intent intent=new Intent(MainActivity.this,GameActivity.class);
        		intent.putExtra("ActivityName", "MainActivity");
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
