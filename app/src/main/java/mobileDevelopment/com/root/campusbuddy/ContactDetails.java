package mobileDevelopment.com.root.campusbuddy;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;


public class ContactDetails extends ActionBarActivity {
    String contact,emailid;
    Toolbar toolbar;
    int contact1;
    FloatingActionButton fabc,fabe;
    CardView c,c1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_up, R.anim.fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        toolbar = (Toolbar) findViewById(R.id.tool_bar1);
        DayNightTheme.setToolbar(toolbar);
        toolbar.setTitle(getIntent().getExtras().getString("Clicked name"));
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
        c=(CardView)findViewById(R.id.phone);
        c1=(CardView)findViewById(R.id.email);

//        fabc=(FloatingActionButton)findViewById(R.id.fabc);
//
//        fabe=(FloatingActionButton)findViewById(R.id.fabe);
//
//        setfab(fabc);
//        setfab(fabe);
//
//        setupfabc();
//        setupfabe();

        Intent m=getIntent();
        Bundle b=m.getExtras();
        contact=b.getString("Clicked Contact number");
        emailid=b.getString("Clicked email-id");

        TextView ptv=(TextView)findViewById(R.id.phonetext);
        TextView etv=(TextView)findViewById(R.id.emialtext1);
        ptv.setText(contact);
        etv.setText(emailid);
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contact));
                startActivity(intent);
            }
        });

        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setType("plain/text");
//                intent.setData(Uri.parse("www.gmail.com"));
                intent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                intent.putExtra(Intent.EXTRA_EMAIL,new String[]{emailid});
                startActivity(intent);
            }
        });

//        EditText edit_contact=(EditText)findViewById(R.id.contactno);
//        EditText edit_email=(EditText)findViewById(R.id.emailid);
////        Button callbutton=(Button)findViewById(R.id.callbutton);
//        Button emailbutton=(Button)findViewById(R.id.emailbutton);

//        contact1=Integer.parseInt(contact);


        TelephoneContacts tc=new TelephoneContacts();
//        edit_contact.setText(contact+"");
//        edit_contact.setTextColor(Color.parseColor("#000000"));
//        edit_email.setText(emailid+"");
//        edit_email.setTextColor(Color.parseColor("#000000"));

//        callbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contact));
//                startActivity(intent);
//            }
//        });
//
//        emailbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(Intent.ACTION_VIEW);
//                intent.setType("plain/text");
////                intent.setData(Uri.parse("www.gmail.com"));
//                intent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
//                intent.putExtra(Intent.EXTRA_EMAIL,new String[]{emailid});
//                startActivity(intent);
//            }
//        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    public void setupfabc()
//    {
//
//        fabc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Snackbar.make(findViewById(R.id.rootlayout),"Make a call:-",Snackbar.LENGTH_LONG).setAction("Call", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v)
//                    {
//                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contact));
//                        startActivity(intent);
//                    }
//                }).show();
//            }
//        });
//    }
//
//    public void setupfabe()
//    {
//        fabe.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Snackbar.make(findViewById(R.id.rootlayout),"Write a mail:-",Snackbar.LENGTH_LONG).setAction("E-mail", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v)
//                    {
//                        Intent intent=new Intent(Intent.ACTION_VIEW);
//                intent.setType("plain/text");
//                 intent.setData(Uri.parse("www.gmail.com"));
//                intent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
//                intent.putExtra(Intent.EXTRA_EMAIL,new String[]{emailid});
//                startActivity(intent);
//                    }
//                }).show();
//            }
//        });
//    }
//
//    public void setfab(FloatingActionButton fab)
//    {
//        Calendar c = Calendar.getInstance();
//        int hour = c.get(Calendar.HOUR_OF_DAY);
//
//        if (hour>=19 || hour<=6) {
////            fab.setBackgroundTintList(Color.parseColor("#7B1FA2"));
//            fab.setBackgroundTintList(getResources().getColorStateList(R.color.fabcolor));
//        }
//        else{
//            fab.setBackgroundTintList(getResources().getColorStateList(R.color.fabcolorday));
//        }}

    @Override
    public void onDestroy(){
        super.onDestroy();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_out_up);

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_out_up);
        /*
        timetable_navigation2.fa.finish();
        Intent ttIntent = new Intent(NewEvent.this, timetable_navigation2.class);
        startActivity(ttIntent);
        */
        finish();


    }
}
