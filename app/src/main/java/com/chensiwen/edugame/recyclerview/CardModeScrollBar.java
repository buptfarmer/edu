package com.chensiwen.edugame.recyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.chensiwen.edugame.R;
import com.chensiwen.edugame.Utils;

/**
 * 新版书架-卡片模式滚动条
 *
 * @author chencheng
 * @since 2018/4/10
 */
public class CardModeScrollBar extends View {
    private static final String TAG = "CardModeScrollBar";
    private Paint mBackgroundPaint;
    private Paint mForegroundPaint;
    // [0,100]
    private int mPercent;
    private int mFgWidth;
    private int mItemCount;

    public CardModeScrollBar(Context context) {
        super(context);
        init();
    }

    public CardModeScrollBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CardModeScrollBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(getResources().getColor(R.color.bookshelf_scroll_bg));
        mBackgroundPaint.setStrokeCap(Paint.Cap.ROUND);
        mBackgroundPaint.setStyle(Paint.Style.STROKE);
        mBackgroundPaint.setStrokeWidth(Utils.dp2Px(2));
        mForegroundPaint = new Paint(mBackgroundPaint);
        mForegroundPaint.setColor(getResources().getColor(R.color.bookshelf_scroll_fg));
        mFgWidth = Utils.dp2Px(10);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();
        mBackgroundPaint.setStrokeWidth(height);
        mForegroundPaint.setStrokeWidth(height);
        mFgWidth = width / mItemCount;
        int paintX = height / 2;
        int paintY = height / 2;
        canvas.drawLine(paintX, paintY, width - paintX, paintY, mBackgroundPaint);
        final float percentage = 1f * mPercent / 100;
        int fgStart = (int) ((width - mFgWidth) * percentage);
        canvas.drawLine(fgStart + paintX, paintY, fgStart + mFgWidth - paintX, paintY, mForegroundPaint);
    }

    public void setPercent(int percent) {
        if (percent >= 99) {
            percent = 100;
        }
        this.mPercent = percent;
        postInvalidate();
    }

    public void setItemCount(int itemCount) {
        this.mItemCount = itemCount;
        postInvalidate();
    }
}
