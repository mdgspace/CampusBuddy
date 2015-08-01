package mobileDevelopment.com.root.campusbuddy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by rc on 15/5/15.
 */
public class DetailsActivity extends Activity {

    TextView tv ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        tv = (TextView) findViewById(R.id.details_textview_activity);
        Intent detailsIntent = getIntent();
        Bundle extras = detailsIntent.getExtras();
        String details = extras.getString("details");
        String venue = extras.getString("venue");
        String date = extras.getString("date");
        String start = extras.getString("start");
        String end = extras.getString("end");
        String title = extras.getString("title");
        String type = extras.getString("type");

        tv.setText(title + "\n"+details + "\nDate: "+ date + "\nTime: "  + start + " - " + end + "\nVenue: " + venue + "\nType of Event: " + type);


    }
}
