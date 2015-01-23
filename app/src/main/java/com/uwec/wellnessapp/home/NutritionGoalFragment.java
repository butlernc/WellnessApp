package com.uwec.wellnessapp.home;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uwec.wellnessapp.R;
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

    Button linksButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.nutrition_goal_fragment, container, false);
        getActivity().getActionBar().setTitle("Step It Up");

        ngDescTextView = (TextView) rootView.findViewById(R.id.nutrition_goal_desc);
        ngDescTextView.setText(Statics.globalWeekDataList.get(Statics.sessionData.getWeekNumber() - 1).getNutrition_goal_description());

        weekDisplayNutrition = (TextView) rootView.findViewById(R.id.week_display_nutrition);
        weekDisplayNutrition.setText("Week " + Statics.sessionData.getWeekNumber() + ": Nutrition Goal");

        nutritionGoal = (TextView) rootView.findViewById(R.id.nutrition_goal);
        nutritionGoal.setText("Goal: " + Statics.globalWeekDataList.get(Statics.sessionData.getWeekNumber() - 1).getNutrition_goal());

        LinearLayout linear_nutrition_buttons = (LinearLayout) rootView.findViewById(R.id.linear_nutrition_button);
        ArrayList<Button> checkOffButtons = new ArrayList<>();
        /* make the correct amount of buttons for this current week */
        for(int i = 0; i < Statics.getCurrentWeekData().getNg_days_per_week(); i++) {
            checkOffButtons.add(new Button(getActivity()));
        }

        for(int i = 0; i < Statics.getUsersCurrentWeekData().getNutritionGoalCheckOffAmount(); i++ ) {
            boolean current    = Statics.getUsersCurrentWeekData().getNutritionGoalCheckOffs().get(i);
            Log.e("NG_STRING", "" + Statics.getCurrentWeekData().getWeek());
            String current_tag = Statics.getCurrentWeekData().getNg_strings().get(i);


            if(current) {//already saved to be checked off
                checkOffButtons.get(i).setText("Completed!");
            } else {//not saved to be checked off
                checkOffButtons.get(i).setText(current_tag);
            }
            checkOffButtons.get(i).setBackgroundResource(R.drawable.completion_button);
            checkOffButtons.get(i).setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

            checkOffButtons.get(i).setOnClickListener(new FitnessTrackerButtonListener(getActivity().getBaseContext(), i, false, checkOffButtons.get(i)));

            linear_nutrition_buttons.addView(checkOffButtons.get(i), new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }

        linksButton = (Button) rootView.findViewById(R.id.nutrition_goal_links_button);
        linksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Statics.getCurrentWeekData().getNg_link_amount() > 0) {
                    LinksFragment linksFragment = new LinksFragment();
                    Bundle args = new Bundle();
                    args.putBoolean("from_physical_activity", false);
                    linksFragment.setArguments(args);
                    NutritionGoalFragment.this.getActivity().getFragmentManager().beginTransaction().replace(R.id.main_nav_fragment, linksFragment).commit();
                }else {
                    Toast.makeText(getActivity().getBaseContext(), "No links to be displayed", Toast.LENGTH_LONG).show();
                }
            }
        });

        return rootView;
    }

}
