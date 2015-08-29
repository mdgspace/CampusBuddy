package mobileDevelopment.com.root.campusbuddy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by rc on 15/5/15.
 */
public class DetailsActivity extends Activity {

    TextView tv_time, tv_date, tv_venue, tv_title, tv_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        tv_venue = (TextView) findViewById(R.id.details_Venue);
        tv_date = (TextView) findViewById(R.id.details_date);
        tv_title = (TextView) findViewById(R.id.details_title);
        tv_time = (TextView) findViewById(R.id.details_time);
        tv_details = (TextView) findViewById(R.id.details_details);
        Intent detailsIntent = getIntent();
        Bundle extras = detailsIntent.getExtras();
        String details = extras.getString("details").trim();
        String venue = extras.getString("venue").trim();
        String date = extras.getString("date");
        String start = extras.getString("start");
        String end = extras.getString("end");
        String title = extras.getString("title");
        String type = extras.getString("type");

//        String message = "Date: "+ date + "\nTime: "  + start + " - " + end;
//
//        if(venue!=null && !venue.isEmpty()) message = message + "\nVenue: " + venue;
//        if(details!=null && !details.isEmpty()) message = "Details: " + details +  message + "\n";
//         message = "Title: " + title + message + "\n";

        tv_title.setText("Title: " + title);

        tv_time.setText("Time: " + start + " - " + end);
        tv_date.setText("Date: " + date);


        if(!venue.isEmpty()){

            tv_venue.setVisibility(View.VISIBLE);
            tv_venue.setText("Venue: " + venue);}
        if(!details.isEmpty()){
            tv_details.setVisibility(View.VISIBLE);
            tv_details.setText("Details: " + details);}

    }
}
