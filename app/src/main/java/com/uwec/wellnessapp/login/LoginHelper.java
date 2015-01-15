package com.uwec.wellnessapp.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.uwec.wellnessapp.R;
import com.uwec.wellnessapp.data.SessionData;
import com.uwec.wellnessapp.start.MainNavActivity;
import com.uwec.wellnessapp.statics.Statics;
import com.uwec.wellnessapp.utils.FileSourceConnector;

/**
 * Created by butlernc on 12/2/2014.
 *
 * Used as static reference for basic user log information
 */
public class LoginHelper extends Thread{

    private static boolean isLogged;

    SessionData.SaveLoginSession saveLoginSessionThread;

    Activity activity;
    String email;
    String password;
    boolean rememberMe;
    boolean showLoading;

    public LoginHelper(Activity activity, String email, String password, boolean rememberMe, boolean showLoading) {
        this.activity    = activity;
        this.email       = email;
        this.password    = password;
        this.rememberMe  = rememberMe;
        this.showLoading = showLoading;
    }

    public static boolean isLogged() {return isLogged;}

    public static void setLogged(boolean set) {isLogged = set;}

    /**
     * Used to log a user into the system
     */
    public void run() {

        synchronized (this) {

            if(showLoading) {
                /* TODO: make sure the loading activity is showing */
            }

            Log.d("LOGIN", "Start server setup");
            //create a FileSourceConnector, used to read and write to the server.
            FileSourceConnector fileSourceConnector = new FileSourceConnector();
            fileSourceConnector.queue("readUser", email, password);

            Log.e("LOGIN", "Return: " + fileSourceConnector.getRETURN_STR());
            if (fileSourceConnector.getRETURN_STR().contentEquals("GOOD")) {
                Log.d("login", "logging you in");
                if (rememberMe) {
                    Statics.sessionData.setUsername(email);
                    Statics.sessionData.setPassword(password);
                }

                saveLoginSessionThread = Statics.sessionData.createSaveLoginSessionThread(activity.getBaseContext(), rememberMe);
                saveLoginSessionThread.start();
                synchronized (saveLoginSessionThread) {
                    while(!saveLoginSessionThread.isDone) {
                        try {
                            saveLoginSessionThread.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                setLogged(true);

                /* logged in successfully, switch to main activity */
                Intent intent = new Intent(activity.getBaseContext(), MainNavActivity.class);
                activity.startActivity(intent);

            } else if (fileSourceConnector.getRETURN_STR().contentEquals("NCP")) {
                /* logged in unsuccessful, switch to login activity */
                Intent intent = new Intent(activity.getBaseContext(), LoginActivity.class);
                activity.startActivity(intent);
                setLogged(false);
            }

            //finished logging in, notify the thread
            notify();
        }


    }

    /**
     * Used to start the Login Activity without running a lot of the loading scripts
     * if there is any strings in extras
     * @param current activity
     */
    public static void startLoginActivity(Activity current, String[] extras) {
        Intent intent = new Intent(current, LoginActivity.class);
        intent.putExtra("extras", extras);
        current.startActivity(intent);
    }
}
