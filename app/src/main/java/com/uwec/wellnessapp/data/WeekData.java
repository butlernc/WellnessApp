package com.uwec.wellnessapp.data;

/**
 * Created by butlernc on 12/9/2014.
 *
 * Used to save the current week's data.
 */
public class WeekData {

    private String week;

    /** name of the current week's physical activity */
    private String physical_activity;
    /** int used to keep track of physical activity check offs there are
      * this will set the physicalGoalCheckOffAmount in the WeeklyData object in UserData */
    private int pa_days_per_week;

    /* name of the current week's nutrition goal */
    private String nutrition_goal;
    /** int used to keep track of nutrition goal check offs there are for the current week
      * this will set the nutritionalGoalCheckOffAmount in the WeeklyData object in UserData */
    private int ng_days_per_week;

    /** detailed desc of the current week's nutrition goal */
    private String nutrition_goal_description;
    /** saves the desc of the current week's supporting_evidence */
    private String supporting_evidence;

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getPhysical_activity() {
        return physical_activity;
    }

    public void setPhysical_activity(String physical_activity) {
        this.physical_activity = physical_activity;
    }

    public int getPa_days_per_week() {
        return pa_days_per_week;
    }

    public void setPa_days_per_week(int pa_days_per_week) {
        this.pa_days_per_week = pa_days_per_week;
    }

    public String getNutrition_goal() {
        return nutrition_goal;
    }

    public void setNutrition_goal(String nutrition_goal) {
        this.nutrition_goal = nutrition_goal;
    }

    public int getNg_days_per_week() {
        return ng_days_per_week;
    }

    public void setNg_days_per_week(int ng_days_per_week) {
        this.ng_days_per_week = ng_days_per_week;
    }

    public String getNutrition_goal_description() {
        return nutrition_goal_description;
    }

    public void setNutrition_goal_description(String nutrition_goal_description) {
        this.nutrition_goal_description = nutrition_goal_description;
    }

    public String getSupporting_evidence() {
        return supporting_evidence;
    }

    public void setSupporting_evidence(String supporting_evidence) {
        this.supporting_evidence = supporting_evidence;
    }
}