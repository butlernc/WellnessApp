package com.uwec.wellnessapp.home;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.uwec.wellnessapp.R;
import com.uwec.wellnessapp.statics.Statics;

/**
 * Created by butlernc on 1/15/2015.
 */
public class LinksFragment extends Fragment {

    View rootView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.links_and_suggested_workout_fragment, container, false);
        getActivity().getActionBar().setTitle("Step It Up");

        Bundle args = getArguments();

        ListView listView = (ListView) rootView.findViewById(R.id.link_list);

        if(args.getBoolean("from_physical_activity")) {
            listView.setAdapter(new ArrayAdapter<>(getActivity().getBaseContext(), android.R.layout.simple_list_item_1, Statics.globalWeekDataList.get(Statics.sessionData.getWeekNumber()).getPa_links()));
        }else{
            listView.setAdapter(new ArrayAdapter<>(getActivity().getBaseContext(), android.R.layout.simple_list_item_1, Statics.globalWeekDataList.get(Statics.sessionData.getWeekNumber()).getNg_links()));
        }

        return rootView;

    }

}
