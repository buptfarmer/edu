package com.chensiwen.edugame.recyclerview;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;
import android.widget.Toast;

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
    private CardRecyclerView mRecyclerView;
    private ArrayList<String> mContentList = new ArrayList<>();
    private CardModeScrollBar mCardModeScrollBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareData();
        setContentView(R.layout.activity_horizontal_recycler_view);

        mRecyclerView = (CardRecyclerView) findViewById(R.id.recycler_view);
        mRecyclerAdapter = new RecordRecyclerAdapter(this, mRecyclerView, mContentList, mHandler);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.setOnItemScorllChangeListener(new CardRecyclerView.OnItemScorllChangeListener() {
            @Override
            public void onItemScrollChange(int position) {
                Toast.makeText(HorizontalRecyclerViewActivity.this, "lastPosition:" + position, Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // calculate scroll percentage
                int offset = recyclerView.computeHorizontalScrollOffset();
                int extent = recyclerView.computeHorizontalScrollExtent();
                int range = recyclerView.computeHorizontalScrollRange();
                int percentage;
                if (range - extent <= 0) {
                    percentage = 0;
                } else {
                    percentage = 100 * offset / (range - extent); // [0-100]
                }
                mCardModeScrollBar.setPercent(percentage);
            }
        });
        mCardModeScrollBar = (CardModeScrollBar) findViewById(R.id.scroll_bar);
        mCardModeScrollBar.setItemCount(mContentList.size());
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

        findViewById(R.id.prev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentScrollX = mRecyclerView.getScrollX();

                Log.d(TAG, "onScrollStateChanged: currentScrollX:" + currentScrollX);
                int deltaScroll = (int) getResources().getDimension(R.dimen.horizontal_card_width);
                mRecyclerView.smoothScrollBy(-deltaScroll, 0);
            }
        });
        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentScrollX = mRecyclerView.getScrollX();
                int deltaScroll = (int) getResources().getDimension(R.dimen.horizontal_card_width);
                Log.d(TAG, "onScrollStateChanged: currentScrollX:" + currentScrollX);
                mRecyclerView.smoothScrollBy(deltaScroll, 0);
            }
        });
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
            Log.d(TAG, "update: mOwnRecyclerView.getWidth():" + mOwnRecyclerView.getWidth());
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

            // 判断是否是第一个、或者最后一个，增加相应的margin
            float parentWidth = mOwnRecyclerView.getWidth();
            if (position == 0) {
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) mRootView.getLayoutParams();
                layoutParams.leftMargin = (int) (parentWidth * 0.2f);
                layoutParams.rightMargin = 0;
                mRootView.setLayoutParams(layoutParams);
            } else if (position == list.size() - 1) {
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) mRootView.getLayoutParams();
                layoutParams.leftMargin = 0;
                layoutParams.rightMargin = (int) (parentWidth * 0.2f);
                mRootView.setLayoutParams(layoutParams);
            } else {
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) mRootView.getLayoutParams();
                layoutParams.leftMargin = 0;
                layoutParams.rightMargin = 0;
                mRootView.setLayoutParams(layoutParams);
            }
            mRootView.requestLayout();
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
