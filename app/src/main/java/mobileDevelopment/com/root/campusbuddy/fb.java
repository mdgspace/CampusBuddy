package mobileDevelopment.com.root.campusbuddy;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class fb extends Activity {

    String messages[];
    Button fbbt1;
    CallbackManager callbackManager;
    JSONObject m;
    JSONArray n;
    AccessTokenTracker accessTokenTracker;
//    ListView list;
    RecyclerView recyclerView;
    public static Context c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb);
        c=this;
//        list=(ListView)findViewById(R.id.listfb);
        try {

            FacebookSdk.sdkInitialize(getApplicationContext());

            fbbt1 = (Button) findViewById(R.id.fbbutton);


            callbackManager = CallbackManager.Factory.create();

       /* accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {

            }
        };

        Log.d("Access Token: ", AccessToken.getCurrentAccessToken().toString());
        */
            // if (AccessToken.getCurrentAccessToken().toString().equals(null)) {

            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            Toast.makeText(fb.this,"Logged in",Toast.LENGTH_LONG).show();
                            // App code
                        }

                        @Override
                        public void onCancel() {
                            // App code
                            // savedInstanceState
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            // App code
                            Toast.makeText(fb.this, exception.toString(),  Toast.LENGTH_LONG).show();
                        }
                    });
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile, user_groups"));

            //   }
            accessTokenTracker = new AccessTokenTracker() {
                @Override
                protected void onCurrentAccessTokenChanged(
                        AccessToken oldAccessToken,
                        AccessToken currentAccessToken) {

                }
            };

            fbbt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        getUserData(AccessToken.getCurrentAccessToken());
//                        Toast.makeText(fb.this, "Access Token: "+ AccessToken.getCurrentAccessToken().getToken(), Toast.LENGTH_LONG).show();
                    }
                    catch (Exception e){
                        Toast.makeText(fb.this, "error is: "+e.toString(), Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
        catch (Exception e){
            Toast.makeText(fb.this, e.toString(),  Toast.LENGTH_LONG).show();
        }


    }


    public void getUserData(AccessToken accessToken){
        /*

        Bundle parameters = new Bundle();
        parameters.putString("posts", "posts");
        request.setParameters(parameters);

        request.executeAsync();*/

         GraphRequest.newGraphPathRequest(
                accessToken, "/415004402015833/posts",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse) {

                        try {
                            String resp = graphResponse.getRawResponse();
//                            String r=graphResponse.getJSONObject().getString("data");
                            Toast.makeText(fb.this, "response is: " + resp, Toast.LENGTH_LONG).show();
//                            Toast.makeText(fb.this, "response is: " + r, Toast.LENGTH_LONG).show();

//                            edit.setText(resp);
//                            edit.setTextColor(Color.parseColor("#000000"));
                            m=graphResponse.getJSONObject();
                            n=m.getJSONArray("data");
//                            messages=new String[n.length()];
//                            for(int i=0;i<n.length();i++) {
//                                JSONObject a = n.getJSONObject(i);
//                                messages[i]=a.optString("message");
//                            }

                            recyclerView=(RecyclerView)findViewById(R.id.recyclerview1);
                            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(fb.this);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            recyclerView.setAdapter(new MyRecyclerAdapterfb(generatePosts()));

//                            list.setAdapter(new ArrayAdapter<String>(fb.this,android.R.layout.simple_list_item_1,messages));

                        } catch (Exception e) {
                            Toast.makeText(fb.this, "error is: " + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                }).executeAsync();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
        accessTokenTracker.stopTracking();}
        catch (Exception e){
            Toast.makeText(fb.this, "error is: "+e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public ArrayList<Post> generatePosts()
    {
        ArrayList<Post> posts=new ArrayList<>();
        try {
            for (int i = 0; i < n.length(); i++) {
                posts.add(new Post(n.getJSONObject(i)));
            }
        }
        catch (Exception e)
        {
            Log.d("Error: ",e.toString());
        }
        return posts;
    }
}
