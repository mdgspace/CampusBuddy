package com.example.root.campusbuddy;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

/**
 * Created by rc on 19/5/15.
 */
public class map3 extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_map);
        try {
            Toast.makeText(map3.this, "Reaced map 3 part1", Toast.LENGTH_LONG).show();
            setContentView(R.layout.activity_map3);
            Toast.makeText(map3.this, "Reaced map 3 part2", Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
            Toast.makeText(map3.this, e.toString(), Toast.LENGTH_LONG).show();
        }


    }
}
