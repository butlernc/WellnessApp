package com.uwec.wellnessapp.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
public class LoginHelper {

    private static boolean isLogged;

    SessionData.SaveLoginSession saveLoginSessionThread;

    public static boolean isLogged() {return isLogged;}

    public static void setLogged(boolean set) {isLogged = set;}

    /**
     * Used to log a user into the system
     *
     * @param email
     * @param password
     * @return boolean
     */
    public void login(final Activity activtiy, final String email, final String password, final boolean rememberMe, boolean showLoading) {

        if(showLoading) {
            /* make sure the loading fragment is showing */
            Statics.loadingFragment = new LoadingFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("filler", 1);
            Statics.loadingFragment.setArguments(bundle);
            activtiy.getFragmentManager().beginTransaction().replace(R.id.main_login_area, Statics.loadingFragment).commit();
            activtiy.getFragmentManager().executePendingTransactions();
        }

        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                synchronized (this) {
                    Log.d("LOGIN", "Start server setup");
                    //create a FileSourceConnector, used to read and write to the server.
                    FileSourceConnector fileSourceConnector = new FileSourceConnector();

                    //TODO: fix
                    //Statics.singleExecutor.runTask(fileSourceConnector.queue("readUser", email, password));

                    Log.e("LOGIN", "Return: " + fileSourceConnector.getRETURN_STR());
                    if (fileSourceConnector.getRETURN_STR().contentEquals("GOOD")) {
                        Log.d("login", "logging you in");
                        if (rememberMe) {
                            Statics.sessionData.setUsername(email);
                            Statics.sessionData.setPassword(password);
                        }

                        saveLoginSessionThread = Statics.sessionData.createSaveLoginSessionThread(activtiy.getBaseContext(), rememberMe);
                        saveLoginSessionThread.start();
                        synchronized (saveLoginSessionThread) {
                            try {
                                saveLoginSessionThread.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        setLogged(true);

                    /* logged in successfully, switch to main activity */
                        //Toast.makeText(activtiy.getBaseContext(), "Successfully Logged In!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(activtiy.getBaseContext(), MainNavActivity.class);
                        activtiy.startActivity(intent);

                    } else if (fileSourceConnector.getRETURN_STR().contentEquals("NCP")) {
                    /* logged in unsuccessful, switch to login activity */
                        Looper.prepare();
                        Toast.makeText(activtiy.getBaseContext(), "Incorrect username/password", Toast.LENGTH_LONG).show();
                        activtiy.getFragmentManager().beginTransaction().replace(R.id.main_login_area, new LoginFragment()).commit();
                        setLogged(false);
                    }

                    //finished logging in, notify the thread
                    notify();
                }
            }
        };
        //TODO: fix
        //Statics.singleExecutor.runTask(runnable);

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
