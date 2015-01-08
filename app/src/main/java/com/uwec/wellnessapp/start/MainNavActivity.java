package com.uwec.wellnessapp.start;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import com.uwec.wellnessapp.R;
import com.uwec.wellnessapp.login.LoginHelper;
import com.uwec.wellnessapp.statics.Statics;
import com.uwec.wellnessapp.utils.FileSourceConnector;

import java.io.File;


public class MainNavActivity extends Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the
     * navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in
     * {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    /** used to get titles of the fragments */
    private String[] drawerListTitles;

    /** used in creating a new fragment as arg key*/
    final String ARG_SECTION_NUMBER = "section_number";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** get fragment names */
        drawerListTitles = getResources().getStringArray(R.array.drawerListTitles);

        /** grab the drawer from activity_main_nav.xml */
        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

        // Set default Fragment
        getFragmentManager().beginTransaction().replace(R.id.main_nav_fragment, newFragment(0)).commit();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_nav_fragment, newFragment(position)).commit();
    }

    public void onSectionAttached(int number) {
        mTitle = drawerListTitles[number];
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main_nav, menu);
            MenuItem sign_IO_button = menu.getItem(0);
            //set the option menu text for the sign in/out button
            String welcome = "Welcome, " + Statics.globalUserData.getFirst_name();
            if(LoginHelper.isLogged()) {
                sign_IO_button.setTitle(welcome);
            }else{
                sign_IO_button.setTitle(R.string.sign_in);
            }
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.sign_IO) {
            //user clicked to sign out
            String[] extras = {"!load"};
            LoginHelper.startLoginActivity(this, extras);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** get the selected (selected on drawer) Fragment
     *
     * @see	case 0: DefaultMainFragment
     *
     * @return Fragment
     * */
    public Fragment newFragment(int sectionNumber) {

        Fragment newFragment;

        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);

        switch(sectionNumber) {

            default:
                newFragment = new DefaultMainFragment();
                break;
        }

        newFragment.setArguments(args);
        return newFragment;
    }

}