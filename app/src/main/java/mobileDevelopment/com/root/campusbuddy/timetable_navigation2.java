package mobileDevelopment.com.root.campusbuddy;

        import android.content.ContentValues;
        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.graphics.Color;
        import android.graphics.RectF;
        import android.os.Bundle;
        import android.app.Activity;
        import android.support.v7.app.ActionBarActivity;
        import android.util.TypedValue;
        import android.view.View;
        import android.widget.Toast;

        import com.alamkanak.weekview.DateTimeInterpreter;
        import com.alamkanak.weekview.WeekView;
        import com.alamkanak.weekview.WeekViewEvent;
        import com.getbase.floatingactionbutton.FloatingActionButton;

        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.List;
        import java.util.Locale;

public class timetable_navigation2 extends ActionBarActivity  implements WeekView.MonthChangeListener,
        WeekView.EventClickListener, WeekView.EventLongPressListener ,delete_edit_choose.AlertPositiveListener{

    SQLiteDatabase db;
    Cursor cr;

    ContentValues values;
    long longClickedID;



    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;
    private WeekView mWeekView;
    //String delvalue=null;
    SharedPreferences pref;
    int position=0;

    public static Activity fa;


    public FloatingActionButton fab_new_event, fab_go_today, fab_three_day, fab_one_day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.drawer_layout);

        Calendar cd = Calendar.getInstance();

//        cd.add(Calendar.DAY_OF_MONTH, -1);
//       cd.add(cd.DATE, -1);
//        Toast.makeText(this, cd.DAY_OF_MONTH + "he;llo "+ cd.MONTH + " " + cd.YEAR, Toast.LENGTH_LONG).show();


// 2.2 Set actionBarDrawerToggle as the DrawerListener

        Calendar c=Calendar.getInstance();
        c.set(Calendar.YEAR,2015);
        Toast.makeText(this,c.get(Calendar.YEAR)+"",Toast.LENGTH_LONG).show();
        fa = this;

        pref = this.getSharedPreferences(
                "com.example.appss", Context.MODE_PRIVATE);

        int a = pref.getInt("DELETE_OR_EDIT", 0);

        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("DELETE_OR_EDIT", 0);
        editor.commit();



        db = CalendarDBHelper.getInstance(getApplicationContext()).getReadableDatabase();


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

        try {
            cr = db.query(CalendarDB.CalendarEntry.TABLE_NAME, eventList, null, null, null, null, null);
        } catch (Exception err) {
            Toast.makeText(timetable_navigation2.this, err.toString(), Toast.LENGTH_LONG).show();
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
        fab_new_event.setBackgroundColor(Color.parseColor("#00ff00"));
        fab_one_day=(FloatingActionButton)findViewById(R.id.fab_one);
        fab_three_day=(FloatingActionButton)findViewById(R.id.fab_three);
        fab_go_today=(FloatingActionButton)findViewById(R.id.fab_today);

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



    /*
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
           // Toast.makeText(timetable_navigation2.this, ((TextView)view).getText(), Toast.LENGTH_LONG).show();
           if (position==0){
               mWeekView.goToToday();
           }
            if (position==1){
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
            if (position==2){
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
            if (position==3){
                if (mWeekViewType != TYPE_WEEK_VIEW) {
                   // item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_WEEK_VIEW;
                    mWeekView.setNumberOfVisibleDays(7);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                }
            }





        }

    }
    */


    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {

        // Populate the week view with some events.
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
        int sem = 1;
        int month = Calendar.MONTH;
        if (month>5) //month values start from 0. So, it is june at 5.
            sem=1;
        else
            sem = 2;

        Calendar startTime, endTime;
        WeekViewEvent event;
        cr.moveToFirst();
        startTime = Calendar.getInstance();
        int day3, month3, year3;

        if(cr.getCount() >0){



            startTime.set(Calendar.DATE, cr.getInt(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_DAY)));
            startTime.set(Calendar.MONTH, cr.getInt(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_MONTH)));
            startTime.set(Calendar.YEAR, cr.getInt(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_YEAR)));
            startTime.set(Calendar.HOUR_OF_DAY, cr.getInt(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_STARTHOUR)));
            startTime.set(Calendar.MINUTE, cr.getInt(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_STARTMIN)));
            endTime = (Calendar) startTime.clone();
            endTime.set(Calendar.HOUR_OF_DAY, cr.getInt(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_ENDHOUR)));
            endTime.set(Calendar.MINUTE, cr.getInt(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_ENDMIN)));

            event = new WeekViewEvent(cr.getLong(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_ID)), cr.getString(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_TITLE)), startTime, endTime);
            event.setColor(getResources().getColor(R.color.wallet_hint_foreground_holo_light));
            events.add(event);
        }


        while(cr.moveToNext()){

            //   Toast.makeText(timetable.this,  String.valueOf(cr.getString(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_MONTH))), Toast.LENGTH_LONG).show();




                startTime.set(Calendar.DATE, cr.getInt(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_DAY)));
                startTime.set(Calendar.MONTH, cr.getInt(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_MONTH)));
                startTime.set(Calendar.YEAR, cr.getInt(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_YEAR)));
                startTime.set(Calendar.HOUR_OF_DAY, cr.getInt(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_STARTHOUR)));
                startTime.set(Calendar.MINUTE, cr.getInt(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_STARTMIN)));
                endTime = (Calendar) startTime.clone();
                endTime.set(Calendar.HOUR_OF_DAY, cr.getInt(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_ENDHOUR)));
                endTime.set(Calendar.MINUTE, cr.getInt(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_ENDMIN)));

                event = new WeekViewEvent(cr.getLong(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_ID)), cr.getString(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_TITLE)), startTime, endTime);
                event.setColor(getResources().getColor(R.color.wallet_hint_foreground_holo_light));
                events.add(event);
        }


        return events;
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {

        long ID = event.getId();
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

        Intent detailsIntent = new Intent(timetable_navigation2
                .this, DetailsActivity.class);
        detailsIntent.putExtra("details",detail);
        detailsIntent.putExtra("venue",venue);
        detailsIntent.putExtra("title",title);
        detailsIntent.putExtra("start",starthour+ " : " +startmin);
        detailsIntent.putExtra("end", endhour + " : " + endmin);
        detailsIntent.putExtra("date",day+ " : " + (month+1) + " : " +year);
        detailsIntent.putExtra("type",  type);

        startActivity(detailsIntent);


        Toast.makeText(timetable_navigation2.this, "Event ID: " + ID, Toast.LENGTH_SHORT).show();

    }


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
      /*  Intent tte=new Intent(timetable.this,DeleteandEditEvents.class);
        tte.putExtra("event id",event.getId());
        startActivity(tte);
        Toast.makeText(timetable.this, "Long pressed event: " + event.getName(), Toast.LENGTH_SHORT).show();*/


        longClickedID = event.getId();
        delete_edit_choose dialog=new delete_edit_choose();
        Bundle b=new Bundle();
        b.putInt("position",position);
        dialog.setArguments(b);
        new delete_edit_choose().show(this, 0);

    }

    @Override
    public void onPositiveClick(int a)
    {
        if(a==0)
        {

            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("DELETE_OR_EDIT", 1);
            editor.commit();

            //editcounter++;
            Intent ne=new Intent(timetable_navigation2.this,NewEvent.class);
            ne.putExtra("value for editing", longClickedID );
            startActivity(ne);
        }
        else
        {
            try {
                db.delete(CalendarDB.CalendarEntry.TABLE_NAME, CalendarDB.CalendarEntry.COLUMN_NAME_ID + "=" + longClickedID, null);
            } catch (Exception e) {
                Toast.makeText(timetable_navigation2.this, e.toString(), Toast.LENGTH_LONG).show();
            }
            finish();
            startActivity(getIntent());

        }
    }


}


