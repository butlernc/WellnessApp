package com.uwec.wellnessapp.statics;

import android.os.Handler;

import com.uwec.wellnessapp.data.SessionData;
import com.uwec.wellnessapp.data.WeekData;
import com.uwec.wellnessapp.data.UserData;
import com.uwec.wellnessapp.login.LoadingFragment;
import com.uwec.wellnessapp.login.LoginHelper;
import com.uwec.wellnessapp.register.RegisterHelper;
import com.uwec.wellnessapp.utils.Messenger;
import com.uwec.wellnessapp.utils.SingleExecutor;

import java.util.concurrent.Executor;

/**
 * Created by Noah Butler on 12/23/2014.
 *
 * Used to hold the static objects in the app
 */
public class Statics {

    public static UserData globalUserData;
    public static WeekData globalWeekData;
    public static SessionData sessionData = new SessionData();

    public static LoginHelper loginHelper;
    public static RegisterHelper registerHelper;
    public static LoadingFragment loadingFragment;

    public static Handler handler;
    public static Messenger messenger = new Messenger();
    public static SingleExecutor singleExecutor = new SingleExecutor();

    public static String[] weeks = {"WEEK_ONE", "WEEK_TWO", "WEEK_THREE", "WEEK_FOUR", "WEEK_FIVE"};
}
