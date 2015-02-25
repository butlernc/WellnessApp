package com.uwec.wellnessapp.utils;

import android.os.Bundle;
import android.os.Message;

import com.uwec.wellnessapp.statics.Statics;

/**
 * Created by butlernc on 1/8/2015.
 *
 * Allows the sending of messages between threads
 */
public class Messenger {

    Message message;
    Bundle bundle;

    public static String[] keys = {
            "registeringText",
            "loggingIn",
            "message",
            "progress",
            "toast",
    };

    private void init() {
        message = new Message();
        bundle = new Bundle();
    }

    public void registering(String string) {
        init();
        bundle.putString(keys[0], string);
        message.setData(bundle);
        Statics.handler.sendMessage(message);
    }

    public void loggingIn(String string) {
        init();
        bundle.putString(keys[1], string);
        message.setData(bundle);
        Statics.handler.sendMessage(message);
    }

    public void sendMessage(String string) {
        init();
        bundle.putString(keys[2], string);
        message.setData(bundle);
        Statics.handler.sendMessage(message);
    }

    public void sendProgress(double progress) {
        init();
        bundle.putDouble(keys[3], progress);
        message.setData(bundle);
        Statics.handler.sendMessage(message);
    }

    public void sendToastMessage(String string) {
        init();
        bundle.putString(keys[4], string);
        message.setData(bundle);
        Statics.handler.sendMessage(message);
    }

}
