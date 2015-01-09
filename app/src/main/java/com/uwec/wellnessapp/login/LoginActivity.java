package com.uwec.wellnessapp.login;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.uwec.wellnessapp.R;
import com.uwec.wellnessapp.register.RegisterHelper;
import com.uwec.wellnessapp.start.MainNavActivity;
import com.uwec.wellnessapp.statics.Statics;
import com.uwec.wellnessapp.utils.FileSourceConnector;

import org.w3c.dom.Text;

/**
 * Created by butlernc on 12/2/2014.
 */
public class LoginActivity extends Activity {

    private boolean shouldLoad;
    /** text view from the loading fragment */
    private TextView loadingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Statics.loginHelper = new LoginHelper();
        Statics.registerHelper = new RegisterHelper();

        /* show loading fragment first */
        Statics.loadingFragment = new LoadingFragment();
        getFragmentManager().beginTransaction().replace(R.id.main_login_area, Statics.loadingFragment).commit();
        getFragmentManager().executePendingTransactions();

        Log.d("TEST", "Loading fragment should be shown");

        /* used as a callback from threads that are doing work */
        Statics.handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                loadingText = Statics.loadingFragment.getLoadingText();
                loadingText.setText(msg.getData().getString("message"));
            }
        };
    }
}
