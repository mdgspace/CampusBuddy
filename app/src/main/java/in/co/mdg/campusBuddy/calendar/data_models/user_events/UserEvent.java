package in.co.mdg.campusBuddy.calendar.data_models.user_events;

import in.co.mdg.campusBuddy.calendar.data_models.Time;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Chirag on 04-06-2016.
 */

public class UserEvent extends RealmObject {

    @PrimaryKey
    private long id;

    private long groupId;
    private Time time;
    private String title;
    private String details;
    private String venue;
    private String color;

    public long getId() {return id;}
    public void setId(long id) {this.id = id;}
    public long getGroupId() {return groupId;}
    public void setGroupId(long groupId) {this.groupId = groupId;}
    public Time getTime() {return time;}
    public void setTime(Time time) {this.time = time;}
    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}
    public String getDetails() {return details;}
    public void setDetails(String details) {this.details = details;}
    public String getVenue() {return venue;}
    public void setVenue(String venue) {this.venue = venue;}
    public String getColor() {return color;}
    public void setColor(String color) {this.color = color;}
}
