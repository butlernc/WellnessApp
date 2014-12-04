package com.uwec.wellnessapp.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.util.Log;

/**
 * Created by butlernc on 12/2/2014.
 *
 * Used as static reference for basic user log information
 */
public class LoginHelper {

    private static boolean isLogged;

    /**
     * FileIO Helper Object
     */
    private static FileSourceConnector fileSourceConnector;

    static {
        fileSourceConnector = new FileSourceConnector();
    }

    public LoginHelper(boolean isLogged) {
        this.isLogged = isLogged;
    }

    public static boolean isLogged() {
        return isLogged;
    }

    public static void setLogged(boolean set) {
        isLogged = set;
    }

    /**
     *TEST PUSH FROM ANDROID STUDIO
     * Used to log a user into the system
     *
     * @param email
     * @param password
     * @return boolean
     */
    public static boolean login(String email, String password) {
        boolean email_exists = false;
        boolean correct_password = false;
        //check database for email
        email_exists = true;
        Log.d("LOGIN", "Start server setup");
        new FileSourceConnector().execute(email, password, "create");

        if(email_exists) {
            //check database for correct password
            correct_password = true;
        }

        if(correct_password) {
            setLogged(true);
            return true;
        }
        return false;
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
