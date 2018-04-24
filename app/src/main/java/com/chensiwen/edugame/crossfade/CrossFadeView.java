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

public class CrossFadeView extends FrameLayout {

    private ImageView mFrontView;
    private ImageView mBackView;

    public CrossFadeView(Context context) {
        super(context);
        init(context);
    }

    public CrossFadeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public CrossFadeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    /**
     * 初始化
     */
    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_crossfade, this);
        mFrontView = (ImageView) findViewById(R.id.front_view);
        mBackView = (ImageView) findViewById(R.id.back_view);

    }

    public void crossFade(Drawable front, Drawable back, int progress) {
        mFrontView.setImageDrawable(front);
        mBackView.setImageDrawable(back);
        crossFade(progress);
    }

    public void crossFade(int progress) {
        float backAlpha = 1f * progress / 100;
        float frontAlpha = 1 - backAlpha;
        mFrontView.setAlpha(frontAlpha);
        mBackView.setAlpha(backAlpha);
    }
}
