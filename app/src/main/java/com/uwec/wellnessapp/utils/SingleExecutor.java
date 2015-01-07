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

    private static boolean finished;

    public void push(Runnable runnable) {
        tasks.push(runnable);
    }

    public synchronized void runTask() {
        try {
            finished = false;
            new Thread(tasks.pop()).start();
        } finally {
            finished = true;
        }
    }

    public boolean isFinished() {
        return finished;
    }
}
