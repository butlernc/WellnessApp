package com.uwec.wellnessapp.pointsbreakdown;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uwec.wellnessapp.R;

/**
 * Created by Noah Butler on 1/20/2015.
 */
public class FitnessBreakDownFragment extends Fragment {

    View rootView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.links_and_suggested_workout_fragment, container, false);
        getActivity().getActionBar().setTitle("Step It Up");


        return rootView;
    }

}
