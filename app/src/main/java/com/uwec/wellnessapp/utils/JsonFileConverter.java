package com.uwec.wellnessapp.utils;

import android.util.Log;

import com.uwec.wellnessapp.data.BonusData;
import com.uwec.wellnessapp.data.WeekData;
import com.uwec.wellnessapp.data.UserData;
import com.uwec.wellnessapp.data.WeeklyUserData;
import com.uwec.wellnessapp.statics.Statics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Noah Butler on 12/5/2014.
 *
 * Class used to create json object from the UserData and GoalData object
 * and vice versa.
 */
public class JsonFileConverter {

    private static String week_data_json_value_names[]  = {"week", "physical_activity", "physical_activity_description", "pa_link_amount", "pa_links", "pa_days_per_week", "pa_strings", "nutrition_goal", "nutrition_goal_description", "ng_link_amount", "ng_links", "ng_days_per_week", "ng_strings", "suggested_workout_link", "suggested_workout_type", "week_dates"};
    private static String bonusData_json_value_names[]  = {"amount", "titles", "links", "total_per_program", "complete_per_week", "per_completion", "once"};

    /**
     * Convers the BonusData's json object to the BonusData object.
     * @param jsonObject
     * @return
     * @throws JSONException
     */
    public BonusData convertBonusDataJSON(JSONObject jsonObject) throws JSONException {
        BonusData bonusData = new BonusData();

        int amount = jsonObject.getInt(bonusData_json_value_names[0]);

        /* get titles for bonus activities */
        JSONObject jsonTitlesObject = jsonObject.getJSONObject(bonusData_json_value_names[1]);
        bonusData.setTitles(getBonusActivityArrayData(jsonTitlesObject, amount, true));

        /* get links for each title */
        JSONObject jsonLinksObject = jsonObject.getJSONObject(bonusData_json_value_names[2]);
        bonusData.setLinks(getBonusActivityArrayData(jsonLinksObject, amount, true));

        /* get how many times/weeks they can complete the bonus activity */
        JSONObject jsonTotalPerProgram = jsonObject.getJSONObject(bonusData_json_value_names[3]);
        bonusData.setTotalPerProgram(getBonusActivityArrayData(jsonTotalPerProgram, amount));

        /* get how many times the user may do this per week */
        JSONObject jsonCompletePerWeek = jsonObject.getJSONObject(bonusData_json_value_names[4]);
        bonusData.setCompletePerWeek(getBonusActivityArrayData(jsonCompletePerWeek, amount));

        /* get how many points the user can get each time they complete a certain bonus activity */
        JSONObject jsonPerCompletion = jsonObject.getJSONObject(bonusData_json_value_names[5]);
        bonusData.setPerCompletion(getBonusActivityArrayData(jsonPerCompletion, amount));

        /* get the indexes for bonus activities that can only be completed once */
        JSONObject jsonCanCompleteOnce = jsonObject.getJSONObject(bonusData_json_value_names[6]);
        bonusData.setCanCompleteOnce(getBonusActivityArrayData(jsonCanCompleteOnce, 6));

        return bonusData;
    }

    /**
     * Helper methods for the convertBonusDataJSON method
     * @param jsonObject
     * @param arraySize
     * @param placeholder
     * @return
     * @throws JSONException
     */
    private ArrayList<String> getBonusActivityArrayData(JSONObject jsonObject, int arraySize, boolean placeholder) throws JSONException{
        ArrayList<String> outputArray = new ArrayList<>();
        for(int i = 0; i < arraySize; i++) {
            outputArray.add(jsonObject.getString("" + i));
        }
        return outputArray;
    }

    private ArrayList<Integer> getBonusActivityArrayData(JSONObject jsonObject, int arraySize) throws JSONException{
        ArrayList<Integer> outputArray = new ArrayList<>();
        for(int i = 0; i < arraySize; i++) {
            outputArray.add(jsonObject.getInt("" + i));
        }
        return outputArray;
    }

    /**
     * takes the static week data and converts it to a weekData object.
     * @param jsonObject
     * @return
     * @throws JSONException
     */
    public WeekData convertWeekDataJSON(JSONObject jsonObject) throws JSONException {
        WeekData weekData = new WeekData();
        Log.d("WEEK", "week: " + jsonObject.getString("week"));
        weekData.setWeek(jsonObject.getString(week_data_json_value_names[0]));
        weekData.setPhysical_activity(jsonObject.getString(week_data_json_value_names[1]));
        weekData.setPhysical_activity_description(jsonObject.getString(week_data_json_value_names[2]));

        /* get physical activity links for the current week */
        weekData.setPa_link_amount(jsonObject.getInt(week_data_json_value_names[3]));
        JSONObject pa_links_subJSONObject = jsonObject.getJSONObject(week_data_json_value_names[4]);
        ArrayList<String> temp_pa_links = new ArrayList<>();
        for(int i = 0; i < weekData.getPa_link_amount(); i++) {
            temp_pa_links.add(pa_links_subJSONObject.getString("" + i));
        }
        weekData.setPa_links(temp_pa_links);

        weekData.setPa_days_per_week(jsonObject.getInt(week_data_json_value_names[5]));

        JSONObject pa_button_strings_json = jsonObject.getJSONObject(week_data_json_value_names[6]);
        ArrayList<String> pa_button_strings = new ArrayList<>();
        for(int i = 0; i < weekData.getPa_days_per_week(); i++) {
            pa_button_strings.add(pa_button_strings_json.getString("" + i));
        }
        weekData.setPa_strings(pa_button_strings);

        /* set nutrition goal and desc for the current week */
        weekData.setNutrition_goal(jsonObject.getString(week_data_json_value_names[7]));
        weekData.setNutrition_goal_description(jsonObject.getString(week_data_json_value_names[8]));

        /* get nutrition goal links for the current week */
        weekData.setNg_link_amount(jsonObject.getInt(week_data_json_value_names[9]));
        JSONObject ng_links_subJSONObject = jsonObject.getJSONObject(week_data_json_value_names[10]);
        ArrayList<String> temp_ng_links = new ArrayList<>();
        for(int i = 0; i < weekData.getNg_link_amount(); i++) {
            temp_ng_links.add(ng_links_subJSONObject.getString("" + i));
        }
        weekData.setNg_links(temp_ng_links);

        /* set the amount of buttons need for the nutrition goal for the current week */
        weekData.setNg_days_per_week(jsonObject.getInt(week_data_json_value_names[11]));

        /* set the button strings for the nutrition goal */
        JSONObject ng_button_strings_json = jsonObject.getJSONObject(week_data_json_value_names[12]);
        ArrayList<String> ng_button_strings = new ArrayList<>();
        for(int i = 0; i < weekData.getNg_days_per_week(); i++) {
            ng_button_strings.add(ng_button_strings_json.getString("" + i));
        }
        weekData.setNg_strings(ng_button_strings);

        weekData.setSuggestedWorkoutLink(jsonObject.getString(week_data_json_value_names[13]));
        weekData.setSuggestedWorkoutType(jsonObject.getString(week_data_json_value_names[14]));
        weekData.setWeekDates(jsonObject.getString(week_data_json_value_names[15]));
        return weekData;
    }

}
