package in.co.mdg.campusBuddy.calendar;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.alamkanak.weekview.WeekView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import in.co.mdg.campusBuddy.calendar.data_models.Time;
import in.co.mdg.campusBuddy.calendar.data_models.acad.Event;
import in.co.mdg.campusBuddy.calendar.data_models.acad.EventList;
import in.co.mdg.campusBuddy.calendar.data_models.acad.LastUpdated;
import in.co.mdg.campusBuddy.calendar.data_models.acad.MainModel;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Chirag on 27-05-2016.
 */

public class GetEventsFromGCal {


//    private final static String API = "https://script.google.com";
    private static GetEventsFromGCal instance;
    private CalendarApi service;
    private Realm realm;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    private GetEventsFromGCal() {
        Gson gson = new GsonBuilder()
                    .setDateFormat("dd.MM.yyyy").create();
//                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
        String API_2 = "https://www.sdsmdg.ml";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_2)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        service = retrofit.create(CalendarApi.class);
        realm = Realm.getDefaultInstance();

    }

    public static synchronized GetEventsFromGCal getInstance() {
        if (instance == null) {
            instance = new GetEventsFromGCal();
        }
        return instance;
    }

    public void getAcadEvents(final WeekView mWeekView, final ProgressBar calendarLoad) {
        if(realm.isClosed())
            realm = Realm.getDefaultInstance();
        Call<EventList> call = service.getAcadEvents();
        call.enqueue(new Callback<EventList>() {
            @Override
            public void onResponse(Call<EventList> call, final Response<EventList> response) {

                //If result is successful, then process the output
//                    final ArrayList<Event> result = new ArrayList<>();
//                    result.addAll(response.body().getEvents());
                if(response.isSuccessful() && response.body() != null) {
                    for(int i=0;i<response.body().getEvents().size();i++) {
                        final int finalI = i;
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.copyToRealm(response.body().getEvents().get(finalI));
                            }
                        });
                    }

                    setLastUpdatedDate(sdf.format(new Date()));
                    calendarLoad.setVisibility(View.GONE);
                    mWeekView.notifyDatasetChanged();
                } else {
                    Log.d("FailureGetEvents", "response is null");
                }

            }

            @Override
            public void onFailure(Call<EventList> call, Throwable t) {
                calendarLoad.setVisibility(View.GONE);
                Log.d("FailureGetEvents", t.getMessage());
                t.printStackTrace();
            }
        });
    }



//    public void getEvents(final WeekView mWeekView, final ProgressBar calendarLoad) {
//        if(realm.isClosed())
//        {
//            realm = Realm.getDefaultInstance();
//        }
//        //first get the last updated date from realm database
//        LastUpdated lastUpdated = realm.where(LastUpdated.class).findFirst();
//        String date=(lastUpdated != null)?lastUpdated.getDate():"0000000000";
//        //here 0000000000 is used for getting the feed for the first time
//
//        Call<MainModel> call = service.getAcadEvents(date);
//        call.enqueue(new Callback<MainModel>()
//        {
//            @Override
//            public void onResponse(Call<MainModel> call, Response<MainModel> response) {
//                if(response.body().getResult() == 1)
//                {
//                    //If result is successful, then process the output and add/remove the events from database
//                    ArrayList<Event> result = new ArrayList<>();
//                    result.addAll(response.body().getEvents());
//                    for(int i=0;i<result.size();i++)
//                    {
//                        final Event event = result.get(i);
//                        if(event.getStatus())
//                        {
//                            //to add new event to database if status of the event is true
//                            realm.executeTransaction(new Realm.Transaction() {
//                                @Override
//                                public void execute(Realm realm) {
//                                    realm.copyToRealm(event);
//                                }
//                            });
//                        }
//                        else
//                        {
//                            //to remove an event from database if status is false
//                            realm.executeTransaction(new Realm.Transaction() {
//                                @Override
//                                public void execute(Realm realm) {
//                                    Event event1 = realm.where(Event.class).equalTo("title",event.getTitle()).findFirst();
//                                    event1.deleteFromRealm();
//                                }
//                            });
//                        }
//
//
//                    }
//
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//                    setLastUpdatedDate(sdf.format(new Date()));
//                    calendarLoad.setVisibility(View.GONE);
//                    mWeekView.notifyDatasetChanged();
//                }
//                else
//                {
//                    //If there is error in result then remove the database and start from scratch.
//                    //This may be because the lastUpdatedDate is older then 1 month. Google Calendar API does not give results prior 1 month.
//                    clearEventsDatabase();
//                    setLastUpdatedDate("0000000000");
//                    getEvents(mWeekView, calendarLoad);
//
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<MainModel> call, Throwable t) {
//                calendarLoad.setVisibility(View.GONE);
//                Log.d("FailureGetEvents", t.getMessage());
//            }
//
//        });
//    }
//
    private void clearEventsDatabase()
    {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Event> acadEvents = realm.where(Event.class).findAll();
                acadEvents.deleteAllFromRealm();
            }
        });
    }

    private void setLastUpdatedDate(final String date)
    {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LastUpdated lastUpdated = realm.where(LastUpdated.class).findFirst();
                if(lastUpdated != null)
                {
                    lastUpdated.setDate(date);
                }
                else
                {
                    lastUpdated = realm.createObject(LastUpdated.class);
                    lastUpdated.setDate(date);
                }
            }
        });
    }
}