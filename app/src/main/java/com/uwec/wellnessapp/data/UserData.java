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

    protected class WeeklyData {

        private int checkOffAmount;
        private ArrayList<Boolean> checkOffs;

        private int physicalGoalPoints;
        private int nutritionGoalPoints;

        public WeeklyData(int checkOffAmount) {
            this.checkOffAmount = checkOffAmount;
            checkOffs = new ArrayList<Boolean>();
            for(int i = 0; i < checkOffAmount; i++) {
                checkOffs.add(false);
            }
        }

        public int getCheckOffAmount() {
            return checkOffAmount;
        }

    }

}
