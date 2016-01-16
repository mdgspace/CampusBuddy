package in.co.mdg.campusBuddy;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Akshay
 * @version 1.0.0
 * @since 24-Aug-15
 */
public class DateFormatter {

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static String formatDate(String dateText) {
        if (dateText != null) {
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = null;
            try {
                date = originalFormat.parse(dateText);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return targetFormat.format(date);
        } else {
            return null;
        }

    }

    public static String getTimeAgo(String timestamp) {

        long time = formatTimestamp(timestamp);
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
//        if (time > now || time <= 0) {
//            return null;
//        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "Just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "A minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "An hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "Yesterday";
        } else {
           /* if (diff / DAY_MILLIS >= 7) {
                return getDate(timestamp) + "," + getDayHour(timestamp);
            } else {*/
            return diff / DAY_MILLIS + " days ago";
            //}
        }

    }

    public static long formatTimestamp(String dateText) {
        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        Date date = null;
        try {
            date = originalFormat.parse(dateText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static String getDate(String dateText) {

        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        DateFormat dateValue = new SimpleDateFormat("d MMM, yyyy");
        Date date = null;
        try {
            date = originalFormat.parse(dateText);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateValue.format(date);
    }

    public static String getTime(String dateText) {

        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        DateFormat timeValue = new SimpleDateFormat("HH:mm");
        Date date = null;
        try {
            date = originalFormat.parse(dateText);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeValue.format(date);
    }

    public static String getDayHour(String timestamp) {
        String time = DateFormatter.getTime(timestamp);
        String time1 = time.substring(2, 5);

        if (Integer.parseInt(time.substring(0, 2)) == 12) {
            return time + " pm ";
        } else if (Integer.parseInt(time.substring(0, 2)) > 12) {
            return String.valueOf(Integer.parseInt(time.substring(0, 2)) - 12) + time1 + " pm ";
        } else {
            return time + " am ";
        }
    }
}
