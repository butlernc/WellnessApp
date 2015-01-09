package com.uwec.wellnessapp.scripts;

import com.uwec.wellnessapp.utils.JsonFileConverter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Noah Butler on 12/23/2014.
 *
 * push static data such as goal data etc. to our server
 */
public class PushStaticData {

    private static String week_data_json_value_names[] = {"week", "physical_activity", "physical_activity_description", "pa_days_per_week", "nutrition_goal", "nutrition_goal_description", "ng_days_per_week", "supporting_evidence"};

    /** creates a string for a jsonObject to be written out to a file */
    public static String weekStartDays() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("WEEK_ONE", 1);
            jsonObject.put("WEEK_TWO", 8);
            jsonObject.put("WEEK_THREE", 15);
            jsonObject.put("WEEK_FOUR", 22);
            jsonObject.put("WEEK_FIVE", 1);
            jsonObject.put("WEEK_SIX", 8);

            jsonObject.put("MONTH_ONE", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return (jsonObject.toString());
    }
}
