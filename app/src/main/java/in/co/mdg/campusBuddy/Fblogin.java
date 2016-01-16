package in.co.mdg.campusBuddy;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;


public class Fblogin extends ActionBarActivity {

    Button b;
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fblogin);
        callbackManager = CallbackManager.Factory.create();
        b=(Button)findViewById(R.id.fbloginb);


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                Toast.makeText(Fblogin.this, "Logged in", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(Fblogin.this, Fblist.class);
                                finish();
                                startActivity(i);
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
                                Toast.makeText(Fblogin.this, exception.toString(), Toast.LENGTH_LONG).show();
                            }
                        });
                LoginManager.getInstance().logInWithReadPermissions(Fblogin.this, Arrays.asList("public_profile"));

                   }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fblogin, menu);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
