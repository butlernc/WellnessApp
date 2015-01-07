package com.uwec.wellnessapp.utils;

import android.util.Log;

import com.uwec.wellnessapp.data.WeekData;
import com.uwec.wellnessapp.data.UserData;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Noah Butler on 12/5/2014.
 *
 * Class used to create json object from the UserData and GoalData object
 * and vice versa.
 */
public class JsonFileConverter {

    private static String json_value_names[] = {"email", "first_name", "last_name", "password", "total_score", "weekly_data"};
    private static String week_data_json_value_names[] = {"week", "physical_activity", "physical_activity_description", "pa_days_per_week", "nutrition_goal", "nutrition_goal_description", "ng_days_per_week", "supporting_evidence"};

    public UserData convertUserDataJSON(JSONObject jsonObject) throws JSONException {
        UserData userData = new UserData();
        userData.setEmail(jsonObject.getString(json_value_names[0]));
        userData.setFirst_name(jsonObject.getString(json_value_names[1]));
        userData.setLast_name(jsonObject.getString(json_value_names[2]));
        userData.setPassword(jsonObject.getString(json_value_names[3]));
        userData.setTotal_score(jsonObject.getInt(json_value_names[4]));
        //TODO: enable weeklyData in the userObject
        //userData.setWeeklyData((ArrayList<UserData.WeeklyData>)jsonObject.get(json_value_names[5]));

        return userData;
    }

    public WeekData convertWeekDataJSON(JSONObject jsonObject) throws JSONException {
        WeekData weekData = new WeekData();
        weekData.setWeek(jsonObject.getString(week_data_json_value_names[0]));
        weekData.setPhysical_activity(jsonObject.getString(week_data_json_value_names[1]));
        weekData.setPhysical_activity_description(jsonObject.getString(week_data_json_value_names[2]));
        weekData.setPa_days_per_week(jsonObject.getInt(week_data_json_value_names[3]));
        weekData.setNutrition_goal(jsonObject.getString(week_data_json_value_names[4]));
        weekData.setNutrition_goal_description(jsonObject.getString(week_data_json_value_names[5]));
        weekData.setNg_days_per_week(jsonObject.getInt(week_data_json_value_names[6]));
        weekData.setSupporting_evidence(jsonObject.getString(week_data_json_value_names[7]));

        return weekData;
    }

    public JSONObject convertToJSON(UserData userData) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(json_value_names[0], userData.getEmail());
        jsonObject.put(json_value_names[1], userData.getFirst_name());
        jsonObject.put(json_value_names[2], userData.getLast_name());
        jsonObject.put(json_value_names[3], userData.getPassword());
        jsonObject.put(json_value_names[4], userData.getTotal_score());
        jsonObject.put(json_value_names[5], userData.getWeeklyData());

        Log.e("JSON", "" + jsonObject.toString());

        return jsonObject;
    }
}
