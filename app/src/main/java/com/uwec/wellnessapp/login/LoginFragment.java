package com.uwec.wellnessapp.login;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.uwec.wellnessapp.R;
import com.uwec.wellnessapp.register.RegisterHelper;
import com.uwec.wellnessapp.start.MainNavActivity;
import com.uwec.wellnessapp.statics.Statics;

/**
 * Created by butlernc on 1/8/2015.
 */
public class LoginFragment extends Fragment {

    EditText email_input;
    EditText password_input;

    Button email_sign_in_button;
    Button email_register_button;

    CheckBox rememberMe;

    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.login_fragment, container, false);

        email_sign_in_button = (Button) rootView.findViewById(R.id.email_sign_in_button);
        email_register_button = (Button) rootView.findViewById(R.id.email_register_button);

        email_input = (EditText) rootView.findViewById(R.id.login_input_email);
        password_input = (EditText) rootView.findViewById(R.id.login_input_password);

        rememberMe = (CheckBox) rootView.findViewById(R.id.remember_me_check_box);

        email_sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("LOGIN", "clicked login button");

                //run login and set the user as logged in if successful
                Log.d("RememberMe", "Status: " + String.valueOf(rememberMe.isChecked()));
                Statics.sessionData.setupSession();
                Statics.loginHelper.login(getActivity(), email_input.getText().toString(), password_input.getText().toString(), rememberMe.isChecked(), true);
            }

        });

        email_register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterHelper.startRegisterActivity(getActivity());
            }
        });

        return rootView;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
}
