package mobileDevelopment.com.root.campusbuddy;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;

import java.util.Calendar;

/**
 * Created by root on 23/7/15.
 */
public class DayNightTheme{

    public static void setToolbar(Toolbar toolbar)
    {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        if (hour>=19 || hour<=6) {
            toolbar.setBackgroundColor(Color.parseColor("#7B1FA2"));
        }
        else{
            toolbar.setBackgroundColor(Color.parseColor("#ED1C24"));        }
    }
    }

