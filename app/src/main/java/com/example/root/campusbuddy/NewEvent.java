package com.example.root.campusbuddy;


import android.app.Activity;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;


public class NewEvent extends Activity implements DateDialog.OnDateSelectedListener, StartTimeDialog.OnStartTimeSelectedListener, EndTimeDialog.OnEndTimeSelectedListener{

    EditText editt_date, editt_start, editt_end, editt_title, editt_details, editt_venue;
    Button submitBut;

    SQLiteDatabase db;
 //   CalendarDBHelper mDbHelper;
    ContentValues values;
    int year, day, month, starthour, startminute, endhour, endminute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

     //   mDbHelper = new CalendarDBHelper(getApplicationContext());

        db = CalendarDBHelper.getInstance(getApplicationContext()).getWritableDatabase();



       // CalendarDBHelper.getInstance(getApplicationContext()).onCreate(db);   added by myself
       // db = mDbHelper.getWritableDatabase();
         values = new ContentValues();



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        editt_date = (EditText) findViewById(R.id.edit_date);
        editt_start = (EditText) findViewById(R.id.edit_start_time);
        editt_end = (EditText) findViewById(R.id.edit_end_time);
        editt_title = (EditText) findViewById(R.id.edit_title);
        editt_details = (EditText) findViewById(R.id.edit_details);
        editt_venue = (EditText) findViewById(R.id.edit_venue);

        submitBut = (Button) findViewById(R.id.submit);


        editt_date.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DateDialog();
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

        editt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new StartTimeDialog();
                newFragment.show(getFragmentManager(), "startTimePicker");
            }
        });

        editt_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new EndTimeDialog();
                newFragment.show(getFragmentManager(), "endTimePicker");
            }
        });

        submitBut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_TITLE, editt_title.getText().toString());
                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_DAY, day);
                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_MONTH, month);
                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_YEAR, year);
                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_STARTHOUR, starthour);
                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_STARTMIN, startminute);
                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_ENDHOUR, endhour);
                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_ENDMIN, endminute);
                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_DETAIL, editt_details.getText().toString());
                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_VENUE, editt_venue.getText().toString());

                db.insert(
                        CalendarDB.CalendarEntry.TABLE_NAME,
                        null,
                        values);

        Toast.makeText(NewEvent.this, "Details submitted", Toast.LENGTH_LONG).show();
            finish();
            }
        });
     }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    @Override
    public void onDateSelected(int year1, int month1, int day1) {
        editt_date.setText(year1 + " " + month1 + " " + day1, TextView.BufferType.EDITABLE);
        year = year1;
        month = month1;
        day = day1;
    }

    @Override
    public void onEndTimeSelected(int hour, int minute) {
        editt_end.setText(hour + " " + minute, TextView.BufferType.EDITABLE);
        endhour = hour;
        endminute = minute;
    }

    @Override
    public void onStartTimeSelected(int hour, int minute) {
        String d = hour + " " + minute;
        editt_start.setText(d, TextView.BufferType.EDITABLE);
        starthour = hour;
        startminute = minute;
    }
    }

