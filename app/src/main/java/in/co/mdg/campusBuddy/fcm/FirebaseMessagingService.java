package in.co.mdg.campusBuddy.fcm;

/**
 * Created by mohit on 17/8/16.
 */
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;

import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import in.co.mdg.campusBuddy.Fb;
import in.co.mdg.campusBuddy.MainActivity;
import in.co.mdg.campusBuddy.R;

/**
 * Created by mohit on 11/8/16.
 */
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        Log.d("FCM","Message Received");
        final String img = remoteMessage.getData().get("img");
        final String name = remoteMessage.getData().get("name");
        final String content = remoteMessage.getData().get("content");
        if(img.equals(""))
        {
            showNotification(name,content);
        }
        else
        {
            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                               @Override
                               public void run() {
                                   Picasso.with(getApplicationContext()).load(img).into(new Target() {
                                       @Override
                                       public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                           showPictureNotification(name,content,bitmap);
                                       }

                                       @Override
                                       public void onBitmapFailed(Drawable errorDrawable) {
                                           showNotification(name,content);
                                       }

                                       @Override
                                       public void onPrepareLoad(Drawable placeHolderDrawable) {

                                       }
                                   });
                               }
            });

        }
    }

    private void showNotification(String title,String message) {
        Intent i = new Intent(this,Fb.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.buddy_icon)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0,builder.build());

    }
    @SuppressWarnings("deprecation")
    public void showPictureNotification(String title,String message,Bitmap img) {
        Intent i = new Intent(this,Fb.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle(title)
                .setAutoCancel(true)
                .setContentText(message)
                .setSmallIcon(R.drawable.buddy_icon)
//                .addAction(R.drawable.ic_launcher_web, "show activity", pi)
//                .addAction(
//                        R.drawable.ic_launcher_share,
//                        "Share",
//                        PendingIntent.getActivity(getApplicationContext(), 0,
//                                getIntent(), 0, null))
 ;

        Notification notification = new Notification.BigPictureStyle(builder).bigPicture(img).setSummaryText(message).build();
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, notification);
    }
}
