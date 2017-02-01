package in.co.mdg.campusBuddy;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import in.co.mdg.campusBuddy.contacts.ViewPagerAdapter;

public class HomeActivity extends AppCompatActivity {
    private ViewPagerAdapter adapter;
    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_contacts:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.action_notices:
                                break;
                            case R.id.action_maps:
                                break;
                            case R.id.action_calendar:
                                break;
                        }
                        return true;
                    }
                });
    }

    private void initViews() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
    }

    private void setupViewPager()
    {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //initialize fragments and add to adapter
        viewPager.setAdapter(adapter);
    }
}
