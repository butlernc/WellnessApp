package com.uwec.wellnessapp.home;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.uwec.wellnessapp.R;
import com.uwec.wellnessapp.data.LoggingHelper;
import com.uwec.wellnessapp.statics.Statics;

import java.util.ArrayList;

/**
 * Created by Noah Butler on 1/15/2015.
 *
 * Used to display the nutrition goal data for the current week and allows users to complete said
 * goals.
 *
 * Basically the same thing as FitnessGoalFragment.
 */
public class NutritionGoalFragment extends Fragment {

    View rootView;

    TextView weekDisplayNutrition;
    TextView nutritionGoal;
    TextView ngDescTextView;

    TextView suggestedWorkOutText;
    ArrayList<String> link_names;
    TextView currentWeekNutritionPoints;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.goal_fragment, container, false);
        getActivity().getActionBar().setTitle("Nutrition Goal");

        /* grab the TextViews that will display our weekly info */
        weekDisplayNutrition = (TextView) rootView.findViewById(R.id.week_display);
        weekDisplayNutrition.setText("Week " + Statics.sessionData.getWeekNumber() + ": Nutrition Goal");

        nutritionGoal    = (TextView) rootView.findViewById(R.id.goal);
        nutritionGoal.setText("Goal: " + Statics.getCurrentWeekData().getNutrition_goal());

        createDescTextLinksList();

        ngDescTextView      = (TextView) rootView.findViewById(R.id.goal_desc);
        ngDescTextView.setText("More Info: \n" + Statics.globalWeekDataList.get(Statics.sessionData.getWeekNumber() - 1).getNutrition_goal_description());

        currentWeekNutritionPoints = (TextView)rootView.findViewById(R.id.current_week_goal_points);

        /* check if we need to display "completed!" or points earned so far */
        if(Statics.getUsersCurrentWeekData().getNutritionGoalPoints() < Statics.getCurrentWeekData().getNg_days_per_week()) {
            currentWeekNutritionPoints.setTextSize(25);
            currentWeekNutritionPoints.setText("" + Statics.getUsersCurrentWeekData().getNutritionGoalPoints());
        }else{
            currentWeekNutritionPoints.setTextSize(13);
            currentWeekNutritionPoints.setText("Completed!");
        }

        ImageButton addNutritionPointButton = (ImageButton)rootView.findViewById(R.id.add_point_button);
        addNutritionPointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Here!", "Here");
                int currentProgress = Statics.getUsersCurrentWeekData().getNutritionGoalPoints();

                /*refresh text views */
                if(currentProgress < Statics.getCurrentWeekData().getNg_days_per_week()) {
                    Statics.getUsersCurrentWeekData().getNutritionGoalCheckOffs().set(currentProgress, true);

                    LoggingHelper loggingHelper = new LoggingHelper(getActivity().getBaseContext(), getActivity(), 1);
                    loggingHelper.logPoints();

                    currentWeekNutritionPoints.setTextSize(25);
                    currentWeekNutritionPoints.setText("" + Statics.getUsersCurrentWeekData().getNutritionGoalPoints());
                    currentWeekNutritionPoints.invalidate();
                }else {
                    currentWeekNutritionPoints.setTextSize(13);
                    currentWeekNutritionPoints.setText("Completed!");
                }
            }
        });

        TextView activityShortDesc = (TextView)rootView.findViewById(R.id.activity_short_desc);

        String first  = Statics.getCurrentWeekData().getNg_strings().get(0);
        activityShortDesc.setText(first);

        return rootView;
    }

    /**
     * Called to set up the list of links from the desc text.
     *
     */
    private void createDescTextLinksList() {
        ListView listView = (ListView) rootView.findViewById(R.id.desc_text_links_list);
        suggestedWorkOutText = (TextView) rootView.findViewById(R.id.suggested_workout_link);
        suggestedWorkOutText.setVisibility(View.INVISIBLE);

        link_names = new ArrayList<>();

        if(Statics.getCurrentWeekData().getNg_link_amount() > 0) {

            for (int i = 0; i < Statics.getCurrentWeekData().getNg_link_amount(); i++) {
                link_names.add("Link " + (i + 1));
            }

            /* set the list view adapter to display the simple link names */
            listView.setAdapter(new ArrayAdapter<>(getActivity().getBaseContext(), R.layout.link_list_text_layout, link_names));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    /* open the current selected link in the browser */
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(Statics.getCurrentWeekData().getNg_links().get(i)));
                    startActivity(intent);
                }
            });
        }
    }

}
