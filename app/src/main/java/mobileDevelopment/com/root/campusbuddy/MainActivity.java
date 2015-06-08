package mobileDevelopment.com.root.campusbuddy;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    Button mapButt, ttButt, tnButt2,tdbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapButt = (Button) findViewById(R.id.mapBut);

        tnButt2 = (Button) findViewById(R.id.tnBut2);

        tdbtn = (Button) findViewById(R.id.tdbtn);

        mapButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(MainActivity.this, map3.class);
                startActivity(mapIntent);
            }
        });



        tnButt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ttIntent = new Intent(MainActivity.this, timetable_navigation2.class);
                startActivity(ttIntent);
            }
        });

        tdbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tdIntent = new Intent(MainActivity.this, TelephoneContacts.class);
                startActivity(tdIntent);
            }
        });


    }}



