package com.chensiwen.edugame.lottie;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.OnCompositionLoadedListener;
import com.chensiwen.edugame.R;

public class LottieActivity extends AppCompatActivity {

    private boolean mIsCardMode = true;
    final LottieDrawable mLottieSwitchToCardMode = new LottieDrawable();
    final LottieDrawable mLottieSwitchToListMode = new LottieDrawable();
    private ImageView mLottieImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottie);
        mLottieImage = (ImageView) findViewById(R.id.lottie_image);
        String animSwitchToCardMode = "lottie/lottie_switch_to_card_mode.json";
        String animSwitchToListMode = "lottie/lottie_switch_to_list_mode.json";
        LottieComposition.Factory.fromAssetFileName(getApplicationContext(), animSwitchToCardMode, new OnCompositionLoadedListener() {
            @Override
            public void onCompositionLoaded(@Nullable LottieComposition composition) {
                if (composition != null) {
                    mLottieSwitchToCardMode.setComposition(composition);
                }
            }
        });
        LottieComposition.Factory.fromAssetFileName(getApplicationContext(), animSwitchToListMode, new OnCompositionLoadedListener() {
            @Override
            public void onCompositionLoaded(@Nullable LottieComposition composition) {
                if (composition != null) {
                    mLottieSwitchToListMode.setComposition(composition);
                }
            }
        });
        mLottieImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeButtonIcon();
            }
        });
    }

    private void changeButtonIcon(){
        LottieDrawable currentDrawable = mIsCardMode ? mLottieSwitchToCardMode : mLottieSwitchToListMode;
        mLottieImage.setImageDrawable(currentDrawable);
        currentDrawable.playAnimation();
        mIsCardMode = !mIsCardMode;
    }
}
