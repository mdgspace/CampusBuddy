package in.co.mdg.campusBuddy.fb;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;

import java.util.ArrayList;

import in.co.mdg.campusBuddy.ClickListener;
import in.co.mdg.campusBuddy.CustomList;
import in.co.mdg.campusBuddy.Data;
import in.co.mdg.campusBuddy.NewCustomList;
import in.co.mdg.campusBuddy.R;
import in.co.mdg.campusBuddy.SelectedFbPagesAdapter;


public class Fblist extends AppCompatActivity implements ClickListener {


    Cursor cr;
    ContentValues values;
    String page_name;
    String pageId;
    RelativeLayout fbPageLayout;
    ProgressBar progressBar;
    RecyclerView listview;
    RecyclerView selectedFbPagesList;
    ArrayList<String> fbpagesliked;
    String[] fbPageImages;
    String[] fbSelectedPageImages;
    CallbackManager callbackManager;
    Button submitb;
    CustomList adapter;
    NewCustomList fbPageListAdapter;
    SelectedFbPagesAdapter selectedFbPagesAdapter;
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
        fbPageLayout=(RelativeLayout)findViewById(R.id.fb_page_list_layout);
        listofvalues = Data.getFbPageList();
        fbPageImages=new String[listofvalues.size()];
        listview = (RecyclerView) findViewById(R.id.listfbpages);
        progressBar=(ProgressBar)findViewById(R.id.fb_page_list_progress_bar);
        selectedFbPagesList=(RecyclerView)findViewById(R.id.list_selected_fb_pages);
        listview.setVisibility(View.INVISIBLE);
        selectedFbPagesList.setVisibility(View.INVISIBLE);
        final int[] imageUrls = {0};
        for(int i=0;i<listofvalues.size();i++) {
            final int position =i;
            Bundle params = new Bundle();
            params.putBoolean("redirect", false);
            final boolean[] urlReceived = {false};
            String pageId = listofvalues.get(position).getPage_id();
            new GraphRequest(AccessToken.getCurrentAccessToken(), "/" + pageId + "/picture", params, HttpMethod.GET,
                    new GraphRequest.Callback() {
                        @Override
                        public void onCompleted(GraphResponse response) {
                            if (response != null) {
                                try {
                                    fbPageImages[position] = (response.getJSONObject().getJSONObject("data").getString("url"));
                                    imageUrls[0]++;
                                    if( imageUrls[0] == listofvalues.size()){
                                        setListAdapter();
                                    }
                                }catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(Fblist.this,"JSON",Toast.LENGTH_SHORT).show();
                                    Log.e("responseCheck", "Response not received");
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Snackbar snackbar = Snackbar
                                            .make(fbPageLayout, "No Internet Connection!", Snackbar.LENGTH_LONG);

                                    snackbar.show();
                                }
                            } else {
                                Log.e("responseRecCheck", "Response null");
                            }
                        }
                    }).executeAsync();
        }

        fbpagesliked = new ArrayList<String>();
        callbackManager = CallbackManager.Factory.create();
        submitb = (Button) findViewById(R.id.submitbutton);
        try {

            if(getIntent().getBooleanExtra("firstTime",false))
                listofvalues.get(16).setIsSelected(true);
            else {
                cr.moveToFirst();
                if (cr.getCount() > 0) {
                    do {
                        page_name = cr.getString(cr.getColumnIndex(PagesDB.PagesEntry.COLUMN_NAME_Page_name));
                        pageId=cr.getString(cr.getColumnIndex(PagesDB.PagesEntry.COLUMN_NAME_Pages_ID));
                        for (int i = 0; i < listofvalues.size(); i++) {
                            if (page_name.equals(listofvalues.get(i).getPage_name())) {
                                listofvalues.get(i).setIsSelected(true);
                                fbpagesliked.add(pageId);
                            }
                        }
                    } while (cr.moveToNext());
                }
            }
            /*
            int number_liked = 0;
            for (int i = 0; i < listofvalues.size(); i++) {
                String page_id = listofvalues.get(i).getPage_id();
                if (listofvalues.get(i).isSelected()) {
                    number_liked++;
                    fbpagesliked.add(page_id);
                }
            }
            */
            fbSelectedPageImages=new String[fbpagesliked.size()];
            final int[] selectedImageUrls=new int[1];
            for(int i=0;i<fbpagesliked.size();i++) {
                final int position =i;
                Bundle params = new Bundle();
                params.putBoolean("redirect", false);
                final boolean[] urlReceived = {false};
                String pageId = fbpagesliked.get(position);
                new GraphRequest(AccessToken.getCurrentAccessToken(), "/" + pageId + "/picture", params, HttpMethod.GET,
                        new GraphRequest.Callback() {
                            @Override
                            public void onCompleted(GraphResponse response) {
                                if (response != null) {
                                    try {
                                        fbSelectedPageImages[position] = (response.getJSONObject().getJSONObject("data").getString("url"));
                                        selectedImageUrls[0]++;
                                        if( selectedImageUrls[0] == fbpagesliked.size()){
                                            setSelectedListAdapter();
                                        }
                                    }catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(Fblist.this,"JSON",Toast.LENGTH_SHORT).show();
                                        Log.e("responseCheck", "Response not received");
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Snackbar snackbar = Snackbar
                                                .make(fbPageLayout, "No Internet Connection!", Snackbar.LENGTH_LONG);
                                        snackbar.show();
                                    }
                                } else {
                                    Log.e("responseRecCheck", "Response null");
                                }
                            }
                        }).executeAsync();
            }
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
                        setResult(RESULT_OK);
                        finish();
                    }
                }


            });
        } catch (Exception e) {
            Toast.makeText(Fblist.this, e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void setSelectedListAdapter() {
        selectedFbPagesList.setVisibility(View.VISIBLE);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        selectedFbPagesList.setLayoutManager(layoutManager);
        selectedFbPagesList.setItemAnimator(new DefaultItemAnimator());
        selectedFbPagesAdapter = new SelectedFbPagesAdapter(this,fbSelectedPageImages);
        selectedFbPagesList.setAdapter(selectedFbPagesAdapter);
    }

    private void setListAdapter() {
        progressBar.setVisibility(View.INVISIBLE);
        listview.setVisibility(View.VISIBLE);
        selectedFbPagesList.setVisibility(View.VISIBLE);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this);
        listview.setLayoutManager(layoutManager);
        listview.setItemAnimator(new DefaultItemAnimator());
        fbPageListAdapter = new NewCustomList(this,listofvalues,fbPageImages);
        fbPageListAdapter.setClickListener(this);
        listview.setAdapter(fbPageListAdapter);

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

    @Override
    public void itemClicked(View view, int position) {
        CheckBox cb=(CheckBox)view.findViewById(R.id.checkBox);
            cb.setChecked(!cb.isChecked());
    }
}
