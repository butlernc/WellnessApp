package com.uwec.wellnessapp.register;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.uwec.wellnessapp.R;
import com.uwec.wellnessapp.data.UserData;
import com.uwec.wellnessapp.data.WeeklyUserData;
import com.uwec.wellnessapp.login.LoginActivity;
import com.uwec.wellnessapp.login.LoginHelper;
import com.uwec.wellnessapp.statics.Statics;
import com.uwec.wellnessapp.utils.FileSourceConnector;

/**
 * Created by Noah Butler on 12/5/2014.
 *
 * Small wrapper class that will call the will
 * start an async task that will call the server
 * and write our new user to it.
 *
 * also includes a method that will start our register activity
 * easily.
 */
public class RegisterHelper extends Thread{

    private Activity activity;
    private String[] input;
    private boolean worked;
    private boolean isDone;

    public RegisterHelper(Activity activity, String...input) {
        this.activity = activity;
        this.input = input;
    }


    /**
     * Used to start the Register Activity
     * @param current activity
     */
    public static void startRegisterActivity(Activity current) {
        Intent intent = new Intent(current, RegisterActivity.class);
        current.startActivity(intent);
    }

    public void run() {
        synchronized (this) {
            isDone = false;
            UserData userData = new UserData();
            userData.setLast_name(input[0]);
            userData.setFirst_name(input[1]);
            userData.setEmail(input[2]);
            userData.setPassword(input[3]);
            userData.setTotal_score(0);

            for (int i = 0; i < Statics.weeks.length; i++) {
                userData.getWeeklyData().add(new WeeklyUserData(i));
            }

            Statics.globalUserData = userData;

            //create a FileSourceConnector, used to read and write to the server.
            FileSourceConnector fileSourceConnector = new FileSourceConnector(activity.getBaseContext());
            fileSourceConnector.queue("writeUser", userData.getEmail(), "new");
            Log.e("REGISTER", "Return string: " + fileSourceConnector.getRETURN_STR());
            if (fileSourceConnector.getRETURN_STR().contentEquals("GOOD")) {
                worked = true;
            } else if (fileSourceConnector.getRETURN_STR().contentEquals("NOGOOD")) {
                worked = false;
            }

            isDone = true;
            notify();
        }
    }

    public boolean worked() {
        return worked;
    }

    public boolean isDone() {
        return isDone;
    }


}
