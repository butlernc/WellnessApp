package com.uwec.wellnessapp.login;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;

/**
 * Created by butlernc on 12/2/2014.
 *
 * Used as static reference for basic user log information
 */
public class LoginHelper {

    private static boolean isLogged;

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
    public static boolean login(Editable email, Editable password) {
        boolean email_exists = false;
        boolean correct_password = false;
        //check database for email
        email_exists = true;

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

    public static void startLoginActivity(Activity current) {
        Intent intent = new Intent(current, LoginActivity.class);
        current.startActivity(intent);
    }
}
