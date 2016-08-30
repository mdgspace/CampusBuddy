package in.co.mdg.campusBuddy;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.co.mdg.campusBuddy.calendar.data_models.acad.Event;
import in.co.mdg.campusBuddy.calendar.GetEventsFromGCal;
import in.co.mdg.campusBuddy.calendar.data_models.acad.LastUpdated;
import in.co.mdg.campusBuddy.calendar.data_models.user_events.UserEvent;
import io.realm.Realm;
import io.realm.RealmResults;

public class TimetableNavigation extends AppCompatActivity implements MonthLoader.MonthChangeListener,
        WeekView.EventClickListener, WeekView.EventLongPressListener ,DeleteEditChoose.AlertPositiveListener{

    private long longClickedID;

    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int NEW_EVENT_INTENT = 1;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;
    private WeekView mWeekView;
    private ProgressBar calendarLoad;
    private RealmResults<Event> acadEvents;
    private RealmResults<UserEvent> userEvents;
    private boolean calledNetwork = false;
    private Realm realm;
    private SimpleDateFormat sdfDate = new SimpleDateFormat("EEE, dd MMM yyyy",Locale.getDefault());
    private SimpleDateFormat sdfDateString = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm",Locale.getDefault());

    private FloatingActionsMenu fab_menu;


    private FloatingActionButton fab_new_event, fab_go_today, fab_three_day, fab_one_day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        fab_menu = (FloatingActionsMenu) findViewById(R.id.right_labels);

        calendarLoad = (ProgressBar) findViewById(R.id.calendarLoad);
        realm = Realm.getDefaultInstance();

        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) findViewById(R.id.weekView);

        // Show a toast message about the touched event.
        mWeekView.setOnEventClickListener(this);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(this);

        // Set up a date time interpreter to interpret how the date and time will be formatted in
        // the week view. This is optional.
        setupDateTimeInterpreter(false);
        fab_new_event=(FloatingActionButton)findViewById(R.id.fab_new);
        fab_new_event.setTitle("New Event");

        //  fab_new_event.setLabelText("New Event");
        fab_one_day=(FloatingActionButton)findViewById(R.id.fab_one);
        fab_three_day=(FloatingActionButton)findViewById(R.id.fab_three);
        fab_go_today=(FloatingActionButton)findViewById(R.id.fab_today);

        fab_new_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newEventIntent = new Intent(TimetableNavigation.this, NewEvent.class);
                newEventIntent.putExtra("ADD_OR_EDIT",0);
                startActivityForResult(newEventIntent, NEW_EVENT_INTENT);

            }
        });

        fab_one_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWeekViewType != TYPE_DAY_VIEW) {
                    //item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(1);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }

            }
        });

        fab_three_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
                    // item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_THREE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(3);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }

            }
        });

        fab_go_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWeekView.goToToday();

            }
        });

    }

    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        if(!calledNetwork)
        {
            calledNetwork=true;
            String lastUpdatedDate=(realm.where(LastUpdated.class).findFirst() != null)?realm.where(LastUpdated.class).findFirst().getDate():null;
            String todaysDate = sdfDateString.format(new Date());
            if(lastUpdatedDate==null) {
                calendarLoad.setVisibility(View.VISIBLE);
                GetEventsFromGCal g = GetEventsFromGCal.getInstance();
                g.getAcadEvents(mWeekView,calendarLoad);
            }
        }

        Calendar startCal, endCal;
        WeekViewEvent event;
        ArrayList<WeekViewEvent> events = new ArrayList<>();

        int d;
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy",Locale.getDefault());
        switch(newMonth)
        {
            case 1:case 3:case 5:case 7:case 8:case 10:case 12:
                d=31;break;
            case 2:
                d=28;break;
            default:
                d=30;
        }

        try {
            Date startDate,endDate;
            startDate = sdf.parse("01"+String.format(Locale.US,"%02d",newMonth)+newYear);
            endDate = sdf.parse((d+1)+String.format(Locale.US,"%02d",newMonth)+newYear);
            acadEvents = realm.where(Event.class).greaterThan("time.start",startDate).lessThan("time.start",endDate).findAll();
            for(Event acadEvent:acadEvents)
            {
                try {
                    startCal = Calendar.getInstance();
                    endCal = Calendar.getInstance();
                    startCal.setTime(acadEvent.getTime().getStart());
                    endCal.setTime(acadEvent.getTime().getEnd());
                    event = new WeekViewEvent(-1, acadEvent.getTitle(), startCal, endCal);
                    //Log.v("AcadEventsADD","StartDate:"+event.getStartTime().get(Calendar.DATE)+"/"+event.getStartTime().get(Calendar.MONTH)+"/"+event.getStartTime().get(Calendar.YEAR)+" | Name:"+event.getName());
                    event.setColor(ContextCompat.getColor(this,R.color.primary));
                    events.add(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            userEvents = realm.where(UserEvent.class).greaterThan("time.start",startDate).lessThan("time.start",endDate).findAll();
            for(UserEvent userEvent:userEvents)
            {
                try {
                    startCal = Calendar.getInstance();
                    endCal = Calendar.getInstance();
                    startCal.setTime(userEvent.getTime().getStart());
                    endCal.setTime(userEvent.getTime().getEnd());
                    event = new WeekViewEvent(userEvent.getId(), userEvent.getTitle(), startCal, endCal);
                    //Log.v("AcadEventsADD","StartDate:"+event.getStartTime().get(Calendar.DATE)+"/"+event.getStartTime().get(Calendar.MONTH)+"/"+event.getStartTime().get(Calendar.YEAR)+" | Name:"+event.getName());
                    event.setColor(Color.parseColor(userEvent.getColor()));
                    events.add(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return events;
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {

        long ID = event.getId();
        if(ID!=-1)
        {
            UserEvent userEvent = realm.where(UserEvent.class).equalTo("id",ID).findFirst();
            String detail, venue;
            detail = userEvent.getDetails().trim();
            venue = userEvent.getVenue().trim();

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.activity_details, null);
            dialogBuilder.setView(dialogView);
            dialogBuilder.setTitle("Event Details");
            dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            TextView tv_venue = (TextView) dialogView.findViewById(R.id.details_Venue);
            if(venue.isEmpty() || venue.length() == 0 || venue.equals("") ){
                tv_venue.setVisibility(View.GONE);
            }
            else {
                tv_venue.setVisibility(View.VISIBLE);
                tv_venue.setText("Venue: " + venue);
            }

            TextView tv_details = (TextView) dialogView.findViewById(R.id.details_details);
            if(detail.isEmpty() || detail.length() == 0 || detail.equals("")){
                tv_details.setVisibility(View.GONE);
            }
            else {
                tv_details.setVisibility(View.VISIBLE);
                tv_details.setText("Details: " + detail);
            }
            TextView tv_title = (TextView) dialogView.findViewById(R.id.details_title);
            tv_title.setText("Title: "+ userEvent.getTitle());

            TextView tv_date = (TextView) dialogView.findViewById(R.id.details_date);
            tv_date.setText("Date: " + sdfDate.format(userEvent.getTime().getStart()));

            TextView tv_time = (TextView) dialogView.findViewById(R.id.details_time);
            tv_time.setText("Time: " + sdfTime.format(userEvent.getTime().getStart())+ " - " + sdfTime.format(userEvent.getTime().getEnd()));

            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
    }}


    private void setupDateTimeInterpreter(final boolean shortDate) {
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat(" M/d", Locale.getDefault());

                // All android api level do not have a standard way of getting the first letter of
                // the week day name. Hence we get the first char programmatically.
                // Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657
                if (shortDate)
                    weekday = String.valueOf(weekday.charAt(0));
                return weekday.toUpperCase() + format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour) {
                return hour > 11 ? (hour - 12) + " PM" : (hour == 0 ? "12 AM" : hour + " AM");
            }
        });
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {

        longClickedID = event.getId();
        if(longClickedID!=-1) {
            long groupID = realm.where(UserEvent.class).equalTo("id",longClickedID).findFirst().getGroupId();
            boolean isMultiEnabled = (groupID != -1);
            DeleteEditChoose dialog = new DeleteEditChoose();
            Bundle b = new Bundle();
            b.putBoolean("isMultiEnabled", isMultiEnabled);
            dialog.setArguments(b);
            dialog.show(getFragmentManager(), "delete_and_choose");
        }
    }

    @Override
    public void onPositiveClick(int choice,boolean isMultiChecked)
    {
        if(choice == 1)
        {
            Intent in = new Intent(TimetableNavigation.this,NewEvent.class);
            in.putExtra("ADD_OR_EDIT",1);
            in.putExtra("multi_edit", isMultiChecked);
            in.putExtra("value for editing",longClickedID);

            startActivityForResult(in, NEW_EVENT_INTENT);

        }
        else if(choice == 2)
        {

            if(isMultiChecked) {
                //code for deleting all repetitive events
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        long groupId = realm.where(UserEvent.class).equalTo("id",longClickedID).findFirst().getGroupId();
                        RealmResults<UserEvent> userEvents = realm.where(UserEvent.class).equalTo("groupId",groupId).findAll();
                        userEvents.deleteAllFromRealm();
                    }
                });
            }
            else
            {
                //Code for deleting single event
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        UserEvent userEvent = realm.where(UserEvent.class).equalTo("id",longClickedID).findFirst();
                        userEvent.deleteFromRealm();
                    }
                });
            }
            mWeekView.notifyDatasetChanged();
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == NEW_EVENT_INTENT) {

            if(resultCode == RESULT_OK){
                mWeekView.notifyDatasetChanged();
                try {
                    Calendar eventDate = Calendar.getInstance();
                    eventDate.setTime(sdfDateString.parse(data.getStringExtra("Date")));
                    mWeekView.goToDate(eventDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(fab_menu.isExpanded())
                    fab_menu.collapse();
            }
            if (resultCode == RESULT_CANCELED) {
                //Do nothing
            }

        }
    }
    @Override
    public void onBackPressed(){

        if(fab_menu.isExpanded())
            fab_menu.collapse();
        else
        {
            realm.close();
            super.onBackPressed();
        }


    }
}