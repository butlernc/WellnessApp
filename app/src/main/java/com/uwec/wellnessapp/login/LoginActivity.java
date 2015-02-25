package com.uwec.wellnessapp.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.uwec.wellnessapp.R;
import com.uwec.wellnessapp.data.SessionData;
import com.uwec.wellnessapp.data.UserData;
import com.uwec.wellnessapp.data.WeekData;
import com.uwec.wellnessapp.register.RegisterHelper;
import com.uwec.wellnessapp.start.MainNavActivity;
import com.uwec.wellnessapp.statics.Statics;
import com.uwec.wellnessapp.utils.Messenger;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by Noah on 12/2/2014.
 *
 * Starts an Activity that will give the user the option to login or start the register activity.
 *
 */
public class LoginActivity extends Activity {


    /* all of the threads that will be started in this activity */
    SessionData.LoadLastSession lastSessionThread;
    SessionData.SetupSession setupSessionThread;
    SessionData.LoadWeekDataList loadWeekDataListThread;

    Button email_sign_in_button, email_register_button;
    public EditText email_input, password_input;
    ProgressBar loadingBar;

    private int progressHolder = 0;
    TextView loadingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(isNetworkAvailable()) {
            create();
        }else{
            setContentView(R.layout.popup_layout);
            TextView info = (TextView)findViewById(R.id.popup_info);
            info.setText("An internet connection is required.");
        }
    }

    private void create() {
        /* used as a callback from threads that are doing work */
        Statics.handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.getData().containsKey(Messenger.keys[3])) {
                    loadingBar.setIndeterminate(false);
                    progressHolder += (int) msg.getData().getDouble(Messenger.keys[3]);
                    loadingBar.setProgress(progressHolder);

                    if (Statics.appLoaded || progressHolder > 90) {
                        try {
                            Thread.sleep(600);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        loadingBar.setVisibility(View.INVISIBLE);
                        loadingText.setVisibility(View.INVISIBLE);
                    }
                }else if(msg.getData().containsKey(Messenger.keys[4])) {
                    Toast.makeText(getBaseContext(), msg.getData().getString(Messenger.keys[4]), Toast.LENGTH_LONG).show();
                    email_input.setText("");
                    password_input.setText("");
                    loadingText.setVisibility(View.INVISIBLE);
                }else if(msg.getData().containsKey(Messenger.keys[1])) {
                    loadingText.setVisibility(View.VISIBLE);
                    loadingText.setText(msg.getData().getString(Messenger.keys[1]));
                }
            }

        };

        email_sign_in_button = (Button) findViewById(R.id.email_sign_in_button);
        email_register_button = (Button) findViewById(R.id.email_register_button);

        email_input = (EditText) findViewById(R.id.login_input_email);
        password_input = (EditText) findViewById(R.id.login_input_password);

        loadingBar = (ProgressBar) findViewById(R.id.data_loading_progress_bar);
        loadingText = (TextView) findViewById(R.id.login_loading_text);

        email_sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* hide keyboard */
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(password_input.getWindowToken(), 0);

                /* make sure our data is loaded first */
                if (Statics.appLoaded) {
                    Log.d("LOGIN", "clicked login button");

                    /* run login and set the user as logged in if successful */
                    LoginHelper loginHelper = new LoginHelper(getBaseContext(), LoginActivity.this, email_input.getText().toString(), password_input.getText().toString(), false);
                    loginHelper.start();

                }else {
                    Toast.makeText(getBaseContext(), "Loading data, please wait...", Toast.LENGTH_LONG).show();
                }
            }
        });

        email_register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Statics.appLoaded) {
                    RegisterHelper.startRegisterActivity(LoginActivity.this);
                }else {
                    Toast.makeText(getBaseContext(), "Loading data, please wait...", Toast.LENGTH_LONG).show();
                }
            }
        });

        /* check's to see if the user has returned to the login screen and info is already loaded */
        if(getIntent() == null) {
            /* instantiate our list of week data for the app */
            Statics.globalUserData = new UserData();
            Statics.globalWeekDataList = new ArrayList<>();
            Statics.sessionData = new SessionData();
            Statics.appLoaded = false;

            load();
        }else{
            if(!getIntent().hasExtra("!load")) {
                /* instantiate our list of week data for the app */
                Statics.globalUserData = new UserData();
                Statics.globalWeekDataList = new ArrayList<>();
                Statics.sessionData = new SessionData();
                Statics.appLoaded = false;

                load();
            }else {
                loadingBar.setVisibility(View.INVISIBLE);
                loadingText.setVisibility(View.INVISIBLE);
                Statics.appLoaded = true;
            }
        }
    }

    private void load() {
        lastSessionThread = Statics.sessionData.createLoadLastSession(getBaseContext());
        lastSessionThread.start();

        synchronized (lastSessionThread) {
            while (!lastSessionThread.isDone) {
                try {

                    lastSessionThread.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        Log.d("thread", "finished last session thread");
        setupSessionThread = Statics.sessionData.createSetupSession();
        setupSessionThread.start();
        synchronized (setupSessionThread) {
            while (!setupSessionThread.isDone) {
                try {
                    setupSessionThread.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d("thread", "finished session setup thread");
        loadWeekDataListThread = Statics.sessionData.createLoadWeekDataListThread(getBaseContext());
        loadWeekDataListThread.start();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
