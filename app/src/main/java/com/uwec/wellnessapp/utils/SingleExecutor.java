package com.uwec.wellnessapp.utils;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.Executor;

/**
 * Created by butlernc on 1/7/2015.
 */
public class SingleExecutor {

     Stack<Runnable> tasks = new Stack<Runnable>();


    public void push(Runnable runnable) {
        tasks.push(runnable);
    }

    public synchronized boolean runTask() {
        try {
            new Thread(tasks.pop()).start();
        } finally {
            return true;
        }
    }
}
