package in.co.mdg.campusBuddy.fragments;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Date;
import java.util.Locale;

import in.co.mdg.campusBuddy.HomeActivity;
import in.co.mdg.campusBuddy.R;
import in.co.mdg.campusBuddy.contacts.ContactsRecyclerAdapter;
import in.co.mdg.campusBuddy.contacts.DeptListFragment;
import in.co.mdg.campusBuddy.contacts.SearchSuggestionAdapter;
import in.co.mdg.campusBuddy.contacts.ShowContact;
import in.co.mdg.campusBuddy.contacts.ShowDepartmentContacts;
import in.co.mdg.campusBuddy.contacts.ShowGroupDepartments;
import in.co.mdg.campusBuddy.contacts.data_models.ContactSearchModel;
import io.realm.Realm;

import static in.co.mdg.campusBuddy.contacts.ContactsRecyclerAdapter.ENGLISH;
import static in.co.mdg.campusBuddy.contacts.ContactsRecyclerAdapter.HINDI;


public class ContactsFragment extends Fragment implements ContactsRecyclerAdapter.ClickListener {

    public static Boolean loadImages = true;
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private static int SPEECHORCLEAR = 1;
    private SearchSuggestionAdapter searchAdapter;
    private AutoCompleteTextView searchBox;
    private ImageView searchButton;
    private LinearLayout searchBar;
    private ImageView backButton;
    private ImageView speechButton;
    private FrameLayout dimLayout;
    private Switch languageSwitcher;

    private DeptListFragment groupList;

    public ContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        initView(view);
        registerListeners();
        searchAdapter = new SearchSuggestionAdapter(getActivity(), R.layout.search_suggestion_listitem);
        searchBox.setThreshold(1);
        searchBox.setDropDownAnchor(R.id.searchbar);
        searchBox.setAdapter(searchAdapter);
        groupList = DeptListFragment.newInstance(1);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.inside_fragment, groupList).commit();
        return view;
    }

    private void initView(View v) {
        searchButton = (ImageView) v.findViewById(R.id.search_button);
        speechButton = (ImageView) v.findViewById(R.id.speechbutton);
        backButton = (ImageView) v.findViewById(R.id.backbutton);
        searchBox = (AutoCompleteTextView) v.findViewById(R.id.searchbox);
        searchBar = (LinearLayout) v.findViewById(R.id.searchbar);
//        progressBar = (ProgressBar) v.findViewById(R.id.progressbar);
        dimLayout = (FrameLayout) v.findViewById(R.id.dim_layout);
        languageSwitcher = (Switch) v.findViewById(R.id.language_switch);
    }

    private void registerListeners() {
        languageSwitcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                groupList.setLang(b ? HINDI : ENGLISH);
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchBar.setVisibility(View.VISIBLE);
                dimLayout.setVisibility(View.VISIBLE);
                if (searchBox.getText().toString().equals("")) {
                    searchAdapter.getFilter().filter(null);
                }
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                searchBox.requestFocus();
                searchBox.showDropDown();
            }
        });

        searchBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final ContactSearchModel contact = searchAdapter.getItem(position);
                ((HomeActivity) getActivity()).getRealm().executeTransaction(new Realm.Transaction() {
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
                            showContact(contact.getName(), contact.getDept(), contact.getGroup());
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
                    speechButton.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_clear_black_24dp));
                } else {
                    SPEECHORCLEAR = 1;
                    speechButton.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_mic_black_24dp));
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBox.clearFocus();
                dimLayout.setVisibility(View.GONE);
                searchBar.setVisibility(View.GONE);
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
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
                        speechButton.setImageDrawable(
                                ContextCompat.getDrawable(
                                        speechButton.getContext()
                                        , R.drawable.ic_mic_black_24dp));
                        break;
                }
            }
        });
    }

    private void showContact(String name, String dept, String group) {
        searchBox.setText("");
        Intent in = new Intent(getActivity(), ShowContact.class);
        in.putExtra("name", name);
        in.putExtra("dept", dept);
        in.putExtra("group", group);
        startActivity(in);
        getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    private void showDepartmentContacts(String deptName) {
        searchBox.setText("");
        Intent in = new Intent(getActivity(), ShowDepartmentContacts.class);
        in.putExtra("dept_name", deptName);
        startActivity(in);
        getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    private void showGroupDepartments(String groupName) {
        searchBox.setText("");
        Intent in = new Intent(getActivity(), ShowGroupDepartments.class);
        in.putExtra("group_name", groupName);
        startActivity(in);
        getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
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
            Toast.makeText(getActivity(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void itemClicked(int type, String contactName, String deptName, String groupName) {
        switch (type) {
            case 1:
                showGroupDepartments(groupName);
                break;
            case 2:
                showDepartmentContacts(deptName);
                break;
            case 3:
                showContact(contactName, deptName, groupName);
                break;

        }
    }

}
