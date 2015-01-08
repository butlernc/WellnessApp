package com.uwec.wellnessapp.utils;

import com.uwec.wellnessapp.statics.Statics;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.Executor;

/**
 * Created by butlernc on 1/7/2015.
 */
public class SingleExecutor {

    public synchronized void runTask(Runnable runnable) {
        Statics.messenger.messageSent = false;
        new Thread(runnable).start();
    }
}
