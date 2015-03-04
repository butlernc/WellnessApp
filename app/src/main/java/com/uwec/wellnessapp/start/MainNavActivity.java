package com.uwec.wellnessapp.start;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.uwec.wellnessapp.R;
import com.uwec.wellnessapp.challengeInfo.ChallengeInfoFragment;
import com.uwec.wellnessapp.login.LoginHelper;
import com.uwec.wellnessapp.pointsbreakdown.PointsBreakDownFragment;
import com.uwec.wellnessapp.statics.Statics;
import com.uwec.wellnessapp.utils.FileSourceConnector;
import com.uwec.wellnessapp.utils.Messenger;

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

    /** current root view */
    View rootView;

    final int SPONSORS_ID = 5;

    FileSourceConnector fileSourceConnector;
    String queueMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* used to write cached data to server when called */
        Statics.handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                fileSourceConnector = new FileSourceConnector(getBaseContext());
                Log.e("SERVER", "writing to the server");

                queueMessage = msg.getData().getString(Messenger.keys[2]);
                Log.e("MESS", "Message: " + queueMessage);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        fileSourceConnector.queue(queueMessage, Statics.globalUserData.getEmail(), Statics.globalUserData.getPassword());
                        fileSourceConnector.queue("writeUserInfoToServer", Statics.globalUserData.getEmail(), Statics.globalUserData.getPassword());
                        Statics.writeToServer = false;
                    }
                }).start();

                /* let the user know were saving */
                Toast.makeText(getBaseContext(), "Saving...", Toast.LENGTH_LONG).show();
            }
        };

        /** get fragment names */
        drawerListTitles = getResources().getStringArray(R.array.drawerListTitles);

        /** grab the drawer from activity_main_nav.xml */
        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

        // Set default Fragment
        getFragmentManager().beginTransaction().replace(R.id.main_nav_fragment, newFragment(0)).commit();

        /* for testing */
        //Log.i("TEST", Statics.printUserData());

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
            sign_IO_button.setTitle(welcome);
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
            LoginHelper.startLoginActivity(this);
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



        Fragment newFragment = new Fragment();

        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);

        switch(sectionNumber) {

            case 0:
                newFragment = new DefaultMainFragment();
                break;
            case 1:
                newFragment = new ChallengeInfoFragment();
                break;
            case 2:
                newFragment = new PointsBreakDownFragment();
                break;
            case 3:
                drawerLinks("http://www.uwec.edu/Recreation/fitness/Stepitup/recipes.htm");
                break;
            case 4:
                drawerLinks("http://www.uwec.edu/Recreation/fitness/Stepitup/bonusactivities.htm");
                break;
            case SPONSORS_ID:
                drawerLinks("http://www.uwec.edu/Recreation/fitness/Stepitup/prizes.htm");
                break;
        }

        newFragment.setArguments(args);
        return newFragment;
    }

    private void quizPopUp(Activity activity) {

        final PopupWindow quizPopup;
        Log.d("POPUP", "should pop up?: " + String.valueOf(!Statics.sessionData.isFirstTime()));
        if(!Statics.sessionData.isFirstTime()) {

            /* initialize the popup's layout */
            LinearLayout viewGroup = (LinearLayout)activity.findViewById(R.id.quiz_popup_layout);
            LayoutInflater layoutInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = layoutInflater.inflate(R.layout.popup_layout, viewGroup);

            /* set up the popup window */
            quizPopup = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            quizPopup.showAtLocation(layout, Gravity.CENTER, 0, 0);
        }
    }

    private void drawerLinks(String link) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(link));
        MainNavActivity.this.startActivity(intent);
    }

}