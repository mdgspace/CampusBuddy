package mobileDevelopment.com.root.campusbuddy;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


public class Fblist extends AppCompatActivity{

    ListView listview;
    String[] fbpages;
    boolean[] fbpagesliked=null;
    ArrayAdapter<String> adapter;
    LoginButton loginButton;
    CallbackManager callbackManager;
    Button submitb;
    CheckedTextView c;
    int i,count=0;
    public static boolean flag=true;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fblist);

        toolbar = (Toolbar) findViewById(R.id.tool_barfblist);
//        ctoolbar=(CollapsingToolbarLayout)findViewById(R.id.collapsingtoolbar);
        toolbar.setTitle("Facebook page list");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        flag=false;


        callbackManager = CallbackManager.Factory.create();
        submitb=(Button)findViewById(R.id.submitbutton);
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Toast.makeText(Fblist.this, "Logged in", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
        try{

        listview=(ListView)findViewById(R.id.listfbpages);
        fbpages=getResources().getStringArray(R.array.fbpages);
            listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            adapter=new ArrayAdapter<String>(Fblist.this,android.R.layout.simple_list_item_multiple_choice,fbpages);
        listview.setAdapter(adapter);

        submitb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SparseBooleanArray checked = listview.getCheckedItemPositions();
//                fbpagesliked=new boolean[checked.size()];
//                for (int i = 0; i < checked.size(); i++) {
//                    if (checked.valueAt(i)) {
//                        fbpagesliked[i] =true;
//                    }
//                    else{
//                        fbpagesliked[i]=false;
//                    }
//                }
                try{
                    fbpagesliked=new boolean[listview.getChildCount()];
                    for(int i=0;i<listview.getChildCount();i++)
                {
                    c=(CheckedTextView)listview.getChildAt(i);
                    if(c.isChecked())
                    {
                        fbpagesliked[i]=true;
                    }
                    else
                    if(c.isChecked()!=true)
                    {
                        fbpagesliked[i]=false;
                    }
                }

                    if(fbpagesliked!=null)
                    {
                        for(i=0;i<fbpagesliked.length;i++)
                        {
                            if(fbpagesliked[i]==true)
                                count++;
                        }}

                    if(fbpagesliked==null || count==0)
                    {
                        Toast.makeText(Fblist.this,"Atleast one of the pages have to be chosen to get the feeds",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        fb.fbpls=fbpagesliked;
                        Intent intent = new Intent(Fblist.this, fb.class);
                        Bundle b = new Bundle();
                        b.putBooleanArray("pagesliked", fbpagesliked);
                        intent.putExtras(b);
                        startActivity(intent);
                        finish();
                    }

//                onPause();
//                Intent intent = new Intent(Fblist.this, fb.class);
//                Bundle b = new Bundle();
//                b.putBooleanArray("pagesliked", fbpagesliked);
//                intent.putExtras(b);
//                startActivity(intent);

            }
                catch (Exception e)
                {
//                    Toast.makeText(Fblist.this,e.toString(),Toast.LENGTH_LONG).show();
                }
            }
        });}
        catch(Exception e)
        {
//            Toast.makeText(Fblist.this,e.toString(),Toast.LENGTH_LONG).show();
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

//    @Override
//    protected void onPause(){
//        Toast.makeText(this,"Atleast one of the pages have to be chosen to get the feeds",Toast.LENGTH_LONG).show();
//        if(fbpagesliked!=null)
//        {
//        for(i=0;i<fbpagesliked.length;i++)
//        {
//            if(fbpagesliked[i]==true)
//                count++;
//        }}
//
//        if(fbpagesliked==null || count==0)
//        {
//            Toast.makeText(this,"Atleast one of the pages have to be chosen to get the feeds",Toast.LENGTH_LONG).show();
//        }
//        else
//        {
//            Intent intent = new Intent(Fblist.this, fb.class);
//            Bundle b = new Bundle();
//            b.putBooleanArray("pagesliked", fbpagesliked);
//            intent.putExtras(b);
//            startActivity(intent);
//            finish();
//            super.onPause();}
//    }

    @Override
    public void onBackPressed()
    {
        flag=true;
        Intent i=new Intent(Fblist.this,MainActivity.class);
        startActivity(i);
        finish();

    }

}
