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

import com.uwec.wellnessapp.R;
import com.uwec.wellnessapp.statics.Statics;

import java.util.ArrayList;

/**
 * Created by butlernc on 1/15/2015.
 */
public class LinksFragment extends Fragment {

    View rootView;

    ArrayList<String> link_names;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.links_and_suggested_workout_fragment, container, false);
        getActivity().getActionBar().setTitle("Step It Up");

        Bundle args = getArguments();

        ListView listView = (ListView) rootView.findViewById(R.id.link_list);

        link_names = new ArrayList<>();
        if(args.getBoolean("from_physical_activity")) {
            //create list button names
            for(int i = 0; i < Statics.getCurrentWeekData().getPa_link_amount(); i++) {
                link_names.add("Link " + (i + 1));
            }
            listView.setAdapter(new ArrayAdapter<>(getActivity().getBaseContext(), R.layout.link_list_text_layout, link_names));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(Statics.getCurrentWeekData().getPa_links().get(i)));
                    startActivity(intent);
                }
            });

        }else{
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
