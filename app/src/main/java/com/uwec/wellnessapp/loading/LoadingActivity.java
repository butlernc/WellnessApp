package com.uwec.wellnessapp.loading;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import com.uwec.wellnessapp.R;
import com.uwec.wellnessapp.data.SessionData;
import com.uwec.wellnessapp.login.LoginHelper;
import com.uwec.wellnessapp.register.RegisterHelper;
import com.uwec.wellnessapp.statics.Statics;

import java.util.ArrayList;

/**
 * Created by Noah Butler on 1/14/2015.
 */
public class LoadingActivity extends Activity{


    private boolean shouldLoad;
    /** text view from the loading fragment */
    private TextView loadingText;

    SessionData.LoadLastSession lastSessionThread;
    SessionData.SetupSession setupSessionThread;
    SessionData.LoadWeekDataList loadWeekDataListThread;
    SessionData.LoadStartWeekData loadStartWeekDataThread;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        loadingText = (TextView) findViewById(R.id.loading_text);

        Statics.registerHelper = new RegisterHelper();
        Statics.globalWeekDataList = new ArrayList<>();

        /* used as a callback from threads that are doing work */
        Statics.handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                loadingText.setText(msg.getData().getString("message"));
            }
        };

        loadingText.setText("loading...");

        /* load app data */
        load();

        /* auto login */
        if (Statics.sessionData.rememberedMe()) {
            Log.d("Session", "Using remember me username and password");
            LoginHelper loginHelper = new LoginHelper(this, Statics.sessionData.getUsername(), Statics.sessionData.getPassword(), true, false);
            loginHelper.start();
        } else {
            Log.d("LOAD", "starting login activity");
                /* use default login page now */
            LoginHelper.startLoginActivity(this, null);
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
            Statics.messenger.sendMessage("waiting for saved file...");
        }

        Log.d("thread", "finished last session thread");
        setupSessionThread = Statics.sessionData.createSetupSession();
        setupSessionThread.start();
        synchronized (setupSessionThread) {
            while(!setupSessionThread.isDone) {
                try {
                    setupSessionThread.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Statics.messenger.sendMessage("waiting for creating session...");
        }
        Log.d("thread", "finished session setup thread");
        loadWeekDataListThread = Statics.sessionData.createLoadWeekDataListThread();
        loadWeekDataListThread.start();
        synchronized (loadWeekDataListThread) {
            while(!loadWeekDataListThread.isDone) {
                try {
                    loadWeekDataListThread.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Statics.messenger.sendMessage("loading weekly data...");
        }
        loadStartWeekDataThread = Statics.sessionData.createLoadStartWeekDataThread();
        loadStartWeekDataThread.start();
        synchronized (loadStartWeekDataThread) {
            while(!loadStartWeekDataThread.isDone) {
                try {
                    loadStartWeekDataThread.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
