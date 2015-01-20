package com.uwec.wellnessapp.data;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.uwec.wellnessapp.statics.Statics;
import com.uwec.wellnessapp.utils.FileSourceConnector;

/**
 * Created by butlernc on 1/20/2015.
 */
public class LoggingHelper extends Thread{

    private Context context;

    /**
     * Saves the current user data to the corresponding file in the database
     */
    public void run() {
        /* TODO: calculate user weekly physical points points */
        for(int i = 0; i < Statics.getUsersCurrentWeekData().getPhysicalGoalCheckOffs().size(); i++) {
            if(Statics.getUsersCurrentWeekData().getPhysicalGoalCheckOffs().get(i)) {
                int current = Statics.getUsersCurrentWeekData().getPhysicalGoalPoints();
                Statics.getUsersCurrentWeekData().setPhysicalGoalPoints(current + 5);
            }
        }

        /* TODO: calculate user weekly nutrition goal points */
        for(int i = 0; i < Statics.getUsersCurrentWeekData().getNutritionGoalCheckOffs().size(); i++) {
            if(Statics.getUsersCurrentWeekData().getNutritionGoalCheckOffs().get(i)) {
                int current = Statics.getUsersCurrentWeekData().getNutritionGoalPoints();
                Statics.getUsersCurrentWeekData().setNutritionGoalPoints(current + 5);
            }
        }

        /* TODO: add weeklytotal to each weeklyuserdata */
        Statics.globalUserData.setWeekly_score(Statics.getUsersCurrentWeekData().getNutritionGoalPoints() + Statics.getUsersCurrentWeekData().getPhysicalGoalPoints());

        /* TODO: calculate users total points */
        int temp_total = 0;
        for(int i = 0; i < Statics.globalUserData.getWeeklyData().size(); i++) {
            temp_total += Statics.globalUserData.getWeeklyData().get(i).getPhysicalGoalPoints();
            temp_total += Statics.globalUserData.getWeeklyData().get(i).getNutritionGoalPoints();
        }

        Statics.globalUserData.setTotal_score(temp_total);

        FileSourceConnector fileSourceConnector = new FileSourceConnector();
        fileSourceConnector.setContext(context);
        fileSourceConnector.queue("writeUser", Statics.globalUserData.getEmail(), "old");
        Log.e("YUP", "SAVING USER DATA BITCHES");
    }

    public void setContext(Context context) {
        this.context = context;
    }

}
