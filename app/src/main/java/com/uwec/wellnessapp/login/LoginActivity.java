package com.uwec.wellnessapp.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.uwec.wellnessapp.R;
import com.uwec.wellnessapp.register.RegisterHelper;
import com.uwec.wellnessapp.start.MainNavActivity;

/**
 * Created by butlernc on 12/2/2014.
 */
public class LoginActivity extends Activity {

    EditText email_input;
    EditText password_input;

    Button email_sign_in_button;
    Button email_register_button;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_sign_in_button = (Button) findViewById(R.id.email_sign_in_button);
        email_register_button = (Button) findViewById(R.id.email_register_button);

        email_sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("LOGIN", "clicked login button");
                email_input = (EditText) findViewById(R.id.login_input_email);
                password_input = (EditText) findViewById(R.id.login_input_password);
                LoginHelper.setLogged(LoginHelper.login(email_input.getText().toString(), password_input.getText().toString()));

                if(LoginHelper.isLogged()) {
                    Toast.makeText(email_sign_in_button.getContext(), "Successfully logged in", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(email_sign_in_button.getContext(), MainNavActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(email_sign_in_button.getContext(), "Incorrect Email or Password", Toast.LENGTH_SHORT).show();
                }


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
