package com.example.root.campusbuddy;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/**
 * Created by Raquib-ul-Alam Kanak on 7/21/2014.
 * Website: http://alamkanak.github.io/
 */
public class timetable3 extends AppCompatActivity implements WeekView.MonthChangeListener, WeekView.EventClickListener, WeekView.EventLongPressListener ,delete_edit_choose.AlertPositiveListener,Navigation_Drawer_Fragment.NavigationDrawerCallbacks {

    SQLiteDatabase db;
    Cursor cr;

    ContentValues values;
    long longClickedID;



    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;
    private WeekView mWeekView;
    private Navigation_Drawer_Fragment mNavigationDrawerFragment;

    //String delvalue=null;
    SharedPreferences pref;
        int position=0;

    public static Activity fa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fa = this;


        try{
        setContentView(R.layout.activity_timetable);
         mNavigationDrawerFragment = (Navigation_Drawer_Fragment)(getSupportFragmentManager().findFragmentById(R.id.navigation_drawer));

        }
        catch(Exception e)
        {
            Toast.makeText(this,e.toString()+"1",Toast.LENGTH_SHORT).show();
        }
        mTitle = getTitle();

        // Set up the drawer.
        try{

            mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));}
        catch(Exception e)
        {
            Toast.makeText(this,e.toString()+"2",Toast.LENGTH_SHORT).show();
        }


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
            Toast.makeText(timetable3.this, err.toString(), Toast.LENGTH_LONG).show();
        }





        cr.moveToFirst();
      /*
        try {
            Toast.makeText(timetable.this,  String.valueOf(cr.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_DAY)), Toast.LENGTH_LONG).show();}
        catch (Exception err){
            Toast.makeText(timetable.this, err.toString(), Toast.LENGTH_LONG).show();
        }
*/

        try{
        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) fa.findViewById(R.id.weekView);

        // Show a toast message about the touched event.
        mWeekView.setOnEventClickListener(this);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(this);

        // Set up a date time interpreter to interpret how the date and time will be formatted in
        // the week view. This is optional.
        setupDateTimeInterpreter(false);}

        catch(Exception e)
        {
            Toast.makeText(this,e.toString()+"3",Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tt, menu);
//        try{
//        if (!mNavigationDrawerFragment.isDrawerOpen()) {
////            // Only show items in the action bar relevant to this screen
////            // if the drawer is not showing. Otherwise, let the drawer
////            // decide what to show in the action bar.
//            getMenuInflater().inflate(R.menu.menu_tt, menu);
//            restoreActionBar();
//            return true;
//        }}
//        catch (Exception e)
//        {
//            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
//        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        setupDateTimeInterpreter(id == R.id.action_week_view);
//        switch (id){
//            case R.id.action_today:
//                mWeekView.goToToday();
//                return true;
//            case R.id.action_day_view:
//                if (mWeekViewType != TYPE_DAY_VIEW) {
//                    item.setChecked(!item.isChecked());
//                    mWeekViewType = TYPE_DAY_VIEW;
//                    mWeekView.setNumberOfVisibleDays(1);
//
//                    // Lets change some dimensions to best fit the view.
//                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
//                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
//                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
//                }
//                return true;
//            case R.id.action_three_day_view:
//                if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
//                    item.setChecked(!item.isChecked());
//                    mWeekViewType = TYPE_THREE_DAY_VIEW;
//                    mWeekView.setNumberOfVisibleDays(3);
//
//                    // Lets change some dimensions to best fit the view.
//                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
//                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
//                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
//                }
//                return true;
//            case R.id.action_week_view:
//                if (mWeekViewType != TYPE_WEEK_VIEW) {
//                    item.setChecked(!item.isChecked());
//                    mWeekViewType = TYPE_WEEK_VIEW;
//                    mWeekView.setNumberOfVisibleDays(7);
//
//                    // Lets change some dimensions to best fit the view.
//                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
//                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
//                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
//                }
//                return true;
//
//            case R.id.action_new_event:
//
//                Intent newEventIntent = new Intent(timetable3.this, NewEvent.class);
//                startActivity(newEventIntent);
//                return true;
//
//
//
//        }

        return super.onOptionsItemSelected(item);
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

        Intent detailsIntent = new Intent(timetable3.this, DetailsActivity.class);
        detailsIntent.putExtra("details",detail);
        detailsIntent.putExtra("venue",venue);
        detailsIntent.putExtra("title",title);
        detailsIntent.putExtra("start",starthour+ " : " +startmin);
        detailsIntent.putExtra("end", endhour + " : " + endmin);
        detailsIntent.putExtra("date",day+ " : " + month + " : " +year);

        startActivity(detailsIntent);


        Toast.makeText(timetable3.this, "Event ID: " + ID, Toast.LENGTH_SHORT).show();

    }


    /**
     * Set up a date time interpreter which will show short date values when in week view and long
     * date values otherwise.
     * @param shortDate True if the date values should be short.
     */
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
                Intent ne=new Intent(timetable3.this,NewEvent.class);
                ne.putExtra("value for editing", longClickedID );
                startActivity(ne);
                }
                else
                {
                        try {
                    db.delete(CalendarDB.CalendarEntry.TABLE_NAME, CalendarDB.CalendarEntry.COLUMN_NAME_ID + "=" + longClickedID, null);
                } catch (Exception e) {
                    Toast.makeText(timetable3.this, e.toString(), Toast.LENGTH_LONG).show();
                }
                finish();
                startActivity(getIntent());

            }
                }

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */

    /*
     * Used to store the last screen title. For use in {@link restoreActionBar()}.
     */
    private CharSequence mTitle;


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.weekView, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = "Today";
                mWeekView.goToToday();
                break;
            case 2:
                mTitle = "3-day view";
                if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
                   // item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_THREE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(3);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                break;
            case 3:
                mTitle = "7-day view";
                if (mWeekViewType != TYPE_WEEK_VIEW) {
                   // item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_WEEK_VIEW;
                    mWeekView.setNumberOfVisibleDays(7);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                }
                break;
            case 4:
                Intent newEventIntent = new Intent(timetable3.this, NewEvent.class);
                startActivity(newEventIntent);

        }
    }

//    public void restoreActionBar() {
//        ActionBar actionBar =getSupportActionBar();
//
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
//        actionBar.setDisplayShowTitleEnabled(true);
//        actionBar.setTitle(mTitle);
//    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        if (!mNavigationDrawerFragment.isDrawerOpen()) {
//            // Only show items in the action bar relevant to this screen
//            // if the drawer is not showing. Otherwise, let the drawer
//            // decide what to show in the action bar.
//            getMenuInflater().inflate(R.menu.main, menu);
//            restoreActionBar();
//            return true;
//        }
//        return super.onCreateOptionsMenu(menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((timetable3) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}






