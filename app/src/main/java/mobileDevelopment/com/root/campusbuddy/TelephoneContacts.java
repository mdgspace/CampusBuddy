package mobileDevelopment.com.root.campusbuddy;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;


public class TelephoneContacts extends ActionBarActivity {
    int size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telephone_contacts);
        DatabaseHelper dbh=new DatabaseHelper(this);
        try {
            dbh.createDataBase();

        if(dbh.open())
        {
            List<TelephoneDirectory> contacts=dbh.getContacts();
            size=DatabaseHelper.i;
            String[] contact_data=new String[size];
            int i=0;
            for(TelephoneDirectory td:contacts)
            {
                contact_data[i]=td.name;
                i++;
            }
            ListAdapter listAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,contact_data);
            ListView listV=(ListView)findViewById(R.id.listview1);
            listV.setAdapter(listAdapter);
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
