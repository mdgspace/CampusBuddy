package in.co.mdg.campusBuddy;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import in.co.mdg.campusBuddy.contacts.ViewPagerAdapter;
import in.co.mdg.campusBuddy.contacts.data_models.CustomStringArrayGsonAdapter;
import in.co.mdg.campusBuddy.contacts.data_models.Group;
import in.co.mdg.campusBuddy.fb.FbFeedFragment;
import in.co.mdg.campusBuddy.fragments.ContactsFragment;
import io.realm.Realm;

public class HomeActivity extends FragmentActivity {
    private ViewPager viewPager;
    private Realm realm;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        realm = Realm.getDefaultInstance();
        checkDbExists();
        initViews();
        setupViewPager();
        registerListeners();
    }

    private void initViews() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new ContactsFragment(), "Contacts Fragment");
        adapter.addFrag(new FbFeedFragment(), "FbFeedFragment Fragment");
//        adapter.addFrag(new SimpleMap(), "Map Fragment");
        viewPager.setAdapter(adapter);
    }

    private void registerListeners() {
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_contacts:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.action_notices:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.action_maps:
//                                viewPager.setCurrentItem(2);
                                break;
                        }
                        return true;
                    }
                });
    }

    private void checkDbExists() {
        if (realm.where(Group.class).count() == 0) {

            try {
                InputStream stream;
                final StringBuilder buf = new StringBuilder();
                stream = getAssets().open("contacts.min.json");
                BufferedReader in = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
                String str;
                while ((str = in.readLine()) != null) {
                    buf.append(str);
                }
                in.close();
                final Gson gson = new CustomStringArrayGsonAdapter().getGson();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        List<Group> groups = gson.fromJson(String.valueOf(buf), new TypeToken<List<Group>>() {
                        }.getType());
                        realm.copyToRealm(groups);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Realm getRealm() {
        return realm;
    }
}
