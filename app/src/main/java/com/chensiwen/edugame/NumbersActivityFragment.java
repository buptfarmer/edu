package com.chensiwen.edugame;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class NumbersActivityFragment extends BaseFragment {
    private static final String TAG = "NumbersActivityFragment";
    public NumbersActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_numbers, container, false);
    }

    @Override
    protected String getUmengTag() {
        return TAG;
    }
}
