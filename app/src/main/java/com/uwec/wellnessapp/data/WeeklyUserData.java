package com.uwec.wellnessapp.data;

import android.util.Log;

import com.uwec.wellnessapp.statics.Statics;

import java.util.ArrayList;

/**
 * Created by Noah Butler on 1/9/2015.
 * Used as a framework for the user's weekly data
 * There is one of these object saved in the user's data
 * on the server for each week of the program.
 *
 * The ArrayList that holds all of these objects is the UserData object.
 */
public class WeeklyUserData {

    private int physicalGoalCheckOffAmount;
    private int nutritionGoalCheckOffAmount;
    /* keeps track of the 5 fitness activities/goals that the user has completed for the given week */
    private ArrayList<Boolean> physicalGoalCheckOffs;
    /* keeps track of the nutrition goals that the user has completed for the given week */
    private ArrayList<Boolean> nutritionGoalCheckOffs;

    /* the amount of physical goal/activity points the user has gained for the given week */
    private int physicalGoalPoints;
    /* the amount of nutrition goal points the user has gained for the given week */
    private int nutritionGoalPoints;

    /* the user's total score of the program at the during and at the end of the given week */
    private int snapShotTotalScore;
    /* the user's current score of that week only */
    private int snapShotWeekScore;
    /* bonus points held here for each week*/
    private int[] bonusPoints;
    /*week associated with, held for testing */
    private int week;

    /* this constructor is used when first creating the user, so we can zero all of their data */
    public WeeklyUserData(int week) {

        /* assign the correct amount of check offs for the given goal for the week */
        physicalGoalCheckOffAmount  = Statics.globalWeekDataList.get(week).getPa_days_per_week();
        nutritionGoalCheckOffAmount = Statics.globalWeekDataList.get(week).getNg_days_per_week();
        /* zero points initially */
        physicalGoalPoints  = 0;
        nutritionGoalPoints = 0;
        /* holds whether they completed the check offs or not */
        physicalGoalCheckOffs  = new ArrayList<>();
        nutritionGoalCheckOffs = new ArrayList<>();

        /* when the weekly data object is created, default all check offs to completed as false */
        for(int i = 0; i < physicalGoalCheckOffAmount; i++) {
            physicalGoalCheckOffs.add(false);
        }

        /* same thing as above, just for nutrition goal now */
        for(int i = 0; i < nutritionGoalCheckOffAmount; i++) {
            nutritionGoalCheckOffs.add(false);
        }

        /* bonus points holder */
        bonusPoints = new int[12];
        for(int i = 0; i < 12; i++) {
            bonusPoints[i] = 0;
        }
    }

    public WeeklyUserData() {}

    public int getPhysicalGoalCheckOffAmount() {
        return physicalGoalCheckOffAmount;
    }

    public void setPhysicalGoalCheckOffAmount(int physicalGoalCheckOffAmount) {
        this.physicalGoalCheckOffAmount = physicalGoalCheckOffAmount;
    }

    public int getNutritionGoalCheckOffAmount() {
        return nutritionGoalCheckOffAmount;
    }

    public void setNutritionGoalCheckOffAmount(int nutritionGoalCheckOffAmount) {
        this.nutritionGoalCheckOffAmount = nutritionGoalCheckOffAmount;
    }

    public ArrayList<Boolean> getPhysicalGoalCheckOffs() {
        return physicalGoalCheckOffs;
    }

    public void setPhysicalGoalCheckOffs(ArrayList<Boolean> physicalGoalCheckOffs) {
        this.physicalGoalCheckOffs = physicalGoalCheckOffs;
    }

    public ArrayList<Boolean> getNutritionGoalCheckOffs() {
        return nutritionGoalCheckOffs;
    }

    public void setNutritionGoalCheckOffs(ArrayList<Boolean> nutritionGoalCheckOffs) {
        this.nutritionGoalCheckOffs = nutritionGoalCheckOffs;
    }

    public int getPhysicalGoalPoints() {
        return physicalGoalPoints;
    }

    public void setPhysicalGoalPoints(int physicalGoalPoints) {
        this.physicalGoalPoints = physicalGoalPoints;
    }

    public int getNutritionGoalPoints() {
        return nutritionGoalPoints;
    }

    public void setNutritionGoalPoints(int nutritionGoalPoints) {
        this.nutritionGoalPoints = nutritionGoalPoints;
    }

    public int getSnapShotTotalScore() {
        return snapShotTotalScore;
    }

    public void setSnapShotTotalScore(int snapShotTotalScore) {
        this.snapShotTotalScore = snapShotTotalScore;
    }

    public int getSnapShotWeekScore() {
        return snapShotWeekScore;
    }

    public void setSnapShotWeekScore(int snapShotWeekScore) {
        Log.d("SETTING", "Week Snap: " + snapShotWeekScore);
        this.snapShotWeekScore = snapShotWeekScore;
    }

    public int[] getBonusPoints() {
        return bonusPoints;
    }

    public void setBonusPoints(int[] bonusPoints) {
        this.bonusPoints = bonusPoints;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" -Current Total Points: " + getSnapShotTotalScore() + "\n");
        sb.append(" -Current Week Score: " + getSnapShotWeekScore() + "\n");
        sb.append(" -Current Physical Points: " + getPhysicalGoalPoints() + "\n");
        sb.append(" -Current Nutritional Points: " + getNutritionGoalPoints() + "\n");

        sb.append("  - Bonus Points: [");
        for (int j = 0; j < getBonusPoints().length; j++) {
            sb.append(getBonusPoints()[j] + ",");
        }
        sb.append("] \n");

        sb.append("  - Fitness Points: [");
        for (int j = 0; j < getPhysicalGoalCheckOffs().size(); j++) {
            sb.append(getPhysicalGoalCheckOffs().get(j) + ",");
        }
        sb.append("] \n");

        sb.append("  - Nutrition Points: [");
        for (int j = 0; j < getNutritionGoalCheckOffs().size(); j++) {
            sb.append(getNutritionGoalCheckOffs().get(j) + ",");
        }
        sb.append("] \n");
        sb.append("END OF WEEK: " + week);
        sb.append("\n \n");

        return sb.toString();
    }
}

