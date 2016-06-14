
package in.co.mdg.campusBuddy.calendar.data_models.acad;

import in.co.mdg.campusBuddy.calendar.data_models.Time;
import io.realm.RealmObject;

public class Event extends RealmObject {

    private String title;
    private Boolean status;
    private Time time;

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}
    public Boolean getStatus() {return status;}
    public void setStatus(Boolean status) {this.status = status;}
    public Time getTime() {return time;}
    public void setTime(Time time) {this.time = time;}


}
