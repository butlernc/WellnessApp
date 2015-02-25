package com.uwec.wellnessapp.login;

import android.app.Activity;
import android.content.Context;
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

    Context context;
    LoginActivity activity;
    String email;
    String password;
    boolean rememberMe;
    boolean worked;
    boolean isDone;
    boolean wasInvalid;

    public LoginHelper(Context context, Activity activity, String email, String password, boolean rememberMe) {
        this.context    = context;
        this.activity   = (LoginActivity)activity;
        this.email      = email;
        this.password   = password;
        this.rememberMe = rememberMe;
    }

    public static boolean isLogged() {return isLogged;}

    public static void setLogged(boolean set) {isLogged = set;}

    /**
     * Used to log a user into the system
     */
    public void run() {

        synchronized (this) {
            isDone = false;

            Log.d("LOGIN", "Start server setup");
            //create a FileSourceConnector, used to read and write to the server.
            FileSourceConnector fileSourceConnector = new FileSourceConnector(activity.getBaseContext());
            fileSourceConnector.queue("readUser", email, password);

            Log.e("LOGIN", "Return: " + fileSourceConnector.getRETURN_STR());
            if (fileSourceConnector.getRETURN_STR().contentEquals("GOOD")) {
                Log.d("login", "logging you in");

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
                /* logged in successfully, switch to main activity */
                worked = true;
                setLogged(true);
            } else if (fileSourceConnector.getRETURN_STR().contentEquals("NCP")) {
                worked = false;
                wasInvalid = false;
                setLogged(false);
            } else if (fileSourceConnector.getRETURN_STR().contentEquals("INVALID")) {
                worked = false;
                wasInvalid = true;
                setLogged(false);
            }

            if (worked) {
                Statics.messenger.sendToastMessage("Login was successful!");
                Intent intent = new Intent(context, MainNavActivity.class);
                activity.startActivity(intent);
            } else {
                if(wasInvalid) {
                    Statics.messenger.sendToastMessage("That email has not been registered!");
                }
                Statics.messenger.sendToastMessage("Wrong username/password, please try again.");
            }
            isDone = true;
        }


    }

    /**
     * Used to start the Login Activity without running a lot of the loading scripts
     * if there is any strings in extras
     * @param current activity
     */
    public static void startLoginActivity(Activity current) {
        Intent intent = new Intent(current, LoginActivity.class);
        intent.putExtra("!load", true);
        current.startActivity(intent);
    }

    public boolean worked() {
        return worked;
    }

    public boolean wasInvalid() { return wasInvalid; }

    public boolean isDone() {
        return isDone;
    }
}
