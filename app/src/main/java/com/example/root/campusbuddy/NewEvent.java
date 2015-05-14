package com.example.root.campusbuddy;


import android.app.Activity;
import android.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class NewEvent extends Activity implements DateDialog.OnDateSelectedListener, StartTimeDialog.OnStartTimeSelectedListener, EndTimeDialog.OnEndTimeSelectedListener {

    EditText editt_date, editt_start, editt_end;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        editt_date = (EditText) findViewById(R.id.edit_date);
        editt_start = (EditText) findViewById(R.id.edit_start_time);
        editt_end = (EditText) findViewById(R.id.edit_end_time);

        editt_date.setOnClickListener(new View.OnClickListener() {
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
    public void onDateSelected(int year, int month, int day) {
        Toast.makeText(NewEvent.this, "" + year, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onEndTimeSelected(int hour, int minute) {

    }

    @Override
    public void onStartTimeSelected(int hour, int minute) {


    }
}

