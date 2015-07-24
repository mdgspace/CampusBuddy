package mobileDevelopment.com.root.campusbuddy;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class fb extends AppCompatActivity{

    String[] ids;
    Toolbar toolbar;
    boolean[] fbpl;
    ArrayList<String> fbpliked;
    JSONObject m;
    JSONArray n;
    AccessTokenTracker accessTokenTracker;
//    ListView list;
     int i;
    static boolean[] fbpls;
    RecyclerView recyclerView;
    ArrayList<Post> posts;
//    FloatingActionButton fabfbu;
    public static Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb);

        c=this;
//        fabfbu=(FloatingActionButton)findViewById(R.id.fabfb);
//        list=(ListView)findViewById(R.id.listfb);
        toolbar = (Toolbar) findViewById(R.id.tool_barfb);
        DayNightTheme.setToolbar(toolbar);
//        ctoolbar=(CollapsingToolbarLayout)findViewById(R.id.collapsingtoolbar);
        toolbar.setTitle("Facebook posts");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        try{
////            Bundle b=getIntent().getExtras();
////            fbpl=b.getBooleanArray("pagesliked");
//        }
//        catch(Exception e)
//        {
//            fbpl=fbpls;
//        }
//        for(i=0;i<fbpl.length;i++)
//        {
//            if(fbpl[i]==true)
//                count++;
//        }
//
//        if(fbpl==null || count==0)
//        {
//            Toast.makeText(this,"Choose atleast one of the pages to get feeds",Toast.LENGTH_LONG).show();
//            Intent i=new Intent(fb.this,Fblist.class);
//            startActivity(i);
//        }

        ids=new String[21];
        ids[12]= "415004402015833"; // IIT Roorkee
        ids[1]="415004402015833";//id for iit r freshers
        ids[2]="182484805131346";//id for SDSLabs
        ids[3]="257702554250168";//id for robocon
        ids[4]="265096099170"; // EDC
        ids[5]="671125706342859"; // Notice Board
        ids[6]="418543801611643"; //  Audio Section
        ids[7]="420363998145999"; // Sanskriti Club
        ids[8]="146825225353259"; // Group For Interactive Learning, IITR
        ids[9]="754869404569818"; // ASHRAE
        ids[10]="217963184943488"; // Cognizance
        ids[11]="317158211638196"; // Photography Section
        ids[0]="231275190406200"; // Cinema Club
        ids[13]="369513426410599"; // TECHNOLOGIC 2015
        ids[14]="503218879766649"; // ELECTRONICS SECTION
        ids[15]="242919515859218"; // NCC
        ids[16]="100641016663545"; // CINEMATIC SECTION
        ids[17]="567441813288417"; // FINE ARTS SECTION
        ids[18]="272394492879208"; // ANUSHRUTI
        ids[19]="1410660759172170"; // RHAPSODY
        ids[20]="292035034247"; // SHARE

        posts=new ArrayList<Post>();

        fbpliked=PagesSelected.getSelectedPageIds(fb.this);
        try {

            // if (AccessToken.getCurrentAccessToken().toString().equals(null)) {




//            LoginManager.getInstance().registerCallback(callbackManager,
//                    new FacebookCallback<LoginResult>() {
//                        @Override
//                        public void onSuccess(LoginResult loginResult) {
//                            Toast.makeText(fb.this,"Logged in",Toast.LENGTH_LONG).show();
//                            // App code
//                        }
//
//                        @Override
//                        public void onCancel() {
//                            // App code
//                            // savedInstanceState
//                        }
//
//                        @Override
//                        public void onError(FacebookException exception) {
//                            // App code
//                            Toast.makeText(fb.this, exception.toString(),  Toast.LENGTH_LONG).show();
//                        }
//                    });
//            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile, user_groups"));

            //   }
            accessTokenTracker = new AccessTokenTracker() {
                @Override
                protected void onCurrentAccessTokenChanged(
                        AccessToken oldAccessToken,
                        AccessToken currentAccessToken) {

                }
            };

                    try {
                        getUserData(AccessToken.getCurrentAccessToken());
//                        Toast.makeText(fb.this, "Access Token: "+ AccessToken.getCurrentAccessToken().getToken(), Toast.LENGTH_LONG).show();
                    }
                    catch (Exception e){
//                        Toast.makeText(fb.this, "error is: "+e.toString(), Toast.LENGTH_LONG).show();
                    }

                }
        catch (Exception e)
        {
//            Toast.makeText(fb.this, e.toString(),  Toast.LENGTH_LONG).show();
        }

//        fabfbu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i=new Intent(fb.this,Fblist.class);
//                startActivity(i);
//                finish();
//            }
//        });
            }




    public void getUserData(AccessToken accessToken){

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(fb.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        final MyRecyclerAdapterfb adapterfb = new MyRecyclerAdapterfb(posts);
        recyclerView.setAdapter(adapterfb);

       for(i=0;i<fbpliked.size();i++) {


               GraphRequest.newGraphPathRequest(accessToken,
                       "/" + fbpliked.get(i) + "/posts",
                       new GraphRequest.Callback() {
                           @Override
                           public void onCompleted(GraphResponse graphResponse) {

                               try {
                                   String resp = graphResponse.getRawResponse();
//                               Toast.makeText(fb.this, "response is: " + resp, Toast.LENGTH_LONG).show();
                                    Log.e("Response",resp);
                                   m = graphResponse.getJSONObject();

                                   n = m.getJSONArray("data");
//                            messages=new String[n.length()];
//                            for(int i=0;i<n.length();i++) {
//                                JSONObject a = n.getJSONObject(i);
//                                messages[i]=a.optString("message");
//                            }

                                   try {
                                       for (int j = 0; j <5; j++) {
                                           posts.add(new Post(n.getJSONObject(j)));
                                       }
                                   }
                                   catch (Exception e)
                                   {
//                                       Log.d("Error: ",e.toString());
                                   }
                                   Collections.sort(posts);
                                   adapterfb.posts = posts;
//                            list.setAdapter(new ArrayAdapter<String>(fb.this,android.R.layout.simple_list_item_1,messages));
                                   adapterfb.notifyDataSetChanged();

                               }
                               catch (Exception e) {
//                                   Toast.makeText(fb.this, "error is: " + e.toString(), Toast.LENGTH_LONG).show();
                               }

                           }

                       }).executeAsync();


       }

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
        accessTokenTracker.stopTracking();}
        catch (Exception e){
//            Toast.makeText(fb.this, "error is: "+e.toString(), Toast.LENGTH_LONG).show();
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
            Intent i=new Intent(fb.this,MainActivity.class);
            finish();
            startActivity(i);
            return true;
        }

        else
        if(id==R.id.addpages)
        {
            Intent i=new Intent(fb.this,Fblist.class);
            finish();
                startActivity(i);

        }

        return super.onOptionsItemSelected(item);
    }

//    public ArrayList<Post> generatePosts()
//    {
//        try {
//            for (int i = 0; i < n.length(); i++) {
//                posts.add(new Post(n.getJSONObject(i)));
//            }
//        }
//        catch (Exception e)
//        {
//            Log.d("Error: ",e.toString());
//        }
//        return posts;
//    }
}
