package com.uwec.wellnessapp.challengeInfo;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.uwec.wellnessapp.R;
import com.uwec.wellnessapp.statics.Statics;

import java.util.ArrayList;

/**
 * Created by butlernc on 1/14/2015.
 */
public class ChallengeInfoFragment extends Fragment {

    /* http://stackoverflow.com/questions/7441077/how-could-i-add-a-spinner-in-listview-with-its-listitems-by-using-customadapter */

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View rootView = inflater.inflate(R.layout.challenge_info_fragment, container, false);
        Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner_chunk);

        ArrayList<SpinnerOptions> words = new ArrayList<>();
        for(int i = 0; i < 6; i++) {
            words.add(new SpinnerOptions("Week" + ": " + (i + 1), i));
        }

        spinner.setAdapter(new ArrayAdapter<SpinnerOptions>(getActivity().getBaseContext(), R.layout.challenge_info_spinner_row, R.id.challenge_info_spinner_row_name, words));

        return rootView;
    }

}
