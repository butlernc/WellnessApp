package com.uwec.wellnessapp.data;

/**
 * Created by butlernc on 12/9/2014.
 */
public class GoalData {

    private String week;
    private String physical_activity;
    private String nutrition_goal;

    private String nutrition_goal_description;
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

    public String getNutrition_goal() {
        return nutrition_goal;
    }

    public void setNutrition_goal(String nutrition_goal) {
        this.nutrition_goal = nutrition_goal;
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
