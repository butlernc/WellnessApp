package com.uwec.wellnessapp.home;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.VideoView;

import com.uwec.wellnessapp.R;
import com.uwec.wellnessapp.statics.Statics;
import java.util.ArrayList;

/**
 * Created by Noah Butler on 1/15/2015.
 *
 * A simple fragment meant to display the links that are in the goals desc text as clickable links
 * so the user can go to the respective websites
 */
public class LinksFragment extends Fragment {

    View rootView;
    /* instead of displaying the actual web address, we create a list of simple names "Link n"
     * where n is the number of links
     */
    ArrayList<String> link_names;
    TextView suggestedWorkOutText;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.links_and_suggested_workout_fragment, container, false);
        getActivity().getActionBar().setTitle("Step It Up");

        Bundle args = getArguments();

        ListView listView = (ListView) rootView.findViewById(R.id.link_list);
        suggestedWorkOutText = (TextView) rootView.findViewById(R.id.suggested_workout_link);
        suggestedWorkOutText.setText("Suggested Workout: " + Statics.getCurrentWeekData().getSuggestedWorkoutType());

        link_names = new ArrayList<>();

        /* physical goals will have a suggested workout of the week that we will display along with
         * the links. This check figures out if we came from the physical activity goal/fitness goal
         * fragment (one in the same) or if we came from the nutrition goal fragment, in which case
         * there will only be the links and no video. */
        if(args.getBoolean("from_physical_activity")) {
            //create list button names
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
                }
            });

        }else{// same thing as the if block, but this is for nutrition and has no suggested workout
            for(int i = 0; i < Statics.getCurrentWeekData().getNg_link_amount(); i++) {
                link_names.add("Link" + (i + 1));
            }
            listView.setAdapter(new ArrayAdapter<>(getActivity().getBaseContext(), R.layout.link_list_text_layout, link_names));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(Statics.getCurrentWeekData().getNg_links().get(i)));
                    startActivity(intent);
                }
            });
        }

        return rootView;

    }

}
