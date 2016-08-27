package in.co.mdg.campusBuddy;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.AccessToken;

import in.co.mdg.campusBuddy.contacts.ContactsMainActivity;

public class MainActivity extends AppCompatActivity {

    ImageButton mapButt1, tnButt2, tdbtt1, fbbtt1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.disclaimer, null);
            dialogBuilder.setView(dialogView);
            dialogBuilder.setTitle("Disclaimer");
            dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            TextView tv_dis = (TextView) dialogView.findViewById(R.id.disclaimera);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                tv_dis.setText(Html.fromHtml(getString(R.string.disclaimer_text), Html.FROM_HTML_MODE_LEGACY));
            else
                tv_dis.setText(Html.fromHtml(getString(R.string.disclaimer_text)));
            tv_dis.setMovementMethod(LinkMovementMethod.getInstance());

            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();

            // mark first time app run.
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }


        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = getPrefs.edit();

        boolean isSet = getPrefs.getBoolean("alarm_set", false);
        if (!isSet) {
            NotificationHandler.buildNotification(this);
            editor.putBoolean("alarm_set", true);
            editor.commit();
        }
        mapButt1 = (ImageButton) findViewById(R.id.mapBut1);
        tnButt2 = (ImageButton) findViewById(R.id.tnBut2);
        tdbtt1 = (ImageButton) findViewById(R.id.tdbtn);
        fbbtt1 = (ImageButton) findViewById(R.id.fbbtn);

        mapButt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(MainActivity.this, SimpleMap.class);
                startActivity(mapIntent);
            }
        });

        tnButt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ttIntent = new Intent(MainActivity.this, TimetableNavigation.class);
                startActivity(ttIntent);
            }
        });

        tdbtt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tdIntent = new Intent(MainActivity.this, ContactsMainActivity.class);
                startActivity(tdIntent);
            }
        });
        fbbtt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (AccessToken.getCurrentAccessToken() == null) {
                    Intent tdIntent = new Intent(MainActivity.this, Fblogin.class);
                    startActivity(tdIntent);
                } else {
                    Intent tdIntent = new Intent(MainActivity.this, Fb.class);
                    startActivity(tdIntent);

                }
            }
        });
    }
}




