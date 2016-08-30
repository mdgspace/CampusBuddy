package in.co.mdg.campusBuddy.calendar;

import in.co.mdg.campusBuddy.calendar.data_models.acad.Event;
import in.co.mdg.campusBuddy.calendar.data_models.acad.EventList;
import in.co.mdg.campusBuddy.calendar.data_models.acad.MainModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Chirag on 27-05-2016.
 */

public interface CalendarApi {
//    @GET("/macros/s/AKfycbwVkKBh5xpoIq-gH5LHQen5-Z5Gfv_sSpo-WqkHy2S02JLhtWY/exec")
//    Call<MainModel> getAcadEvents(@Query("d") String d);

    @GET("/cb/eventsIITR.json")
    Call<EventList> getAcadEvents();
}
