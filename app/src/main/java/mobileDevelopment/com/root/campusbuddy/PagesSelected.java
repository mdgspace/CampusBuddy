package mobileDevelopment.com.root.campusbuddy;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by root on 22/7/15.
 */
public class PagesSelected {

    private static String preferences_file = "pages_list";

    public static void writeSelectedPageIds(Context context, ArrayList<String> idList){
        try {
            String idString = "";
            for(int i=0;i<idList.size();i++){
                idString += idList.get(i) + "\n";
            }

            FileOutputStream outputStream = context.openFileOutput(preferences_file, Context.MODE_PRIVATE);
            outputStream.write(idString.getBytes());
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getSelectedPageIds(Context context){
        try {
            FileInputStream inputStream = null;
            ArrayList<String> idList = new ArrayList<String>();
            inputStream = context.openFileInput(preferences_file);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
                String id = line.replace('\n','\0');
                id = id.replace('\r','\0');
                Log.e("ID",id+"");
                idList.add(id);
            }

            return idList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
