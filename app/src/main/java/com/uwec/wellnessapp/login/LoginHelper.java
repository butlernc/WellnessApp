package com.uwec.wellnessapp.login;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.uwec.wellnessapp.utils.FileSourceConnector;

/**
 * Created by butlernc on 12/2/2014.
 *
 * Used as static reference for basic user log information
 */
public class LoginHelper {

    private static boolean isLogged;

    public static boolean isLogged() {return isLogged;}

    public static void setLogged(boolean set) {isLogged = set;}

    /**
     * Used to log a user into the system
     *
     * @param email
     * @param password
     * @return boolean
     */
    public static boolean login(String email, String password) {
        Log.d("LOGIN", "Start server setup");

        //create a FileSourceConnector, used to read and write to the server.
        FileSourceConnector fileSourceConnector = new FileSourceConnector();
        fileSourceConnector.execute(email, password, "readUser");

        //wait until async task is over with because I can't do network operations on the
        //UI thread, so I have to use an async task.

        /* TODO: Show progress */
        while(!fileSourceConnector.isDone()) {}
        return true;
    }

    /**
     * Used to start the Login Activity
     * @param current activity
     */
    public static void startLoginActivity(Activity current) {
        Intent intent = new Intent(current, LoginActivity.class);
        current.startActivity(intent);
    }
}
