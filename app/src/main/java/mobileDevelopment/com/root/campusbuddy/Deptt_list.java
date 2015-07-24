package mobileDevelopment.com.root.campusbuddy;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
        DayNightTheme.setToolbar(toolbar);
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
            /*
                SpringSystem springSystem = SpringSystem.create();
                //Add a spring to the system.
                final Spring spring = springSystem.createSpring();
                final View v1 = v;
                final int i1 = i;
                if (e.getAction() == MotionEvent.ACTION_DOWN) {
                    spring.addListener(new SimpleSpringListener() {

                        @Override
                        public void onSpringUpdate(Spring spring) {
                            // You can observe the updates in the spring
                            // state by asking its current value in onSpringUpdate.
                            float value = (float) spring.getCurrentValue();
                            float scale = 1f - (value * 0.1f);
                            v1.setScaleX(scale);
                            v1.setScaleY(scale);
                        }
                    });

                    //Set the spring in motion; moving from 0 to 1
                    spring.setEndValue(5);
                } else if (e.getAction() == MotionEvent.ACTION_UP) {
                    spring.addListener(new SimpleSpringListener() {

                        @Override
                        public void onSpringUpdate(Spring spring) {
                            // You can observe the updates in the spring
                            // state by asking its current value in onSpringUpdate.
                            float value = (float) spring.getCurrentValue();
                            float scale = (value * 0.2f);
                            v1.setScaleX(scale);
                            v1.setScaleY(scale);
                        }
                    });

                    //Set the spring in motion; moving from 0 to 1
                    spring.setEndValue(5);

                }
            */
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

// {
//

/*
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedItem = (String) list.getItemAtPosition(position);
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

        });
        */

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

}

//    // when an item of the list is clicked
//    @Override
//    protected void onListItemClick(ListView list, View view, int position, long id) {
//        super.onListItemClick(list, view, position, id);
//
//        String selectedItem = (String) getListView().getItemAtPosition(position);
//        String table_name="";
//        //String selectedItem = (String) getListAdapter().getItem(position);
//
//        switch(selectedItem){
//            case "Architecture and Planning" :
//                table_name = "ARCHI";
//                break;
//
//            case "Applied Science and Engineering" :
//                table_name = "APPLIEDSCI";
//                break;
//
//            case "Biotechnology" :
//                table_name = "BIOTECH";
//                break;
//
//            case "Chemical Engineering" :
//                table_name = "CHEMICAL";
//                break;
//
//            case "Chemistry" :
//                table_name = "CHEMISTRY";
//                break;
//
//            case "Civil Engineering" :
//                table_name = "CIVIL";
//                break;
//
//            case "Computer Science and Engineering" :
//                table_name = "CSE";
//                break;
//
//            case "Earthquake Engineering" :
//                table_name = "EARTHQUAKE";
//                break;
//
//            case "Earth Sciences" :
//                table_name = "EARTHSCI";
//                break;
//
//            case "Electrical Engineering" :
//                table_name = "EE";
//                break;
//
//            case "Electronics and Communication Engineering" :
//                table_name = "ECE";
//                break;
//
//            case "Humanities and Social Sciences" :
//                table_name = "HUMANITIES";
//                break;
//
//            case "Hydrology" :
//                table_name = "HYDRO";
//                break;
//
//            case "Management Studies" :
//                table_name = "DOMS";
//                break;
//
//            case "Mathematics" :
//                table_name = "MATHS";
//                break;
//
//            case "Mechanical and Industrial Engineering" :
//                table_name = "ME_PI";
//                break;
//
//            case "Metallurgical and Materials Engineering" :
//                table_name = "META";
//                break;
//
//            case "Physics" :
//                table_name = "PHYSICS";
//                break;
//
//            case "Water Resources Development and Management" :
//                table_name = "WATERRES";
//                break;
//
//        }
//        try {
//
//            Intent deptt_intent = new Intent(Deptt_list.this, TelephoneContacts.class);
//            deptt_intent.putExtra("table_name", table_name);
//            startActivity(deptt_intent);
//        }
//        catch (Exception e){
//            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
//        }
//
//
//    }
//
//}
