package in.co.mdg.campusBuddy.fcm;

/**
 * Created by mohit on 17/8/16.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat.BigTextStyle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

import in.co.mdg.campusBuddy.Fb;
import in.co.mdg.campusBuddy.R;

/**
 * Created by mohit on 11/8/16.
 */
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    Random r = new Random();
    final int random = r.nextInt(1000 - 1) + 1;
    final SimpleDateFormat df = new SimpleDateFormat("hh:mm a", Locale.getDefault());
    final String currentTime = df.format(Calendar.getInstance().getTime());
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
//            Handler uiHandler = new Handler(Looper.getMainLooper());
//            uiHandler.post(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            });
            showPictureNotification(name, content, img);

        }
    }

    private void showNotification(String title, String message) {
        BigTextStyle bigTextStyle = new BigTextStyle();
        bigTextStyle.setBigContentTitle(title);
        bigTextStyle.bigText(message);
        String shortText = message.length()>100?message.substring(0,100).concat("..."):message;
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.buddy_icon)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(shortText)
                .setContentIntent(pi)
                .setStyle(bigTextStyle)
                .build();
        notification.defaults |= Notification.DEFAULT_LIGHTS; // LED
        notification.defaults |= Notification.DEFAULT_VIBRATE; //Vibration
        notification.defaults |= Notification.DEFAULT_SOUND; // Sound
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(random, notification);
    }

    public void showPictureNotification(final String title, final String message, final String img) {
        final int id = random;
        RemoteViews collapsedView = new RemoteViews(getPackageName(), R.layout.notification_collapsed_layout);
//        String shortText = message.length()>100?message.substring(0,100).concat("..."):message;
        collapsedView.setTextViewText(R.id.title, title);
        collapsedView.setTextViewText(R.id.message, message);
        collapsedView.setTextViewText(R.id.time, currentTime);

        final android.support.v4.app.NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.buddy_icon)
                .setAutoCancel(true)
                .setContentIntent(pi)
                .setCustomContentView(collapsedView);
        Notification notification = builder.build();
        notification.defaults |= Notification.DEFAULT_LIGHTS; // LED
        notification.defaults |= Notification.DEFAULT_VIBRATE; //Vibration
        notification.defaults |= Notification.DEFAULT_SOUND; // Sound
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(id, notification);

        Glide.with(getApplicationContext()).load(img).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                RemoteViews collapsedView = new RemoteViews(getPackageName(), R.layout.notification_collapsed_layout);
                collapsedView.setTextViewText(R.id.title, title);
                collapsedView.setTextViewText(R.id.message, message);
                collapsedView.setTextViewText(R.id.time, currentTime);
                final RemoteViews expandedView = new RemoteViews(getPackageName(), R.layout.notification_expanded_layout);
                expandedView.setTextViewText(R.id.title, title);
                expandedView.setTextViewText(R.id.message, message);
                expandedView.setImageViewBitmap(R.id.post_image, resource);
                builder.setCustomContentView(collapsedView);
                builder.setCustomBigContentView(expandedView);
                Notification notification = builder.build();
                notification.defaults |= Notification.DEFAULT_VIBRATE; //Vibration
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.notify(id, notification);
            }
        });


    }
}
