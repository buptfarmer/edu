package com.chensiwen.edugame;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.chensiwen.edugame.databinding.ActivityAnimatedVectorDrawableBinding;
import com.chensiwen.edugame.databinding.ActivityRxjavaExampleBinding;

public class RxJavaExampleActivity extends BaseAppCompatActivity {
    private static final String TAG = "RxJavaExampleActivity";

    public class BindHanlders {
        public void onClickNumbers(View view) {
        }

        public void onClickTest1(View view) {
            Log.d(TAG, "onClickTest1: ");
            binding.rxTest1.append("clicked!");
        }

        public void onClickAnimated(View view) {
            ImageView imageView = binding.animated;
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof Animatable) {
                ((Animatable) drawable).start();
            }
        }
    }

    private ActivityRxjavaExampleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_rxjava_example, null, false);
        setContentView(binding.getRoot());
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        BindHanlders handlers = new BindHanlders();
        binding.setHandlers(handlers);

    }

}
