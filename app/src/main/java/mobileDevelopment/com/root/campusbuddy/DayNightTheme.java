package mobileDevelopment.com.root.campusbuddy;

import java.util.Calendar;

/**
 * Created by root on 23/7/15.
 */
public class DayNightTheme {

    public static int getThemeId()
    {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        if (hour>19 && hour<6) {
            return R.style.NightTheme;
        }
        else{
            return R.style.AppTheme;
        }
    }
}
