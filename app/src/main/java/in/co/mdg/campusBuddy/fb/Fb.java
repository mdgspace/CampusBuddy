package in.co.mdg.campusBuddy.fb;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import in.co.mdg.campusBuddy.BuildConfig;
import in.co.mdg.campusBuddy.NetworkCheck;
import in.co.mdg.campusBuddy.R;

public class Fb extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final int PAGE_SELECTED = 1;
    public static Boolean loadImages = true;
    Toolbar toolbar;
    ArrayList<String> fbpliked;
    JSONObject m;
    JSONArray n;

    int pageNumber = 0, ongoingpage = 0;

    int prelast = 0, count = 0;
    FBFeedAdapter adapterfb;
    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private View overlay;
    private ImageButton closeBtn;
    private CheckedTextView checkedTextView;
    private TextView dataPackTV;
    private ConnectivityManager connMgr;
    private boolean isRefreshed = true;

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
        connMgr = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);
        overlay = findViewById(R.id.settings);
        closeBtn = (ImageButton) overlay.findViewById(R.id.close_btn);
        checkedTextView = (CheckedTextView) overlay.findViewById(R.id.enable_images);
        dataPackTV = (TextView) overlay.findViewById(R.id.data_pack_notif_tv);
        mRecyclerView = (RecyclerView) findViewById(R.id.grid_view);
        adapterfb = new FBFeedAdapter(Fb.this);
        mRecyclerView.setAdapter(adapterfb);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.accent), ContextCompat.getColor(this, R.color.primary));
        swipeRefreshLayout.setOnRefreshListener(this);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overlay.setVisibility(View.GONE);
            }
        });
        checkedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CheckedTextView) view).toggle();
                loadImages = ((CheckedTextView) view).isChecked();
                if (loadImages) {
                    adapterfb.notifyDataSetChanged();
                }
            }
        });
        int status = NetworkCheck.chkStatus(connMgr);
        if (status == 2) {//mobile data warning
            overlay.setVisibility(View.VISIBLE);
            loadImages = false;
            checkedTextView.setChecked(false);
            dataPackTV.setVisibility(View.VISIBLE);
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                new checkNetwork().execute();
            }
        }, 100);

    }

    @Override
    public void onRefresh() {
        isRefreshed = true;
        new checkNetwork().execute();
    }


    private class checkNetwork extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return NetworkCheck.isNetConnected();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result)
                processFeeds();
            else {
                swipeRefreshLayout.setRefreshing(false);
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

        fbpliked = PagesSelected.getSelectedPageIds(Fb.this);
        // Log.e("dta in file",fbpliked.toString());
        if (fbpliked.size() == 0) {
            Toast.makeText(this, "Select pages to get the feeds", Toast.LENGTH_LONG).show();
            Intent i = new Intent(Fb.this, Fblist.class);
            i.putExtra("firstTime", true);
            startActivityForResult(i, PAGE_SELECTED);
            return;
        }
        try {
            AccessToken accessToken = new AccessToken(getString(R.string.facebook_app_id) + "|" + BuildConfig.CB_APP_SECRET
                    , getString(R.string.facebook_app_id), getString(R.string.facebook_app_id), null, null, null, null, null);
            AccessToken.setCurrentAccessToken(accessToken);
            getUserData(accessToken);

        } catch (Exception e) {
            e.printStackTrace();
        }
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = ((LinearLayoutManager) mRecyclerView.getLayoutManager());
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                Log.d("last",lastVisibleItem + "");
                if (lastVisibleItem == (adapterfb.getItemCount()-5)) {
                    if (prelast != lastVisibleItem) {
                        prelast = lastVisibleItem;
                        ongoingpage++;
                        fetchData(pageNumber, AccessToken.getCurrentAccessToken());
                    }
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PAGE_SELECTED) {
            if (resultCode == RESULT_OK) {
                isRefreshed = true;
                new checkNetwork().execute();
            } else if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }

    public void getUserData(final AccessToken accessToken) {

        fetchData(0, accessToken);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fb, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addpages) {
            Intent i = new Intent(Fb.this, Fblist.class);
            startActivityForResult(i, PAGE_SELECTED);
        } else if (id == R.id.settings) {
            int status = NetworkCheck.chkStatus(connMgr);
            overlay.setVisibility(View.VISIBLE);
            if (status == 2) {
                dataPackTV.setVisibility(View.VISIBLE);
            } else {
                dataPackTV.setVisibility(View.GONE);
            }
        }
        return super.onOptionsItemSelected(item);
    }


    public void fetchData(final int page, final AccessToken accessToken) {

        if (ongoingpage == page) {

            final ArrayList<GraphRequest> postsArrayList = new ArrayList<>();
            for (int i = 0; i < fbpliked.size(); i++) {
                postsArrayList.add(GraphRequest.newGraphPathRequest(accessToken, "/" + fbpliked.get(i) +
                        "/posts", new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse) {
                        final ArrayList<Post> pageSpecificPosts = new ArrayList<>();
                        try {
                            m = graphResponse.getJSONObject();
                            n = m.getJSONArray("data");

                            try {
                                for (int j = page * 5; j < (page + 1) * 5; j++) {
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
                                            if (data.length() > 0) {
                                                JSONObject jsonObject = data.getJSONObject(0);
                                                if (jsonObject.has("media")) {
                                                    String imageUrl = jsonObject.getJSONObject("media").getJSONObject("image").getString("src");
                                                    pageSpecificPosts.get(j).setImageUrl(imageUrl);
                                                }

                                                if (jsonObject.has("url")) {
                                                    String urlOfLink = jsonObject.getString("url");
                                                    pageSpecificPosts.get(j).setLinkUrl(urlOfLink);
                                                }
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
                                    count++;
                                    if(isRefreshed) {
                                        adapterfb.clear();
                                        isRefreshed = false;
                                    }
                                    adapterfb.addAll(pageSpecificPosts);
                                    Log.d("count",adapterfb.getItemCount()+"");
                                    if (count == (fbpliked.size()-1)) {
                                        count = 0;
                                        swipeRefreshLayout.setRefreshing(false);
                                    }
                                }
                            });
                            if (picsRequestBatch.size() > 0)
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
                    pageNumber++;
                }
            });

            graphRequestBatch.executeAsync();
        } else {
            swipeRefreshLayout.setRefreshing(true);
        }
    }
}
