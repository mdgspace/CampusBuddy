package mobileDevelopment.com.root.campusbuddy;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;


public class TelephoneContacts extends ActionBarActivity {
    int size;
    Toolbar toolbar;
    String[] names,emailids,contactnos;
//    double[] contactnos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telephone_contacts);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("List of Important Contacts");
        setSupportActionBar(toolbar);

        DatabaseHelper dbh=new DatabaseHelper(this);
        try {
            dbh.createDataBase();

        if(dbh.open())
        {
            List<TelephoneDirectory> contacts=dbh.getContacts();
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
            ListAdapter listAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,names);
            ListView listV=(ListView)findViewById(R.id.listview1);
            listV.setAdapter(listAdapter);


            listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    Intent c=new Intent(TelephoneContacts.this,ContactDetails.class);
                    c.putExtra("Clicked Contact number",contactnos[position]);
                    c.putExtra("Clicked email-id",emailids[position]);
                    startActivity(c);
                }
            });
        }
        }
        catch (Exception  e){
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        }

        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_telephone_contacts, menu);
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
}
