package com.uwec.wellnessapp.utils;

import android.util.Log;

import com.uwec.wellnessapp.data.WeekData;
import com.uwec.wellnessapp.data.UserData;
import com.uwec.wellnessapp.data.WeeklyUserData;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Noah Butler on 12/5/2014.
 *
 * Class used to create json object from the UserData and GoalData object
 * and vice versa.
 */
public class JsonFileConverter {

    private static String json_value_names[] = {"email", "first_name", "last_name", "password", "total_score", "weekly_data"};
    private static String week_data_json_value_names[] = {"week", "physical_activity", "physical_activity_description", "pa_days_per_week", "pa_strings", "nutrition_goal", "nutrition_goal_description", "ng_days_per_week", "ng_strings", "supporting_evidence"};
    private static String weeklyData_json_value_names[] = {"pa_points", "ng_points", "pa_amount", "ng_amount", "pa_checkOff", "pa_checkOffArray", "ng_checkOff", "ng_checkOffArray"};

    public UserData convertJSONToUser(JSONObject jsonObject) throws JSONException {
        UserData userData = new UserData();
        userData.setEmail(jsonObject.getString(json_value_names[0]));
        userData.setFirst_name(jsonObject.getString(json_value_names[1]));
        userData.setLast_name(jsonObject.getString(json_value_names[2]));
        userData.setPassword(jsonObject.getString(json_value_names[3]));
        userData.setTotal_score(jsonObject.getInt(json_value_names[4]));
        userData.setWeeklyData(jsonToWeeklyDataArray(jsonObject.getJSONObject(json_value_names[5])));

        return userData;
    }

    public WeekData convertWeekDataJSON(JSONObject jsonObject) throws JSONException {
        WeekData weekData = new WeekData();
        weekData.setWeek(jsonObject.getString(week_data_json_value_names[0]));
        weekData.setPhysical_activity(jsonObject.getString(week_data_json_value_names[1]));
        weekData.setPhysical_activity_description(jsonObject.getString(week_data_json_value_names[2]));
        weekData.setPa_days_per_week(jsonObject.getInt(week_data_json_value_names[3]));

        JSONObject pa_button_strings_json = jsonObject.getJSONObject(week_data_json_value_names[4]);
        ArrayList<String> pa_button_strings = new ArrayList<>();
        for(int i = 0; i < weekData.getPa_days_per_week(); i++) {
            pa_button_strings.add(pa_button_strings_json.getString("" + i));
        }
        weekData.setPa_strings(pa_button_strings);

        weekData.setNutrition_goal(jsonObject.getString(week_data_json_value_names[5]));
        weekData.setNutrition_goal_description(jsonObject.getString(week_data_json_value_names[6]));
        weekData.setNg_days_per_week(jsonObject.getInt(week_data_json_value_names[7]));

        JSONObject ng_button_strings_json = jsonObject.getJSONObject(week_data_json_value_names[8]);
        ArrayList<String> ng_button_strings = new ArrayList<>();
        for(int i = 0; i < weekData.getNg_days_per_week(); i++) {
            ng_button_strings.add(ng_button_strings_json.getString("" + i));
        }
        weekData.setNg_strings(ng_button_strings);

        weekData.setSupporting_evidence(jsonObject.getString(week_data_json_value_names[9]));

        return weekData;
    }

    public JSONObject convertUserToJSON(UserData userData) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(json_value_names[0], userData.getEmail());
        jsonObject.put(json_value_names[1], userData.getFirst_name());
        jsonObject.put(json_value_names[2], userData.getLast_name());
        jsonObject.put(json_value_names[3], userData.getPassword());
        jsonObject.put(json_value_names[4], userData.getTotal_score());
        jsonObject.put(json_value_names[5], weeklyDataArrayToJson(userData.getWeeklyData()));

        Log.e("JSON", "" + jsonObject.toString());

        return jsonObject;
    }

    public JSONObject weeklyDataArrayToJson(ArrayList<WeeklyUserData> weeklyData) throws JSONException {
        JSONObject jsonObject = new JSONObject();

        for(int i = 0; i < weeklyData.size(); i++) {
            WeeklyUserData single_weeklyData = weeklyData.get(i);
            JSONObject subJsonObject = new JSONObject();
            subJsonObject.put(weeklyData_json_value_names[0], single_weeklyData.getPhysicalGoalPoints());
            subJsonObject.put(weeklyData_json_value_names[1], single_weeklyData.getNutritionGoalPoints());
            subJsonObject.put(weeklyData_json_value_names[2], single_weeklyData.getPhysicalGoalCheckOffAmount());
            subJsonObject.put(weeklyData_json_value_names[3], single_weeklyData.getNutritionGoalCheckOffAmount());

            JSONObject pa_checkOffs_jsonObject = new JSONObject();
            for(int j = 0; j < single_weeklyData.getPhysicalGoalCheckOffs().size(); j++) {
                pa_checkOffs_jsonObject.put(weeklyData_json_value_names[4] + j, single_weeklyData.getPhysicalGoalCheckOffs().get(j));
            }
            subJsonObject.put(weeklyData_json_value_names[5], pa_checkOffs_jsonObject);
            subJsonObject.put("pa_checkOff_size", single_weeklyData.getPhysicalGoalCheckOffs().size());

            JSONObject ng_checkOffs_jsonObject = new JSONObject();
            for(int j = 0; j < single_weeklyData.getNutritionGoalCheckOffs().size(); j++) {
                ng_checkOffs_jsonObject.put(weeklyData_json_value_names[6] + j, single_weeklyData.getNutritionGoalCheckOffs().get(j));
            }
            subJsonObject.put(weeklyData_json_value_names[7], ng_checkOffs_jsonObject);
            subJsonObject.put("ng_checkOff_size", single_weeklyData.getNutritionGoalCheckOffs().size());

            jsonObject.put("weeklyData" + i, subJsonObject);
            jsonObject.put("size", weeklyData.size());
        }

        return jsonObject;
    }

    public ArrayList<WeeklyUserData> jsonToWeeklyDataArray(JSONObject baseJsonObject) throws JSONException{
        ArrayList<WeeklyUserData> weeklyUserDataList = new ArrayList<>();
        for(int i = 0; i < baseJsonObject.getInt("size"); i++) {
            WeeklyUserData weeklyUserData = new WeeklyUserData();
            JSONObject subJsonObject = baseJsonObject.getJSONObject("weeklyData" + i);

            weeklyUserData.setPhysicalGoalPoints(subJsonObject.getInt(weeklyData_json_value_names[0]));
            weeklyUserData.setNutritionGoalPoints(subJsonObject.getInt(weeklyData_json_value_names[1]));
            weeklyUserData.setPhysicalGoalCheckOffAmount(subJsonObject.getInt(weeklyData_json_value_names[2]));
            weeklyUserData.setNutritionGoalCheckOffAmount(subJsonObject.getInt(weeklyData_json_value_names[3]));

            JSONObject pa_checkOffs_jsonObject = subJsonObject.getJSONObject(weeklyData_json_value_names[5]);
            ArrayList<Boolean> physicalGoalCheckOffs = new ArrayList<>();
            for(int j = 0; j < subJsonObject.getInt("pa_checkOff_size"); j++) {
                physicalGoalCheckOffs.add(pa_checkOffs_jsonObject.getBoolean(weeklyData_json_value_names[4] + j));
            }
            weeklyUserData.setPhysicalGoalCheckOffs(physicalGoalCheckOffs);

            JSONObject ng_checkOffs_jsonObject = subJsonObject.getJSONObject(weeklyData_json_value_names[7]);
            ArrayList<Boolean> nutritionGoalCheckOffs = new ArrayList<>();
            for(int j = 0; j < subJsonObject.getInt("ng_checkOff_size"); j++) {
                nutritionGoalCheckOffs.add(ng_checkOffs_jsonObject.getBoolean(weeklyData_json_value_names[6] + j));
            }
            weeklyUserData.setNutritionGoalCheckOffs(nutritionGoalCheckOffs);

            weeklyUserDataList.add(weeklyUserData);
        }

        return weeklyUserDataList;
    }
}
