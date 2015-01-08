package com.uwec.wellnessapp.utils;

import android.os.Bundle;
import android.os.Message;

import com.uwec.wellnessapp.statics.Statics;

/**
 * Created by butlernc on 1/8/2015.
 */
public class Messenger {

    Message message;
    public boolean messageSent;

    public void sendMessage(String string) {
        message = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("message", string);
        message.setData(bundle);

        Statics.handler.sendMessage(message);
        messageSent = true;
    }

}
