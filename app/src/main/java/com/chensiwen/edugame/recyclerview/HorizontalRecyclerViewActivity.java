package com.chensiwen.edugame.recyclerview;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.chensiwen.edugame.BaseAppCompatActivity;
import com.chensiwen.edugame.R;
import com.chensiwen.edugame.StatsConstants;
import com.chensiwen.edugame.particle.ExplosionField;
import com.chensiwen.edugame.particle.factory.FallingParticleFactory;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HorizontalRecyclerViewActivity extends BaseAppCompatActivity implements Handler.Callback {
    private static final String TAG = "HorizontalRecyclerViewA";
    private static final int MSG_EXPLOSION_DONE = 1;
    private static final int[] COLORS = new int[]{
            R.color.common_red,
            R.color.common_pink,
            R.color.common_purple,
            R.color.common_deep_purple,
            R.color.common_indigo,
            R.color.common_blue,
            R.color.common_light_blue,
            R.color.common_cyan,
            R.color.common_teal,
            R.color.common_green,
            R.color.common_light_green,
            R.color.common_lime,
            R.color.common_yellow,
            R.color.common_amber,
            R.color.common_orange,
            R.color.common_deep_orange,
            R.color.common_brown,
            R.color.common_grey,
            R.color.common_blue_grey,
    };
    private Handler mHandler = new Handler(Looper.myLooper(), this);

    @Override
    public boolean handleMessage(Message msg) {
        if (MSG_EXPLOSION_DONE == msg.what) {
            int position = msg.arg1;
            mContentList.remove(position);
            mRecyclerAdapter.notifyItemRemoved(position);
            mRecyclerAdapter.notifyItemRangeChanged(position, mRecyclerAdapter.getItemCount());
//            moveToPosition(position);
        }
        return true;
    }

    private RecordRecyclerAdapter mRecyclerAdapter;
    private RecyclerView mRecyclerView;
    private ArrayList<String> mContentList = new ArrayList<>();


    /**
     * 用户点击的分类在rv的位置
     */
    private int mIndex;
    /**
     * rv是否需要第二次滚动
     */
    private boolean mNeedToMove = false;

    private void moveToPosition(int index) {
        Log.d(TAG, "moveToPosition() called with: index = [" + index + "]");
        //获取当前recycleView屏幕可见的第一项和最后一项的Position
        int firstItem = mLayoutManager.findFirstVisibleItemPosition();
        int lastItem = mLayoutManager.findLastVisibleItemPosition();
        //然后区分情况
        if (index <= firstItem) {
            //当要置顶的项在当前显示的第一个项的前面时
            mRecyclerView.scrollToPosition(index);
        } else if (index <= lastItem) {
            //当要置顶的项已经在屏幕上显示时，计算它离屏幕原点的距离
            int top = mRecyclerView.getChildAt(index - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            //当要置顶的项在当前显示的最后一项的后面时
            mRecyclerView.scrollToPosition(index);
            //记录当前需要在RecyclerView滚动监听里面继续第二次滚动
            mNeedToMove = true;
        }
    }

    private LinearLayoutManager mLayoutManager;

    private float getScaleFromViewCenter(View view) {
        Log.d(TAG, "onScrolled: firstVisibleView:" + view.toString());
        int viewCenter = view.getLeft() + (view.getRight() - view.getLeft()) / 2;
        int middle = mRecyclerView.getWidth() / 2;
        float scaleFactor = 0.3f;
        if (viewCenter < middle) {
            float scale = 1f * viewCenter / middle * scaleFactor + (1 - scaleFactor); // [0.7, 1]
            Log.d(TAG, "onScrolled: viewCenter:" + viewCenter + ", middle :" + middle + ", scale:" + scale);
            view.setScaleX(scale);
            view.setScaleY(scale);
            return scale;
        } else {
            float scale = 1f * (2 * middle - viewCenter) / middle * scaleFactor + (1 - scaleFactor); // [0.7, 1]
            Log.d(TAG, "onScrolled: viewCenter:" + viewCenter + ", middle :" + middle + ", scale:" + scale);
            view.setScaleX(scale);
            view.setScaleY(scale);
            return scale;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareData();
        setContentView(R.layout.activity_horizontal_recycler_view);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerAdapter = new RecordRecyclerAdapter(this, mRecyclerView, mContentList, mHandler);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d(TAG, "onScrollStateChanged() called with: recyclerView = [" + recyclerView + "], newState = [" + newState + "]");
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d(TAG, "onScrolled() called with: recyclerView = [" + recyclerView + "], dx = [" + dx + "], dy = [" + dy + "]");
                //在这里进行第二次滚动（最后的距离）
                if (mNeedToMove) {
                    mNeedToMove = false;
                    //获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
                    int n = mIndex - mLayoutManager.findFirstVisibleItemPosition();
                    if (0 <= n && n < mRecyclerView.getChildCount()) {
                        //获取要置顶的项顶部离RecyclerView顶部的距离
                        int top = mRecyclerView.getChildAt(n).getTop();
                        int left = mRecyclerView.getChildAt(n).getLeft();

                        //最后的移动
                        mRecyclerView.scrollBy(0, top);
                    }
                }
                int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();
                int lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();
                for (int i = firstVisibleItemPosition; i <= lastVisibleItemPosition; i++) {
                    View view = mLayoutManager.findViewByPosition(i);
                    Log.d(TAG, "onScrolled: firstVisibleView:" + view.toString());
                    float scale = getScaleFromViewCenter(view);
                    view.setScaleX(scale);
                    view.setScaleY(scale);
                }

            }
        });
        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//                Log.d(TAG, "onInterceptTouchEvent() called with: rv = [" + rv + "], e = [" + e + "]");
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
//                Log.d(TAG, "onTouchEvent() called with: rv = [" + rv + "], e = [" + e + "]");
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//                Log.d(TAG, "onRequestDisallowInterceptTouchEvent() called with: disallowIntercept = [" + disallowIntercept + "]");
            }
        });
//        RecyclerView.ItemAnimator itemAnimator = new ExplodeItemAnimator(this);
//        itemAnimator.setMoveDuration(1000);
//        itemAnimator.setChangeDuration(1000);
//        itemAnimator.setRemoveDuration(1000);
//        mRecyclerView.setItemAnimator(itemAnimator);
        long duration = 1000;
        RecyclerView.ItemAnimator itemAnimator = new ZoomItemAnimator();
        itemAnimator.setAddDuration(duration);
        itemAnimator.setChangeDuration(duration);
        itemAnimator.setRemoveDuration(duration);
        itemAnimator.setMoveDuration(duration);
        mRecyclerView.setItemAnimator(itemAnimator);



        // start animation
        boolean showStartAnimation = false;
        if (showStartAnimation) {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.layout_item_anim);
            animation.setInterpolator(new OvershootInterpolator());
            LayoutAnimationController layoutAnimationController = new LayoutAnimationController(animation);
            //layoutAnimationController.setInterpolator(new OvershootInterpolator());
            layoutAnimationController.setDelay(0.1f);
            layoutAnimationController.setOrder(LayoutAnimationController.ORDER_RANDOM);
            mRecyclerView.setLayoutAnimation(layoutAnimationController);
        }
    }


    private void prepareData() {
        for (int i = 0; i < 21; i++) {
            mContentList.add(String.valueOf(i));
        }
    }

    static class RecordRecyclerAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private ArrayList<String> mList;
        private BaseAppCompatActivity context;
        private Handler mHandler;
        private RecyclerView mOwnRecyclerView;

        public RecordRecyclerAdapter(BaseAppCompatActivity context, RecyclerView ownRecyclerView, ArrayList<String> lists, Handler handler) {
            this.context = context;
            this.mOwnRecyclerView = ownRecyclerView;
            this.mList = lists;
            this.mHandler = handler;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            CardView view = (CardView) LayoutInflater.from(context).inflate(R.layout.horizontal_recyelerview_item, parent, false);
            MyViewHolder viewHolder = new MyViewHolder(context, mOwnRecyclerView, view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            holder.update(mList, position, mHandler);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView mRootView;
        public TextView mContent;
        private BaseAppCompatActivity mActivity;
        private ExplosionField mExplosion;
        private Random mRandom;
        private RecyclerView mOwnRecyclerView;

        public MyViewHolder(BaseAppCompatActivity context, RecyclerView ownRecyclerView, CardView itemView) {
            super(itemView);
            mActivity = context;
            this.mOwnRecyclerView = ownRecyclerView;
            mRootView = itemView;
            mContent = (TextView) itemView.findViewById(R.id.content);
            mExplosion = new ExplosionField(mActivity, new FallingParticleFactory());
        }

        public RecyclerView getOwnRecyclerView() {
            return this.mOwnRecyclerView;
        }

        private int getRandomColor() {
            return mActivity.getResources().getColor(COLORS[mRandom.nextInt(COLORS.length)]);
        }
        private int getBackgroundColor(int position) {
            return mActivity.getResources().getColor(COLORS[position % COLORS.length]);
        }

        public void update(final List<String > list, final int position, final Handler handler) {
            Log.i(TAG, String.format("update: value:%s, position:%d", list.get(position), position));

            mRandom = new Random(position);
            mContent.setText(list.get(position));
            mRootView.setBackgroundColor(getBackgroundColor(list.indexOf(list.get(position))));
            mRootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (handler != null) {
                        handler.obtainMessage(MSG_EXPLOSION_DONE, position, 0).sendToTarget();
                    }
                    MobclickAgent.onEvent(mActivity, StatsConstants.NUMBERS_CLICK_PREFIX + list.get(position));
                }
            });

//            ViewPropertyAnimator animator = mRootView.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(1000);
//            animator.setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    super.onAnimationEnd(animation);
//                }
//            });
//            animator.start();
        }
    }
}
