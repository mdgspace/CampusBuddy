package in.co.mdg.campusBuddy.fb;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

import in.co.mdg.campusBuddy.CustomList;
import in.co.mdg.campusBuddy.Data;
import in.co.mdg.campusBuddy.R;


public class Fblist extends AppCompatActivity {


    Cursor cr;
    ContentValues values;
    String page_name;
    ListView listview;
    ArrayList<String> fbpagesliked;
    CallbackManager callbackManager;
    Button submitb;
    CustomList adapter;
    int i;
    Toolbar toolbar;
    private ArrayList<Page> listofvalues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fblist);

        final SQLiteDatabase db = PagesDBHelper.getInstance(getApplicationContext()).getWritableDatabase();


        String[] eventList = {
                PagesDB.PagesEntry.COLUMN_NAME_Pages_ID,
                PagesDB.PagesEntry.COLUMN_NAME_Page_name
        };

        try {
            cr = db.query(PagesDB.PagesEntry.TABLE_NAME, eventList, null, null, null, null, null);
        } catch (Exception err) {
            Toast.makeText(Fblist.this, err.toString(), Toast.LENGTH_LONG).show();
        }
        values = new ContentValues();


        toolbar = (Toolbar) findViewById(R.id.tool_barfblist);
        toolbar.setTitle("Facebook page list");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//        flag = false;
        listofvalues = Data.getFbPageList();


        fbpagesliked = new ArrayList<>();
        callbackManager = CallbackManager.Factory.create();
        submitb = (Button) findViewById(R.id.submitbutton);
        try {

            listview = (ListView) findViewById(R.id.listfbpages);
            listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            cr.moveToFirst();
            if (cr.getCount() > 0) {
                do {
                    page_name = cr.getString(cr.getColumnIndex(PagesDB.PagesEntry.COLUMN_NAME_Page_name));
                    for (int i = 0; i < listofvalues.size(); i++) {
                        if (page_name.equals(listofvalues.get(i).getPage_name())) {
                            listofvalues.get(i).setIsSelected(true);
                        }
                    }
                } while (cr.moveToNext());
            }
            adapter = new CustomList(Fblist.this, listofvalues);
            listview.setAdapter(adapter);


            submitb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.delete(PagesDB.PagesEntry.TABLE_NAME, null, null);
                    int number_liked = 0;
                    for (int i = 0; i < listofvalues.size(); i++) {
                        String page_id = listofvalues.get(i).getPage_id();
                        if (listofvalues.get(i).isSelected()) {
                            number_liked++;
                            fbpagesliked.add(page_id);
                            values.put(PagesDB.PagesEntry.COLUMN_NAME_Pages_ID, page_id);
                            values.put(PagesDB.PagesEntry.COLUMN_NAME_Page_name, listofvalues.get(i).getPage_name());
                            db.insert(
                                    PagesDB.PagesEntry.TABLE_NAME,
                                    null,
                                    values);
                        } else {
                            FirebaseMessaging.getInstance().unsubscribeFromTopic(page_id);
                            Log.d("pageID", page_id);
                        }
                    }
                    PagesSelected.writeSelectedPageIds(Fblist.this, fbpagesliked);
                    if (fbpagesliked == null || number_liked == 0) {
                        Toast.makeText(Fblist.this, "Please Select at least one page to get the feeds", Toast.LENGTH_LONG).show();
                        onResume();
                    } else {
                        finish();
                    }
                }


            });
        } catch (Exception e) {
            Toast.makeText(Fblist.this, e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fblist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (adapter != null) {


            switch (id) {
                case R.id.action_select_all:
                    for (int i = 0; i < adapter.getCount(); i++)
                        adapter.getItem(i).setIsSelected(true);
                    adapter.notifyDataSetChanged();
                    return true;
                case R.id.action_select_none:
                    for (int i = 0; i < adapter.getCount(); i++)
                        adapter.getItem(i).setIsSelected(false);
                    adapter.notifyDataSetChanged();
                    return true;
                case R.id.action_select_invert:
                    for (int i = 0; i < adapter.getCount(); i++)
                        adapter.getItem(i).setIsSelected(!adapter.getItem(i).isSelected());
                    adapter.notifyDataSetChanged();
                    return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
