package com.uwec.wellnessapp.data;

import java.util.ArrayList;

/**
 * Created by Noah Butler on 12/2/2014.
 * the global UserData object that will be used to store
 * the user's app data is stored in Statics
 */


public class UserData {

    private String first_name;
    private String last_name;

    private String email;
    private String password;

    private int total_score;

    /* this will hold all of the data for each week */
    private ArrayList<WeeklyData> weeklyData;

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTotal_score() {
        return total_score;
    }

    public void setTotal_score(int total_score) {
        this.total_score = total_score;
    }

    public ArrayList<WeeklyData> getWeeklyData() {
        return weeklyData;
    }

    public void setWeeklyData(ArrayList<WeeklyData> weeklyData) {
        this.weeklyData = weeklyData;
    }

    /**
     * This inner class will be used to store the user's data for each week
     *
     */

    public class WeeklyData {

        private int physicalGoalCheckOffAmount;
        private int nutritionalGoalCheckOffAmount;
        private ArrayList<Boolean> physicalGoalCheckOffs;
        private ArrayList<Boolean> nutritionGoalCheckOffs;

        private int physicalGoalPoints;
        private int nutritionGoalPoints;

        public WeeklyData(int physicalGoalCheckOffAmount, int nutritionalGoalCheckOffAmount) {
            /* assign the correct amount of check offs for the given goal for the week */
            this.nutritionalGoalCheckOffAmount = nutritionalGoalCheckOffAmount;
            this.physicalGoalCheckOffAmount    = physicalGoalCheckOffAmount;

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

        public ArrayList<Boolean> getNutritionGoalCheckOffs() {
            return nutritionGoalCheckOffs;
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
}
