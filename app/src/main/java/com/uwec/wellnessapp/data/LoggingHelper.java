package com.uwec.wellnessapp.data;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.uwec.wellnessapp.R;
import com.uwec.wellnessapp.statics.Statics;
import com.uwec.wellnessapp.utils.FileSourceConnector;

/**
 * Created by Noah Butler on 1/20/2015.
 * Used to calculate the users points and has all the functionality to
 * spawn a thread that will save the userData to the server (to that you
 * create a new object of this and start it, like a thread).
 */
public class LoggingHelper {

    private Context context;
    private Activity activity;

    private int option;
    /**
     *
     * @param context
     * @param option
     * 0: physical
     * 1: nutrition
     * 2: bonus points
     */

    public LoggingHelper(Context context, Activity activity, int option) {
        this.context = context;
        this.activity = activity;
        this.option = option;
    }

    /**
     * Saves the current user data to the corresponding file in the database
     */
    public void logPoints() {
        Statics.writeToServer = true;
        int current_temp = 0;

        switch(option) {
            case 0:
                current_temp = Statics.getUsersCurrentWeekData().getPhysicalGoalPoints();
                Statics.getUsersCurrentWeekData().setPhysicalGoalPoints(current_temp + 2);
                break;
            case 1:
                current_temp = Statics.getUsersCurrentWeekData().getNutritionGoalPoints();
                Statics.getUsersCurrentWeekData().setNutritionGoalPoints(current_temp + 1);
                break;
            default:
                break;
        }
        int currentBonusPoints = 0;
        for(int i = 0; i < 12; i++) {
            currentBonusPoints += Statics.getUsersCurrentWeekData().getBonusPoints()[i];
        }

        Statics.globalUserData.setWeekly_score(Statics.getUsersCurrentWeekData().getNutritionGoalPoints() + Statics.getUsersCurrentWeekData().getPhysicalGoalPoints() + currentBonusPoints);

        calculateWeekPoints();
        calculateTotalPoints();

        /* save data to phone, where it waits to written to the server */
        FileSourceConnector fileSourceConnector = new FileSourceConnector(context);
        switch (option) {
            case 0:
                fileSourceConnector.queue("writePaPointsCache");
                break;
            case 1:
                fileSourceConnector.queue("writeNgPointsCache");
                break;
            case 2:
                fileSourceConnector.queue("writeBonusPointsCache");
                break;
        }

        Statics.messenger.sendMessage("SaveToServer");
    }

    public void calculateWeekPoints() {

        /* calculate weekly points for physical activities */
        int temp = 0;
        for(int i = 0; i < Statics.getUsersCurrentWeekData().getPhysicalGoalCheckOffs().size(); i++) {
            if(Statics.getUsersCurrentWeekData().getPhysicalGoalCheckOffs().get(i)) {
                temp += 2;
            }
        }
        /* add on the nutrition points to the total weekly point tally */
        for(int i = 0; i < Statics.getUsersCurrentWeekData().getNutritionGoalCheckOffs().size(); i++) {
            if(Statics.getUsersCurrentWeekData().getNutritionGoalCheckOffs().get(i)) {
                temp += 1;
            }
        }

        for(int i = 0; i < Statics.getUsersCurrentWeekData().getBonusPoints().length; i++) {
            temp += Statics.getUsersCurrentWeekData().getBonusPoints()[i];
        }

        /* record what we calculated to the user's current week's weekly score */
        Statics.globalUserData.setWeekly_score(temp);

    }

    public void calculateTotalPoints() {
        Statics.globalUserData.setTotal_score(0);
        int total = 0;

        /* loop for each week */
        for(int i = 0; i < Statics.globalUserData.getWeeklyData().size(); i++) {
            int temp = 0;
            /* loop through each week's physical activity points */
            for(int j = 0; j < Statics.globalUserData.getWeeklyData().get(i).getPhysicalGoalCheckOffs().size(); j++) {
                if(Statics.globalUserData.getWeeklyData().get(i).getPhysicalGoalCheckOffs().get(j)) {
                    temp += 2;
                }
            }

            /* loop through each week's nutrition points */
            for(int j = 0; j < Statics.globalUserData.getWeeklyData().get(i).getNutritionGoalCheckOffs().size(); j++) {
                if(Statics.globalUserData.getWeeklyData().get(i).getNutritionGoalCheckOffs().get(j)) {
                    temp += 1;
                }
            }

            /* loop through bonus points */
            for(int j = 0; j < Statics.globalUserData.getWeeklyData().get(i).getBonusPoints().length; j++) {
                temp += Statics.globalUserData.getWeeklyData().get(i).getBonusPoints()[j];
            }

            total += temp;
        }
        Statics.globalUserData.setTotal_score(total);
    }

    public int tallyAllFitness() {

        int temp = 0;

        /* loop for each week */
        for(int i = 0; i < Statics.globalUserData.getWeeklyData().size(); i++) {

            /* loop through each week's physical activity points */
            for(int j = 0; j < Statics.globalUserData.getWeeklyData().get(i).getPhysicalGoalCheckOffs().size(); j++) {
                if(Statics.globalUserData.getWeeklyData().get(i).getPhysicalGoalCheckOffs().get(j)) {
                    temp += 2;
                }
            }
        }

        return temp;
    }

    public int tallyAllNutrition() {
        int temp = 0;

        /* loop through each week */
        for(int i = 0; i < Statics.globalUserData.getWeeklyData().size(); i++) {

            /* loop through each week's nutrition points */
            for(int j = 0; j < Statics.globalUserData.getWeeklyData().get(i).getNutritionGoalCheckOffs().size(); j++) {
                if(Statics.globalUserData.getWeeklyData().get(i).getNutritionGoalCheckOffs().get(j)) {
                    temp += 1;
                }
            }
        }

        return temp;
    }

    public int tallyAllBonusPoints() {
        int temp = 0;

        /* loop through each week */
        for(int i = 0; i < Statics.globalUserData.getWeeklyData().size(); i++) {

            /* loop through each week's Bonus Points */
            for(int j = 0; j < Statics.globalUserData.getWeeklyData().get(i).getBonusPoints().length; j++) {
                temp += Statics.globalUserData.getWeeklyData().get(i).getBonusPoints()[j];
            }
        }

        return temp;
    }
}
