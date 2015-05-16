package com.example.root.campusbuddy;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;


public class DeleteandEditEvents extends ActionBarActivity {

    public static int editcounter=0,deletecounter=0;
    int value_edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleteand_edit_events);
        Intent dae=getIntent();
        Bundle extra=dae.getExtras();
        value_edit=Integer.parseInt(extra.getString("event id"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_deleteand_edit_events, menu);
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

    public  void onRadioButtonClick(View view)
    {
        boolean checked=((RadioButton)view).isChecked();
        switch(view.getId())
        {
            case R.id.radio_edit:
                if(checked)
                {
                    editcounter++;
                    Intent ne=new Intent(DeleteandEditEvents.this,NewEvent.class);
                    ne.putExtra("value for editing",value_edit+"");
                    startActivity(ne);
                }
            case R.id.radio_delete:
            {
                deletecounter++;
                Intent ne=new Intent(DeleteandEditEvents.this,timetable.class);
                ne.putExtra("value for deleting",value_edit+"");
                startActivity(ne);
            }
        }
    }
}
