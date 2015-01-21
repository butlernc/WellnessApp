package com.uwec.wellnessapp.home;

import android.app.Activity;
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
import com.uwec.wellnessapp.start.MainNavActivity;
import com.uwec.wellnessapp.statics.Statics;

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

    Button linksButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.fitness_goal_fragment, container, false);
        getActivity().getActionBar().setTitle(R.string.fitness_goal_title);

        weekDisplayPhysical = (TextView) rootView.findViewById(R.id.week_display_physical);
        weekDisplayPhysical.setText("Week " + Statics.sessionData.getWeekNumber() + ": Physical Activity");

        physicalActivity    = (TextView) rootView.findViewById(R.id.physical_activity);
        physicalActivity.setText("Goal: " + Statics.getCurrentWeekData().getPhysical_activity());

        paDescTextView      = (TextView) rootView.findViewById(R.id.physical_activity_desc);
        paDescTextView.setText(Statics.globalWeekDataList.get(Statics.sessionData.getWeekNumber() - 1).getPhysical_activity_description());

        LinearLayout linear_physical_buttons = (LinearLayout) rootView.findViewById(R.id.linear_physical_button);
        ArrayList<Button> checkOffButtons = new ArrayList<>();
        /* make the correct amount of buttons for this current week */
        for(int i = 0; i < Statics.getCurrentWeekData().getPa_days_per_week(); i++) {
            checkOffButtons.add(new Button(getActivity()));
        }

        for(int i = 0; i < Statics.getUsersCurrentWeekData().getPhysicalGoalCheckOffAmount(); i++ ) {
            boolean current    = Statics.getUsersCurrentWeekData().getPhysicalGoalCheckOffs().get(i);
            String current_tag = Statics.getCurrentWeekData().getPa_strings().get(i);
            Log.e("PA_STRING", "" + Statics.getCurrentWeekData().getWeek());


            if(current) {//already saved to be checked off
                checkOffButtons.get(i).setText("Completed!");
            } else {//not saved to be checked off
                checkOffButtons.get(i).setText(current_tag);
            }
            checkOffButtons.get(i).setBackgroundResource(R.drawable.completion_button);
            checkOffButtons.get(i).setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

            checkOffButtons.get(i).setOnClickListener(new FitnessTrackerButtonListener(getActivity().getBaseContext(), i, true, checkOffButtons.get(i)));

            linear_physical_buttons.addView(checkOffButtons.get(i), new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }

        linksButton = (Button) rootView.findViewById(R.id.physical_activity_links_button);
        linksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Statics.getCurrentWeekData().getPa_link_amount() > 0) {
                    LinksFragment linksFragment = new LinksFragment();
                    Bundle args = new Bundle();
                    args.putBoolean("from_physical_activity", true);
                    linksFragment.setArguments(args);
                    FitnessGoalFragment.this.getActivity().getFragmentManager().beginTransaction().replace(R.id.main_nav_fragment, linksFragment).commit();
                }else {
                    Toast.makeText(getActivity().getBaseContext(), "No links to be displayed", Toast.LENGTH_LONG).show();
                }
            }
        });

        return rootView;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainNavActivity) activity).onSectionAttached(getArguments().getInt(ARG_BUTTON_NUMBER));
    }
}
