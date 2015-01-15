package com.uwec.wellnessapp.login;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.uwec.wellnessapp.R;
import com.uwec.wellnessapp.register.RegisterHelper;
import com.uwec.wellnessapp.statics.Statics;

/**
 * Created by butlernc on 12/2/2014.
 */
public class LoginActivity extends Activity {

    Button email_sign_in_button, email_register_button;
    EditText email_input, password_input;
    CheckBox rememberMe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_sign_in_button = (Button) findViewById(R.id.email_sign_in_button);
        email_register_button = (Button) findViewById(R.id.email_register_button);

        email_input = (EditText) findViewById(R.id.login_input_email);
        password_input = (EditText) findViewById(R.id.login_input_password);

        rememberMe = (CheckBox) findViewById(R.id.remember_me_check_box);

        email_sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("LOGIN", "clicked login button");

                //run login and set the user as logged in if successful
                Log.d("RememberMe", "Status: " + String.valueOf(rememberMe.isChecked()));
                LoginHelper loginHelper = new LoginHelper(LoginActivity.this, email_input.getText().toString(), password_input.getText().toString(), rememberMe.isChecked(), true);
                loginHelper.start();
            }

        });

        email_register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterHelper.startRegisterActivity(LoginActivity.this);
            }
        });

    }
}
