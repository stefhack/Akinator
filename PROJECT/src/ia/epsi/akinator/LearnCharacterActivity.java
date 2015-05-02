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
import android.widget.RadioButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import Management.Algorithm;
import Management.JsonOdm;
import Management.JsonReader;
import Management.JsonSingleton;
import Management.JsonWriter;

public class LearnCharacterActivity extends Activity {

	// Declaration
	RadioButton buttonYes;
	RadioButton buttonNo;
	Button buttonSave;
	EditText characterName;
	EditText characterQuestion;
	private JsonOdm jsonOdm;
	private JsonWriter jsonWriter;
	private JsonReader jsonReader;
	private JsonSingleton jsonSingleton;
	private HashMap<String, String> hashMapQuestionResponse;
	private Algorithm algorithm = new Algorithm(this.getBaseContext());
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_learn_character);

		// Assignement
		Intent intent = getIntent();
		hashMapQuestionResponse = (HashMap<String, String>) intent.getSerializableExtra("responses");
		buttonSave = (Button) findViewById(R.id.buttonSaveCharacter);
		jsonOdm = new JsonOdm(getApplicationContext());
		jsonWriter = new JsonWriter(getApplicationContext());
		jsonReader = new JsonReader(getApplicationContext());
		jsonSingleton = JsonSingleton.getInstance(getApplicationContext());
		characterName = (EditText) findViewById(R.id.editTextNameCharacter);
		characterQuestion = (EditText) findViewById(R.id.EditTextQuestion);
		
		buttonYes = (RadioButton) findViewById(R.id.radioYes);
		buttonNo = (RadioButton) findViewById(R.id.radioNo);
		// Button click
		buttonSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Get response for the new question
				String responseToNewQuestion = "inconnu";
				if(buttonYes.isChecked()){
					responseToNewQuestion = "oui";
				}else{
					responseToNewQuestion = "non";
				}
				
				// TESTS
				// TODO REPLACE WITH EDIT TEXT VALUES
				// TODO FIND THE KEY FOR NEW QUESTION
				String newQuestionKey = "KEYTEST";
				String newQuestionValue = characterQuestion.getText().toString();

				// Reset the JSON's with values from start
				jsonSingleton.initializeJSONs();

				// Add new character to JSON with his responses
				JSONObject newCharacter = new JSONObject();
				//Fill character with question responded before
				for (Map.Entry<String, String> entry : hashMapQuestionResponse.entrySet()) {
					String questionKey = entry.getKey();
					String response = entry.getValue();
					Log.i("LEARN ACTIVITY KEY QUESTION : ", questionKey);
					Log.i("LEARN ACTIVITY RESPONSE : ", response);
					try {
						// Test here "prob oui" , "prob non"
						newCharacter.put(questionKey, algorithm.getResponseByCode(response));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				//Fille character with other question unresponded
				JSONArray questionsFromMemory = jsonSingleton.getJsonQuestions();
				ArrayList<String> arrayTampon = new ArrayList<String>();
				try {
					JSONObject questions = questionsFromMemory.getJSONObject(0);
					// Log.i("QUESTIONS ", questions.toString());

					Iterator<?> keys = questions.keys();
					boolean isAlreadyInCharacter = false;
					while (keys.hasNext()) {
						if(isAlreadyInCharacter){
							isAlreadyInCharacter = false;
						}
						String questionKey = (String) keys.next();
						Iterator<?> keysOnPerso = newCharacter.keys();
						while(keysOnPerso.hasNext() && !isAlreadyInCharacter){
							String questionKeyPerso = (String) keysOnPerso.next();
							if(questionKey.equals(questionKeyPerso)){
								isAlreadyInCharacter = true;
							}
						}
						//If the question isn't already defined for the new character, add it
						if(!isAlreadyInCharacter){
							arrayTampon.add(questionKey);
						}
					}
					//Add all missing keys for the new personnage
					for(String key:arrayTampon){
						newCharacter.put(key, "inconnu");
					}
					
				} catch (JSONException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				// Put personnage name
				try {
					newCharacter.put("Personnage", characterName.getText().toString());
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
					//Fill new question and its response
					newCharacter.put(newQuestionKey, responseToNewQuestion);
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				// Insert new character
				jsonOdm.insertCharacter(newCharacter);// TODO Check insertion
				Log.i("LEARN ACTIVITY NEW CHARACTER : ",newCharacter.toString());
				try {
					//Write new json personnages
					jsonWriter.writeJsonIntoInternalStorage(jsonOdm.getJsonCharacter().toString(),"personnages.json");
					//Write new json questions
					jsonWriter.writeJsonIntoInternalStorage(jsonOdm.getJsonQuestions().toString(), "questions.json");
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
