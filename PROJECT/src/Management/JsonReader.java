package Management;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Stef on 27/04/2015.
 */
public class JsonReader {

    private Context context;

    public JsonReader(Context context){
        this.context=context;
    }

    /*
    * Permet de récupérer un JSON sous forme de chaine
    * à partir de storage de l'application
    * @param String filename Le nom du fichier JSON à lire
    * */
    public String readJSONfromInternalStorage(String filename) throws IOException {

        InputStreamReader reader = new InputStreamReader(context.openFileInput(filename));
        BufferedReader buffer = new BufferedReader(reader);
        String line="";
        StringBuilder builder = new StringBuilder();
        while( (line = buffer.readLine())!=null){
            builder.append(line);
        }
        return builder.toString();
    }

    /*
    * Permet de récupérer un JSON sous forme de chaine
    * à partir du dossier ASSETS
    * @param String filename Le nom du fichier JSON à lire
    * */
    public String readJSONfromAssets(String filename){
        String json = null;
		try {

		    InputStream is = this.context.getAssets().open(filename);
		    int size = is.available();
		    byte[] buffer = new byte[size];
		    is.read(buffer);
		    is.close();
		    json = new String(buffer, "ISO-8859-1");
		    return json;

		} catch (IOException ex) {
		    ex.printStackTrace();
		    return null;
		}

    }
}
