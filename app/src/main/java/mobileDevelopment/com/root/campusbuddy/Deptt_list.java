package mobileDevelopment.com.root.campusbuddy;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rc on 29/6/15.
 */

public class Deptt_list extends AppCompatActivity{


    private List<String> listValues;
    SharedPreferences pref;
    Toolbar toolbar;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deptt_list);

        list=(ListView)findViewById(R.id.list);
        toolbar = (Toolbar) findViewById(R.id.tool_bardeptlist);
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


        listValues = new ArrayList<String>();
        listValues.add("Architecture and Planning");
        listValues.add("Applied Science and Engineering");
        listValues.add("Biotechnology");
        listValues.add("Chemical Engineering");
        listValues.add("Chemistry");
        listValues.add("Civil Engineering");
        listValues.add("Computer Science and Engineering");
        listValues.add("Earthquake Engineering");
        listValues.add("Earth Sciences");
        listValues.add("Electrical Engineering");
        listValues.add("Electronics and Communication Engineering");
        listValues.add("Humanities and Social Sciences");
        listValues.add("Hydrology");
        listValues.add("Management Studies");
        listValues.add("Mathematics");
        listValues.add("Mechanical and Industrial Engineering");
        listValues.add("Metallurgical and Materials Engineering");
        listValues.add("Physics");
        listValues.add("Water Resources Development and Management");


        // initiate the listadapter
        ArrayAdapter<String> myAdapter = new ArrayAdapter <String>(this,
                R.layout.mytextview, listValues);

// {
//            @Override
//        public View getView(int p,View view,ViewGroup parent)
//            {
//                View view1=super.getView(p,view,parent);
//                TextView txt=(TextView)view1.findViewById(android.R.id.text1);
//                txt.setTextColor(Color.BLACK);
//                txt.setBackgroundColor(Color.WHITE);
//                return view;}
//        };

        // assign the list adapter
        list.setAdapter(myAdapter);

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
                    startActivity(deptt_intent);
                } catch (Exception e) {
                    Toast.makeText(Deptt_list.this, e.toString(), Toast.LENGTH_LONG).show();
                }

            }

        });

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        // Toast.makeText(this, "Back button works", Toast.LENGTH_LONG).show();
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
