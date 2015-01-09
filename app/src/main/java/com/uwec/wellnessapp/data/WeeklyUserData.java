package com.uwec.wellnessapp.data;

import java.util.ArrayList;

/**
 * Created by butlernc on 1/9/2015.
 */
public class WeeklyUserData {

    private int physicalGoalCheckOffAmount;
    private int nutritionalGoalCheckOffAmount;
    private ArrayList<Boolean> physicalGoalCheckOffs;
    private ArrayList<Boolean> nutritionGoalCheckOffs;

    private int physicalGoalPoints;
    private int nutritionGoalPoints;

    public WeeklyUserData() {
            /* assign the correct amount of check offs for the given goal for the week */

            /* holds whether they completed the check offs or not */
        physicalGoalCheckOffs  = new ArrayList<Boolean>();
        nutritionGoalCheckOffs = new ArrayList<Boolean>();

            /* when the weekly data object is created, default all check offs to completed as false */
        for(int i = 0; i < physicalGoalCheckOffAmount; i++) {
            physicalGoalCheckOffs.add(false);
        }

        for(int i = 0; i < nutritionalGoalCheckOffAmount; i++) {
            nutritionGoalCheckOffs.add(false);
        }
    }

    public int getPhysicalGoalCheckOffAmount() {
        return physicalGoalCheckOffAmount;
    }

    public void setPhysicalGoalCheckOffAmount(int physicalGoalCheckOffAmount) {
        this.physicalGoalCheckOffAmount = physicalGoalCheckOffAmount;
    }

    public int getNutritionalGoalCheckOffAmount() {
        return nutritionalGoalCheckOffAmount;
    }

    public void setNutritionalGoalCheckOffAmount(int nutritionalGoalCheckOffAmount) {
        this.nutritionalGoalCheckOffAmount = nutritionalGoalCheckOffAmount;
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
}

