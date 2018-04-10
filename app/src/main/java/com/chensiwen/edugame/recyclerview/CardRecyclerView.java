package com.chensiwen.edugame.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * @author chencheng
 * @since 2018/4/8
 */
public class CardRecyclerView extends RecyclerView {
    interface OnItemScorllChangeListener {
        void onItemScrollChange(int position);
    }

    private static final boolean DEBUG = false;
    private static final String TAG = "CardRecyclerView";
    private static final float sScaleFactor = 0.2f;
    private LinearLayoutManager mLayoutManager;
    private OnItemScorllChangeListener onItemScorllChangeListener;

    public CardRecyclerView(Context context) {
        super(context);
        init();
    }

    public CardRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CardRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        this.setLayoutManager(mLayoutManager);

        this.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int mLastPosition = -1;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (DEBUG) {
                    Log.d(TAG, "onScrollStateChanged() called with: recyclerView = [" + recyclerView + "], newState = [" + newState + "]");
                }
                if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                    return;
                }
                {
                    // 松手后自动滑动到中间位置
                    int position = 0;

                    if (DEBUG) {
                        Log.d(TAG, "onScrollStateChanged: position:" + position);
                    }
                    View visibleView = mLayoutManager.getChildAt(position);
                    int viewCenter = visibleView.getLeft() + (visibleView.getRight() - visibleView.getLeft()) / 2;

                    if (DEBUG) {
                        Log.d(TAG, "onScrollStateChanged: viewCenter:" + viewCenter);
                    }
                    if (viewCenter < 0) {
                        visibleView = mLayoutManager.getChildAt(position + 1);
                        viewCenter = (visibleView.getLeft() + visibleView.getRight()) / 2;

                        if (DEBUG) {
                            Log.d(TAG, "onScrollStateChanged: viewCenter:" + viewCenter);
                        }
                    }
                    int middle = CardRecyclerView.this.getWidth() / 2;

                    if (DEBUG) {
                        Log.d(TAG, "onScrollStateChanged: middle:" + middle);
                    }

                    int deltaScroll = middle - viewCenter;
                    int currentScrollX = CardRecyclerView.this.getScrollX();

                    if (DEBUG) {
                        Log.d(TAG, "onScrollStateChanged: currentScrollX:" + currentScrollX);
                    }
                    CardRecyclerView.this.smoothScrollBy(-deltaScroll, 0);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (DEBUG) {
                    Log.d(TAG, "onScrolled() called with: recyclerView = [" + recyclerView + "], dx = [" + dx + "], dy = [" + dy + "]");
                }
                {
                    // calculate scroll percentage
                    int offset = recyclerView.computeHorizontalScrollOffset();
                    int extent = recyclerView.computeHorizontalScrollExtent();
                    int range = recyclerView.computeHorizontalScrollRange();

                    int percentage = (int) (100.0 * offset / (float) (range - extent));
                    Log.d(TAG, "onScrolled: percentage:" + percentage);
                }
                {
                    // 控制滑动过程中的缩放
                    int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();
                    int lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();
                    for (int i = firstVisibleItemPosition; i <= lastVisibleItemPosition; i++) {
                        View view = mLayoutManager.findViewByPosition(i);

                        if (DEBUG) {
                            Log.d(TAG, "onScrolled: firstVisibleView:" + view.toString());
                        }
                        float scale = getScaleFromViewCenter(view);
                        view.setScaleX(scale);
                        view.setScaleY(scale);
                    }
                }

                {
                    // 滑动时item 的变化
                    int start = mLayoutManager.findFirstVisibleItemPosition();
                    int end = mLayoutManager.findLastVisibleItemPosition();
                    int offset = 0;
                    for (int index = 0; index <= end - start; index++) {
                        View visibleView = mLayoutManager.getChildAt(index);
                        int viewCenter = visibleView.getLeft() + (visibleView.getRight() - visibleView.getLeft()) / 2;
                        if (viewCenter > 0) {
                            offset = index;
                            break;
                        }
                    }
                    int currentPosition = start + offset;
                    if (mLastPosition != currentPosition) {
                        mLastPosition = currentPosition;
                        if (onItemScorllChangeListener != null) {
                            onItemScorllChangeListener.onItemScrollChange(mLastPosition);
                        }

                        if (DEBUG) {
                            Log.d(TAG, "onScrollStateChanged: item changed:" + mLastPosition);
                        }
                    }
                }

            }
        });


        CardRecyclerView.this.setOnFlingListener(new RecyclerView.OnFlingListener() {
            @Override
            public boolean onFling(int velocityX, int velocityY) {

                if (DEBUG) {
                    Log.d(TAG, "onFling() called with: velocityX = [" + velocityX + "], velocityY = [" + velocityY + "]" + ", state:" + CardRecyclerView.this.getScrollState());
                }
                if (CardRecyclerView.this.getScrollState() == SCROLL_STATE_SETTLING) {

                    if (DEBUG) {
                        Log.d(TAG, "onFling: scroll state:SCROLL_STATE_SETTLING");
                    }
                    return true;
                }
                int minFlingVelocity = CardRecyclerView.this.getMinFlingVelocity();
                if (Math.abs(velocityX) > minFlingVelocity) {
                    int vel = velocityX > 0 ? Math.min(velocityX, 2500) : Math.max(velocityX, -2500);

                    if (DEBUG) {
                        Log.d(TAG, "onFling: velocity result:" + vel);
                    }
                    {
                        if (vel > 0) {
                            // 手向左滑动，卡片向左滑动，展示下一个右边的卡片
                            int position = 0;
                            View visibleView = mLayoutManager.getChildAt(position);
                            int viewCenter = visibleView.getLeft() + (visibleView.getRight() - visibleView.getLeft()) / 2;

                            int middle = CardRecyclerView.this.getWidth() / 2;
                            int distance = viewCenter + visibleView.getWidth() - middle;
                            CardRecyclerView.this.smoothScrollBy(distance, 0);
                        } else {
                            // 手向右边滑动，卡片向右边滑动，展示下一个左边的卡片

                            int position = 0;
                            View visibleView = mLayoutManager.getChildAt(position);
                            int viewCenter = visibleView.getLeft() + (visibleView.getRight() - visibleView.getLeft()) / 2;

                            int middle = CardRecyclerView.this.getWidth() / 2;
                            int distance = middle - viewCenter;
                            CardRecyclerView.this.smoothScrollBy(-distance, 0);
                        }
                    }
                    return true;
                }
                return false;
            }
        });

        long duration = 1000;
        RecyclerView.ItemAnimator itemAnimator = new ZoomItemAnimator();
        itemAnimator.setAddDuration(duration);
        itemAnimator.setChangeDuration(duration);
        itemAnimator.setRemoveDuration(duration);
        itemAnimator.setMoveDuration(duration);
        CardRecyclerView.this.setItemAnimator(itemAnimator);
    }

    /**
     * 根据view 的位置计算需要缩放的大小。XY 缩放比例一样
     *
     * @param view
     * @return
     */
    private float getScaleFromViewCenter(View view) {
        if (DEBUG) {
            Log.d(TAG, "onScrolled: firstVisibleView:" + view.toString());
        }
        int viewCenter = view.getLeft() + (view.getRight() - view.getLeft()) / 2;
        int middle = CardRecyclerView.this.getWidth() / 2;
        float scaleFactor = sScaleFactor;
        if (viewCenter < middle) {
            float scale = 1f * viewCenter / middle * scaleFactor + (1 - scaleFactor); // [0.7, 1]

            if (DEBUG) {
                Log.d(TAG, "onScrolled: viewCenter:" + viewCenter + ", middle :" + middle + ", scale:" + scale);
            }
            view.setScaleX(scale);
            view.setScaleY(scale);
            return scale;
        } else {
            float scale = 1f * (2 * middle - viewCenter) / middle * scaleFactor + (1 - scaleFactor); // [0.7, 1]

            if (DEBUG) {
                Log.d(TAG, "onScrolled: viewCenter:" + viewCenter + ", middle :" + middle + ", scale:" + scale);
            }
            view.setScaleX(scale);
            view.setScaleY(scale);
            return scale;
        }
    }

    public void setOnItemScorllChangeListener(OnItemScorllChangeListener onItemScorllChangeListener) {
        this.onItemScorllChangeListener = onItemScorllChangeListener;
    }
}
