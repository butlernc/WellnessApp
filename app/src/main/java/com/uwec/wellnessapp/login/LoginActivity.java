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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.uwec.wellnessapp.R;
import com.uwec.wellnessapp.register.RegisterHelper;
import com.uwec.wellnessapp.start.MainNavActivity;
import com.uwec.wellnessapp.statics.Statics;

/**
 * Created by butlernc on 12/2/2014.
 */
public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /* make sure we aren't trying to log out */
        boolean shouldLoad = true;
        if(getIntent().getStringArrayExtra("extras") != null) {
            /* any activity will send a message to the login screen that the user is trying to logout
            it should not try to auto log back in.
             */
            Log.d("Session", "Don't auto login this time");
            shouldLoad = false;
        }
        Fragment loadingFragment = new LoadingFragment();
        getFragmentManager().beginTransaction().replace(R.id.main_login_area, loadingFragment).commit();

        Log.d("Session", "SessionData is loading");
        Statics.getSessionData().loadLastSession(getBaseContext());
        if(Statics.getSessionData().rememberedMe() && shouldLoad) {
            Log.d("Session", "Using remember me username and password");
            LoginHelper.setLogged(LoginHelper.login(getBaseContext(), Statics.getSessionData().getUsername(), Statics.getSessionData().getPassword(), true));
            Toast.makeText(getBaseContext(), "Successfully logged in", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getBaseContext(), MainNavActivity.class);
            startActivity(intent);
        }

        Fragment defaultLoginFragment = new DefaultLoginFragment();
        getFragmentManager().beginTransaction().replace(R.id.main_login_area, defaultLoginFragment).commit();

    }

    public static class LoadingFragment extends Fragment {

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            View rootView = inflater.inflate(R.layout.loading_fragment, container, false);



            return rootView;
        }
    }

    public class DefaultLoginFragment extends Fragment {

        EditText email_input;
        EditText password_input;

        Button email_sign_in_button;
        Button email_register_button;

        CheckBox rememberMe;

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            View rootView = inflater.inflate(R.layout.login_fragment, container, false);

            email_sign_in_button = (Button) rootView.findViewById(R.id.email_sign_in_button);
            email_register_button = (Button) rootView.findViewById(R.id.email_register_button);

            rememberMe = (CheckBox) rootView.findViewById(R.id.remember_me_check_box);

            email_sign_in_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("LOGIN", "clicked login button");
                    email_input = (EditText) findViewById(R.id.login_input_email);
                    password_input = (EditText) findViewById(R.id.login_input_password);

                    //run login and set the user as logged in if successful
                    Log.d("RememberMe", "Status: " + String.valueOf(rememberMe.isChecked()));
                    LoginHelper.setLogged(LoginHelper.login(getBaseContext(), email_input.getText().toString(), password_input.getText().toString(), rememberMe.isChecked()));

                    //check to see if the user cred will showed positive results
                    if(LoginHelper.isLogged()) {
                        Toast.makeText(email_sign_in_button.getContext(), "Successfully logged in", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(email_sign_in_button.getContext(), MainNavActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(email_sign_in_button.getContext(), "Incorrect Email or Password", Toast.LENGTH_SHORT).show();

                        email_input.setText("");
                        password_input.setText("");
                    }

                }
            });

            email_register_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RegisterHelper.startRegisterActivity(LoginActivity.this);
                }
            });


            return rootView;
        }
    }
}
