package in.co.mdg.campusBuddy.calendar.data_models.acad;

import io.realm.RealmObject;

/**
 * Created by Chirag on 01-06-2016.
 */

public class LastUpdated extends RealmObject{
    private String date;

    public String getDate() {return date;}
    public void setDate(String date) {this.date = date;}
}
