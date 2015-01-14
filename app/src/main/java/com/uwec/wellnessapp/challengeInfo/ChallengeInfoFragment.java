package com.uwec.wellnessapp.challengeInfo;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View rootView = inflater.inflate(R.layout.challenge_info_fragment, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.challenges_list);

        ArrayList<Spinner> spinners = new ArrayList<>();
        for(int i = 0; i < 6; i++) {
            spinners.add(new Spinner(getActivity().getBaseContext()));
        }

        listView.setAdapter(new ChallengeListAdapter(getActivity().getBaseContext(), R.id.challenge_info_spinner, spinners));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity().getBaseContext(), "Test: " + i, Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

}
