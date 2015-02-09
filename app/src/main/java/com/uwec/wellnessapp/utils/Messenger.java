package com.uwec.wellnessapp.utils;

import android.os.Bundle;
import android.os.Message;

import com.uwec.wellnessapp.statics.Statics;

/**
 * Created by butlernc on 1/8/2015.
 */
public class Messenger {

    Message message;
    Bundle bundle;
    private void init() {
        message = new Message();
        bundle = new Bundle();
    }

    public void sendMessage(String string) {
        init();
        bundle.putString("message", string);
        message.setData(bundle);
        Statics.handler.sendMessage(message);
    }

    public void sendProgress(double progress) {
        init();
        bundle.putDouble("progress", progress);
        message.setData(bundle);
        Statics.handler.sendMessage(message);
    }

}
