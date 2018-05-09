package com.chensiwen.edugame.bold;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import com.binaryfork.spanny.Spanny;
import com.chensiwen.edugame.R;

public class AlignTextActivity extends AppCompatActivity {

    private SeekBar seekBar;
    private TextView textView;
    private TextView textView2;
    private TextView textView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_align_text);
        seekBar = (SeekBar) findViewById(R.id.progress);
        textView = (TextView) findViewById(R.id.text1);
        textView2 = (TextView) findViewById(R.id.text2);
        textView3 = (TextView) findViewById(R.id.text3);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 当拖动条的滑块位置发生改变时触发该方法,在这里直接使用参数progress，即当前滑块代表的进度值
                String s = "来一点中文，潺森！Value:" + Integer.toString(progress);

                textView.setText(new Spanny().append(s, new FakeBoldSpan(progress)));
                textView2.setText(new Spanny().append(s, new FakeBoldSpan2(progress)));
                textView3.setText(s);
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

    public static class FakeBoldSpan extends CharacterStyle {
        private int stokeWidth;

        public FakeBoldSpan(int stokeWidth) {
            this.stokeWidth = stokeWidth;
        }

        @Override
        public void updateDrawState(TextPaint tp) {
            tp.setFakeBoldText(true);//一种伪粗体效果，比原字体加粗的效果弱一点
            // tp.setStyle(Paint.Style.FILL_AND_STROKE);
            // tp.setColor(Color.RED);//字体颜色
            // tp.setStrokeWidth(10);//控制字体加粗的程度
        }
    }

    public static class FakeBoldSpan2 extends CharacterStyle {
        private int stokeWidth;

        public FakeBoldSpan2(int stokeWidth) {
            this.stokeWidth = stokeWidth;
        }

        @Override
        public void updateDrawState(TextPaint tp) {
//            tp.setFakeBoldText(true);//一种伪粗体效果，比原字体加粗的效果弱一点
            tp.setStyle(Paint.Style.FILL_AND_STROKE);
            tp.setColor(Color.RED);//字体颜色
            tp.setStrokeWidth(stokeWidth);//控制字体加粗的程度
        }
    }
}
