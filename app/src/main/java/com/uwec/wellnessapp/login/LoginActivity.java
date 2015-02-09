package com.uwec.wellnessapp.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
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
    EditText email_input, password_input;
    ProgressBar loadingBar;
    private int progressHolder = 0;
    TextView loadingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /* instantiate our list of week data for the app */
        Statics.globalUserData = new UserData();
        Statics.globalWeekDataList = new ArrayList<>();
        Statics.sessionData = new SessionData();
        Statics.appLoaded = false;

        /* used as a callback from threads that are doing work */
        Statics.handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                loadingBar.setIndeterminate(false);
                progressHolder += (int)msg.getData().getDouble("progress");
                loadingBar.setProgress(progressHolder);

                if(loadingBar.getProgress() >= 100) {
                    loadingBar.setVisibility(View.INVISIBLE);
                    loadingText.setVisibility(View.INVISIBLE);
                }
            }
        };

        load();

        email_sign_in_button = (Button) findViewById(R.id.email_sign_in_button);
        email_register_button = (Button) findViewById(R.id.email_register_button);

        email_input = (EditText) findViewById(R.id.login_input_email);
        password_input = (EditText) findViewById(R.id.login_input_password);

        loadingBar = (ProgressBar) findViewById(R.id.data_loading_progress_bar);
        loadingText = (TextView) findViewById(R.id.login_loading_text);

        email_sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* make sure our data is loaded first */
                if (Statics.appLoaded) {
                    Log.d("LOGIN", "clicked login button");

                    /* run login and set the user as logged in if successful */
                    LoginHelper loginHelper = new LoginHelper(LoginActivity.this, email_input.getText().toString(), password_input.getText().toString(), false, true);
                    loginHelper.start();
                    synchronized (loginHelper) {
                        while (!loginHelper.isDone()) {
                            try {
                                loginHelper.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    if (loginHelper.worked()) {
                        Toast.makeText(LoginActivity.this.getBaseContext(), "Login was successful!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LoginActivity.this.getBaseContext(), MainNavActivity.class);
                        LoginActivity.this.startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this.getBaseContext(), "Wrong username/password, please try again.", Toast.LENGTH_LONG).show();
                        email_input.setText("");
                        password_input.setText("");
                    }
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
            Statics.messenger.sendMessage("waiting for saved file...");
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
            Statics.messenger.sendMessage("waiting for creating session...");
        }
        Log.d("thread", "finished session setup thread");
        loadWeekDataListThread = Statics.sessionData.createLoadWeekDataListThread(getBaseContext());
        loadWeekDataListThread.start();
    }
}
