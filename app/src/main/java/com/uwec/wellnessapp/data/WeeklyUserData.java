package com.uwec.wellnessapp.data;

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
        this.snapShotWeekScore = snapShotWeekScore;
    }
}

