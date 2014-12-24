package com.uwec.wellnessapp.statics;

import com.uwec.wellnessapp.data.GoalData;
import com.uwec.wellnessapp.data.UserData;

/**
 * Created by Noah Butler on 12/23/2014.
 *
 * Used to hold the static objects in the app
 */
public class Statics {

    private static UserData globalUserData;
    private static GoalData globalGoalData;

    public static UserData getGlobalUserData() {
        return globalUserData;
    }

    public static void setGlobalUserData(UserData globalUserData) {
        Statics.globalUserData = globalUserData;
    }

    public static GoalData getGlobalGoalData() {
        return globalGoalData;
    }

    public static void setGlobalGoalData(GoalData globalGoalData) {
        Statics.globalGoalData = globalGoalData;
    }
}
