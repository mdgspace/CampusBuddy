package mobileDevelopment.com.root.campusbuddy;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.FacebookSdk;

public class MainActivity extends Activity {

    Button mapButt1, mapButt2, tnButt2,tdbtt1, fbbtt1;
    public static int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FacebookSdk.sdkInitialize(getApplicationContext());

        mapButt1 = (Button) findViewById(R.id.mapBut1);
        tnButt2 = (Button) findViewById(R.id.tnBut2);
        tdbtt1=(Button)findViewById(R.id.tdbtn);
        fbbtt1=(Button)findViewById(R.id.fbbtn);

        mapButt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(MainActivity.this, SimpleMap.class);
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

        tdbtt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tdIntent = new Intent(MainActivity.this,Deptt_list.class);
                startActivity(tdIntent);
            }
        });
        fbbtt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                if(count==1) {
                    Intent tdIntent = new Intent(MainActivity.this, Fblist.class);
                    startActivity(tdIntent);
                }
                else
                {
                    Intent tdIntent = new Intent(MainActivity.this, fb.class);
                    startActivity(tdIntent);
                }
            }
        });
    }}




