package ia.epsi.akinator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import Management.JsonOdm;
import Management.JsonReader;
import Management.JsonSingleton;
import Management.JsonWriter;

public class LearnCharacterActivity extends Activity {

	// Declaration
	Button buttonSave;
	EditText characterName;
	EditText characterQuestion;
	private JsonOdm jsonOdm;
	private JsonWriter jsonWriter;
	private JsonReader jsonReader;
	private JsonSingleton jsonSingleton;
	private HashMap<String, String> hashMapQuestionResponse;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_learn_character);

		// Assignement
		Intent intent = getIntent();
		hashMapQuestionResponse = (HashMap<String, String>) intent
				.getSerializableExtra("responses");
		buttonSave = (Button) findViewById(R.id.buttonSaveCharacter);
		jsonOdm = new JsonOdm(getApplicationContext());
		jsonWriter = new JsonWriter(getApplicationContext());
		jsonReader = new JsonReader(getApplicationContext());
		jsonSingleton = JsonSingleton.getInstance(getApplicationContext());
		characterName = (EditText) findViewById(R.id.editTextNameCharacter);
		characterQuestion = (EditText) findViewById(R.id.EditTextQuestion);

		// Button click
		buttonSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// TESTS
				// TODO REPLACE WITH EDIT TEXT VALUES
				// TODO FIND THE KEY FOR NEW QUESTION
				String newQuestionKey = "KEYTEST";
				String newQuestionValue = characterQuestion.getText()
						.toString();

				// Reset the JSON's with values from start
				jsonSingleton.initializeJSONs();

				// Add new character to JSON with his responses
				JSONObject newCharacter = new JSONObject();

				for (Map.Entry<String, String> entry : hashMapQuestionResponse
						.entrySet()) {
					String questionKey = entry.getKey();
					String response = entry.getValue();
					Log.i("LEARN ACTIVITY KEY QUESTION : ", questionKey);
					Log.i("LEARN ACTIVITY RESPONSE : ", response);
					try {
						// Test here "prob oui" , "prob non"
						newCharacter.put(questionKey, response);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				JSONArray questionsFromMemory = jsonSingleton
						.getJsonQuestions();

				JSONObject questions;
				try {
					questions = questionsFromMemory.getJSONObject(0);

					// Log.i("QUESTIONS ", questions.toString());

					Iterator keys = questions.keys();
					Iterator keysOnPerso = newCharacter.keys();
					while (keys.hasNext()) {
						boolean isAlreadyInCharacter = false;
						String questionKey = (String) keys.next();
						while(keysOnPerso.hasNext() && !isAlreadyInCharacter){
							String questionKeyPerso = (String) keysOnPerso.next();
							
							if(!questionKeyPerso.equals(questionKey))
							{
								break;
							}
							else{
								isAlreadyInCharacter = true;
								break;
							}
						}
						//If the question isn't already defined for the new character, add it
						if(!isAlreadyInCharacter){
							newCharacter.put(questionKey,"inconnu");
						}
					}
					
				} catch (JSONException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				// Put personnage name
				try {
					newCharacter.put("Personnage", characterName.getText()
							.toString());
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				// Insert question
				try {
					jsonOdm.insertQuestion(newQuestionKey, newQuestionValue);// Insertion
																				// OK
																				// in
																				// internal
																				// storage

				} catch (JSONException e) {
					e.printStackTrace();
				}
				// Insert new character
				jsonOdm.insertCharacter(newCharacter);// TODO Check insertion
				Log.i("LEARN ACTIVITY NEW CHARACTER : ",
						newCharacter.toString());
				// Log.i("LEARN ACTIVITY PERSOS FROM ODM",
				// jsonOdm.getJsonCharacter().toString());

				try {
					// jsonWriter.writeJsonIntoInternalStorage(jsonOdm.getJsonCharacter().toString(),"personnages.json");
					jsonWriter.writeJsonIntoInternalStorage(jsonOdm
							.getJsonQuestions().toString(), "questions.json");
				} catch (IOException e) {
					e.printStackTrace();
				}

				try {
					Log.i("LEARN ACTIVITY PERSONNAGES FROM INTERNAL STORAGE",
							jsonReader
									.readJSONfromInternalStorage("personnages.json"));
					Log.i("LEARN ACTIVITY QUESTIONS FROM INTERNAL STORAGE",
							jsonReader
									.readJSONfromInternalStorage("questions.json"));
				} catch (IOException e) {
					e.printStackTrace();
				}

				Intent intent = new Intent(LearnCharacterActivity.this,
						EndGameActivity.class);
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
