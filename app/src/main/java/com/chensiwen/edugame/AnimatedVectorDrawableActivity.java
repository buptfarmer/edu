package com.chensiwen.edugame;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.chensiwen.edugame.databinding.ActivityAnimatedVectorDrawableBinding;

public class AnimatedVectorDrawableActivity extends BaseAppCompatActivity {


    public class BindHanlders {
        public void onClickNumbers(View view) {
        }

        public void onClickAnimated(View view) {
            ImageView imageView = binding.animated;
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof Animatable) {
                ((Animatable) drawable).start();
            }
        }
        public void onClickRepeatAnimated(View view) {
            ImageView imageView = binding.repeatAnimated;
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof Animatable) {
                if (((Animatable) drawable).isRunning()) {
                    ((Animatable) drawable).stop();
                } else {
                    ((Animatable) drawable).start();
                }

            }
        }
        public void onClickMorphOnly(View view) {
            ImageView imageView = binding.morphAnimated;
            Drawable drawable = getResources().getDrawable(R.drawable.ic_test_vector_morph_only);
            imageView.setImageDrawable(drawable);
            if (drawable instanceof Animatable) {
                if (((Animatable) drawable).isRunning()) {
                    ((Animatable) drawable).stop();
                } else {
                    ((Animatable) drawable).start();
                }

            }
        }
        public void onClickAniamtedLeaf(View view) {
            ImageView imageView = binding.leafAnimated2;
            Drawable drawable = getResources().getDrawable(R.drawable.animated_leaf);
            imageView.setImageDrawable(drawable);
            //imageView.setImageResource(R.drawable.animated_leaf);
            if (drawable instanceof Animatable) {
                if (((Animatable) drawable).isRunning()) {
                    ((Animatable) drawable).stop();
                } else {
                    ((Animatable) drawable).start();
                }

            }
        }
    }

    private ActivityAnimatedVectorDrawableBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_animated_vector_drawable, null, false);
        setContentView(binding.getRoot());
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        BindHanlders handlers = new BindHanlders();
        binding.setHandlers(handlers);

    }

}
