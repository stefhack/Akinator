package Management;

import android.content.Context;

import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by Stef on 27/04/2015.
 */
public class JsonWriter {

    private Context context;

    public JsonWriter(Context context){
        this.context=context;
    }

    /*
    * Save a file from a JSON
     * @param JSONArray a JSON you wish to write into a file
     *
     * TODO check if file is already written (multiple launch of application)
     * TODO check if file is really persistent
    * */
    public void writeJsonIntoInternalStorage(String jsonToWrite,String filename) throws IOException {

        OutputStreamWriter streamWriter = new OutputStreamWriter(context.openFileOutput(filename,Context.MODE_PRIVATE));
        streamWriter.write(jsonToWrite);
        streamWriter.close();
    }


 }
