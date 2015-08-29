package mobileDevelopment.com.root.campusbuddy;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class timetable_navigation2 extends ActionBarActivity  implements WeekView.MonthChangeListener,
        WeekView.EventClickListener, WeekView.EventLongPressListener ,Dialog_for_more.AlertPositiveListenerforedit,delete_edit_choose.AlertPositiveListener, Dialog_for_more_delete.AlertPositiveListenerfordelete{

    SQLiteDatabase db;
    Cursor cr, cursor;

    long longClickedID;

    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;
    private WeekView mWeekView;
    SharedPreferences pref;
    int position=0;
    boolean  ismultiedit = false;
    int size,day[],month[],year[],starth[],startmin[],endh[],endmin[];
    String[] title,venue,details;

    public static Activity fa;
    FloatingActionsMenu fab_menu;

    String[] eventList = {
            CalendarDB.CalendarEntry.COLUMN_NAME_ID,
            CalendarDB.CalendarEntry.COLUMN_NAME_TITLE,
            CalendarDB.CalendarEntry.COLUMN_NAME_DAY,
            CalendarDB.CalendarEntry.COLUMN_NAME_MONTH,
            CalendarDB.CalendarEntry.COLUMN_NAME_YEAR,
            CalendarDB.CalendarEntry.COLUMN_NAME_STARTHOUR,
            CalendarDB.CalendarEntry.COLUMN_NAME_STARTMIN,
            CalendarDB.CalendarEntry.COLUMN_NAME_ENDHOUR,
            CalendarDB.CalendarEntry.COLUMN_NAME_ENDMIN,
            CalendarDB.CalendarEntry.COLUMN_NAME_DETAIL,
            CalendarDB.CalendarEntry.COLUMN_NAME_VENUE,

    };

    public FloatingActionButton fab_new_event, fab_go_today, fab_three_day, fab_one_day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        fab_menu = (FloatingActionsMenu) findViewById(R.id.right_labels);

        fa = this;

        pref = this.getSharedPreferences(
                "com.example.appss", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("DELETE_OR_EDIT", 0);
        editor.commit();


        db = CalendarDBHelper.getInstance(getApplicationContext()).getReadableDatabase();


        try {
            cr = db.query(CalendarDB.CalendarEntry.TABLE_NAME, eventList, null, null, null, null, null);
        } catch (Exception err) {
            err.printStackTrace();
        }

        cr.moveToFirst();

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


        try{
            DatabaseHelperforAC dbhac=new DatabaseHelperforAC(this);
            dbhac.createDataBase();

            if (dbhac.open())
            {
                List<AcadEvents> events=dbhac.getEvents("Acadevents");
                size=DatabaseHelperforAC.i;
                title=new String[size];
                details=new String[size];
                venue=new String[size];
                day=new int[size];
                month=new int[size];
                year=new int[size];
                starth=new int[size];
                startmin=new int[size];
                endh=new int[size];
                endmin=new int[size];

                int i=0;
                for(AcadEvents acadevents:events)
                {
                    day[i]=acadevents.day;
                    month[i]=acadevents.month;
                    year[i]=acadevents.year;
                    starth[i]=acadevents.starth;
                    startmin[i]=acadevents.startmin;
                    endh[i]=acadevents.endh;
                    endmin[i]=acadevents.endmin;
                    title[i]=acadevents.title;
                    venue[i]=acadevents.venue;
                    details[i]=acadevents.details;
                    i++;
                }
            }
        }
        catch (Exception e)
        {
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();

        }

        fab_new_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newEventIntent = new Intent(timetable_navigation2.this, NewEvent.class);
                startActivity(newEventIntent);

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
        Calendar startTime, endTime;
        WeekViewEvent event;
        List<WeekViewEvent> events = new ArrayList<>();

        for(int i=0;i<size;i++)
        {
            startTime = Calendar.getInstance();
            startTime.set(Calendar.DATE,day[i]);
            startTime.set(Calendar.MONTH, month[i]-1);
            startTime.set(Calendar.YEAR, year[i]);
            startTime.set(Calendar.HOUR_OF_DAY,starth[i]);
            startTime.set(Calendar.MINUTE,startmin[i]);
            endTime = (Calendar) startTime.clone();
            endTime.set(Calendar.HOUR_OF_DAY,endh[i]);
            endTime.set(Calendar.MINUTE, endmin[i]);


            event = new WeekViewEvent(-1,
                    title[i], startTime, endTime);
            Log.v("Id", ""+event.getId());
            event.setColor(getResources().getColor(R.color.com_facebook_blue));
            events.add(event);
        }


        try {
            cursor = db.query(CalendarDB.CalendarEntry.TABLE_NAME, eventList,
                    CalendarDB.CalendarEntry.COLUMN_NAME_MONTH + " = '" +
                            String.valueOf(newMonth-1) + "'", null, null, null, null);
        } catch (Exception err) {
            err.printStackTrace();
        }


        cursor.moveToFirst();

        if(cursor.getCount() >0 ){
            startTime = Calendar.getInstance();

            startTime.set(Calendar.DATE, cursor.getInt(cursor.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_DAY)));
            startTime.set(Calendar.MONTH, cursor.getInt(cursor.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_MONTH)));
            startTime.set(Calendar.YEAR, cursor.getInt(cursor.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_YEAR)));
            startTime.set(Calendar.HOUR_OF_DAY, cursor.getInt(cursor.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_STARTHOUR)));
            startTime.set(Calendar.MINUTE, cursor.getInt(cursor.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_STARTMIN)));
            endTime = (Calendar) startTime.clone();
            endTime.set(Calendar.HOUR_OF_DAY, cursor.getInt(cursor.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_ENDHOUR)));
            endTime.set(Calendar.MINUTE, cursor.getInt(cursor.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_ENDMIN)));


            event = new WeekViewEvent(cursor.getLong(cursor.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_ID)),
                    cursor.getString(cursor.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_TITLE)), startTime, endTime);
            Log.v("Id", ""+event.getId());
            Log.v("Title", cursor.getString(cursor.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_TITLE)));
            event.setColor(getResources().getColor(R.color.colorPrimaryLight));
            events.add(event);
        }


        while(cursor.moveToNext()){

            startTime = Calendar.getInstance();

            startTime.set(Calendar.DATE, cursor.getInt(cursor.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_DAY)));
            startTime.set(Calendar.MONTH, cursor.getInt(cursor.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_MONTH)));
            startTime.set(Calendar.YEAR, cursor.getInt(cursor.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_YEAR)));
            startTime.set(Calendar.HOUR_OF_DAY, cursor.getInt(cursor.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_STARTHOUR)));
            startTime.set(Calendar.MINUTE, cursor.getInt(cursor.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_STARTMIN)));
            endTime = (Calendar) startTime.clone();
            endTime.set(Calendar.HOUR_OF_DAY, cursor.getInt(cursor.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_ENDHOUR)));
            endTime.set(Calendar.MINUTE, cursor.getInt(cursor.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_ENDMIN)));


            event = new WeekViewEvent(cursor.getLong(cursor.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_ID)),
                    cursor.getString(cursor.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_TITLE)), startTime, endTime);
            Log.v("Id", "" + event.getId());
            Log.v("Title", cursor.getString(cursor.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_TITLE)));
            event.setColor(getResources().getColor(R.color.colorPrimaryLight));
            events.add(event);
        }
        return events;
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {

        long ID = event.getId();
        if(ID!=-1)
        {
        String detail = "", venue = "", title = "", type = "";
        int day = 0, month = 0, year = 0, starthour = 0, startmin = 0, endhour = 0, endmin = 0;

        cr.moveToFirst();
        if (cr.getLong(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_ID)) == ID)
        {
            detail = cr.getString(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_DETAIL));
            venue = cr.getString(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_VENUE));
            day = cr.getInt(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_DAY));
            month = cr.getInt(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_MONTH));
            year = cr.getInt(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_YEAR));
            starthour = cr.getInt(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_STARTHOUR));
            startmin = cr.getInt(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_STARTMIN));
            endhour = cr.getInt(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_ENDHOUR));
            endmin = cr.getInt(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_ENDMIN));
            title = cr.getString(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_TITLE));

        }

        else {
            while(cr.moveToNext()){
                if (cr.getLong(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_ID)) == ID)
                {
                    detail = cr.getString(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_DETAIL));
                    venue = cr.getString(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_VENUE));
                    day = cr.getInt(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_DAY));
                    month = cr.getInt(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_MONTH));
                    year = cr.getInt(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_YEAR));
                    starthour = cr.getInt(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_STARTHOUR));
                    startmin = cr.getInt(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_STARTMIN));
                    endhour = cr.getInt(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_ENDHOUR));
                    endmin = cr.getInt(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_ENDMIN));
                    title = cr.getString(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_TITLE));

                }
            }
        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
// ...Irrelevant code for customizing the buttons and title
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
        if(!(venue.equals(null))){
            tv_venue.setVisibility(View.VISIBLE);
            tv_venue.setText("Venue: " + venue);
        }
        else {
            tv_venue.setVisibility(View.GONE);
        }


        TextView tv_details = (TextView) dialogView.findViewById(R.id.details_details);
        if(!(detail.equals(null))) {
            tv_details.setVisibility(View.VISIBLE);
            tv_details.setText("Details: " + detail);
        }
        else {
            tv_details.setVisibility(View.GONE);
        }

        TextView tv_date = (TextView) dialogView.findViewById(R.id.details_date);
        tv_date.setText("Date: " + day + "-" + month + "-" + year);

        TextView tv_title = (TextView) dialogView.findViewById(R.id.details_title);
        if(!(title.equals(null))) {
            tv_title.setVisibility(View.VISIBLE);
            tv_title.setText("Title: " + title);
        }
        else {
            tv_title.setVisibility(View.GONE);
        }

        TextView tv_time = (TextView) dialogView.findViewById(R.id.details_time);
        tv_time.setText("Time: " + starthour + ":" + startmin + " - " + endhour + "-" + endmin);


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
            delete_edit_choose dialog = new delete_edit_choose();
            Bundle b = new Bundle();
            b.putInt("position", 0);
            dialog.setArguments(b);
            new delete_edit_choose().show(getFragmentManager(), "delete_and_choose");
        }
    }

    @Override
    public void onPositiveClick(int a)
    {
        if(a==0)
        {

            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("DELETE_OR_EDIT", 1);
            editor.commit();

            Dialog_for_more d=new Dialog_for_more();
            Bundle b=new Bundle();
            b.putLong("position", longClickedID);
            d.setArguments(b);
            new Dialog_for_more().show(getFragmentManager(), "edit_and_choose");

        }
        else
        {
            try {
                Dialog_for_more_delete d=new Dialog_for_more_delete();
                Bundle b=new Bundle();
                b.putLong("position",position);
                d.setArguments(b);
                new Dialog_for_more_delete().show(getFragmentManager(), "delete_and_choose");

            } catch (Exception e) {
                Toast.makeText(timetable_navigation2.this, e.toString(), Toast.LENGTH_LONG).show();
            }

        }
    }


    @Override
    public void onPositiveClickfordelete(int a) {
        if(a==1) {
            cr.moveToFirst();
            int count = 0;
            while (cr.getLong(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_ID)) != longClickedID && count < cr.getCount()) {
                cr.moveToNext();
                count++;
            }
            String original_title = cr.getString(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_TITLE)),
                    original_startminute = cr.getString(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_STARTMIN)),
                    original_starthour = cr.getString(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_STARTHOUR)),
                    original_endhour = cr.getString(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_ENDHOUR)),
                    original_endminute = cr.getString(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_ENDMIN)),
                    original_venue = cr.getString(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_VENUE));
            try {

                db.delete(CalendarDB.CalendarEntry.TABLE_NAME,
                        CalendarDB.CalendarEntry.COLUMN_NAME_TITLE + "=? AND " + CalendarDB.CalendarEntry.COLUMN_NAME_STARTHOUR + "=? AND " +
                                CalendarDB.CalendarEntry.COLUMN_NAME_STARTMIN + "=? AND " + CalendarDB.CalendarEntry.COLUMN_NAME_ENDHOUR + "=? AND " +
                                CalendarDB.CalendarEntry.COLUMN_NAME_ENDMIN + "=? AND " +
                                CalendarDB.CalendarEntry.COLUMN_NAME_VENUE + "=?",
                        new String[]{original_title, original_starthour, original_startminute, original_endhour, original_endminute, original_venue});
            }
            catch (Exception e){
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
                Log.e(e.toString(), "error");
            }

            finish();
            startActivity(getIntent());


            Log.e("HEY", "HEY"); // Code for check box checked for delete
        }


        else
        {
            db.delete(CalendarDB.CalendarEntry.TABLE_NAME, CalendarDB.CalendarEntry.COLUMN_NAME_ID + "=" + longClickedID, null);
            finish();
            startActivity(getIntent());
            //Code for checkbox not checked for delete
        }
    }

    @Override
    public void onPositiveClickforedit(int position) {
        if(position==1)
        {
            ismultiedit=true; // Code for checkbox checked for edit
        }

        else
        {
            ismultiedit=false;
        }
        Intent in=new Intent(timetable_navigation2.this,NewEvent.class);
        in.putExtra("multi_edit",ismultiedit);
        startActivity(in);

    }
    @Override
    public void onBackPressed(){

        if(fab_menu.isExpanded())
            fab_menu.collapse();
        else  super.onBackPressed();


    }
}