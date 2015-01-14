package com.uwec.wellnessapp.login;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.uwec.wellnessapp.R;
import com.uwec.wellnessapp.data.SessionData;
import com.uwec.wellnessapp.register.RegisterHelper;
import com.uwec.wellnessapp.start.MainNavActivity;
import com.uwec.wellnessapp.statics.Statics;
import com.uwec.wellnessapp.utils.FileSourceConnector;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by butlernc on 12/2/2014.
 */
public class LoginActivity extends Activity {

    private boolean shouldLoad;
    /** text view from the loading fragment */
    private TextView loadingText;

    SessionData.LoadLastSession lastSessionThread;
    SessionData.SetupSession setupSessionThread;
    SessionData.LoadWeekDataList loadWeekDataListThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Statics.loginHelper = new LoginHelper();
        Statics.registerHelper = new RegisterHelper();
        Statics.globalWeekDataList = new ArrayList<>();

        /* show loading fragment first */
        Statics.loadingFragment = new LoadingFragment();
        getFragmentManager().beginTransaction().replace(R.id.main_login_area, new LoadingFragment()).commit();

        Log.d("TEST", "Loading fragment should be shown");

        /* used as a callback from threads that are doing work */
        Statics.handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                loadingText = Statics.loadingFragment.getLoadingText();
                loadingText.setText(msg.getData().getString("message"));
            }
        };

        Log.d("Session", "SessionData is loading");

            /* load the app's data here */
        lastSessionThread = Statics.sessionData.createLoadLastSession(getBaseContext());
        lastSessionThread.start();

        synchronized (lastSessionThread) {
            while (!lastSessionThread.isDone) {
                try {
                    Statics.messenger.sendMessage("waiting for saved file...");
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
            while(!setupSessionThread.isDone) {
                try {
                    Statics.messenger.sendMessage("waiting for creating session...");
                    setupSessionThread.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d("thread", "finished session setup thread");
        loadWeekDataListThread = Statics.sessionData.createLoadWeekDataListThread();
        loadWeekDataListThread.start();
        synchronized (loadWeekDataListThread) {
            while(!loadWeekDataListThread.isDone) {
                try {
                    Statics.messenger.sendMessage("loading weekly data...");
                    loadWeekDataListThread.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
