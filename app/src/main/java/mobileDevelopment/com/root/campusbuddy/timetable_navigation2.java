package mobileDevelopment.com.root.campusbuddy;

        import android.content.ContentValues;
        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.graphics.RectF;
        import android.os.Bundle;
        import android.app.Activity;
        import android.content.res.Configuration;
        import android.support.design.widget.FloatingActionButton;
        import android.support.v4.app.ActionBarDrawerToggle;
        import android.support.v4.view.GravityCompat;
        import android.support.v4.widget.DrawerLayout;
        import android.support.v7.app.ActionBarActivity;
        import android.util.TypedValue;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.ListView;
        import android.widget.Toast;

        import com.alamkanak.weekview.DateTimeInterpreter;
        import com.alamkanak.weekview.WeekView;
        import com.alamkanak.weekview.WeekViewEvent;

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

    private String[] drawerListViewItems;
    private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    public FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.drawer_layout);



// get list items from strings.xml
        drawerListViewItems = getResources().getStringArray(R.array.navigation_events);

// get ListView defined in activity_main.xml
        drawerListView = (ListView) findViewById(R.id.left_drawer);

// Set the adapter for the list view
        drawerListView.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, drawerListViewItems));

// 2. App Icon
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout2);

// 2.1 create ActionBarDrawerToggle
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, /* host Activity */
                drawerLayout, /* DrawerLayout object */
                R.drawable.ic_drawer, /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open, /* "open drawer" description */
                R.string.drawer_close /* "close drawer" description */
        );

// 2.2 Set actionBarDrawerToggle as the DrawerListener
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
       /* try {
// 2.3 enable and show "up" arrow
        getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch (Exception e){
            Toast.makeText(timetable_navigation2.this, e.toString(), Toast.LENGTH_LONG).show();
        }
*/
// just styling option
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        drawerListView.setOnItemClickListener(new DrawerItemClickListener());

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
                CalendarDB.CalendarEntry.COLUMN_NAME_VENUE
        };

        try {
            cr = db.query(CalendarDB.CalendarEntry.TABLE_NAME, eventList, null, null, null, null, null);
        } catch (Exception err) {
            Toast.makeText(timetable_navigation2.this, err.toString(), Toast.LENGTH_LONG).show();
        }





        cr.moveToFirst();
      /*
        try {
            Toast.makeText(timetable.this,  String.valueOf(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_DAY)), Toast.LENGTH_LONG).show();}
        catch (Exception err){
            Toast.makeText(timetable.this, err.toString(), Toast.LENGTH_LONG).show();
        }
*/


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
        fab=(FloatingActionButton)findViewById(R.id.fabae);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newEventIntent = new Intent(timetable_navigation2.this, NewEvent.class);
                startActivity(newEventIntent);

            }
        });

    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
// Sync the toggle state after onRestoreInstanceState has occurred.
        actionBarDrawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// call ActionBarDrawerToggle.onOptionsItemSelected(), if it returns true
// then it has handled the app icon touch event
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
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

//            if (position==4){
//                Intent newEventIntent = new Intent(timetable_navigation2.this, NewEvent.class);
//                startActivity(newEventIntent);
//            }


            drawerLayout.closeDrawer(drawerListView);
        }

    }


    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {

        // Populate the week view with some events.
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

        Calendar startTime, endTime;
        WeekViewEvent event;
        cr.moveToFirst();
        startTime = Calendar.getInstance();
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
            events.add(event);}


        while(cr.moveToNext()){

            //   Toast.makeText(timetable.this,  String.valueOf(cr.getString(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_MONTH))), Toast.LENGTH_LONG).show();

            startTime = Calendar.getInstance();
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
        //      Toast.makeText(timetable.this, "Event: " + event.getName(), Toast.LENGTH_SHORT).show();
/*
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        DetailsFragment fragment = new DetailsFragment();
        fragmentTransaction.add(R.id.calendar, fragment);

        fragmentTransaction.commit();
*/

        // DetailsFragment det = (DetailsFragment) getSupportFragmentManager().findFragmentById(R.id.hddbn);

     /*   Intent detailsIntent = new Intent(timetable.this, DetailsActivity.class);
        detailsIntent.putExtra(deta, "details");
        startActivity(detailsIntent);
        */
        long ID = event.getId();
        String detail = "", venue = "", title = "";
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
        detailsIntent.putExtra("date",day+ " : " + month + " : " +year);

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
        dialog.show(getFragmentManager(), "ChooseDialogFragment");


//        final Dialog dialog = new Dialog(timetable3.this);
//        dialog.setContentView(R.layout.activity_deleteand_edit_events2);
//        dialog.setTitle("Choose action...");
//
//
//        TextView choose = (TextView) dialog.findViewById(R.id.choose_action);
//        TextView edit = (TextView) dialog.findViewById(R.id.text_edit);
//        TextView del = (TextView) dialog.findViewById(R.id.text_delete);
//
//        choose.setVisibility(View.INVISIBLE);
//
//
//        // if button is clicked, close the custom dialog
//        edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharedPreferences.Editor editor = pref.edit();
//                editor.putInt("DELETE_OR_EDIT", 1);
//                editor.commit();
//
//                //editcounter++;
//                Intent ne=new Intent(timetable3.this,NewEvent.class);
//                ne.putExtra("value for editing", longClickedID );
//                startActivity(ne);
//
//                dialog.dismiss();
//            }
//        });
//
//        del.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            try {
//                    db.delete(CalendarDB.CalendarEntry.TABLE_NAME, CalendarDB.CalendarEntry.COLUMN_NAME_ID + "=" + longClickedID, null);
//                } catch (Exception e) {
//                    Toast.makeText(timetable3.this, e.toString(), Toast.LENGTH_LONG).show();
//                }
//                dialog.dismiss();
//                finish();
//                startActivity(getIntent());
//
//            }
//        });
//
//        dialog.show();
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


