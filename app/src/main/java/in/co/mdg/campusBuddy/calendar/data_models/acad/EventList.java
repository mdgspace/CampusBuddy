package in.co.mdg.campusBuddy.calendar.data_models.acad;

import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Chirag on 29-08-2016.
 */

public class EventList {
    private RealmList<Event> events;

    public RealmList<Event> getEvents() { return events; }
    public void setEvents(RealmList<Event> events) { this.events = events; }
}
