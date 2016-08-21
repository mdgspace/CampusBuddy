package in.co.mdg.campusBuddy.calendar.data_models.acad;

import java.util.ArrayList;

/**
 * Created by Chirag on 01-06-2016.
 */

public class MainModel {
    private int result;
    private ArrayList<Event> events;

    public int getResult() {return result;}
    public void setResult(int result) {this.result = result;}
    public ArrayList<Event> getEvents() {return events;}
    public void setEvents(ArrayList<Event> events) {this.events = events;}
}
