/*
 * Copyright (C) 2016. TBReader Inc. All rights reserved.
 */

package com.chensiwen.edugame.crossfade;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.chensiwen.edugame.R;

public class TripleCrossFadeView extends FrameLayout {
    private static final String TAG = "TripleCrossFadeView";
    private ImageView mPrevView;
    private ImageView mCurrentView;
    private ImageView mNextView;

    public TripleCrossFadeView(Context context) {
        super(context);
        init(context);
    }

    public TripleCrossFadeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public TripleCrossFadeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    /**
     * 初始化
     */
    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_crossfade_triple, this);
        mPrevView = (ImageView) findViewById(R.id.front_view);
        mCurrentView = (ImageView) findViewById(R.id.current_view);
        mNextView = (ImageView) findViewById(R.id.back_view);

    }

    public void crossFadePrev(int progress) {
        float prevAlpha = 1f * progress / 100;
        float currentAlpha = 1 - prevAlpha;
        mPrevView.setAlpha(prevAlpha);
        mCurrentView.setAlpha(currentAlpha);
        mNextView.setAlpha(0f);
    }

    public void crossFadeNext(int progress) {
        float nextAlpha = 1f * progress / 100;
        float currentAlpha = 1 - nextAlpha;
        mPrevView.setAlpha(0f);
        mCurrentView.setAlpha(currentAlpha);
        mNextView.setAlpha(nextAlpha);
    }

    public void setPrevView(Drawable front) {
        mPrevView.setImageDrawable(front);
    }

    public void setNextView(Drawable back) {
        mNextView.setImageDrawable(back);
    }

    public ImageView getPrevView() {
        return mPrevView;
    }

    public ImageView getNextView() {
        return mNextView;
    }

    public void setCurrentView(Drawable back) {
        mCurrentView.setImageDrawable(back);
    }

    public ImageView getCurrentView() {
        return mCurrentView;
    }
}
