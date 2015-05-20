package ia.epsi.akinator;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Management.Algorithm;
import Management.JsonOdm;
import Management.JsonWriter;

/**
 * Created by Stef on 09/05/2015.
 */
public class PropositionsActivity extends Activity {
    private HashMap<String, String> hashMapQuestionResponse = new HashMap<String, String>();
    private Button buttonDontAppear ;
    private Algorithm algo;
    private TextView textViewTitle,textViewProposition1st,textViewProposition2nd,textViewProposition3rd;
    private ImageView imageViewProposition1st,imageViewProposition2nd,imageViewProposition3rd;
	private JsonOdm jsonOdm;
	private JsonWriter jsonWriter;
	private Algorithm algorithm = new Algorithm(this.getBaseContext());
	private String namePerso;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_propositions);
        algo=new Algorithm(getApplicationContext());
        this.textViewTitle=(TextView)findViewById(R.id.textViewTitle);
        this.textViewProposition1st=(TextView)findViewById(R.id.textViewProposition1st);
        this.textViewProposition2nd=(TextView)findViewById(R.id.textViewProposition2nd);
        this.textViewProposition3rd=(TextView)findViewById(R.id.textViewProposition3rd);

        this.imageViewProposition1st=(ImageView)findViewById(R.id.imageViewProposition1st);
        this.imageViewProposition2nd=(ImageView)findViewById(R.id.imageViewProposition2nd);
        this.imageViewProposition3rd=(ImageView)findViewById(R.id.imageViewProposition3rd);
        
        
        textViewProposition1st.setText(algo.getPersoByMaxScore());
        imageViewProposition1st.setTag(algo.getPersoByMaxScore());
		showImage(imageViewProposition1st);
        algo.deletePersoFromScore(algo.getPersoByMaxScore());
        
        textViewProposition2nd.setText(algo.getPersoByMaxScore());
        imageViewProposition2nd.setTag(algo.getPersoByMaxScore());
        showImage(imageViewProposition2nd);
        algo.deletePersoFromScore(algo.getPersoByMaxScore());
        
        textViewProposition3rd.setText(algo.getPersoByMaxScore());
        imageViewProposition3rd.setTag(algo.getPersoByMaxScore());
        showImage(imageViewProposition3rd);
        algo.deletePersoFromScore(algo.getPersoByMaxScore());
        
		jsonOdm = new JsonOdm(getApplicationContext());
		jsonWriter = new JsonWriter(getApplicationContext());
        
		Intent intent = getIntent();
		hashMapQuestionResponse = (HashMap<String, String>) intent.getSerializableExtra("responses");

        buttonDontAppear = (Button)findViewById(R.id.buttonDontAppear);
      //mise en place du font
  		Typeface typeFace=Typeface.createFromAsset(getAssets(),"brush.ttf");
  		this.textViewTitle.setTypeface(typeFace);
  		this.textViewProposition1st.setTypeface(typeFace);
  		this.textViewProposition2nd.setTypeface(typeFace);
  		this.textViewProposition3rd.setTypeface(typeFace);
        this.buttonDontAppear.setTypeface(typeFace);
        
        
        buttonDontAppear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PropositionsActivity.this,LearnCharacterActivity.class);
                intent.putExtra("responses", PropositionsActivity.this.hashMapQuestionResponse);
                startActivity(intent);
            }


        });


    }
    

	private void showImage(ImageView imageView){
        namePerso = algo.getPersoByMaxScore().replaceAll("\\s+","").replaceAll("'", "").replace("-", "");
		int imageResource = getResources().getIdentifier(namePerso, "drawable", getPackageName());
		if(imageResource != 0){
			Drawable res = getResources().getDrawable(imageResource);
			imageView.setImageDrawable(res);
		}
	}

    public void onClickProposition(View view) throws JSONException, IOException{
    	
    	String namePersoFromActivity;
    	if((view.equals(imageViewProposition1st))||(view.equals(imageViewProposition2nd))||(view.equals(imageViewProposition3rd))){
    		ImageView imageViewProposition =(ImageView)view;
    		namePersoFromActivity = imageViewProposition.getTag().toString();
    	}
    	else{
    		TextView textViewProposition =(TextView)view;
        	namePersoFromActivity = textViewProposition.getText().toString().toLowerCase();
    	}
    	
		JSONObject personage = null;
		String persoName;
		personage = jsonOdm.getCharacterByName(namePersoFromActivity);
		if(personage == null){
			personage = jsonOdm.getCharacterByName(namePersoFromActivity.toUpperCase());
			persoName = namePersoFromActivity.toUpperCase();
		}else{
			persoName = namePersoFromActivity;
		}
		if(personage != null){
			//Fill it new properties if needed (different or named "inconnu")
			for (Map.Entry<String, String> entry : hashMapQuestionResponse.entrySet()) {
				String questionKey = entry.getKey();
				String response = entry.getValue();

				Log.i("questionKey response",questionKey.toString()+"/////"+response.toString());
				
				//Response comparison
				String responsePerso = personage.getString(questionKey);
				if(!responsePerso.equals(algorithm.getResponseByCode(response))){
					personage.put(questionKey,algorithm.getResponseByCode(response));
				}
			}
			Log.i("THE FUCKING PERSONNAGE WITH NEW VALUES",personage.toString());
			//json Character with this perso
			//First delete perso to refill it
			jsonOdm.deleteCharacterByName(persoName);
			jsonOdm.insertCharacter(personage);
			JSONArray jsonPersonnageWithNewCharacterFilled = jsonOdm.getJsonCharacter();
			//Write personnages
			jsonWriter.writeJsonIntoInternalStorage(jsonPersonnageWithNewCharacterFilled.toString(), "personnages.json");
			//Go on
		}
    			
    			
        Intent intent = new Intent(PropositionsActivity.this,EndGameActivity.class);
        startActivity(intent);
    }
}