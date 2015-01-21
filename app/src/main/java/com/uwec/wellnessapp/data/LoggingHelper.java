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
    private boolean isPhysical;

    public LoggingHelper(Context context, boolean isPhysical) {
        this.context = context;
        this.isPhysical = isPhysical;
    }

    /**
     * Saves the current user data to the corresponding file in the database
     */
    public void run() {

        int current_temp;
        if(isPhysical) {
            current_temp = Statics.getUsersCurrentWeekData().getPhysicalGoalPoints();
            Statics.getUsersCurrentWeekData().setPhysicalGoalPoints(current_temp + 5);
        } else {
            current_temp = Statics.getUsersCurrentWeekData().getNutritionGoalPoints();
            Statics.getUsersCurrentWeekData().setNutritionGoalPoints(current_temp + 5);
        }

        /* TODO: add weeklytotal to each weeklyuserdata */
        Statics.globalUserData.setWeekly_score(Statics.getUsersCurrentWeekData().getNutritionGoalPoints() + Statics.getUsersCurrentWeekData().getPhysicalGoalPoints());

        calculateWeekPoints();
        calculateTotalPoints();

        FileSourceConnector fileSourceConnector = new FileSourceConnector();
        fileSourceConnector.setContext(context);
        fileSourceConnector.queue("writeUser", Statics.globalUserData.getEmail(), "old");
        Log.e("YUP", "SAVING USER DATA BITCHES");
    }

    public void calculateWeekPoints() {

        int temp = 0;
        for(int i = 0; i < Statics.getUsersCurrentWeekData().getPhysicalGoalCheckOffs().size(); i++) {
            if(Statics.getUsersCurrentWeekData().getPhysicalGoalCheckOffs().get(i)) {
                temp += 5;
            }
        }

        for(int i = 0; i < Statics.getUsersCurrentWeekData().getNutritionGoalCheckOffs().size(); i++) {
            if(Statics.getUsersCurrentWeekData().getNutritionGoalCheckOffs().get(i)) {
                temp += 5;
            }
        }

        Statics.globalUserData.setWeekly_score(temp);

    }

    public void calculateTotalPoints() {
        Statics.globalUserData.setTotal_score(0);
        int total = 0;

        for(int i = 0; i < Statics.globalUserData.getWeeklyData().size(); i++) {
            int temp = 0;
            for(int j = 0; j < Statics.globalUserData.getWeeklyData().get(i).getPhysicalGoalCheckOffs().size(); j++) {
                if(Statics.globalUserData.getWeeklyData().get(i).getPhysicalGoalCheckOffs().get(j)) {
                    temp += 5;
                }
            }
            for(int j = 0; j < Statics.globalUserData.getWeeklyData().get(i).getNutritionGoalCheckOffs().size(); j++) {
                if(Statics.globalUserData.getWeeklyData().get(i).getNutritionGoalCheckOffs().get(j)) {
                    temp += 5;
                }
            }

            total += temp;
        }
        Statics.globalUserData.setTotal_score(total);
    }

    public int tallyAllFitness() {

        int temp = 0;

        for(int i = 0; i < Statics.globalUserData.getWeeklyData().size(); i++) {
            for(int j = 0; j < Statics.globalUserData.getWeeklyData().get(i).getPhysicalGoalCheckOffs().size(); j++) {
                if(Statics.globalUserData.getWeeklyData().get(i).getPhysicalGoalCheckOffs().get(j)) {
                    temp += 5;
                }
            }
        }

        return temp;
    }

    public int tallyAllNutrition() {
        int temp = 0;

        for(int i = 0; i < Statics.globalUserData.getWeeklyData().size(); i++) {
            for(int j = 0; j < Statics.globalUserData.getWeeklyData().get(i).getNutritionGoalCheckOffs().size(); j++) {
                if(Statics.globalUserData.getWeeklyData().get(i).getNutritionGoalCheckOffs().get(j)) {
                    temp += 5;
                }
            }
        }

        return temp;
    }
}
