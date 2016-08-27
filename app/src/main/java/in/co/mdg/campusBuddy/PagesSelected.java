package in.co.mdg.campusBuddy;

import android.content.Context;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by root on 22/7/15.
 */
class PagesSelected {

    private static String preferences_file = "pages_list";

    static void writeSelectedPageIds(Context context, ArrayList<String> idList){
        try {
            String idString = "";
            FirebaseMessaging.getInstance().subscribeToTopic("1558962134412332"); //test page subscription
            for(int i=0;i<idList.size();i++){
                idString += idList.get(i) + "\n";
                FirebaseMessaging.getInstance().subscribeToTopic(idList.get(i));
            }
            FileOutputStream outputStream = context.openFileOutput(preferences_file, Context.MODE_PRIVATE);
            outputStream.write(idString.getBytes());
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static ArrayList<String> getSelectedPageIds(Context context){
        try {
            FileInputStream inputStream;
            ArrayList<String> idList = new ArrayList<String>();
            inputStream = context.openFileInput(preferences_file);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String id = line.replace('\n','\0');
                id = id.replace('\r','\0');
                idList.add(id);
            }
            return idList;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
