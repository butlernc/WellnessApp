package com.uwec.wellnessapp.home;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.uwec.wellnessapp.R;
import com.uwec.wellnessapp.data.LoggingHelper;
import com.uwec.wellnessapp.start.MainNavActivity;
import com.uwec.wellnessapp.statics.Statics;

import java.util.ArrayList;

/**
 * Created by Noah Butler on 12/2/2014.
 *
 * Used as the Controller for goal_fragment.xml
 * Parent Activity: MainNavActivity
 */
public class FitnessGoalFragment extends Fragment {

    private static final String ARG_BUTTON_NUMBER = "button_number";
    View rootView = null;
    TextView paDescTextView;
    TextView weekDisplayPhysical;
    TextView physicalActivity;

    TextView suggestedWorkOutText;
    TextView currentWeekPhysicalPoints;
    ArrayList<String> link_names;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.goal_fragment, container, false);
        getActivity().getActionBar().setTitle(R.string.fitness_goal_title);

        /* grab the TextViews that will display our weekly info */
        weekDisplayPhysical = (TextView) rootView.findViewById(R.id.week_display);
        weekDisplayPhysical.setText("Week " + Statics.sessionData.getWeekNumber() + ": Physical Activity");

        physicalActivity    = (TextView) rootView.findViewById(R.id.goal);
        physicalActivity.setText("Goal: " + Statics.getCurrentWeekData().getPhysical_activity());

        createDescTextLinksList();

        paDescTextView      = (TextView) rootView.findViewById(R.id.goal_desc);
        paDescTextView.setText("More Info: \n" + Statics.globalWeekDataList.get(Statics.sessionData.getWeekNumber() - 1).getPhysical_activity_description());

        currentWeekPhysicalPoints = (TextView)rootView.findViewById(R.id.current_week_goal_points);
        /* check if we need to display "completed!" or points earned so far */
        if(Statics.getUsersCurrentWeekData().getPhysicalGoalPoints() < 10) {
            currentWeekPhysicalPoints.setTextSize(25);
            currentWeekPhysicalPoints.setText("" + Statics.getUsersCurrentWeekData().getPhysicalGoalPoints());
        }else{
            currentWeekPhysicalPoints.setTextSize(13);
            currentWeekPhysicalPoints.setText("Completed!");
        }

        ImageButton addPhysicalPointButton = (ImageButton)rootView.findViewById(R.id.add_point_button);
        addPhysicalPointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentProgress = Statics.getUsersCurrentWeekData().getPhysicalGoalPoints();
                /*refresh text views */
                if(currentProgress < 10) {
                    Statics.getUsersCurrentWeekData().getPhysicalGoalCheckOffs().add(currentProgress / 2, true);

                    LoggingHelper loggingHelper = new LoggingHelper(getActivity().getBaseContext(), getActivity(), 0);
                    loggingHelper.logPoints();

                    currentWeekPhysicalPoints.setTextSize(25);
                    currentWeekPhysicalPoints.setText("" + Statics.getUsersCurrentWeekData().getPhysicalGoalPoints());

                }else {
                    currentWeekPhysicalPoints.setTextSize(13);
                    currentWeekPhysicalPoints.setText("Completed!");
                }

                currentWeekPhysicalPoints.invalidate();
                Log.e("TEST", "Test run");
            }
        });

        TextView activityShortDesc = (TextView)rootView.findViewById(R.id.activity_short_desc);

        String first  = Statics.getCurrentWeekData().getPa_strings().get(0);
        String second = Statics.getCurrentWeekData().getPa_strings().get(4);
        activityShortDesc.setText(first + " and " + second);

        return rootView;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainNavActivity) activity).onSectionAttached(getArguments().getInt(ARG_BUTTON_NUMBER));
    }

    /**
     * Called to set up the list of links from the desc text.
     *
     */
    private void createDescTextLinksList() {
        ListView listView = (ListView) rootView.findViewById(R.id.desc_text_links_list);
        suggestedWorkOutText = (TextView) rootView.findViewById(R.id.suggested_workout_link);
        suggestedWorkOutText.setText("Suggested Workout: " + Statics.getCurrentWeekData().getSuggestedWorkoutType());

        link_names = new ArrayList<>();

        for(int i = 0; i < Statics.getCurrentWeekData().getPa_link_amount(); i++) {
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
                intent.setData(Uri.parse(Statics.getCurrentWeekData().getPa_links().get(i)));
                startActivity(intent);
            }
        });


        suggestedWorkOutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(Statics.getCurrentWeekData().getSuggestedWorkoutLink()));
                startActivity(intent);
            }
        });
    }
}
