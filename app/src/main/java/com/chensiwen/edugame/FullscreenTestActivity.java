package com.chensiwen.edugame;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class FullscreenTestActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private Button showStatusBar;
    private Button showCoverStatusBar;
    private Button hideCoverStatusBar;
    private Button hideStatusBar;
    private View rootView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_test);
        rootView = findViewById(R.id.root_view);
        showStatusBar = findViewById(R.id.show_status_bar);
        showCoverStatusBar = findViewById(R.id.show_cover_status_bar);
        hideCoverStatusBar = findViewById(R.id.hide_cover_status_bar);
        hideStatusBar = findViewById(R.id.hide_status_bar);
        showStatusBar.setOnClickListener(this);
        showCoverStatusBar.setOnClickListener(this);
        hideCoverStatusBar.setOnClickListener(this);
        hideStatusBar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.show_status_bar) {
            showStatusBar(this, rootView);
        } else if (id == R.id.show_cover_status_bar) {
            showCoverStatusBar(this, rootView);
        } else if (id == R.id.hide_status_bar) {
            hideStatusBar(this, rootView);
        } else if (id == R.id.hide_cover_status_bar) {
            hideCoverStatusBar(this, rootView);
        }
    }

    /**
     * 隐藏通知栏
     *
     * @param context
     * @param view
     */
    public static void hideStatusBar(Context context, View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            if (null == context || !(context instanceof Activity)) {
                return;
            }
            Activity activity = (Activity) context;
            WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
            attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            activity.getWindow().setAttributes(attrs);
        } else {
            if (null == view) {
                return;
            }
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;

            view.setSystemUiVisibility(uiOptions);
        }
    }


    /**
     * 展示通知栏
     *
     * @param context
     * @param view
     */
    public static void showCoverStatusBar(Context context, View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            if (null == context || !(context instanceof Activity)) {
                return;
            }
            Activity activity = (Activity) context;
            WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
            attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
            activity.getWindow().setAttributes(attrs);
        } else {
            if (null == view) {
                return;
            }
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
            view.setSystemUiVisibility(uiOptions);

        }
    }

    public static void showStatusBar(Context context, View view) {
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE;
        if (view != null) {
            view.setSystemUiVisibility(uiOptions);
        }
    }

    /**
     * 展示通知栏
     *
     * @param context
     * @param view
     */
    public static void hideCoverStatusBar(Context context, View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            if (null == context || !(context instanceof Activity)) {
                return;
            }
            Activity activity = (Activity) context;
            WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
            attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
            activity.getWindow().setAttributes(attrs);
        } else {
            if (null == view) {
                return;
            }
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            view.setSystemUiVisibility(uiOptions);

        }
    }
}
