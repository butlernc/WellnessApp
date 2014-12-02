package com.uwec.wellnessapp.start;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.uwec.wellnessapp.R;
import com.uwec.wellnessapp.home.FitnessGoalFragment;

/**
 * The Main Activity.
 * 
 * This activity starts up the RegisterActivity immediately, which communicates
 * with your App Engine backend using Cloud Endpoints. It also receives push
 * notifications from backend via Google Cloud Messaging (GCM).
 * 
 * Check out RegisterActivity.java for more details.
 */
public class DefaultMainFragment extends Fragment {

	/** objects for actionBar */
	CharSequence menuTitle;
	
	/** Main Fragment Buttons */
	Button btnFitnessGoals;
	Button btnNutritionGoals;
	Button btnAllBonusEvents;
	
	private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_BUTTON_NUMBER = "button_number";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		View rootView = inflater.inflate(R.layout.activity_main_fragment_default, container, false);
		
		// create button objects
				btnFitnessGoals = (Button) rootView.findViewById(R.id.btnFitnessGoals);
				btnNutritionGoals = (Button) rootView.findViewById(R.id.btnNutritionGoals);
				btnAllBonusEvents = (Button) rootView.findViewById(R.id.btnBonusEvents);
				
				 //Set the list's click listener

				/** Click Listener for Fitness Goals Button */
				if(btnFitnessGoals != null) {
					btnFitnessGoals.setOnClickListener(new View.OnClickListener() {
			
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Toast.makeText(getActivity(), "Clicked Fitness Goals", Toast.LENGTH_SHORT).show();
                            getActivity().getFragmentManager().beginTransaction().replace(R.id.main_nav_fragment, createFragmentFromButtonClick(0)).commit();

						}
					});
				}

				/** Click Listener for Nutrition Goals Button */
				if(btnNutritionGoals != null) {
					btnNutritionGoals.setOnClickListener(new View.OnClickListener() {
			
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Toast.makeText(getActivity(), "Clicked Nutrition Goals", Toast.LENGTH_SHORT).show();
						}
					});
				}
				
				/** Click Listener for All Bonus Events Button */
				if(btnAllBonusEvents != null) {
					btnAllBonusEvents.setOnClickListener(new View.OnClickListener() {
			
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Toast.makeText(getActivity(), "Clicked All Bonus Events", Toast.LENGTH_SHORT).show();
						}
					});
				}
		
		return rootView;

		// Start up RegisterActivity right away
		//Intent intent = new Intent(this, RegisterActivity.class);
		//startActivity(intent);
		// Since this is just a wrapper to start the main activity,
		// finish it after launching RegisterActivity
		//finish();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainNavActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
		
	}

    public Fragment createFragmentFromButtonClick(int button_number){
        Fragment fragment = null;
        Bundle args = new Bundle();
        args.putInt(ARG_BUTTON_NUMBER, button_number);

        switch(button_number) {
            case 0:
                fragment = new FitnessGoalFragment();
                break;
            case 1:
                break;
            case 2:
                break;
            default:
                break;
        }
        if(fragment != null) {
            fragment.setArguments(args);
        }
        return fragment;
    }
}
