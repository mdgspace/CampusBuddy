package mobileDevelopment.com.root.campusbuddy;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONObject;

import java.util.Arrays;

public class fb extends Activity {

    Button fbbt1;
    CallbackManager callbackManager;

    AccessTokenTracker accessTokenTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb);

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
                        Toast.makeText(fb.this, "Access Token: "+ AccessToken.getCurrentAccessToken().getToken().toString(), Toast.LENGTH_LONG).show();
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
        GraphRequest request = GraphRequest.newGraphPathRequest(
                accessToken, "/726074494095651/posts",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse) {

                        try {
                            String resp = graphResponse.getRawResponse();
                            Toast.makeText(fb.this, "response is: " + resp, Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(fb.this, "error is: " + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("posts", "posts");
        request.setParameters(parameters);

        request.executeAsync();
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
}
