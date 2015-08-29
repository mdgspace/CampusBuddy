package mobileDevelopment.com.root.campusbuddy;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
/*
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;
*/
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rc on 29/6/15.
 */

public class Deptt_list extends AppCompatActivity{

    SharedPreferences pref;
    Toolbar toolbar;
    //ListView list;
    RecyclerView recyclerView;
    ArrayList<Contact> listValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telephone_contacts);

       // list=(ListView)findViewById(R.id.list);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
//        DayNightTheme.setToolbar(toolbar);
//        ctoolbar=(CollapsingToolbarLayout)findViewById(R.id.collapsingtoolbar);
        toolbar.setTitle("List of departments");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new MyRecyclerAdapter(generateContacts()));

        recyclerView.setOnScrollListener(new MyScrollListener(this) {
            @Override
            public void onMoved(int distance) {
              //  toolbar.setTranslationY(-distance);
            }
        });

        recyclerView.addOnItemTouchListener(new MyItemClickListener(this, new MyItemClickListener.OnItemClickListener() {
            @Override
            public void OnItemClick(View v, int i, MotionEvent e) {

            final int position = i;
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        String selectedItem = listValues.get(position).getName();
                        String table_name = "";
                        //String selectedItem = (String) getListAdapter().getItem(position);

                        switch (selectedItem) {
                            case "Architecture and Planning":
                                table_name = "ARCHI";
                                break;

                            case "Applied Science and Engineering":
                                table_name = "APPLIEDSCI";
                                break;

                            case "Biotechnology":
                                table_name = "BIOTECH";
                                break;

                            case "Chemical Engineering":
                                table_name = "CHEMICAL";
                                break;

                            case "Chemistry":
                                table_name = "CHEMISTRY";
                                break;

                            case "Civil Engineering":
                                table_name = "CIVIL";
                                break;

                            case "Computer Science and Engineering":
                                table_name = "CSE";
                                break;

                            case "Earthquake Engineering":
                                table_name = "EARTHQUAKE";
                                break;

                            case "Earth Sciences":
                                table_name = "EARTHSCI";
                                break;

                            case "Electrical Engineering":
                                table_name = "EE";
                                break;

                            case "Electronics and Communication Engineering":
                                table_name = "ECE";
                                break;

                            case "Humanities and Social Sciences":
                                table_name = "HUMANITIES";
                                break;

                            case "Hydrology":
                                table_name = "HYDRO";
                                break;

                            case "Management Studies":
                                table_name = "DOMS";
                                break;

                            case "Mathematics":
                                table_name = "MATHS";
                                break;

                            case "Mechanical and Industrial Engineering":
                                table_name = "ME_PI";
                                break;

                            case "Metallurgical and Materials Engineering":
                                table_name = "META";
                                break;

                            case "Physics":
                                table_name = "PHYSICS";
                                break;

                            case "Water Resources Development and Management":
                                table_name = "WATERRES";
                                break;

                        }
                        try {

                            Intent deptt_intent = new Intent(Deptt_list.this, TelephoneContacts.class);
                            deptt_intent.putExtra("table_name", table_name);
                            deptt_intent.putExtra("dept_name", selectedItem);
                            startActivity(deptt_intent);
                        } catch (Exception e) {
                            Toast.makeText(Deptt_list.this, e.toString(), Toast.LENGTH_LONG).show();
                        }

                    }
                }, 500);


            }
        }));
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        // Toast.makeText(this, "Back button works", Toast.LENGTH_LONG).show();
    }

    public ArrayList<Contact> generateContacts()
    {
        listValues=new ArrayList<>();

        listValues.add(new Contact("Architecture and Planning"));
        listValues.add(new Contact("Applied Science and Engineering"));
        listValues.add(new Contact("Biotechnology"));
        listValues.add(new Contact("Chemical Engineering"));
        listValues.add(new Contact("Chemistry"));
        listValues.add(new Contact("Civil Engineering"));
        listValues.add(new Contact("Computer Science and Engineering"));
        listValues.add(new Contact("Earthquake Engineering"));
        listValues.add(new Contact("Earth Sciences"));
        listValues.add(new Contact("Electrical Engineering"));
        listValues.add(new Contact("Electronics and Communication Engineering"));
        listValues.add(new Contact("Humanities and Social Sciences"));
       // listValues.add(new Contact("Hydrology"));
        listValues.add(new Contact("Management Studies"));
        listValues.add(new Contact("Mathematics"));
        listValues.add(new Contact("Mechanical and Industrial Engineering"));
        listValues.add(new Contact("Metallurgical and Materials Engineering"));
        listValues.add(new Contact("Physics"));
        listValues.add(new Contact("Water Resources Development and Management"));



        return listValues;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id==R.id.disclaimer)
        { AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
// ...Irrelevant code for customizing the buttons and title
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.disclaimer, null);
            dialogBuilder.setView(dialogView);
            dialogBuilder.setTitle("Disclamer");
            dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            TextView tv_dis = (TextView) dialogView.findViewById(R.id.disclaimera);
            tv_dis.setText("This is an experimental app made by a student's group and we don't take any responsibility for any information present in the app");
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();}

        //noinspection SimplifiableIfStatement
        //  if (id == R.id.action_settings) {
        //     return true;      }


        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_telephone_contacts, menu);
        return true;
    }


}

