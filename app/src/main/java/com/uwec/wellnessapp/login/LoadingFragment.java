package com.uwec.wellnessapp.login;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uwec.wellnessapp.R;
import com.uwec.wellnessapp.statics.Statics;

/**
 * Created by butlernc on 1/8/2015.
 */
public class LoadingFragment extends Fragment {

    TextView loadingText = null;
    private boolean shouldLoad;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View rootView = inflater.inflate(R.layout.loading_fragment, container, false);
        loadingText = (TextView)rootView.findViewById(R.id.welcome_text);


        if(getArguments() == null) {

            /* make sure we aren't trying to log out */
            shouldLoad = true;
            if (getActivity().getIntent().getStringArrayExtra("extras") != null) {
                Log.d("Session", "Don't auto login this time");
            /* any activity will send a message to the login screen that the user is trying to logout
            it should not try to auto log back in. */
                shouldLoad = false;
            }

            Log.d("Session", "SessionData is loading");

            /* load the app's data here */
            Statics.sessionData.loadLastSession(getActivity().getBaseContext());
            //TODO: learn how to lock properly
            while (!Statics.messenger.messageSent) {}
            Statics.sessionData.setupSession();
            Statics.sessionData.loadWeekDataList();
            while (!Statics.messenger.messageSent) {}
            Log.d("THREAD", "loading last session data finished");
            Log.d("THREAD", "ShouldLoad: " + String.valueOf(shouldLoad) + " rememberMe: " + String.valueOf(Statics.sessionData.rememberedMe()));

            /* auto login */
            if (Statics.sessionData.rememberedMe() && shouldLoad) {
                Log.d("Session", "Using remember me username and password");

                Statics.loginHelper.login(getActivity(), Statics.sessionData.getUsername(), Statics.sessionData.getPassword(), true, false);
            } else {
                /* use default login page now */
                getActivity().getFragmentManager().beginTransaction().replace(R.id.main_login_area, new LoginFragment()).commit();
                Log.d("TEST", "Using default login fragment");
            }
        }

        return rootView;
    }

    public TextView getLoadingText() {
        return loadingText;
    }

}
