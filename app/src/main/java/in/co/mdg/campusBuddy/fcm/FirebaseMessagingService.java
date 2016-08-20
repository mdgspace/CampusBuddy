package in.co.mdg.campusBuddy.fcm;

/**
 * Created by mohit on 17/8/16.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat.BigTextStyle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Date;

import in.co.mdg.campusBuddy.Fb;
import in.co.mdg.campusBuddy.R;

/**
 * Created by mohit on 11/8/16.
 */
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    final int random = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
    PendingIntent pi;

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        Log.d("FCM", "Message Received");
        final String img = remoteMessage.getData().get("img");
        final String name = remoteMessage.getData().get("name");
        final String content = remoteMessage.getData().get("content");
        Intent resultIntent = new Intent(this, Fb.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(Fb.class);
        stackBuilder.addNextIntent(resultIntent);
        pi = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        if (img.equals("")) {
            showNotification(name, content);
        } else {
            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    Picasso.with(getApplicationContext()).load(img).into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            showPictureNotification(name, content, bitmap);
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                            showNotification(name, content);
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                        }
                    });
                }
            });
        }
    }

    private void showNotification(String title, String message) {
        BigTextStyle bigTextStyle = new BigTextStyle();
        bigTextStyle.setBigContentTitle(title);
        bigTextStyle.bigText(message);
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.buddy_icon)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pi)
                .setStyle(bigTextStyle)
                .build();
        notification.defaults |= Notification.DEFAULT_LIGHTS; // LED
        notification.defaults |= Notification.DEFAULT_VIBRATE; //Vibration
        notification.defaults |= Notification.DEFAULT_SOUND; // Sound
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(random, notification);
    }

    public void showPictureNotification(String title, String message, Bitmap img) {

        RemoteViews collapsedView = new RemoteViews(getPackageName(), R.layout.notification_collapsed_layout);
        collapsedView.setTextViewText(R.id.title, title);
        collapsedView.setTextViewText(R.id.message, message);

        RemoteViews expandedView = new RemoteViews(getPackageName(), R.layout.notification_expanded_layout);
        expandedView.setTextViewText(R.id.title, title);
        expandedView.setTextViewText(R.id.message, message);
        expandedView.setImageViewBitmap(R.id.post_image, img);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.buddy_icon)
                .setAutoCancel(true)
                .setContentIntent(pi)
                .setContent(collapsedView)
                .setCustomBigContentView(expandedView)
                .build();

        notification.defaults |= Notification.DEFAULT_LIGHTS; // LED
        notification.defaults |= Notification.DEFAULT_VIBRATE; //Vibration
        notification.defaults |= Notification.DEFAULT_SOUND; // Sound
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(random, notification);
    }
}
