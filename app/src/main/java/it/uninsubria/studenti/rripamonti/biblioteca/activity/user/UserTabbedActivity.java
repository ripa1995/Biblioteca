package it.uninsubria.studenti.rripamonti.biblioteca.activity.user;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import it.uninsubria.studenti.rripamonti.biblioteca.R;
import it.uninsubria.studenti.rripamonti.biblioteca.activity.FirebaseLoginActivity;
import it.uninsubria.studenti.rripamonti.biblioteca.activity.user.fragment.LastExtraActivitiesFragment;
import it.uninsubria.studenti.rripamonti.biblioteca.activity.user.fragment.LastObjectFragment;

public class UserTabbedActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, ViewPager.OnPageChangeListener {
    private static final String TAG = "UserTabbedActivity";


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_tabbed);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(myToolbar);
        myToolbar.setOnMenuItemClickListener(this);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setCurrentItem(0);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu, menu);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_logout:
                Log.d(TAG,"action LOGOUT has clicked");
                FirebaseAuth.getInstance().signOut();
                intent = new Intent(getApplicationContext(), FirebaseLoginActivity.class);
                startActivity(intent);
                finish();
                return true;

            case R.id.action_loan_status:
                Log.d(TAG,"action LOANSTATUS has clicked");
                intent = new Intent(getApplicationContext(), LoanStatusActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_search:
                Log.d(TAG,"action SEARCH has clicked");
                intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position){
                case 0:
                    return LastObjectFragment.newInstance();
                case 1:
                    return LastExtraActivitiesFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.new_objects);
                case 1:
                    return getString(R.string.coming_events);
            }
            return null;
        }

    }
}
