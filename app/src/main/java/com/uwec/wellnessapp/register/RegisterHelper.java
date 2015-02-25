package com.uwec.wellnessapp.register;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
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

    private RegisterActivity activity;
    private Context context;

    private String[] input;
    private boolean worked;
    private boolean isDone;
    private boolean notConfirmedP;

    public RegisterHelper(Context context, Activity activity, String...input) {
        this.activity = (RegisterActivity)activity;
        this.context = context;
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

    @Override
    public void run() {
        Statics.registrationIsComplete = false;
        isDone = false;
        String confirmP = input[4];
        String password = input[3];
        if (confirmP.contentEquals(password)) {
            UserData userData = new UserData();
            userData.setLast_name(input[0]);
            userData.setFirst_name(input[1]);
            userData.setEmail(input[2]);
            userData.setPassword(input[3]);
            userData.setTotal_score(0);

            for (int i = 0; i < Statics.weeks.length; i++) {
                userData.getWeeklyData().add(new WeeklyUserData(i));
            }

            for(int i = 6; i < 12; i++) {
                userData.getOneTimeBonusPoints().put(i, false);
            }

            Statics.globalUserData = userData;

            //create a FileSourceConnector, used to read and write to the server.
            Statics.messenger.registering("Creating New User File...");
            FileSourceConnector fileSourceConnector = new FileSourceConnector(activity.getBaseContext());
            fileSourceConnector.queue("writeUser", userData.getEmail(), userData.getPassword());
            Log.e("REGISTER", "Return string: " + fileSourceConnector.getRETURN_STR());
            if (fileSourceConnector.getRETURN_STR().contentEquals("GOOD")) {
                worked = true;
                Statics.messenger.registering("Finishing...");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (fileSourceConnector.getRETURN_STR().contentEquals("NOGOOD")) {
                worked = false;
                notConfirmedP = false;
            }

            isDone = true;
            Statics.registrationIsComplete = true;
        }else{
            notConfirmedP = true;
            worked = false;
        }

        finish();

        Log.e("REGISTER", "Register worked?: " + worked);
    }

    private void finish() {
        if(worked) {
            Looper.prepare();
            Statics.messenger.sendToastMessage("Account creation was successful!");
            LoginHelper.startLoginActivity(activity);
        }else {
            if(notConfirmedP) {
                Statics.messenger.sendToastMessage("Your passwords do not match, please retry.");
            }else{
                Statics.messenger.sendToastMessage("There was an error creating the account, try again.");
            }

        }
    }

}
