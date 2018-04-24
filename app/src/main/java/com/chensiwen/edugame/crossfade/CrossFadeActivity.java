package com.chensiwen.edugame.crossfade;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import com.chensiwen.edugame.R;

public class CrossFadeActivity extends AppCompatActivity {

    private SeekBar seekBar;
    private TextView textView;
    private CrossFadeView crossFadeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cross_fade);

        crossFadeView = findViewById(R.id.cross_fade);
        seekBar = (SeekBar) findViewById(R.id.progress);
        textView = (TextView) findViewById(R.id.text1);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 当拖动条的滑块位置发生改变时触发该方法,在这里直接使用参数progress，即当前滑块代表的进度值
                textView.setText("Value:" + Integer.toString(progress));
                crossFadeView.crossFade(progress);
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
}
