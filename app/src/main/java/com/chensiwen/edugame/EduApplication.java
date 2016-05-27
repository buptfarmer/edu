package com.chensiwen.edugame;

import android.app.Application;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by chencheng on 16/5/28.
 */
public class EduApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        if (StatsConstants.STATS_ENABLE) {
            MobclickAgent.setDebugMode(Constants.DEBUG);
            MobclickAgent.openActivityDurationTrack(false);
        }
    }
}
