package com.chensiwen.edugame;

import android.app.Fragment;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by chencheng on 16/5/28.
 */
public abstract class BaseFragment extends Fragment {
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getUmengTag()); //统计页面，getUmengTag() 为页面名称，可自定义
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getUmengTag());
    }

    protected abstract String getUmengTag();
}
