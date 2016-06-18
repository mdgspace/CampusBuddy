package in.co.mdg.campusBuddy;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TelephoneContacts extends AppCompatActivity{
    int size;
//    Toolbar toolbar;
    static String[] names,emailids,contactnos_iitr_o, contactnos_iitr_r, contactnos_bsnl;
    static String campus_location="roorkee";
    CollapsingToolbarLayout ctoolbar;
    RecyclerView recyclerView;
    static Context c;
//    double[] contactnos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_telephone_contacts);
            c=this;
//            toolbar = (Toolbar) findViewById(R.id.tool_bar);
//            toolbar.setTitle(getIntent().getExtras().getString("dept_name"));
//            toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
//            setSupportActionBar(toolbar);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onBackPressed();
//                }
//            });

        }
        catch(Exception e)
        {
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        }

try{

        DatabaseHelper dbh=new DatabaseHelper(this);

            dbh.createDataBase();

        if(dbh.open())
        {if(getIntent().getExtras().getString("table_name").equals("POLY")){
campus_location = "saharanpur";
        }
            else campus_location = "roorkee";
            List<TelephoneDirectory> contacts=dbh.getContacts(getIntent().getExtras().getString("table_name"));
            size=DatabaseHelper.i;
             names=new String[size];
            contactnos_iitr_o=new String[size];
            contactnos_iitr_r=new String[size];
            contactnos_bsnl=new String[size];
             emailids=new String[size];

            int i=0;
            for(TelephoneDirectory td:contacts)
            {
                names[i]=td.name;
                contactnos_bsnl[i]=td.contact_bsnl;
                contactnos_iitr_o[i]=td.contact_iitr_o;
                contactnos_iitr_r[i]=td.contact_iitr_r;
                emailids[i]=td.emailid;
                i++;
            }
            recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(new MyRecyclerAdapter_departmentcontacts(generateContacts(), this));

            recyclerView.setOnScrollListener(new MyScrollListener(this) {
                @Override
                public void onMoved(int distance){
                  //  toolbar.setTranslationY(-distance);
                }
            });
        }
        }
        catch (Exception  e){
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        }

        }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
       // Toast.makeText(this, "Back button works", Toast.LENGTH_LONG).show();
    }

    public ArrayList<Contact> generateContacts()
    {
        ArrayList<Contact> contacts=new ArrayList<>();
        for(int i=0;i<size;i++)
        {
            if(names[i]!=null)
            contacts.add(new Contact(names[i]));

        }
        return contacts;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id==R.id.disclaimer)
        { AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
// ...Irrelevant code for customizing the buttons and title
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.disclaimer, null);
            dialogBuilder.setView(dialogView);
            dialogBuilder.setTitle("Disclamer");
            dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            TextView tv_dis = (TextView) dialogView.findViewById(R.id.disclaimera);
            TextView tv_dis1 = (TextView) dialogView.findViewById(R.id.disclaimera1);
            TextView tv_dis2 = (TextView) dialogView.findViewById(R.id.disclaimera2);
            tv_dis.setText("This is an test app made by a student's group and we don't take " +
                    "any responsibility for any information present in the app.\n" +
                    " However, we welcome any feedback, which can be mailed to us at: sdsmobilelabs@gmail.com\n"+
                    "Data Sources: \n");
//            tv_dis1.setText(
//                    Html.fromHtml(
//                            "<a href=\"http://www.google.com\" color: white>Academic Calendar</a> "));
//            tv_dis1.setMovementMethod(LinkMovementMethod.getInstance());

            tv_dis1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browser = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.iitr.ac.in/academics/pages/Academic_Calender.html"));
                    startActivity(browser);
                }
            });
            tv_dis2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browser = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.iitr.ac.in/Main/pages/Telephone+Telephone_Directory.html"));
                    startActivity(browser);
                }
            });

            tv_dis1.setText("Academic Calendar");
            tv_dis2.setText("Telephone Directory");
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();}

        if (id==R.id.about_us_menu) {

            Intent i=new Intent(TelephoneContacts.this,AboutUs.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_telephone_contacts, menu);
        return true;
    }
}
