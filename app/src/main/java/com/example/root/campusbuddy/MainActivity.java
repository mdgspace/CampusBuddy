package com.example.root.campusbuddy;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    Button mapButt, ttButt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapButt = (Button) findViewById(R.id.mapBut);
        ttButt = (Button) findViewById(R.id.ttBut);

        mapButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(MainActivity.this, map.class);
                startActivity(mapIntent);
            }
        });

        ttButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ttIntent = new Intent(MainActivity.this, timetable.class);
                startActivity(ttIntent);
            }
        });


    }}




