package com.example.root.campusbuddy;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    Button mapButt, ttButt, tnButt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapButt = (Button) findViewById(R.id.mapBut);
        ttButt = (Button) findViewById(R.id.ttBut);

        tnButt2 = (Button) findViewById(R.id.tnBut2);

        mapButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(MainActivity.this, map3.class);
                startActivity(mapIntent);
            }
        });

        ttButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ttIntent = new Intent(MainActivity.this, timetable3.class);
                startActivity(ttIntent);
            }
        });



        tnButt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ttIntent = new Intent(MainActivity.this, timetable_navigation2.class);
                startActivity(ttIntent);
            }
        });


    }}




