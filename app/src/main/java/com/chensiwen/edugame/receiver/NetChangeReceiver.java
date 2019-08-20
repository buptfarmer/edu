package com.chensiwen.edugame.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NetChangeReceiver extends BroadcastReceiver {
    private static final String TAG = "NetChangeReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.d(TAG, "onReceive: ");
        Log.d(TAG, "onReceive() called with: context = [" + context + "], intent = [" + intent + "]");
    }
}
