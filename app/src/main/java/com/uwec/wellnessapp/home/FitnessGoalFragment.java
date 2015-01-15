package com.uwec.wellnessapp.home;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uwec.wellnessapp.R;
import com.uwec.wellnessapp.start.MainNavActivity;
import com.uwec.wellnessapp.statics.Statics;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by butlernc on 12/2/2014.
 */
public class FitnessGoalFragment extends Fragment {

    private static final String ARG_BUTTON_NUMBER = "button_number";
    View rootView = null;
    TextView paDescTextView;
    TextView weekDisplayPhysical;
    TextView physicalActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.fitness_goal_fragment, container, false);
        getActivity().getActionBar().setTitle(R.string.fitness_goal_title);

        weekDisplayPhysical = (TextView) rootView.findViewById(R.id.week_display_physical);
        weekDisplayPhysical.setText("Week " + Statics.sessionData.getWeekNumber() + ": Physical Activity");

        physicalActivity = (TextView) rootView.findViewById(R.id.physical_activity);
        physicalActivity.setText("Goal: " + Statics.globalWeekDataList.get(Statics.sessionData.getWeekNumber() - 1).getPhysical_activity());

        paDescTextView = (TextView) rootView.findViewById(R.id.physical_activity_desc);
        paDescTextView.setText(Statics.globalWeekDataList.get(Statics.sessionData.getWeekNumber() - 1).getPhysical_activity_description());

        LinearLayout linear_physical_buttons = (LinearLayout) rootView.findViewById(R.id.linear_physical_button);

        //TODO: make buttons green if day completed/checked off
        //TODO: make buttons blue if day needs to be completed/checked off
        //TODO: learn how to add buttons to a view
        /* create a buttons for the amount of days they have to do an activity  */
        ArrayList<Button> buttons = new ArrayList<Button>();
        for(int i = 0; i < Statics.globalWeekDataList.get(Statics.sessionData.getWeekNumber() - 1).getPa_days_per_week(); i++) {
            buttons.add(new Button(rootView.getContext()));
        }

        return rootView;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainNavActivity) activity).onSectionAttached(getArguments().getInt(ARG_BUTTON_NUMBER));
    }
}
