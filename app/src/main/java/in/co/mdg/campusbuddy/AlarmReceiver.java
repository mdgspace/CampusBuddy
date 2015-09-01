package in.co.mdg.campusbuddy;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by root on 25/7/15.
 */
public class AlarmReceiver extends BroadcastReceiver {

    Intent in;
    PendingIntent pendingIntent;
    Notification mBuilder;

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
        // Acquire the lock
        wl.acquire();

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour=c.get(Calendar.HOUR_OF_DAY);
        int min=c.get(Calendar.MINUTE);
        StringBuilder s = new StringBuilder().append(month + 1).append("-")
                .append(day).append("-").append(year).append(" ");

       CalendarDBHelper dbh;
        try {
            dbh = CalendarDBHelper.getInstance(context);

            ArrayList<HashMap<String, Integer>> data =
                    dbh.fetchNotificationData(year,month,day,hour,min);

            if (data != null) {

                for (int i = 0; i < data.size(); i++) {
					/*Toast.makeText(context, i + "", Toast.LENGTH_LONG).show();*/
                    in = new Intent(context, timetable_navigation2.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("year", data.get(i).get("Start year"));


                    Log.e("hour in the receiver", data.get(i).get("Start hour")+"");

                    in.putExtras(bundle);
//                    in.putExtra("id", Integer.parseInt(data.get(i).get("id")));
                    in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    pendingIntent = PendingIntent
                            .getActivity(context, 0, in, PendingIntent.FLAG_UPDATE_CURRENT);

                    mBuilder = new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.buddy_icon)
                            .setContentTitle("You have an event in another one hour")
//                            .setContentText(data.get(i).get("balance"))
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true).build();

                    NotificationManager mNotificationManager = (NotificationManager) context
                            .getSystemService(Context.NOTIFICATION_SERVICE);

                    mNotificationManager.notify(i, mBuilder);

                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Release the lock
        wl.release();
    }
}
