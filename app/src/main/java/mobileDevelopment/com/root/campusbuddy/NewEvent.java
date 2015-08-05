        package mobileDevelopment.com.root.campusbuddy;


        import android.content.ContentValues;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.graphics.Color;
        import android.opengl.Visibility;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.RadioButton;
        import android.widget.RadioGroup;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
        import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
        import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

        import java.util.Calendar;

        import mobileDevelopment.com.root.campusbuddy.CalendarDB;
        import mobileDevelopment.com.root.campusbuddy.CalendarDBHelper;
        import mobileDevelopment.com.root.campusbuddy.R;
        import mobileDevelopment.com.root.campusbuddy.timetable_navigation2;


public class NewEvent extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener{

    EditText editt_date, editt_start, editt_end, editt_title, editt_details, editt_venue;
    Button submitBut;

    SQLiteDatabase db;
    //   CalendarDBHelper mDbHelper;
    ContentValues values;
    int year, day, month, starthour, startminute, endhour, endminute;
    String title, venue, details, event_type= "once";
    Long ID;

    boolean isStartTime = true, ismultiedit = false;

    SharedPreferences prefs, pref_edit;

    Long editvalue;
    SQLiteDatabase db_edit;
    Cursor cr_edit;
    Toolbar toolbar;
    RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //   mDbHelper = new CalendarDBHelper(getApplicationContext());



        overridePendingTransition(R.anim.slide_in_up, R.anim.fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        radioGroup=(RadioGroup)findViewById(R.id.typeofevent);
        toolbar = (Toolbar) findViewById(R.id.tool_bar1);
//        DayNightTheme.setToolbar(toolbar);
        toolbar.setTitle("Add an Event");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
        ismultiedit=getIntent().getBooleanExtra("multi_edit",false);

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
                CalendarDB.CalendarEntry.COLUMN_NAME_VENUE,

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
                /*
                DialogFragment newFragment = new DateDialog();
                newFragment.show(getFragmentManager(), "datePicker");
                */
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        NewEvent.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );


                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        editt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isStartTime = true;
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        NewEvent.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
                );

                tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Log.d("TimePicker", "Dialog was cancelled");
                    }
                });
                tpd.show(getFragmentManager(), "StartTimedialog");
            }
        });

        editt_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isStartTime = false;
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        NewEvent.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
                );

                tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Log.d("TimePicker", "Dialog was cancelled");
                    }
                });
                tpd.show(getFragmentManager(), "EndTimedialog");
            }
        });
        try {
            submitBut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Calendar cd = Calendar.getInstance();

                    Long value = prefs.getLong("ID_KEY", 0);
                    int counter = pref_edit.getInt("DELETE_OR_EDIT", 0);
                    if (counter == 0) {

                        cd.set(Calendar.YEAR, year);
                        cd.set(Calendar.MONTH, month);
                        cd.set(Calendar.DAY_OF_MONTH, day);
                        cd.set(Calendar.HOUR_OF_DAY,starthour);
                        cd.set(Calendar.MINUTE,startminute);

                        title = editt_title.getText().toString();
                        details = editt_details.getText().toString();
                        venue = editt_venue.getText().toString();


                        if (event_type.equals("once")) {
                            values.put(CalendarDB.CalendarEntry.COLUMN_NAME_ID, value);
                            values.put(CalendarDB.CalendarEntry.COLUMN_NAME_TITLE, title);

                            values.put(CalendarDB.CalendarEntry.COLUMN_NAME_STARTHOUR, starthour);
                            values.put(CalendarDB.CalendarEntry.COLUMN_NAME_STARTMIN, startminute);
                            values.put(CalendarDB.CalendarEntry.COLUMN_NAME_ENDHOUR, endhour);
                            values.put(CalendarDB.CalendarEntry.COLUMN_NAME_ENDMIN, endminute);
                            values.put(CalendarDB.CalendarEntry.COLUMN_NAME_DETAIL, details);
                            values.put(CalendarDB.CalendarEntry.COLUMN_NAME_VENUE, venue);
                            values.put(CalendarDB.CalendarEntry.COLUMN_NAME_DAY, day);
                            values.put(CalendarDB.CalendarEntry.COLUMN_NAME_MONTH, month);
                            values.put(CalendarDB.CalendarEntry.COLUMN_NAME_YEAR, year);
                            db.insert(
                                    CalendarDB.CalendarEntry.TABLE_NAME,
                                    null,
                                    values);
                            value++;
                        } else if (event_type.equals("weekly")) {
                            while (cd.get(Calendar.MONTH) <= 10) {
                                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_ID, value);
                                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_TITLE, title);

                                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_STARTHOUR, cd.get(Calendar.HOUR_OF_DAY));
                                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_STARTMIN, cd.get(Calendar.MINUTE));
                                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_ENDHOUR, endhour);
                                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_ENDMIN, endminute);
                                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_DETAIL, details);
                                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_VENUE, venue);
                                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_DAY, cd.get(Calendar.DAY_OF_MONTH));
                                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_MONTH, cd.get(Calendar.MONTH));
                                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_YEAR, cd.get(Calendar.YEAR));

                                db.insert(
                                        CalendarDB.CalendarEntry.TABLE_NAME,
                                        null,
                                        values);
                                value++;

                                cd.add(Calendar.DATE, 7);
                            }
                        }
                        else if (event_type.equals("monthly")) {
                            while (cd.get(Calendar.MONTH) <= 10) {
                                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_ID, value);
                                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_TITLE, title);

                                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_STARTHOUR, starthour);
                                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_STARTMIN, startminute);
                                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_ENDHOUR, endhour);
                                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_ENDMIN, endminute);
                                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_DETAIL, details);
                                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_VENUE, venue);
                                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_DAY, cd.get(Calendar.DAY_OF_MONTH));
                                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_MONTH, cd.get(Calendar.MONTH));
                                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_YEAR, cd.get(Calendar.YEAR));

                                db.insert(
                                        CalendarDB.CalendarEntry.TABLE_NAME,
                                        null,
                                        values);
                                value++;
                                cd.add(Calendar.MONTH, 1);
                            }


                        } else if (event_type.equals("daily")) {
                            while (cd.get(Calendar.MONTH) <= 10) {
                                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_ID, value);
                                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_TITLE, title);

                                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_STARTHOUR, starthour);
                                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_STARTMIN, startminute);
                                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_ENDHOUR, endhour);
                                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_ENDMIN, endminute);
                                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_DETAIL, details);
                                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_VENUE, venue);
                                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_DAY, cd.get(Calendar.DAY_OF_MONTH));
                                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_MONTH, cd.get(Calendar.MONTH));
                                values.put(CalendarDB.CalendarEntry.COLUMN_NAME_YEAR, cd.get(Calendar.YEAR));

                                db.insert(
                                        CalendarDB.CalendarEntry.TABLE_NAME,
                                        null,
                                        values);
                                value++;

                                cd.add(Calendar.DATE, 1);
                            }

                        }


                        Toast.makeText(NewEvent.this, "Details submitted  ", Toast.LENGTH_LONG).show();

                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putLong("ID_KEY", value);
                        editor.commit();
                    } else {

                        radioGroup.setVisibility(View.GONE);

                        if (!(ismultiedit))
                        {
                            Toast.makeText(NewEvent.this,ismultiedit+"",Toast.LENGTH_LONG).show();

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
                            Toast.makeText(NewEvent.this, "Details edited  ", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(NewEvent.this, e.toString(), Toast.LENGTH_LONG).show();
                        }

                        SharedPreferences.Editor editor = pref_edit.edit();
                        editor.putInt("DELETE_OR_EDIT", 0);
                        editor.commit();
                    }
                    else {
                            Toast.makeText(NewEvent.this,ismultiedit+"",Toast.LENGTH_LONG).show();
                            Log.e("HEY","HEY");
                            // code for multi edit.
                        }

                    }


                    timetable_navigation2.fa.finish();
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.slide_out_up);


                    Intent ttIntent = new Intent(NewEvent.this, timetable_navigation2.class);
                    startActivity(ttIntent);

                }
            });
        }
        catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_out_up);
        /*
        timetable_navigation2.fa.finish();
        Intent ttIntent = new Intent(NewEvent.this, timetable_navigation2.class);
        startActivity(ttIntent);
        */
        finish();


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
    public void onDateSet(DatePickerDialog datePickerDialog, int year1, int month1, int day1) {
        editt_date.setText(day1+ "/" + (month1+1) + "/" + year1, TextView.BufferType.EDITABLE);
        year = year1;
        month = month1;
        day = day1;
    }


    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hour, int minute) {

        if(isStartTime){
            editt_start.setText(hour + ":" + minute, TextView.BufferType.EDITABLE);
            starthour = hour;
            startminute = minute;
        }
        else{
            editt_end.setText(hour + ":" + minute, TextView.BufferType.EDITABLE);
            endhour = hour;
            endminute = minute;
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_out_up);

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_once:
                if (checked)
                    event_type = "once";
                break;
            case R.id.radio_daily:
                if (checked)
                    event_type = "daily";
                break;
            case R.id.radio_weekly:
                if (checked)
                    event_type = "weekly";
                break;
            case R.id.radio_monthly:
                if (checked)
                    event_type = "monthly";
                break;
        }
    }


}

