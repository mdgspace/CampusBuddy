package in.co.mdg.campusBuddy.contacts;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import in.co.mdg.campusBuddy.AboutUs;
import in.co.mdg.campusBuddy.R;
import in.co.mdg.campusBuddy.contacts.ContactsRecyclerAdapter.ClickListener;
import in.co.mdg.campusBuddy.contacts.data_models.ContactSearchModel;
import in.co.mdg.campusBuddy.contacts.data_models.Department;
import io.realm.Realm;

/*
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;
*/

/**
 * Created by rc on 29/6/15.
 */

public class ContactsMainActivity extends AppCompatActivity implements ClickListener {

    private AutoCompleteTextView searchBox;
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private static int SPEECHORCLEAR = 1;
    private Realm realm;
    private SearchSuggestionAdapter searchAdapter;
    private FrameLayout dimLayout;
    private LinearLayout searchBar;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telephone_contacts);
        realm = Realm.getDefaultInstance();
        try {
            checkDbExists();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        final AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);
        dimLayout = (FrameLayout) findViewById(R.id.dim_layout);
        final ImageView speechButton = (ImageView) findViewById(R.id.speechbutton);
        backButton = (ImageView) findViewById(R.id.backbutton);
        searchBox = (AutoCompleteTextView) findViewById(R.id.searchbox);
        searchBar = (LinearLayout) findViewById(R.id.searchbar);
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.accent));
        tabLayout.setTabTextColors(Color.parseColor("#A1F5F5F5"), Color.parseColor("#FFF5F5F5"));

        searchAdapter = new SearchSuggestionAdapter(this, R.layout.search_suggestion_listitem);
        searchBox.setThreshold(1);
        searchBox.setDropDownAnchor(R.id.searchbar);
        searchBox.setAdapter(searchAdapter);
        searchBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final ContactSearchModel contact = searchAdapter.getItem(position);
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        ContactSearchModel historySearch = realm.where(ContactSearchModel.class).equalTo("name", contact.getName()).findFirst();
                        if (contact.isDept()) {
                            showDepartmentContacts(contact.getName());
                        } else {
                            if (historySearch == null) {
                                contact.setDateAdded(new Date());
                                contact.setHistorySearch(true);
                                realm.copyToRealm(contact);
                            } else {
                                historySearch.setDateAdded(new Date());
                            }
                            showContact(contact.getName(), contact.getDept());
                        }

                    }
                });
                searchBox.setText("");
                backButton.performClick();
            }
        });
        dimLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButton.performClick();
            }
        });
        searchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBox.showDropDown();
            }
        });

//        searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if(actionId == EditorInfo.IME_NULL)
//                {
//                    search(v.getText().toString());
//                }
//                return true;
//            }
//        });
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    SPEECHORCLEAR = 2;
                    speechButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_clear_black_24dp));
                } else {
                    SPEECHORCLEAR = 1;
                    speechButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_mic_black_24dp));
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBox.clearFocus();
                dimLayout.setVisibility(View.GONE);
                searchBar.setVisibility(View.GONE);
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
            }
        });

        speechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (SPEECHORCLEAR) {
                    case 1:
                        promptSpeechInput();
                        break;
                    case 2:
                        searchBox.setText("");
                        SPEECHORCLEAR = 1;
                        Picasso.with(getApplicationContext()).load(R.drawable.ic_mic_black_24dp).into(speechButton);
                        break;
                }
            }
        });
    }

    private void setUpViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(DeptListFragment.newInstance(1), "DEPARTMENT LIST");
        adapter.addFrag(DeptListFragment.newInstance(2), "A TO Z LIST");
        viewPager.setAdapter(adapter);
    }

    private void showContact(String name, String dept) {
        searchBox.setText("");
        Intent in = new Intent(this, ShowContact.class);
        in.putExtra("name", name);
        in.putExtra("dept", dept);
        startActivity(in);
    }

    private void showDepartmentContacts(String deptName) {
        Intent in = new Intent(this, ShowDepartmentContacts.class);
        in.putExtra("dept_name", deptName);
        startActivity(in);
    }

    private void checkDbExists() throws IOException {
        long count = realm.where(Department.class).count();
        if (count == 0) {
            final InputStream stream = getAssets().open("contacts.json");
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    try {
                        realm.createAllFromJson(Department.class, stream);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (searchBar.getVisibility() == View.VISIBLE)
            backButton.performClick();
        else {
            finish();
            super.onBackPressed();
        }
    }

    @Override
    public void onDestroy() {
        realm.close();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_out_up);
        super.onDestroy();
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT:
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    searchBox.append(result.get(0));
                    searchBox.requestFocus();
                }
                break;
        }
    }

    @Override
    public void itemClicked(int type, String contactName, String deptName) {
        switch (type) {
            case 1:
                showDepartmentContacts(deptName);
                break;
            case 2:
            case 3:
                showContact(contactName, deptName);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.disclaimer) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
// ...Irrelevant code for customizing the buttons and title
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.disclaimer, null);
            dialogBuilder.setView(dialogView);
            dialogBuilder.setTitle("Disclaimer");
            dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            TextView tv_dis = (TextView) dialogView.findViewById(R.id.disclaimera);
            tv_dis.setText(Html.fromHtml(getString(R.string.disclaimer_text)));
            tv_dis.setMovementMethod(LinkMovementMethod.getInstance());
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
        } else if (id == R.id.about_us_menu) {

            Intent i = new Intent(ContactsMainActivity.this, AboutUs.class);
            startActivity(i);
        } else if (id == R.id.search) {
            searchBar.setVisibility(View.VISIBLE);
            dimLayout.setVisibility(View.VISIBLE);
            if (searchBox.getText().toString().equals("")) {
                searchAdapter.getFilter().filter(null);
            }
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
            searchBox.requestFocus();
            searchBox.showDropDown();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_telephone_contacts, menu);
        return true;
    }


}

