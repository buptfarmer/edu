package com.chensiwen.edugame.hsb;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.chensiwen.edugame.R;
import com.chensiwen.edugame.crossfade.CrossFadeView;

public class HSBActivity extends AppCompatActivity {
    private static final String TAG = "HSBActivity";
    private SeekBar seekBar;
    private TextView textView;
    private CrossFadeView crossFadeView;
    private Bitmap tempBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hsb);

        crossFadeView = findViewById(R.id.cross_fade);
        seekBar = (SeekBar) findViewById(R.id.progress);
        textView = (TextView) findViewById(R.id.text1);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 当拖动条的滑块位置发生改变时触发该方法,在这里直接使用参数progress，即当前滑块代表的进度值
                textView.setText("Value:" + Integer.toString(progress));
//                crossFadeView.crossFade(progress);
//                ImageView frontView = crossFadeView.getFrontView();

                Drawable drawable = getResources().getDrawable(R.drawable.crossfade_front);
                if (drawable instanceof BitmapDrawable) {
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                    Bitmap bitmap = bitmapDrawable.getBitmap();
//                    Bitmap effectBitmap = imageEffect(bitmap, 1f * progress / 100);
                    Bitmap effectBitmap = imageScale(bitmap, 1f * progress / 100);
                    crossFadeView.setFrontView(new BitmapDrawable(getResources(), effectBitmap));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.e("------------", "开始滑动！");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e("------------", "停止滑动！");
            }
        });
    }


    public Bitmap imageEffect(Bitmap bm, float saturation) {
        Log.d(TAG, "imageEffect() called with: bm = [" + bm + "], saturation = [" + saturation + "]");
        if (tempBitmap == null) {
            tempBitmap = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(tempBitmap);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        //饱和度调节
        ColorMatrix saturationColorMatrix = new ColorMatrix();
        saturationColorMatrix.setSaturation(saturation);
        ColorMatrix imageMatrix = new ColorMatrix();
        imageMatrix.postConcat(saturationColorMatrix);

        paint.setColorFilter(new ColorMatrixColorFilter(imageMatrix));
        canvas.drawBitmap(bm, 0, 0, paint);

        Matrix matrix = new Matrix();
        matrix.postScale(1.3f, 1.3f);
        tempBitmap = Bitmap.createBitmap(bm ,0, 0, bm.getWidth(), bm.getHeight(), matrix, false);
        return tempBitmap;

    }

    private Bitmap imageScale(Bitmap bm, float scale) {

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        tempBitmap = Bitmap.createBitmap(bm ,bm.getWidth() /2 , bm.getWidth() /2 , bm.getWidth() /2 , bm.getWidth() /2 , matrix, false);
//        tempBitmap = Bitmap.createBitmap(bm ,0, 0, bm.getWidth(), bm.getHeight(), matrix, false);
        return tempBitmap;
    }

    public static Bitmap imageEffect(Bitmap bm, float hue, float saturation, float lum) {
        Log.d(TAG, "imageEffect() called with: bm = [" + bm + "], hue = [" + hue + "], saturation = [" + saturation + "], lum = [" + lum + "]");

        Bitmap bitmap = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        //色相调节
        ColorMatrix hueMatrix = new ColorMatrix();
        hueMatrix.setRotate(0, hue);
        hueMatrix.setRotate(1, hue);
        hueMatrix.setRotate(2, hue);

        //饱和度调节
        ColorMatrix saturationColorMatrix = new ColorMatrix();
        saturationColorMatrix.setSaturation(saturation);

        //亮度调节
        ColorMatrix lumMatrix = new ColorMatrix();
        lumMatrix.setScale(lum, lum, lum, 1);

        ColorMatrix ImageMatrix = new ColorMatrix();
        ImageMatrix.postConcat(hueMatrix);
        ImageMatrix.postConcat(saturationColorMatrix);
        ImageMatrix.postConcat(lumMatrix);

        paint.setColorFilter(new ColorMatrixColorFilter(ImageMatrix));
        canvas.drawBitmap(bm, 0, 0, paint);

        return bitmap;
    }
}
