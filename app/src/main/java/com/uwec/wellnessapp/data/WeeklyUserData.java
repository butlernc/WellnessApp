package com.uwec.wellnessapp.data;

import com.uwec.wellnessapp.statics.Statics;

import java.util.ArrayList;

/**
 * Created by butlernc on 1/9/2015.
 */
public class WeeklyUserData {

    private int physicalGoalCheckOffAmount;
    private int nutritionGoalCheckOffAmount;
    private ArrayList<Boolean> physicalGoalCheckOffs;
    private ArrayList<Boolean> nutritionGoalCheckOffs;

    private int physicalGoalPoints;
    private int nutritionGoalPoints;

    private int snapShotTotalScore;
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

