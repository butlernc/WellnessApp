package com.uwec.wellnessapp.challengeInfo;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.uwec.wellnessapp.R;
import com.uwec.wellnessapp.statics.Statics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Noah Butler on 1/14/2015.
 * Displays a list that allows the user to navigate to a given week's challengeInfo.
 */
public class ChallengeInfoFragment extends Fragment {

    /* http://stackoverflow.com/questions/7441077/how-could-i-add-a-spinner-in-listview-with-its-listitems-by-using-customadapter */

    ChallengeListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.challenge_info_fragment, container, false);
        getActivity().getActionBar().setTitle("Challenge Info");

        /* get the expandable list view */
        expListView = (ExpandableListView) rootView.findViewById(R.id.challenge_info_expandable_list);

        /* prepare the list data */
        prepareListData();

        /* create and set the list adapter */
        listAdapter = new ChallengeListAdapter(getActivity().getBaseContext(), getActivity(), listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        return rootView;
    }

    /**
     * Used to load in the list data and populate an arraylist for the titles and then hashmaps
     * for the sub lists.
     *
     * The hashmap's keys are the elements from the arraylist and the values are "Physical Activity"
     * and "Nutrition Goal"
     *
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding child data
        for(int i = 1; i <= 6; i++) {
            listDataHeader.add("Week " + i + " ... " + Statics.globalWeekDataList.get(i - 1).getWeekDates());
        }

        // Adding child data
        ArrayList<List<String>> childLists = new ArrayList<>();
        for(int i = 0; i < 6; i++) {
            childLists.add(new ArrayList<String>());
            childLists.get(i).add("Physical Activity");
            childLists.get(i).add("Nutrition Goal");
        }

        // Header, Child data
        for(int i = 0; i < 6; i++) {
            listDataChild.put(listDataHeader.get(i), childLists.get(i));
        }
    }

}
