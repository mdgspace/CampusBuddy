package com.example.root.campusbuddy;


import android.app.Activity;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
    String title, venue, details;
    Long ID;

    SharedPreferences prefs, pref_edit;

    Long editvalue;
    SQLiteDatabase db_edit;
    Cursor cr_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

     //   mDbHelper = new CalendarDBHelper(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        db = CalendarDBHelper.getInstance(getApplicationContext()).getWritableDatabase();
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
            cr_edit = db.query(CalendarDB.CalendarEntry.TABLE_NAME, eventList, null, null, null, null, null);
        }
        catch (Exception err){
            Toast.makeText(NewEvent.this, err.toString(), Toast.LENGTH_LONG).show();
        }



       // CalendarDBHelper.getInstance(getApplicationContext()).onCreate(db);   added by myself
       // db = mDbHelper.getWritableDatabase();
         values = new ContentValues();

        prefs = this.getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);

        pref_edit = this.getSharedPreferences(
                "com.example.appss", Context.MODE_PRIVATE);



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        editt_date = (EditText) findViewById(R.id.edit_date);
        editt_start = (EditText) findViewById(R.id.edit_start_time);
        editt_end = (EditText) findViewById(R.id.edit_end_time);
        editt_title = (EditText) findViewById(R.id.edit_title);
        editt_details = (EditText) findViewById(R.id.edit_details);
        editt_venue = (EditText) findViewById(R.id.edit_venue);

        submitBut = (Button) findViewById(R.id.submit);

        int check = pref_edit.getInt("DELETE_OR_EDIT", 0);

        if (check==1)
        {
            Intent ne=getIntent();
            Bundle extra=ne.getExtras();
            editvalue = extra.getLong("value for editing");
            int count=0;

            cr_edit.moveToFirst();

            while (cr_edit.getLong(cr_edit.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_ID)) != editvalue && count<cr_edit.getCount())
            {
                cr_edit.moveToNext();
                count++;
            }

            title = cr_edit.getString(cr_edit.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_TITLE));
            venue = cr_edit.getString(cr_edit.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_VENUE));
            details = cr_edit.getString(cr_edit.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_DETAIL));

            ID = editvalue;
            starthour = cr_edit.getInt(cr_edit.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_STARTHOUR));
            startminute = cr_edit.getInt(cr_edit.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_STARTMIN));
            endhour = cr_edit.getInt(cr_edit.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_ENDHOUR));
            endminute = cr_edit.getInt(cr_edit.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_ENDMIN));
            year = cr_edit.getInt(cr_edit.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_YEAR));
            month = cr_edit.getInt(cr_edit.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_MONTH));
            day = cr_edit.getInt(cr_edit.getColumnIndex(CalendarDB.CalendarEntry.COLUMN_NAME_DAY));

            editt_title.setText(title, TextView.BufferType.EDITABLE);
            editt_venue.setText(venue, TextView.BufferType.EDITABLE);
            editt_details.setText(details, TextView.BufferType.EDITABLE);
            editt_date.setText(day+ "/" + (month+1) + "/" + year, TextView.BufferType.EDITABLE);
            editt_end.setText(endhour + ":" + endminute, TextView.BufferType.EDITABLE);
            editt_start.setText(starthour + ":" + startminute, TextView.BufferType.EDITABLE);
        }


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

                Long value = prefs.getLong("ID_KEY", 0);
               int counter = pref_edit.getInt("DELETE_OR_EDIT", 0);
                if(counter==0) {

                    title = editt_title.getText().toString();
                    details = editt_details.getText().toString();
                    venue = editt_venue.getText().toString();
                    values.put(CalendarDB.CalendarEntry.COLUMN_NAME_ID, value);
                    values.put(CalendarDB.CalendarEntry.COLUMN_NAME_TITLE, title);
                    values.put(CalendarDB.CalendarEntry.COLUMN_NAME_DAY, day);
                    values.put(CalendarDB.CalendarEntry.COLUMN_NAME_MONTH, month);
                    values.put(CalendarDB.CalendarEntry.COLUMN_NAME_YEAR, year);
                    values.put(CalendarDB.CalendarEntry.COLUMN_NAME_STARTHOUR, starthour);
                    values.put(CalendarDB.CalendarEntry.COLUMN_NAME_STARTMIN, startminute);
                    values.put(CalendarDB.CalendarEntry.COLUMN_NAME_ENDHOUR, endhour);
                    values.put(CalendarDB.CalendarEntry.COLUMN_NAME_ENDMIN, endminute);
                    values.put(CalendarDB.CalendarEntry.COLUMN_NAME_DETAIL, details);
                    values.put(CalendarDB.CalendarEntry.COLUMN_NAME_VENUE, venue);

                    db.insert(
                            CalendarDB.CalendarEntry.TABLE_NAME,
                            null,
                            values);


                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putLong("ID_KEY", value + 1);
                    editor.commit();
                }

                else{

                    title = editt_title.getText().toString();
                    details = editt_details.getText().toString();
                    venue = editt_venue.getText().toString();
                    values.put(CalendarDB.CalendarEntry.COLUMN_NAME_ID, editvalue);
                    values.put(CalendarDB.CalendarEntry.COLUMN_NAME_TITLE, title);
                    values.put(CalendarDB.CalendarEntry.COLUMN_NAME_DAY, day);
                    values.put(CalendarDB.CalendarEntry.COLUMN_NAME_MONTH, month);
                    values.put(CalendarDB.CalendarEntry.COLUMN_NAME_YEAR, year);
                    values.put(CalendarDB.CalendarEntry.COLUMN_NAME_STARTHOUR, starthour);
                    values.put(CalendarDB.CalendarEntry.COLUMN_NAME_STARTMIN, startminute);
                    values.put(CalendarDB.CalendarEntry.COLUMN_NAME_ENDHOUR, endhour);
                    values.put(CalendarDB.CalendarEntry.COLUMN_NAME_ENDMIN, endminute);
                    values.put(CalendarDB.CalendarEntry.COLUMN_NAME_DETAIL, details);
                    values.put(CalendarDB.CalendarEntry.COLUMN_NAME_VENUE, venue);

                    try {
                        db.update(
                                CalendarDB.CalendarEntry.TABLE_NAME,
                                values,
                                CalendarDB.CalendarEntry.COLUMN_NAME_ID + "=" + editvalue,
                                null);
                    }
                    catch (Exception e ){
                        Toast.makeText(NewEvent.this, e.toString(), Toast.LENGTH_LONG).show();
                    }

                    SharedPreferences.Editor editor = pref_edit.edit();
                    editor.putInt("DELETE_OR_EDIT", 0);
                    editor.commit();

                }

        Toast.makeText(NewEvent.this, "Details submitted  ", Toast.LENGTH_LONG).show();
            finish();

                Intent ttIntent = new Intent(NewEvent.this, timetable.class);
                startActivity(ttIntent);
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
        editt_date.setText(day1+ "/" + (month1+1) + "/" + year1, TextView.BufferType.EDITABLE);
        year = year1;
        month = month1;
        day = day1;
    }

    @Override
    public void onEndTimeSelected(int hour, int minute) {
        editt_end.setText(hour + ":" + minute, TextView.BufferType.EDITABLE);
        endhour = hour;
        endminute = minute;
    }

    @Override
    public void onStartTimeSelected(int hour, int minute) {
        String d = hour + ":" + minute;
        editt_start.setText(d, TextView.BufferType.EDITABLE);
        starthour = hour;
        startminute = minute;
    }
    }