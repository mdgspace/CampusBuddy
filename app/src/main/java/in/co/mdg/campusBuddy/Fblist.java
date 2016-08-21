package in.co.mdg.campusBuddy;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;


public class Fblist extends AppCompatActivity {


    Cursor cr;
    ContentValues values;
    String page_name;
    ListView listview;
    String[] fbpages;
    ArrayList<String> fbpagesliked;
    public ArrayList<Page> pageList;
    CallbackManager callbackManager;
    Button submitb;
    int i;
    public static boolean flag = true;
    Toolbar toolbar;
    public ArrayList<Page> listofvalues;

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
        flag = false;
        listofvalues = Data.getFbPageList();


        fbpagesliked = new ArrayList<String>();
        callbackManager = CallbackManager.Factory.create();
        submitb = (Button) findViewById(R.id.submitbutton);
        try {

            listview = (ListView) findViewById(R.id.listfbpages);
            fbpages = getResources().getStringArray(R.array.fbpages);
            pageList = new ArrayList<Page>();
            for (int i = 0; i < fbpages.length; i++) {
                Page page = new Page(fbpages[i]);
                page.page_id = listofvalues.get(i).getPage_id();
                pageList.add(page);
            }


            listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            cr.moveToFirst();
            if (cr.getCount() > 0) {
                do {
                    page_name = cr.getString(cr.getColumnIndex(PagesDB.PagesEntry.COLUMN_NAME_Page_name));
                    for (int i = 0; i < fbpages.length; i++) {
                        if (page_name.equals(pageList.get(i).getPage_name())) {
                            pageList.get(i).setIsSelected(true);
                        }
                    }
                } while (cr.moveToNext());
            }
            CustomList adapter = new CustomList(Fblist.this, pageList);
            listview.setAdapter(adapter);


            submitb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cr.moveToFirst();
                    if (cr.getCount() > 0) {
                        do {
                            String page_id = cr.getString(cr.getColumnIndex(PagesDB.PagesEntry.COLUMN_NAME_Pages_ID));
                            FirebaseMessaging.getInstance().unsubscribeFromTopic(page_id);
                            Log.d("pageID",page_id);
                        } while (cr.moveToNext()) ;
                    }
                    db.delete(PagesDB.PagesEntry.TABLE_NAME, null, null);
                    int number_liked = 0;

                    for (int i = 0; i < fbpages.length; i++) {
                        if (pageList.get(i).isSelected()) {
                            number_liked++;
                            fbpagesliked.add(listofvalues.get(i).getPage_id());
                            values.put(PagesDB.PagesEntry.COLUMN_NAME_Pages_ID, listofvalues.get(i).getPage_id());
                            values.put(PagesDB.PagesEntry.COLUMN_NAME_Page_name, pageList.get(i).getPage_name());
                            db.insert(
                                    PagesDB.PagesEntry.TABLE_NAME,
                                    null,
                                    values);
                        }
                    }
                    PagesSelected.writeSelectedPageIds(Fblist.this, fbpagesliked);
                    if (fbpagesliked == null || number_liked == 0) {
                        Toast.makeText(Fblist.this, "Please Select at least one page to get the feeds", Toast.LENGTH_LONG).show();
                        onResume();
                    } else {
                        Intent intent = new Intent(Fblist.this, Fb.class);
                        finish();
                        startActivity(intent);
                    }

                }


            });
        } catch (Exception e) {
            Toast.makeText(Fblist.this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
