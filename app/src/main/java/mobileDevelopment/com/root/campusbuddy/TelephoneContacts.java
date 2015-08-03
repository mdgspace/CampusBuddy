package mobileDevelopment.com.root.campusbuddy;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v7.widget.RecyclerView;
/*
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;
*/

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class TelephoneContacts extends AppCompatActivity{
    int size;
    Toolbar toolbar;
    static String[] names,emailids,contactnos;
    CollapsingToolbarLayout ctoolbar;
    RecyclerView recyclerView;
//    double[] contactnos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_telephone_contacts);

            toolbar = (Toolbar) findViewById(R.id.tool_bar);
            DayNightTheme.setToolbar(toolbar);
//        ctoolbar=(CollapsingToolbarLayout)findViewById(R.id.collapsingtoolbar);
            toolbar.setTitle(getIntent().getExtras().getString("dept_name"));
            toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            // toolbar.setSubtitle("Sub");
           // toolbar.setLogo(R.drawable.ic_launcher);

//        ctoolbar.setContentScrimColor(Color.parseColor("#aa00bb"));


        }
        catch(Exception e)
        {
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        }

try{

        DatabaseHelper dbh=new DatabaseHelper(this);

            dbh.createDataBase();

        if(dbh.open())
        {
            List<TelephoneDirectory> contacts=dbh.getContacts(getIntent().getExtras().getString("table_name"));
            size=DatabaseHelper.i;
             names=new String[size];
             contactnos=new String[size];
             emailids=new String[size];

            int i=0;
            for(TelephoneDirectory td:contacts)
            {
                names[i]=td.name;
                contactnos[i]=td.contact;
                emailids[i]=td.emailid;
                i++;
            }
            recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(new MyRecyclerAdapter_departmentcontacts(generateContacts()));

            recyclerView.setOnScrollListener(new MyScrollListener(this) {
                @Override
                public void onMoved(int distance){
                  //  toolbar.setTranslationY(-distance);
                }
            });

//            recyclerView.addOnItemTouchListener(new MyItemClickListener(this, new MyItemClickListener.OnItemClickListener() {
//                @Override
//                public void OnItemClick(View v, int i, MotionEvent e) {
//                    final int i1=i;
//                /*
//                    SpringSystem springSystem = SpringSystem.create();
//                    //Add a spring to the system.
//                    final Spring spring = springSystem.createSpring();
//                    final View v1=v;
//                    final int i1=i;
//                    if(e.getAction()== MotionEvent.ACTION_DOWN)
//                    {
//                        spring.addListener(new SimpleSpringListener() {
//
//                            @Override
//                            public void onSpringUpdate(Spring spring) {
//                                // You can observe the updates in the spring
//                                // state by asking its current value in onSpringUpdate.
//                                float value = (float) spring.getCurrentValue();
//                                float scale = 1f - (value * 0.1f);
//                                v1.setScaleX(scale);
//                                v1.setScaleY(scale);                    }
//                        });
//
//                        //Set the spring in motion; moving from 0 to 1
//                        spring.setEndValue(5);
//                    }
//                    else
//                    if(e.getAction()== MotionEvent.ACTION_UP)
//                    {
//                        spring.addListener(new SimpleSpringListener() {
//
//                            @Override
//                            public void onSpringUpdate(Spring spring) {
//                                // You can observe the updates in the spring
//                                // state by asking its current value in onSpringUpdate.
//                                float value = (float) spring.getCurrentValue();
//                                float scale = (value * 0.2f);
//                                v1.setScaleX(scale);
//                                v1.setScaleY(scale);                    }
//                        });
//
//                        //Set the spring in motion; moving from 0 to 1
//                        spring.setEndValue(5);
//                    }
//        */
//                    Handler handler=new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            Intent c = new Intent(TelephoneContacts.this, ContactDetails.class);
//                            c.putExtra("Clicked Contact number", contactnos[i1]);
//                            c.putExtra("Clicked email-id", emailids[i1]);
//                            c.putExtra("Clicked name", names[i1]);
//                            startActivity(c);
//                        }
//                    },500);
//
//
//                }
//            }));


//
//
//            listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Intent c = new Intent(TelephoneContacts.this, ContactDetails.class);
//                    c.putExtra("Clicked Contact number", contactnos[position]);
//                    c.putExtra("Clicked email-id", emailids[position]);
//                    startActivity(c);
//                }
//            });
//            listV.setOnScrollListener(new ListView.OnScrollListener() {
//
//                boolean hidetoolbar=false;
//
//                @Override
//                public void onScrollStateChanged(AbsListView view, int scrollState) {
//                    if(hidetoolbar)
//                    {
//                        getSupportActionBar().hide();
//                    }
//                    else
//                    {
//                        getSupportActionBar().show();
//                    }
//                }
//
//                @Override
//                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                    if(visibleItemCount==9)
//                    {
//                        hidetoolbar=true;
//                    }
//                    else
//                    {
//                        hidetoolbar=false;
//                    }
//                }
//            });

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


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_telephone_contacts, menu);
        return true;
    }
    */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
      //  if (id == R.id.action_settings) {
       //     return true;      }


        return super.onOptionsItemSelected(item);
    }
}
