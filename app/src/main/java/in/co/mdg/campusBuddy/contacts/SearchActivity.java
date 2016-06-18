package in.co.mdg.campusBuddy.contacts;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

import in.co.mdg.campusBuddy.MyRecyclerAdapter;
import in.co.mdg.campusBuddy.NewEvent;
import in.co.mdg.campusBuddy.R;
import in.co.mdg.campusBuddy.TimetableNavigation;
import in.co.mdg.campusBuddy.contacts.data_models.Contact;
import io.realm.Realm;
import io.realm.RealmResults;

public class SearchActivity extends AppCompatActivity {

    private AutoCompleteTextView searchBox;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private int SPEECHORCLEAR = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        final FrameLayout dimLayout = (FrameLayout) findViewById(R.id.dim_layout);
        final AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);
        final ImageView speechButton = (ImageView) findViewById(R.id.speechbutton);
        final ImageView backButton = (ImageView) findViewById(R.id.backbutton);
        searchBox = (AutoCompleteTextView) findViewById(R.id.searchbox);
        final LinearLayout searchBar = (LinearLayout) findViewById(R.id.searchbar);
        String queryString = getIntent().getStringExtra("query");
        searchBox.setText(queryString);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        final SearchListAdapter searchListAdapter = new SearchListAdapter(queryString);
        recyclerView.setAdapter(searchListAdapter);
        ViewTreeObserver vto = recyclerView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    recyclerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                final int appBarHeight = appBarLayout.getMeasuredHeight();
                recyclerView.setTranslationY(-appBarHeight);
                recyclerView.getLayoutParams().height = recyclerView.getHeight()+appBarHeight;
            }
        });
        SearchSuggestionAdapter searchAdapter = new SearchSuggestionAdapter(this, R.layout.search_suggestion_listitem);
        searchBox.setAdapter(searchAdapter);
        searchBox.setThreshold(1);
        searchBox.setDropDownAnchor(R.id.searchbar);
        searchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("searchBox","clicked");
            }
        });
        dimLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBox.clearFocus();
            }
        });
        searchBox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                    Log.d("searchBox","has focus");
                    dimLayout.setVisibility(View.VISIBLE);
                    searchBox.showDropDown();
                }
                else
                {
                    dimLayout.setVisibility(View.GONE);
                    Log.d("searchBox","lost focus");
                }
            }
        });
        searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_NULL)
                {
                    searchListAdapter.changeQuery(searchBox.getText().toString());
                    searchListAdapter.notifyDataSetChanged();
                    searchBox.clearFocus();
                }
                return false;
            }
        });
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0)
                {
                    SPEECHORCLEAR =2;
                    speechButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_clear_black_24dp));
                }
                else
                {
                    SPEECHORCLEAR =1;
                    speechButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_mic_black_24dp));
                }


            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        speechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (SPEECHORCLEAR)
                {
                    case 1:
                        promptSpeechInput();
                        break;
                    case 2:
                        searchBox.setText("");
                        SPEECHORCLEAR =1;
                        Picasso.with(getApplicationContext()).load(R.drawable.ic_mic_black_24dp).into(speechButton);
                        break;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(searchBox.isFocused())
            searchBox.clearFocus();
        else
        {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("searchText", searchBox.getText().toString());
            setResult(RESULT_OK, returnIntent);
            finish();
        }
    }
    @Override
    public void onDestroy() {
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
                }
                break;
        }
    }
}
