package in.co.mdg.campusBuddy;

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
import java.util.Date;
import java.util.HashMap;

import in.co.mdg.campusBuddy.calendar.data_models.user_events.UserEvent;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by root on 25/7/15.
 */
public class AlarmReceiver extends BroadcastReceiver {

    Intent in;
    private PendingIntent pendingIntent;
    private Notification mBuilder;
    private Realm realm;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("alarm","start");
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
        // Acquire the lock
        wl.acquire();
        int i=1;
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Calendar c = Calendar.getInstance();
        Date now = c.getTime();
        c.add(Calendar.HOUR_OF_DAY,1);
        Date anHourLater = c.getTime();
        realm = Realm.getDefaultInstance();
        RealmResults<UserEvent> userEvents = realm.where(UserEvent.class).greaterThan("time.start",now).lessThan("time.start",anHourLater).findAll();
        for(UserEvent userEvent:userEvents)
        {

            in = new Intent(context, TimetableNavigation.class);
            Bundle bundle = new Bundle();
            bundle.putInt("year", c.get(Calendar.YEAR));
            in.putExtras(bundle);
            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            pendingIntent = PendingIntent
                    .getActivity(context, 0, in, PendingIntent.FLAG_UPDATE_CURRENT);

            mBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("You have an event in another one hour")
                    .setContentText(userEvent.getTitle())
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true).build();

            mNotificationManager.notify(i++, mBuilder);
        }

        // Release the lock
        wl.release();
    }
}
