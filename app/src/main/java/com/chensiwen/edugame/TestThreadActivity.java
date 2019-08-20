package com.chensiwen.edugame;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class TestThreadActivity extends BaseAppCompatActivity {
    private static final String TAG = "TestCrashActivity";
    private TextView interrupt;
    private Thread newThread;
    private Thread runningThread;
    private Thread interruptedThread;
    private Thread finishThread;
    private Thread waitThread;
    private Object waitLock = new Object();
    private Thread countdownThread;
    private CountDownLatch latch = new CountDownLatch(1);
    private List<Thread> threadList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_thread);
        interrupt = findViewById(R.id.interrupt);
        View get_state = findViewById(R.id.get_state);
        get_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Thread thread : threadList) {
                    Log.d("CCCC", "=====thread dump====");
                    Log.d("CCCC", thread.getName() + ":" + thread.getState());
                    for (StackTraceElement stackTraceElement : thread.getStackTrace()) {
                        Log.d("CCCC", stackTraceElement.toString());
                    }
                }
            }
        });

        View anr = findViewById(R.id.anr);
        anr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                while (true) {

                }
//                try {
//                    // make anr to generate traces.txt
//                    Thread.sleep(10000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        });
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

            }
        };
        Runnable runningRunable = new Runnable() {
            @Override
            public void run() {
                try {
                    while (!Thread.interrupted()) {
                        Thread.sleep(1000);
                        Log.d("CCCC", "wake up:" + Thread.currentThread().getName());
                    }

                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
                Log.d("CCCC", "sleep:" + Thread.currentThread().getName());
                try {
                    Thread.sleep(30 * 60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        newThread = new Thread(runnable, "newThread");
        runningThread = new Thread(runningRunable, "runningThread");
        runningThread.start();

        interruptedThread = new Thread(runningRunable, "interruptedThread");
        interruptedThread.start();

        finishThread = new Thread(runnable, "finishThread");
        finishThread.start();

        interrupt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interruptedThread.interrupt();
            }
        });

        waitThread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (waitLock) {
                    try {
                        waitLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "waitThread");
        waitThread.start();

        countdownThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "countdownThread");
        countdownThread.start();

        threadList.add(newThread);
        threadList.add(runningThread);
        threadList.add(interruptedThread);
        threadList.add(finishThread);
        threadList.add(waitThread);
        threadList.add(countdownThread);

    }
}
