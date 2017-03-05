package in.co.mdg.campusBuddy;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
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
import in.co.mdg.campusBuddy.fragments.AboutUsFragment;
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
        adapter.addFrag(new AboutUsFragment(), "AboutUs Fragment");
        viewPager.setAdapter(adapter);
        if (getIntent().hasExtra("open")) {
            if (getIntent().getStringExtra("open").equals("fb_feed")) {
                viewPager.setCurrentItem(1);
                updateNavigationBarState(R.id.action_notices);
            }
        }
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
                            case R.id.action_about_us:
                                viewPager.setCurrentItem(2);
                                break;
                        }
                        updateNavigationBarState(item.getItemId());
                        return true;
                    }
                });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                updateNavigationBarState(getMenuItemId(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private int getMenuItemId(int position) {
        switch (position) {
            case 0:
                return R.id.action_contacts;
            case 1:
                return R.id.action_notices;
            case 2:
                return R.id.action_about_us;
            default:
                return -1;
        }
    }

    private void updateNavigationBarState(int actionId) {
        Menu menu = bottomNavigationView.getMenu();
        menu.findItem(actionId).setChecked(true);
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
