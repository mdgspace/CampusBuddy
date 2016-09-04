package in.co.mdg.campusBuddy;


import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Fb extends AppCompatActivity {

    Toolbar toolbar;
    ArrayList<String> fbpliked;
    JSONObject m;
    JSONArray n;
    AccessTokenTracker accessTokenTracker;
    int i;
    StaggeredGridView staggeredGridView;
    ArrayList<Post> posts;

    int pageNumber = 0,ongoingpage=0;

    int prelast = 0;
    ProgressBar progressBar;
    FBFeedAdapter adapterfb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb);

        toolbar = (Toolbar) findViewById(R.id.tool_barfb);
        toolbar.setTitle("Facebook posts");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        progressBar=(ProgressBar)findViewById(R.id.progressbar);
        new checkNetwork().execute();
    }


    private class checkNetwork extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            return NetworkCheck.isNetConnected();
        }
        @Override
        protected void onPostExecute(Boolean result)
        {
            if(result)
                processFeeds();
            else
            {
                progressBar.setVisibility(View.INVISIBLE);
                final Snackbar sb = Snackbar.make(findViewById(R.id.main_layout), "No Internet Detected!", Snackbar.LENGTH_INDEFINITE);
                sb.setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new checkNetwork().execute();
                    }
                });
                sb.show();
            }
        }
    }

    private void processFeeds() {

        posts = new ArrayList<>();

        fbpliked = PagesSelected.getSelectedPageIds(Fb.this);
        // Log.e("dta in file",fbpliked.toString());
        if(fbpliked.size()==0)
        {
            Toast.makeText(this,"Select pages to get the feeds",Toast.LENGTH_LONG).show();
            Intent i=new Intent(Fb.this,Fblist.class);
            startActivity(i);
            finish();
        }
        adapterfb = new FBFeedAdapter(this, R.layout.card_viewfb, posts);
        try {
            accessTokenTracker = new AccessTokenTracker() {
                @Override
                protected void onCurrentAccessTokenChanged(
                        AccessToken oldAccessToken,
                        AccessToken currentAccessToken) {

                }
            };
            getUserData(AccessToken.getCurrentAccessToken());

        } catch (Exception e) {
            e.printStackTrace();
        }
        staggeredGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                int lastVisibleItem = firstVisibleItem + visibleItemCount;
                if(lastVisibleItem == totalItemCount){
                    if(prelast != lastVisibleItem){
                        ongoingpage++;
                        fetchData(pageNumber, AccessToken.getCurrentAccessToken());
                        prelast = lastVisibleItem;
                    }
                }
            }
        });
    }

    public void getUserData(final AccessToken accessToken) {

        staggeredGridView = (StaggeredGridView) findViewById(R.id.grid_view);
        staggeredGridView.setAdapter(adapterfb);
        fetchData(0, accessToken);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            accessTokenTracker.stopTracking();
        } catch (Exception e) {
//            Toast.makeText(Fb.this, "error is: "+e.toString(), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fb, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            LoginManager.getInstance().logOut();
            Intent i = new Intent(Fb.this, MainActivity.class);
            finish();
            startActivity(i);
            return true;
        } else if (id == R.id.addpages) {
            Intent i = new Intent(Fb.this, Fblist.class);
            finish();
            startActivity(i);

        }

        return super.onOptionsItemSelected(item);
    }


    public void fetchData(final int page, final AccessToken accessToken) {

        if(ongoingpage==page)
        {

        final ArrayList<GraphRequest> postsArrayList = new ArrayList<>();

                progressBar.setVisibility(View.VISIBLE);
        for (i = 0; i < fbpliked.size(); i++) {
            postsArrayList.add(GraphRequest.newGraphPathRequest(accessToken, "/" + fbpliked.get(i) +
                    "/posts", new GraphRequest.Callback() {
                @Override
                public void onCompleted(GraphResponse graphResponse) {
                    Log.v("Single page", graphResponse.toString());

                    final ArrayList<Post> pageSpecificPosts = new ArrayList<>();

                    try {
                        m = graphResponse.getJSONObject();
                        n = m.getJSONArray("data");

                        try {
                            for (int j = page*5; j < (page+1)*5; j++) {
                                if (n.getJSONObject(j).has("message")) {
                                    pageSpecificPosts.add(new Post(n.getJSONObject(j)));
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        ArrayList<GraphRequest> picsRequestList = new ArrayList<>();

                        for (int i = 0; i < pageSpecificPosts.size(); i++) {
                            final int j = i;
                            picsRequestList.add(GraphRequest.newGraphPathRequest(accessToken, "/" +
                                    pageSpecificPosts.get(i).getPostId() + "/attachments", new GraphRequest.Callback() {
                                @Override
                                public void onCompleted(GraphResponse graphResponse) {

                                    try {
                                        JSONArray data = graphResponse.getJSONObject().getJSONArray("data");
                                        JSONObject jsonObject = data.getJSONObject(0);

                                        if (jsonObject.has("media")) {
                                            String imageUrl = jsonObject.getJSONObject("media").getJSONObject("image").getString("src");

                                            pageSpecificPosts.get(j).setImageUrl(imageUrl);
                                        }

                                        if (jsonObject.has("url")) {
                                            String urlOfLink = jsonObject.getString("url");
                                            pageSpecificPosts.get(j).setLinkUrl(urlOfLink);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }));
                        }

                        GraphRequestBatch picsRequestBatch = new GraphRequestBatch(picsRequestList);
                        picsRequestBatch.addCallback(new GraphRequestBatch.Callback() {
                            @Override
                            public void onBatchCompleted(GraphRequestBatch graphRequestBatch) {
                                posts.addAll(pageSpecificPosts);
                                Collections.sort(posts);
                                Collections.reverse(posts);
                                adapterfb.arrayList = posts;
                                adapterfb.notifyDataSetChanged();
                            }
                        });
                        picsRequestBatch.executeAsync();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }));
        }

        GraphRequestBatch graphRequestBatch = new GraphRequestBatch(postsArrayList);
        graphRequestBatch.addCallback(new GraphRequestBatch.Callback() {
            @Override
            public void onBatchCompleted(GraphRequestBatch graphRequestBatch) {
                //Display
                Log.v("Batch", graphRequestBatch.toString());
                Collections.sort(posts);
                Collections.reverse(posts);
                adapterfb.arrayList = posts;
                adapterfb.notifyDataSetChanged();
                pageNumber++;
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        graphRequestBatch.executeAsync();
    }
    else
        {
            progressBar.setVisibility(View.VISIBLE);
        }
    }
}
