package in.co.mdg.campusBuddy;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import in.co.mdg.campusBuddy.calendar.data_models.Time;
import in.co.mdg.campusBuddy.calendar.data_models.user_events.UserEvent;
import io.realm.Realm;
import io.realm.RealmResults;


public class NewEvent extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, DialogColor.ColorDialogListener {

    private EditText editt_date, editt_start, editt_end, editt_title, editt_details, editt_venue;
    private TextView color_text, typeOfEvent;
    private Button submitButton;
    private CardView color_button;
    private String color_returned = Data.getColor_list().get(0).getHash();

    private int year, day, month, starthour, startminute, endhour, endminute;
    private String title, venue, details, eventType = "once", date;

    private boolean isStartTime = true, isMultiEdit = false;
    private int check = 0;

    private Long editValue;
    private Toolbar toolbar;
    private DialogFragment newFragment;
    private Realm realm;
    private SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private SimpleDateFormat sdfDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    private Date startDate = new Date(), endDate = new Date();
    private Calendar startCal, endCal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        overridePendingTransition(R.anim.slide_in_up, R.anim.fade_out);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_event);

        editt_date = (EditText) findViewById(R.id.edit_date);
        editt_start = (EditText) findViewById(R.id.edit_start_time);
        editt_end = (EditText) findViewById(R.id.edit_end_time);
        editt_title = (EditText) findViewById(R.id.edit_title);
        editt_details = (EditText) findViewById(R.id.edit_details);
        editt_venue = (EditText) findViewById(R.id.edit_venue);
        typeOfEvent = (TextView) findViewById(R.id.text);
        submitButton = (Button) findViewById(R.id.submit);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.typeofevent);
        toolbar = (Toolbar) findViewById(R.id.tool_bar1);
        toolbar.setTitle("Add an Event");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        LinearLayout color_name = (LinearLayout) findViewById(R.id.color_layout);
        color_text = (TextView) findViewById(R.id.color_name_text);
        color_button = (CardView) findViewById(R.id.color_button);

        realm = Realm.getDefaultInstance();

        check = getIntent().getIntExtra("ADD_OR_EDIT", 0);

        if (check == 1) {
            //To edit an event
            radioGroup.setVisibility(View.GONE);
            typeOfEvent.setVisibility(View.INVISIBLE);
            Bundle extra = getIntent().getExtras();
            editValue = extra.getLong("value for editing");
            isMultiEdit = extra.getBoolean("multi_edit");
            UserEvent userEvent = realm.where(UserEvent.class).equalTo("id", editValue).findFirst();
            if (userEvent != null) {
                title = userEvent.getTitle();
                venue = userEvent.getVenue();
                details = userEvent.getDetails();
                editt_title.setText(title, TextView.BufferType.EDITABLE);
                editt_venue.setText(venue, TextView.BufferType.EDITABLE);
                editt_details.setText(details, TextView.BufferType.EDITABLE);
                editt_date.setText(sdfDate.format(userEvent.getTime().getStart()), TextView.BufferType.EDITABLE);
                editt_end.setText(sdfTime.format(userEvent.getTime().getEnd()), TextView.BufferType.EDITABLE);
                editt_start.setText(sdfTime.format(userEvent.getTime().getStart()), TextView.BufferType.EDITABLE);

                color_returned = userEvent.getColor();
                toolbar.setBackgroundColor(Color.parseColor(color_returned));
                color_button.setCardBackgroundColor(Color.parseColor(color_returned));

                startCal = Calendar.getInstance();
                endCal = Calendar.getInstance();
                startCal.setTime(userEvent.getTime().getStart());
                endCal.setTime(userEvent.getTime().getEnd());
                starthour = startCal.get(Calendar.HOUR_OF_DAY);
                startminute = startCal.get(Calendar.MINUTE);
                endhour = endCal.get(Calendar.HOUR_OF_DAY);
                endminute = endCal.get(Calendar.MINUTE);
                year = startCal.get(Calendar.YEAR);
                month = startCal.get(Calendar.MONTH);
                day = startCal.get(Calendar.DAY_OF_MONTH);
            } else {
                Toast.makeText(NewEvent.this, "Event not found", Toast.LENGTH_LONG).show();
                onBackPressed();
            }
        } else {
            //To Create new event
            Calendar cal = Calendar.getInstance();
            day = cal.get(Calendar.DAY_OF_MONTH);
            month = cal.get(Calendar.MONTH);
            year = cal.get(Calendar.YEAR);
            starthour = cal.get(Calendar.HOUR_OF_DAY);
            startminute = cal.get(Calendar.MINUTE);
            if (starthour < 23) {
                cal.add(Calendar.HOUR, 1);
                endhour = cal.get(Calendar.HOUR_OF_DAY);
                endminute = cal.get(Calendar.MINUTE);
            } else {
                endhour = 23;
                endminute = 59;
            }
            editt_start.setText(String.format(Locale.US, "%02d", starthour) + ":" + String.format(Locale.US, "%02d", startminute), TextView.BufferType.EDITABLE);
            editt_end.setText(String.format(Locale.US, "%02d", endhour) + ":" + String.format(Locale.US, "%02d", endminute), TextView.BufferType.EDITABLE);
            editt_date.setText(String.format(Locale.US, "%02d", day) + "/" + String.format(Locale.US, "%02d", (month + 1)) + "/" + year, TextView.BufferType.EDITABLE);

            toolbar.setBackgroundColor(Color.parseColor(color_returned));
            color_button.setCardBackgroundColor(Color.parseColor(color_returned));
        }

        editt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                DialogFragment newFragment = new DateDialog();
                newFragment.show(getFragmentManager(), "datePicker");
                */
                if (check == 0) {
                    Calendar now = Calendar.getInstance();
                    year = now.get(Calendar.YEAR);
                    month = now.get(Calendar.MONTH);
                    day = now.get(Calendar.DAY_OF_MONTH);
                }
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        NewEvent.this,
                        year,
                        month,
                        day
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        color_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newFragment = new DialogColor();
                newFragment.show(getSupportFragmentManager(), "colors");
            }
        });

        editt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartTime = true;
                if (check == 0) {
                    Calendar now = Calendar.getInstance();
                    starthour = now.get(Calendar.HOUR_OF_DAY);
                    startminute = now.get(Calendar.MINUTE);
                }
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        NewEvent.this,
                        starthour,
                        startminute,
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
                if (check == 0) {
                    Calendar now = Calendar.getInstance();
                    endhour = now.get(Calendar.HOUR_OF_DAY) + 1;
                    endminute = now.get(Calendar.MINUTE);
                }
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        NewEvent.this,
                        endhour,
                        endminute,
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

    }

    public void submitButton(View view) {
        startCal = Calendar.getInstance();
        endCal = Calendar.getInstance();

        Number id = realm.where(UserEvent.class).max("id");
        long value = (id == null) ? 0 : (id.longValue() + 1);

        try {
            date = String.format(Locale.US, "%02d", day) + "/" + String.format(Locale.US, "%02d", (month + 1)) + "/" + year;
            startDate = sdfDateTime.parse(date + " " + starthour + ":" + startminute);
            endDate = sdfDateTime.parse(date + " " + endhour + ":" + endminute);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (endDate.after(startDate)) {

            title = editt_title.getText().toString();
            details = editt_details.getText().toString();
            venue = editt_venue.getText().toString();

            if (title != null && !title.isEmpty()) {
                if (check == 0) { //Code for inserting new event to database
                    startCal.setTime(startDate);
                    endCal.setTime(endDate);
                    int semEnd;
                    if (month < 5) semEnd = 4;
                    else semEnd = 10;

                    long groupId = value;
                    switch (eventType) {
                        case "once":
                            createEvent(value, -1);
                            break;
                        case "weekly":
                            while (startCal.get(Calendar.MONTH) <= semEnd) {
                                createEvent(value++, groupId);
                                startCal.add(Calendar.DATE, 7);
                                endCal.add(Calendar.DATE, 7);
                                startDate = startCal.getTime();
                                endDate = endCal.getTime();
                            }
                            break;
                        case "monthly":
                            while (startCal.get(Calendar.MONTH) <= semEnd) {
                                createEvent(value++, groupId);
                                startCal.add(Calendar.MONTH, 1);
                                endCal.add(Calendar.MONTH, 1);
                                startDate = startCal.getTime();
                                endDate = endCal.getTime();
                            }
                            break;
                        case "daily":
                            while (startCal.get(Calendar.MONTH) <= semEnd) {
                                createEvent(value++, groupId);
                                startCal.add(Calendar.DATE, 1);
                                endCal.add(Calendar.DATE, 1);
                                startDate = startCal.getTime();
                                endDate = endCal.getTime();
                            }
                            break;
                    }

                    Toast.makeText(NewEvent.this, "Event Added", Toast.LENGTH_LONG).show();

                } else { //code for editing event/events

                    if (isMultiEdit) {
                        //code for editing multiple events
                        editMultipleEvents(editValue);
                    } else {
                        // code for single event edit
                        editEvent(editValue);
                    }

                    Toast.makeText(NewEvent.this, "Event Updated", Toast.LENGTH_LONG).show();
                }

                Intent returnIntent = new Intent();
                returnIntent.putExtra("Result", "Details Updated");
                returnIntent.putExtra("Date", date);
                setResult(RESULT_OK, returnIntent);
                finish();
            } else {
                Toast.makeText(NewEvent.this, "Title can't be empty.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(NewEvent.this, "End time can't be before start time", Toast.LENGTH_LONG).show();
        }
    }

    private void createEvent(final long id, final long groupId) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                UserEvent userEvent = realm.createObject(UserEvent.class, id);
                userEvent.setGroupId(groupId);
                userEvent.setTitle(title);
                userEvent.setVenue(venue);
                userEvent.setDetails(details);
                userEvent.setColor(color_returned);
                Time time = realm.createObject(Time.class);
                time.setStart(startDate);
                time.setEnd(endDate);
                userEvent.setTime(time);

            }
        });
    }

    private void editEvent(final long id) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                UserEvent userEvent = realm.where(UserEvent.class).equalTo("id", id).findFirst();
                userEvent.setTitle(title);
                userEvent.setVenue(venue);
                userEvent.setDetails(details);
                userEvent.setColor(color_returned);
                userEvent.getTime().setStart(startDate);
                userEvent.getTime().setEnd(endDate);
            }
        });
    }

    private void editMultipleEvents(final long id) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                long groupId = realm.where(UserEvent.class).equalTo("id", id).findFirst().getGroupId();
                RealmResults<UserEvent> userEvents = realm.where(UserEvent.class).equalTo("groupId", groupId).findAll().sort("id");
                for (UserEvent userEvent : userEvents) {

                    try {
                        date = sdfDate.format(userEvent.getTime().getStart());
                        startDate = sdfDateTime.parse(date + " " + starthour + ":" + startminute);
                        endDate = sdfDateTime.parse(date + " " + endhour + ":" + endminute);
                        userEvent.setTitle(title);
                        userEvent.setVenue(venue);
                        userEvent.setDetails(details);
                        userEvent.setColor(color_returned);
                        userEvent.getTime().setStart(startDate);
                        userEvent.getTime().setEnd(endDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("Result", "Back Pressed");
        setResult(RESULT_CANCELED, returnIntent);
        finish();
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
        editt_date.setText(String.format(Locale.US, "%02d", day1) + "/" + String.format(Locale.US, "%02d", (month1 + 1)) + "/" + year1, TextView.BufferType.EDITABLE);
        year = year1;
        month = month1;
        day = day1;
    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hour, int minute) {

        if (isStartTime) {
            editt_start.setText(String.format(Locale.US, "%02d", hour) + ":" + String.format(Locale.US, "%02d", minute), TextView.BufferType.EDITABLE);
            starthour = hour;
            startminute = minute;
        } else {
            editt_end.setText(String.format(Locale.US, "%02d", hour) + ":" + String.format(Locale.US, "%02d", minute), TextView.BufferType.EDITABLE);
            endhour = hour;
            endminute = minute;
        }
    }

    @Override
    public void onDestroy() {
        realm.close();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_out_up);
        super.onDestroy();
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_once:
                if (checked)
                    eventType = "once";
                break;
            case R.id.radio_daily:
                if (checked)
                    eventType = "daily";
                break;
            case R.id.radio_weekly:
                if (checked)
                    eventType = "weekly";
                break;
            case R.id.radio_monthly:
                if (checked)
                    eventType = "monthly";
                break;
        }
    }

    @Override
    public void onColorChoose(int position) {

        newFragment.dismiss();
        color_returned = Data.getColor_list().get(position).getHash();
        toolbar.setBackgroundColor(Color.parseColor(color_returned));
        color_text.setText(Data.getColor_list().get(position).getColor());
        color_button.setCardBackgroundColor(Color.parseColor(color_returned));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.setStatusBarColor(Color.parseColor(color_returned));
        }
        submitButton.setBackgroundColor(Color.parseColor(color_returned));
    }
//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState) {
//        super.onSaveInstanceState(savedInstanceState);
//        // Save UI state changes to the savedInstanceState.
//        // This bundle will be passed to onCreate if the process is
//        // killed and restarted.
//        savedInstanceState.putString("color_name", color_name_fortheme);
//
//        // etc.
//    }
//
//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        // Restore UI state from the savedInstanceState.
//        // This bundle has also been passed to onCreate.
//        color_name_fortheme = savedInstanceState.getString("color_name");
//    }
}