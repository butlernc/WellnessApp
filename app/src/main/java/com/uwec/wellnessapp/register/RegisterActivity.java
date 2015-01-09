package com.uwec.wellnessapp.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.uwec.wellnessapp.R;
import com.uwec.wellnessapp.login.LoginActivity;
import com.uwec.wellnessapp.login.LoginHelper;
import com.uwec.wellnessapp.start.MainNavActivity;
import com.uwec.wellnessapp.statics.Statics;
import com.uwec.wellnessapp.utils.FileSourceConnector;

/**
 * Created by Noah Butler on 12/5/2014.
 */
public class RegisterActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getActionBar().setTitle("Register");

        final Button registerButton = (Button) findViewById(R.id.register_register_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] params = new String[4];

                //create our params for
                EditText last_name = (EditText) findViewById(R.id.last_name_register);
                params[0] = (last_name.getText().toString());
                EditText first_name = (EditText) findViewById(R.id.first_name_register);
                params[1] = (first_name.getText().toString());
                EditText email = (EditText) findViewById(R.id.email_register);
                params[2] = (email.getText().toString());
                EditText password = (EditText) findViewById(R.id.password_register);
                params[3] = (password.getText().toString());
                FileSourceConnector.setContext(RegisterActivity.this);
                Statics.registerHelper.register(RegisterActivity.this, params);
            }
        });

    }

}
