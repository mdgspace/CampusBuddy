package in.co.mdg.campusbuddy;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TelephoneContacts extends AppCompatActivity{
    int size;
    Toolbar toolbar;
    static String[] names,emailids,contactnos_iitr_o, contactnos_iitr_r, contactnos_bsnl;
    CollapsingToolbarLayout ctoolbar;
    RecyclerView recyclerView;
//    double[] contactnos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_telephone_contacts);

            toolbar = (Toolbar) findViewById(R.id.tool_bar);
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



        //noinspection SimplifiableIfStatement
      //  if (id == R.id.action_settings) {
       //     return true;      }


        return super.onOptionsItemSelected(item);
    }
}
