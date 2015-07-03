package mobileDevelopment.com.root.campusbuddy;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.FacebookSdk;

public class MainActivity extends Activity {

    Button mapButt1, mapButt2, tnButt2,tdbtt1, fbbtt1;
    SharedPreferences prefsforfb;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FacebookSdk.sdkInitialize(getApplicationContext());
        prefsforfb=this.getSharedPreferences("com.example.appfb", Context.MODE_PRIVATE);

//        SharedPreferences.Editor editor = prefsforfb.edit();
//        editor.putInt("No of times ", 0);
//        editor.commit();


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
//                count=prefsforfb.getInt("No of times ",0);

                SharedPreferences.Editor editor = prefsforfb.edit();
                editor.putInt("No of times ", count + 1);
                editor.commit();

                count=prefsforfb.getInt("No of times ",0);

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
    }

}




