package ia.epsi.akinator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import Management.JsonOdm;
import Management.JsonReader;
import Management.JsonWriter;

public class LearnCharacterActivity extends Activity{

	//Declaration
	Button buttonSave;
    private JsonOdm jsonOdm;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_learn_character);
		
		//Assignement
		buttonSave = (Button)findViewById(R.id.buttonSaveCharacter);
		jsonOdm = new JsonOdm(getApplicationContext());
        jsonWriter = new JsonWriter(getApplicationContext());
        jsonReader=new JsonReader(getApplicationContext());

		//Button click
		buttonSave.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {

                String newQuestionKey = "KEYTEST";
                String newQuestionValue = "VALUETEST";

                JSONObject newCharacter = new JSONObject();

                try {
                    jsonOdm.insertQuestion(newQuestionKey,newQuestionValue);
                    newCharacter.put("test","test");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                jsonOdm.insertCharacter(newCharacter);
        		Intent intent=new Intent(LearnCharacterActivity.this,EndGameActivity.class);
    			startActivity(intent);

                Log.i("QUESTIONS FROM ODM", jsonOdm.getJsonQuestions().toString());
               // Log.i("PERSOS FROM ODM", jsonOdm.getJsonCharacter().toString());
                try {
                    //jsonWriter.writeJsonIntoInternalStorage(jsonOdm.getJsonCharacter().toString(),"personnages.json");
                    jsonWriter.writeJsonIntoInternalStorage(jsonOdm.getJsonQuestions().toString(),"questions.json");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {

                    Log.i("QUESTIONS FROM INTERNAL STORAGE", jsonReader.readJSONfromInternalStorage("questions.json"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

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
