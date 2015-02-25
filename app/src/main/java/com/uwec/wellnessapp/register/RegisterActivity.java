package com.uwec.wellnessapp.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.uwec.wellnessapp.R;
import com.uwec.wellnessapp.data.LoggingHelper;
import com.uwec.wellnessapp.login.LoginActivity;
import com.uwec.wellnessapp.login.LoginHelper;
import com.uwec.wellnessapp.start.MainNavActivity;
import com.uwec.wellnessapp.statics.Statics;
import com.uwec.wellnessapp.utils.FileSourceConnector;
import com.uwec.wellnessapp.utils.Messenger;

/**
 * Created by Noah Butler on 12/5/2014.
 */
public class RegisterActivity extends Activity{

    TextView registeringText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getActionBar().setTitle("Register");

        registeringText = (TextView)findViewById(R.id.registering_text);
        registeringText.setVisibility(View.INVISIBLE);

        Statics.handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.getData().containsKey(Messenger.keys[0])) {
                    String text = msg.getData().getString(Messenger.keys[0]);
                    registeringText.setText(text);
                    Log.e("REG", "from thread: " + text);
                    if (Statics.registrationIsComplete) {
                        try {
                            Thread.sleep(600);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }else if(msg.getData().containsKey(Messenger.keys[4])) {
                    Toast.makeText(getBaseContext(), msg.getData().getString(Messenger.keys[4]), Toast.LENGTH_LONG).show();
                }
            }
        };

        final Button registerButton = (Button) findViewById(R.id.register_register_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("REG", "Text should be visible");
                registeringText.setVisibility(View.VISIBLE);
                registeringText.setText("Registering...");

                String[] params = new String[5];

                /* create our params for our register helper */
                EditText last_name = (EditText) findViewById(R.id.last_name_register);
                params[0] = (last_name.getText().toString());
                EditText first_name = (EditText) findViewById(R.id.first_name_register);
                params[1] = (first_name.getText().toString());
                EditText email = (EditText) findViewById(R.id.email_register);
                params[2] = (email.getText().toString());
                EditText password = (EditText) findViewById(R.id.password_register);
                params[3] = (password.getText().toString());
                EditText confirmP = (EditText) findViewById(R.id.re_password_register);
                params[4] = (confirmP.getText().toString());
                RegisterHelper registerHelper = new RegisterHelper(getBaseContext(), RegisterActivity.this, params);
                registerHelper.start();

            }
        });

    }

}
